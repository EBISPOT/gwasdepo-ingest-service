package uk.ac.ebi.spot.gwas.deposition.ingest.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.constants.SubmissionProvenanceType;
import uk.ac.ebi.spot.gwas.deposition.domain.*;
import uk.ac.ebi.spot.gwas.deposition.dto.AssociationDto;
import uk.ac.ebi.spot.gwas.deposition.dto.NoteDto;
import uk.ac.ebi.spot.gwas.deposition.dto.SampleDto;
import uk.ac.ebi.spot.gwas.deposition.dto.StudyDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionEnvelopeDto;
import uk.ac.ebi.spot.gwas.deposition.exception.EntityNotFoundException;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.*;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto.*;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.SubmissionAssemblyService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubmissionAssemblyServiceImpl implements SubmissionAssemblyService {

    private static final Logger log = LoggerFactory.getLogger(SubmissionAssemblyService.class);

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private BodyOfWorkRepository bodyOfWorkRepository;

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

    @Override
    public List<SubmissionEnvelopeDto> assembleEnvelopes(List<Submission> submissions) {
        List<String> pubIds = new ArrayList<>();
        List<String> bodyOfWorkIds = new ArrayList<>();
        Map<String, User> userMap = new HashMap<>();
        for (Submission submission : submissions) {
            if (submission.getProvenanceType().equalsIgnoreCase(SubmissionProvenanceType.PUBLICATION.name())) {
                pubIds.add(submission.getPublicationId());
            } else {
                bodyOfWorkIds.addAll(submission.getBodyOfWorks());
            }
            userMap.put(submission.getCreated().getUserId(), null);
        }

        List<Publication> publications = publicationRepository.findByIdIn(pubIds);
        Map<String, Publication> publicationMap = new HashMap<>();
        for (Publication publication : publications) {
            publicationMap.put(publication.getId(), publication);
        }
        List<BodyOfWork> bodyOfWorks = bodyOfWorkRepository.findByIdInAndArchived(bodyOfWorkIds, false);
        Map<String, BodyOfWork> bodyOfWorksMap = new HashMap<>();
        for (BodyOfWork bodyOfWork : bodyOfWorks) {
            bodyOfWorksMap.put(bodyOfWork.getId(), bodyOfWork);
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

    @Override
    public SubmissionDto assemble(Submission submission) {
        log.info("Assembling submission: {}", submission.getId());
        Publication publication = null;
        BodyOfWork bodyOfWork = null;
        if (submission.getProvenanceType().equalsIgnoreCase(SubmissionProvenanceType.PUBLICATION.name())) {
            Optional<Publication> publicationOptional = publicationRepository.findById(submission.getPublicationId());
            if (!publicationOptional.isPresent()) {
                log.error("Unable to find publication: {}", submission.getPublicationId());
                throw new EntityNotFoundException("Unable to find publication: " + submission.getPublicationId());
            }
            publication = publicationOptional.get();
        } else {
            if (!submission.getBodyOfWorks().isEmpty()) {
                Optional<BodyOfWork> bodyOfWorkOptional = bodyOfWorkRepository.findByIdAndArchived(submission.getBodyOfWorks().get(0), false);
                if (!bodyOfWorkOptional.isPresent()) {
                    log.error("Unable to find body of work: {}", submission.getBodyOfWorks());
                    throw new EntityNotFoundException("Unable to find body of work: " + submission.getBodyOfWorks().get(0));
                }
                bodyOfWork = bodyOfWorkOptional.get();
            }
        }

        Optional<User> userOpt = userRepository.findById(submission.getCreated().getUserId());

        List<StudyDto> studyDtoList = new ArrayList<>();
        if (!submission.getStudies().isEmpty()) {
            List<Study> studies = studyRepository.findByIdIn(submission.getStudies());
            studyDtoList = studies.stream().map(StudyDtoAssembler::assemble).collect(Collectors.toList());
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
                ProvenanceDtoAssembler.assemble(submission.getCreated(), userOpt.get()));
    }
}
