package uk.ac.ebi.spot.gwas.deposition.ingest.util;

import uk.ac.ebi.spot.gwas.deposition.constants.Status;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;

import java.util.ArrayList;
import java.util.List;

public class SubmissionsUtil {

    public static List<Submission> filterForOther(List<Submission> submissions) {
        List<Submission> result = new ArrayList<>();
        for (Submission submission : submissions) {
            if (submission.getOverallStatus().equalsIgnoreCase(Status.CURATION_COMPLETE.name()) ||
                    submission.getOverallStatus().equalsIgnoreCase(Status.SUBMITTED.name())) {
                continue;
            }

            result.add(submission);
        }
        return result;
    }
}
