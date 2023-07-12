package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.spot.gwas.deposition.domain.Sample;

public interface SampleService {


    Page<Sample> getSampleBySubmission(String submissionId, Pageable pageable);

    Page<Sample> getSampleBySubmissionAndStudyTag(String submissionId, String studyTag,  Pageable pageable);

    Page<Sample> getSamplesByAccessionId(String accessionId, Pageable pageable);

}
