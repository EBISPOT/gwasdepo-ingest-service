package uk.ac.ebi.spot.gwas.deposition.ingest.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.constants.SubmissionProvenanceType;
import uk.ac.ebi.spot.gwas.deposition.domain.*;
import uk.ac.ebi.spot.gwas.deposition.dto.AssociationDto;
import uk.ac.ebi.spot.gwas.deposition.dto.NoteDto;
import uk.ac.ebi.spot.gwas.deposition.dto.SampleDto;
import uk.ac.ebi.spot.gwas.deposition.dto.StudyDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.MetadataDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionDto;
import uk.ac.ebi.spot.gwas.deposition.exception.EntityNotFoundException;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.*;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto.*;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.SubmissionAssemblyServiceV2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubmissionAssemblyServiceImplV2 implements SubmissionAssemblyServiceV2 {

    private static final Logger log = LoggerFactory.getLogger(SubmissionAssemblyServiceV2.class);

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
                Optional<BodyOfWork> bodyOfWorkOptional = bodyOfWorkRepository.findByBowIdAndArchived(submission.getBodyOfWorks().get(0), false);
                if (!bodyOfWorkOptional.isPresent()) {
                    log.error("Unable to find body of work: {}", submission.getBodyOfWorks());
                    throw new EntityNotFoundException("Unable to find body of work: " + submission.getBodyOfWorks().get(0));
                }
                bodyOfWork = bodyOfWorkOptional.get();
                if (submission.getPublicationId() != null) {
                    Optional<Publication> publicationOptional = publicationRepository.findById(submission.getPublicationId());
                    if (!publicationOptional.isPresent()) {
                        log.error("Unable to find publication: {}", submission.getPublicationId());
                        throw new EntityNotFoundException("Unable to find publication: " + submission.getPublicationId());
                    }
                    publication = publicationOptional.get();
                }
            }
        }

        Optional<User> userOpt = userRepository.findById(submission.getCreated().getUserId());

        return new SubmissionDto(submission.getId(),
                publication != null ? PublicationDtoAssembler.assemble(publication) : null,
                bodyOfWork != null ? BodyOfWorkDtoAssembler.assemble(bodyOfWork) : null,
                submission.getProvenanceType(),
                submission.getOverallStatus(),
                submission.getGlobusFolderId(),
                submission.getGlobusOriginId(),
                null,
                null,
                null,
                null,
                submission.getDateSubmitted(),
                new MetadataDto(submission.getStudies().size(),
                        submission.getAssociations().size(),
                        submission.getSamples().size(),
                        submission.getNotes().size()),
                ProvenanceDtoAssembler.assemble(submission.getCreated(), userOpt.get()));
    }

    @Override
    public List<StudyDto> assembleStudies(String submissionId, Pageable pageable) {
        log.info("Assembling studies for submission: {} - {}", submissionId, pageable.getPageNumber());
        List<StudyDto> result = new ArrayList<>();
        Page<Study> studiesPage = studyRepository.findBySubmissionId(submissionId, pageable);
        for (Study study : studiesPage.getContent()) {
            List<AssociationDto> associationDtos = new ArrayList<>();
            List<SampleDto> sampleDtos = new ArrayList<>();
            List<NoteDto> noteDtos = new ArrayList<>();

            if (study.getStudyTag() != null) {
                List<Association> associations = associationRepository.findByStudyTagAndSubmissionId(study.getStudyTag(), submissionId);
                associationDtos = associations.stream().map(AssociationDtoAssembler::assemble).collect(Collectors.toList());

                List<Sample> samples = sampleRepository.findByStudyTagAndSubmissionId(study.getStudyTag(), submissionId);
                sampleDtos = samples.stream().map(SampleDtoAssembler::assemble).collect(Collectors.toList());

                List<Note> notes = noteRepository.findByStudyTagAndSubmissionId(study.getStudyTag(), submissionId);
                noteDtos = notes.stream().map(NoteDtoAssembler::assemble).collect(Collectors.toList());
            }

            result.add(StudyDtoAssembler.assemble(study, associationDtos, sampleDtos, noteDtos));
        }

        return result;
    }
}
