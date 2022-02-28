package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import uk.ac.ebi.spot.gwas.deposition.domain.EfoTrait;

import java.util.List;

public interface EfoTraitService {

    public List<EfoTrait> getEfoTraits();
}
