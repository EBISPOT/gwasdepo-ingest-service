package uk.ac.ebi.spot.gwas.deposition.ingest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.domain.Sample;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.SampleRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.SampleService;

@Service
public class SampleServiceImpl implements SampleService {

    @Autowired
    private SampleRepository sampleRepository;

    @Override
    public Page<Sample> getSampleBySubmission(String submissionId, Pageable pageable) {
        return sampleRepository.findBySubmissionId(submissionId, pageable);
    }
}
