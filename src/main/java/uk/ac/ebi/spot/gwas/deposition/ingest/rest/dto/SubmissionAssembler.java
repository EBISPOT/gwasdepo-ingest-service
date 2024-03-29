package uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import uk.ac.ebi.spot.gwas.deposition.ingest.config.IngestServiceConfig;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.NoteRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.UserRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers.AssociationController;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers.SamplesController;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers.StudiesController;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers.SubmissionsController;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.BodyOfWorkService;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.PublicationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private NoteRepository noteRepository;

    @Autowired
    StudyDtoAssembler studyDtoAssembler;



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
        List<AssociationDto> associationDtos = new ArrayList<>();
        List<SampleDto> sampleDtos = new ArrayList<>();

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

        resource.add(linkTo(methodOn(SubmissionsController.class).getSubmission(submission.getId())).withSelfRel());
        resource.add(linkTo(StudiesController.class).slash(submission.getId()).slash("studies").withRel("studies"));
        resource.add(linkTo(AssociationController.class).slash(submission.getId()).slash("associations").withRel("associations"));
        resource.add(linkTo(SamplesController.class).slash(submission.getId()).slash("samples").withRel("samples"));

        return resource;
    }



}