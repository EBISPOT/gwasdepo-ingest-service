package uk.ac.ebi.spot.gwas.deposition.ingest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.domain.Study;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.StudyRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.StudyService;

import java.util.Optional;

@Service
public class StudyServiceimpl implements StudyService {

    @Autowired
    private StudyRepository studyRepository;

    @Override
    public Page<Study> getStudiesBySubmission(String submissionId, Pageable pageable) {
        return studyRepository.findBySubmissionId(submissionId, pageable);
    }

    @Override
    public Optional<Study> getOneStudy(Integer studyId) {
        return Optional.empty();
    }
}
