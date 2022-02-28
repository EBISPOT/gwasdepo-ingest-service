package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import uk.ac.ebi.spot.gwas.deposition.domain.EfoTrait;
import uk.ac.ebi.spot.gwas.deposition.dto.curation.EFOTraitWrapperDTO;
import uk.ac.ebi.spot.gwas.deposition.dto.curation.EfoTraitDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.EFOTraitIngestDTO;

public interface EFOTraitAssemblyService {

    public EFOTraitIngestDTO assemble(EfoTrait efoTrait);

    public EfoTraitDto assembleDTO(EfoTrait efoTrait);
}
