package uk.ac.ebi.spot.gwas.deposition.ingest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.domain.Sample;
import uk.ac.ebi.spot.gwas.deposition.domain.Study;
import uk.ac.ebi.spot.gwas.deposition.exception.EntityNotFoundException;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.SampleRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.StudyRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.SampleService;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.StudyService;

import java.util.List;
import java.util.Optional;

@Service
public class SampleServiceImpl implements SampleService {

    @Autowired
    private SampleRepository sampleRepository;
    @Autowired
    private StudyService studyService;

    @Override
    public Page<Sample> getSamplesByAccessionId(String accessionId, Pageable pageable) {
        Study study = studyService.getStudy(accessionId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Accession %s not found", accessionId)));
        return sampleRepository.findByStudyTag(study.getStudyTag(), pageable);
    }
}

