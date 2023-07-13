package uk.ac.ebi.spot.gwas.deposition.ingest.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import uk.ac.ebi.spot.gwas.deposition.constants.Status;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;

import java.util.ArrayList;
import java.util.List;

public class SubmissionsUtil {

    public static Page<Submission> filterForOther(Page<Submission> submissions) {
        List<Submission> result = new ArrayList<>();
        for (Submission submission : submissions) {
            if (submission.getOverallStatus().equalsIgnoreCase(Status.CURATION_COMPLETE.name())) {
                continue;
            }
            if (submission.getOverallStatus().equalsIgnoreCase(Status.SUBMITTED.name()) &&
                    submission.getPublicationId() != null) {
                continue;
            }

            result.add(submission);
        }
        return new PageImpl<>(result, submissions.getPageable(), submissions.getTotalElements());
    }

    public static Page<Submission> filterForReadyToImport(Page<Submission> submissions) {
        List<Submission> result = new ArrayList<>();
        for (Submission submission : submissions) {
            if (submission.getPublicationId() == null) {
                continue;
            }

            result.add(submission);
        }
        return new PageImpl<>(result, submissions.getPageable(), submissions.getTotalElements());
    }
}
