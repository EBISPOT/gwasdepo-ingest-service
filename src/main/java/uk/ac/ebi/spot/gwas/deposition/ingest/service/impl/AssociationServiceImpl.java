package uk.ac.ebi.spot.gwas.deposition.ingest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.domain.Association;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.AssociationRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.AssociationService;

@Service
public class AssociationServiceImpl implements AssociationService {

    @Autowired
    private AssociationRepository associationRepository;

    @Override
    public Page<Association> getAssociationBySubmission(String submissionId, Pageable pageable) {
        return associationRepository.findBySubmissionId(submissionId, pageable);
    }
}
