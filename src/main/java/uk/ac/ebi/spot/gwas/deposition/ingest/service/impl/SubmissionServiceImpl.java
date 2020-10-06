package uk.ac.ebi.spot.gwas.deposition.ingest.service.impl;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    public List<Submission> getSubmissions(String publicationId) {
        log.info("Retrieving submissions.");
        if (publicationId != null) {
            Optional<Submission> optionalSubmission = submissionRepository.findByPublicationIdAndArchived(publicationId, false);
            if (optionalSubmission.isPresent()) {
                return Arrays.asList(new Submission[] {optionalSubmission.get()});
            }
            return new ArrayList<>();
        }
        List<Submission> submissions = submissionRepository.findByArchived(false);
        log.info("Found {} submissions.", submissions.size());
        return submissions;
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
