package uk.ac.ebi.spot.gwas.deposition.ingest.service.impl;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.constants.Status;
import uk.ac.ebi.spot.gwas.deposition.constants.SubmissionProvenanceType;
import uk.ac.ebi.spot.gwas.deposition.domain.CompletedSubmission;
import uk.ac.ebi.spot.gwas.deposition.domain.Provenance;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;
import uk.ac.ebi.spot.gwas.deposition.domain.User;
import uk.ac.ebi.spot.gwas.deposition.exception.EntityNotFoundException;
import uk.ac.ebi.spot.gwas.deposition.ingest.config.IngestServiceConfig;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.CompletedSubmissionRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.SubmissionRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.UserRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.SubmissionService;
import uk.ac.ebi.spot.gwas.deposition.ingest.util.SubmissionsUtil;

import java.util.*;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private static final Logger log = LoggerFactory.getLogger(SubmissionService.class);

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private IngestServiceConfig ingestServiceConfig;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompletedSubmissionRepository completedSubmissionRepository;

    @Override
    public Submission getSubmission(String submissionId) {
        log.info("Retrieving submission: {}", submissionId);
        Optional<Submission> optionalSubmission = submissionRepository.findByIdAndArchived(submissionId, false);
        if (!optionalSubmission.isPresent()) {
            log.error("Unable to find submission: {}", submissionId);
            throw new EntityNotFoundException("Unable to find submission: " + submissionId);
        }
        log.info("Submission successfully retrieved: {}", optionalSubmission.get().getId());
        return optionalSubmission.get();
    }

    @Override
    public Page<Submission>  getSubmissions(String publicationId, String status, Pageable pageable) {
        log.info("Retrieving submissions: {} | {}", publicationId, status);
        if (publicationId != null) {
            Optional<Submission> optionalSubmission = submissionRepository.findByPublicationIdAndArchived(publicationId, false);
            if (optionalSubmission.isPresent()) {
                return new PageImpl<>(Collections.singletonList(optionalSubmission.get()));
            }
            return new PageImpl<>(new ArrayList<>());
        }
        Page<Submission> submissions;
        if (status == null) {
            submissions = submissionRepository.findByArchived(false, pageable);
        } else {
            if (status.equalsIgnoreCase("OTHER")) {
                submissions = SubmissionsUtil.filterForOther(submissionRepository.findByArchived(false, pageable));
            } else {
                if (status.equalsIgnoreCase("READY_TO_IMPORT")) {
                    submissions = submissionRepository.findByOverallStatusAndArchivedAndPublicationIdIsNotNull(Status.SUBMITTED.name(), false, pageable);
                } else {
                    submissions = submissionRepository.findByOverallStatusAndArchived(status, false, pageable);
                }
            }
        }

        log.info("Found {} submissions.", submissions.getTotalElements());
        return submissions;
    }

    public Page<Submission> getSubmissions(Pageable pageable) {

        return submissionRepository.findByArchived(false, pageable);
    }

    public Long countSubmissions() {
        log.info("Count of Submissions block");
        return submissionRepository.countByArchived(false);
    }

    @Override
    public Submission updateSubmission(String submissionId, String status) {
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(ingestServiceConfig.getAutoCuratorServiceAccount());
        User curatorUser = userOptional.get();

        log.info("Updating submission: {} - {}", submissionId, status);
        Optional<Submission> optionalSubmission = submissionRepository.findByIdAndArchived(submissionId, false);
        if (!optionalSubmission.isPresent()) {
            log.error("Unable to find submission: {}", submissionId);
            throw new EntityNotFoundException("Unable to find submission: " + submissionId);
        }
        log.info("Submission successfully retrieved: {}", optionalSubmission.get().getId());
        Submission submission = optionalSubmission.get();
        submission.setOverallStatus(status);
        submission.setLastUpdated(new Provenance(DateTime.now(), curatorUser.getId()));
        submission = submissionRepository.save(submission);

        if (submission.getProvenanceType().equalsIgnoreCase(SubmissionProvenanceType.PUBLICATION.name())) {
            if (status.equalsIgnoreCase(Status.CURATION_COMPLETE.name())) {
                CompletedSubmission completedSubmission = completedSubmissionRepository.insert(new CompletedSubmission(submissionId, submission.getPublicationId()));
                log.info("Inserted new completed submission: {} | {} | {}", completedSubmission.getId(),
                        completedSubmission.getSubmissionId(),
                        completedSubmission.getPublicationId());
            }
        }
        return submission;
    }

    @Override
    public Submission getSubmissionForPublication(String id) {
        log.info("Retrieving submission for publication: {}", id);
        Optional<Submission> optionalSubmission = submissionRepository.findByPublicationIdAndArchived(id, false);
        if (!optionalSubmission.isPresent()) {
            log.error("Unable to find submission for publication: {}", id);
            throw new EntityNotFoundException("Unable to find submission for publication: " + id);
        }
        log.info("Submission successfully retrieved: {}", optionalSubmission.get().getId());
        return optionalSubmission.get();
    }

    @Override
    public boolean submissionExistsForPublication(String id) {
        log.info("Verifying if submission exists for publication: {}", id);
        Optional<Submission> optionalSubmission = submissionRepository.findByPublicationIdAndArchived(id, false);
        log.info("Submission exists: {}", optionalSubmission.isPresent());
        return optionalSubmission.isPresent();
    }

}
