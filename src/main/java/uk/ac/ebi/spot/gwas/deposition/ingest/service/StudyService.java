package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.spot.gwas.deposition.domain.Study;

import java.util.Optional;

public interface StudyService {

    Optional<Study> getStudy(String accessionId);

    Page<Study> getStudiesBySubmission(String submissionId, Pageable pageable);
}
