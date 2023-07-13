package uk.ac.ebi.spot.gwas.deposition.ingest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.domain.Study;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.StudyRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.StudyService;

import java.util.List;
import java.util.Optional;

@Service
public class StudyServiceImpl implements StudyService {

    @Autowired
    private StudyRepository studyRepository;

    @Override
    public Optional<Study> getStudy(String accessionId) {
        List<Study> studies = studyRepository.findByAccession(accessionId);
        Optional<Study> presentStudy = Optional.empty();
        for (Study study : studies){
            if (!study.getSubmissionId().isEmpty()){
                presentStudy = Optional.of(study);
            }
        }
        return presentStudy;
    }

    @Override
    public Page<Study> getStudiesBySubmission(String submissionId, Pageable pageable) {
        return studyRepository.findBySubmissionId(submissionId, pageable);
    }

}

