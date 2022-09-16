package uk.ac.ebi.spot.gwas.deposition.ingest.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.domain.DiseaseTrait;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.DiseaseTraitRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.DiseaseTraitService;

import java.util.List;

@Service
public class DiseaseTraitServiceImpl implements DiseaseTraitService {

    @Autowired
    DiseaseTraitRepository diseaseTraitRepository;

    public List<DiseaseTrait> getDiseaseTraits() {
        return diseaseTraitRepository.findAll();
    }

}
