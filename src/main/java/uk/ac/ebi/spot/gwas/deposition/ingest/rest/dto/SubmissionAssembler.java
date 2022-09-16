package uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import uk.ac.ebi.spot.gwas.deposition.constants.SubmissionProvenanceType;
import uk.ac.ebi.spot.gwas.deposition.domain.*;
import uk.ac.ebi.spot.gwas.deposition.dto.AssociationDto;
import uk.ac.ebi.spot.gwas.deposition.dto.NoteDto;
import uk.ac.ebi.spot.gwas.deposition.dto.SampleDto;
import uk.ac.ebi.spot.gwas.deposition.dto.StudyDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.MetadataDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionEnvelopeDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.config.IngestServiceConfig;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.*;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers.StudiesController;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers.SubmissionsController;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.BodyOfWorkService;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.PublicationService;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class SubmissionAssembler implements ResourceAssembler<Submission, Resource<SubmissionDto>> {

    private static final Logger log = LoggerFactory.getLogger(SubmissionAssembler.class);

    @Autowired
    IngestServiceConfig ingestServiceConfig;

    @Autowired
    private PublicationService publicationService;

    @Autowired
    private BodyOfWorkService bodyOfWorkService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private AssociationRepository associationRepository;

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    StudyDtoAssembler studyDtoAssembler;

    public List<SubmissionEnvelopeDto> assembleEnvelopes(Page<Submission> submissions) {
        List<String> pubIds = new ArrayList<>();
        List<String> bodyOfWorkIds = new ArrayList<>();
        Map<String, User> userMap = new HashMap<>();
        for (Submission submission : submissions) {
            if (submission.getProvenanceType().equalsIgnoreCase(SubmissionProvenanceType.PUBLICATION.name())) {
                pubIds.add(submission.getPublicationId());
            } else {
                bodyOfWorkIds.addAll(submission.getBodyOfWorks());
                if (submission.getPublicationId() != null) {
                    pubIds.add(submission.getPublicationId());
                }
            }
            userMap.put(submission.getCreated().getUserId(), null);
        }

        List<Publication> publications = publicationService.getByIdIn(pubIds);
        Map<String, Publication> publicationMap = new HashMap<>();
        for (Publication publication : publications) {
            publicationMap.put(publication.getId(), publication);
        }
        List<BodyOfWork> bodyOfWorks = bodyOfWorkService.getByBowIdInAndArchived(bodyOfWorkIds, false);
        Map<String, BodyOfWork> bodyOfWorksMap = new HashMap<>();
        for (BodyOfWork bodyOfWork : bodyOfWorks) {
            bodyOfWorksMap.put(bodyOfWork.getBowId(), bodyOfWork);
        }
        List<String> userIds = new ArrayList<>();
        for (String id : userMap.keySet()) {
            userIds.add(id);
        }
        List<User> users = userRepository.findByIdIn(userIds);
        for (User user : users) {
            userMap.put(user.getId(), user);
        }

        List<SubmissionEnvelopeDto> result = new ArrayList<>();
        for (Submission submission : submissions) {
            result.add(new SubmissionEnvelopeDto(
                    submission.getId(),
                    submission.getPublicationId() != null ? PublicationDtoAssembler.assemble(publicationMap.get(submission.getPublicationId())) : null,
                    submission.getBodyOfWorks() != null ? BodyOfWorkDtoAssembler.assemble(bodyOfWorksMap.get(submission.getBodyOfWorks().get(0))) : null,
                    submission.getProvenanceType(),
                    submission.getOverallStatus(),
                    submission.getGlobusFolderId(),
                    submission.getGlobusOriginId(),
                    submission.getDateSubmitted(),
                    ProvenanceDtoAssembler.assemble(submission.getCreated(), userMap.get(submission.getCreated().getUserId()))
            ));
        }
        return result;
    }

    public SubmissionDto assemble(Submission submission) {
        log.info("Assembling submission: {}", submission.getId());
        Publication publication = null;
        BodyOfWork bodyOfWork = null;
        if (submission.getProvenanceType().equalsIgnoreCase(SubmissionProvenanceType.PUBLICATION.name())) {
            publication = publicationService.getPublicationBySubmission(submission);
        } else {
            if (!submission.getBodyOfWorks().isEmpty()) {
                bodyOfWork = bodyOfWorkService.getBodyOfWorkBySubmission(submission);
                if (submission.getPublicationId() != null) {
                    publication = publicationService.getPublicationBySubmission(submission);
                }
            }
        }

        Optional<User> userOpt = userRepository.findById(submission.getCreated().getUserId());

        List<StudyDto> studyDtoList = new ArrayList<>();
        if (!submission.getStudies().isEmpty()) {
            List<Study> studies = studyRepository.findByIdIn(submission.getStudies());
            studyDtoList = studies.stream().map(studyDtoAssembler::assemble).collect(Collectors.toList());
        }

        List<AssociationDto> associationDtos = new ArrayList<>();
        if (!submission.getAssociations().isEmpty()) {
            List<Association> associations = associationRepository.findByIdIn(submission.getAssociations());
            associationDtos = associations.stream().map(AssociationDtoAssembler::assemble).collect(Collectors.toList());
        }

        List<SampleDto> sampleDtos = new ArrayList<>();
        if (!submission.getSamples().isEmpty()) {
            List<Sample> samples = sampleRepository.findByIdIn(submission.getSamples());
            sampleDtos = samples.stream().map(SampleDtoAssembler::assemble).collect(Collectors.toList());
        }

        List<NoteDto> noteDtos = new ArrayList<>();
        if (!submission.getNotes().isEmpty()) {
            List<Note> notes = noteRepository.findByIdIn(submission.getNotes());
            noteDtos = notes.stream().map(NoteDtoAssembler::assemble).collect(Collectors.toList());
        }

        return new SubmissionDto(submission.getId(),
                publication != null ? PublicationDtoAssembler.assemble(publication) : null,
                bodyOfWork != null ? BodyOfWorkDtoAssembler.assemble(bodyOfWork) : null,
                submission.getProvenanceType(),
                submission.getOverallStatus(),
                submission.getGlobusFolderId(),
                submission.getGlobusOriginId(),
                studyDtoList,
                associationDtos,
                sampleDtos,
                noteDtos,
                submission.getDateSubmitted(),
                new MetadataDto(submission.getStudies().size(),
                        submission.getAssociations().size(),
                        submission.getSamples().size(),
                        submission.getNotes().size()),
                ProvenanceDtoAssembler.assemble(submission.getCreated(), userOpt.get()),
                submission.isAgreedToCc0(),
                submission.getOpenTargetsFlag(),
                submission.getUserRequestedFlag());
    }

    @Override
    public Resource<SubmissionDto> toResource(Submission submission) {
        log.info("Assembling submission: {}", submission.getId());
        Publication publication = null;
        BodyOfWork bodyOfWork = null;
        if (submission.getProvenanceType().equalsIgnoreCase(SubmissionProvenanceType.PUBLICATION.name())) {
            publication = publicationService.getPublicationBySubmission(submission);
        } else {
            if (!submission.getBodyOfWorks().isEmpty()) {
                bodyOfWork = bodyOfWorkService.getBodyOfWorkBySubmission(submission);
                if (submission.getPublicationId() != null) {
                    publication = publicationService.getPublicationBySubmission(submission);
                }
            }
        }

        log.info("Assembling submission: {}", submission.getId());

        Optional<User> userOpt = userRepository.findById(submission.getCreated().getUserId());

        List<StudyDto> studyDtoList = new ArrayList<>();
//        if (!submission.getStudies().isEmpty()) {
//            List<Study> studies = studyRepository.findByIdIn(submission.getStudies());
//            studyDtoList = studies.stream().map(studyDtoAssembler::assemble).collect(Collectors.toList());
//        }

        List<AssociationDto> associationDtos = new ArrayList<>();
//        if (!submission.getAssociations().isEmpty()) {
//            List<Association> associations = associationRepository.findByIdIn(submission.getAssociations());
//            associationDtos = associations.stream().map(AssociationDtoAssembler::assemble).collect(Collectors.toList());
//        }

        List<SampleDto> sampleDtos = new ArrayList<>();
//        if (!submission.getSamples().isEmpty()) {
//            List<Sample> samples = sampleRepository.findByIdIn(submission.getSamples());
//            sampleDtos = samples.stream().map(SampleDtoAssembler::assemble).collect(Collectors.toList());
//        }

        List<NoteDto> noteDtos = new ArrayList<>();
        if (!submission.getNotes().isEmpty()) {
            List<Note> notes = noteRepository.findByIdIn(submission.getNotes());
            noteDtos = notes.stream().map(NoteDtoAssembler::assemble).collect(Collectors.toList());
        }

        log.info("Assembling submission: {}", submission.getId());

        SubmissionDto submissionDto = new SubmissionDto(submission.getId(),
                publication != null ? PublicationDtoAssembler.assemble(publication) : null,
                bodyOfWork != null ? BodyOfWorkDtoAssembler.assemble(bodyOfWork) : null,
                submission.getProvenanceType(),
                submission.getOverallStatus(),
                submission.getGlobusFolderId(),
                submission.getGlobusOriginId(),
                studyDtoList,
                associationDtos,
                sampleDtos,
                noteDtos,
                submission.getDateSubmitted(),
                new MetadataDto(submission.getStudies().size(),
                        submission.getAssociations().size(),
                        submission.getSamples().size(),
                        submission.getNotes().size()),
                ProvenanceDtoAssembler.assemble(submission.getCreated(), userOpt.get()),
                submission.isAgreedToCc0(),
                submission.getOpenTargetsFlag(),
                submission.getUserRequestedFlag());


        Resource<SubmissionDto> resource = new Resource<>(submissionDto);
        log.info("EfoTraitDtoAssembler Resource ->" + resource);

        resource.add(linkTo(methodOn(SubmissionsController.class).getSubmission(submission.getId())).withSelfRel());


        return resource;
    }
}
