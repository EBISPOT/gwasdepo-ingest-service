package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import uk.ac.ebi.spot.gwas.deposition.domain.Study;

import java.util.Optional;

public interface StudyService {

    Optional<Study> getStudy(String accessionId);
}
