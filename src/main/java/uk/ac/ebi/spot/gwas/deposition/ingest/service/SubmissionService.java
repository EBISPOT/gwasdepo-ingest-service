package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;

import java.util.List;

public interface SubmissionService {

    Submission getSubmission(String submissionId);

    Page<Submission> getSubmissions(String publicationId, String status, Pageable pageable);

    Submission updateSubmission(String submissionId, String status);

    Submission getSubmissionForPublication(String id);

    boolean submissionExistsForPublication(String id);

    Page<Submission> getSubmissions(Pageable pageable);

    Long countSubmissions();
}
