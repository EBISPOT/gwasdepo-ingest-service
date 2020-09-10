package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import uk.ac.ebi.spot.gwas.deposition.domain.Submission;

import java.util.List;

public interface SubmissionService {

    Submission getSubmission(String submissionId);

    List<Submission> getSubmissions();

    Submission updateSubmission(String submissionId, String status);

    Submission getSubmissionForPublication(String id);

    boolean submissionExistsForPublication(String id);
}
