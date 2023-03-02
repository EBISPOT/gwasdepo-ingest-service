package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.spot.gwas.deposition.domain.Association;

public interface AssociationService {

    Page<Association> getAssociationBySubmission(String submissionId, Pageable pageable);
}
