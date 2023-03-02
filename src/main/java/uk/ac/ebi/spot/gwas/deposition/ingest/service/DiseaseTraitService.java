package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import uk.ac.ebi.spot.gwas.deposition.domain.DiseaseTrait;

import java.util.List;

public interface DiseaseTraitService {

    public List<DiseaseTrait> getDiseaseTraits();
}
