package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.domain.Study;

import java.util.Optional;

@Service
public interface StudyService {

    Page<Study> getStudiesBySubmission(String submissionId, Pageable pageable);

    Optional<Study> getOneStudy(Integer studyId);

}
