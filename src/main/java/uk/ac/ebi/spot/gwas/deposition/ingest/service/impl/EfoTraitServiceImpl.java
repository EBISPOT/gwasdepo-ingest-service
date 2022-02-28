package uk.ac.ebi.spot.gwas.deposition.ingest.service.impl;

import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.domain.EfoTrait;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.EfoTraitRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.EfoTraitService;

import java.util.List;

@Service
public class EfoTraitServiceImpl implements EfoTraitService {

    private final EfoTraitRepository efoTraitRepository;


    public EfoTraitServiceImpl(EfoTraitRepository efoTraitRepository) {
        this.efoTraitRepository = efoTraitRepository;

    }

    @Override
    public List<EfoTrait> getEfoTraits() {

        return efoTraitRepository.findAll();
    }


}
