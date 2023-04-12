package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import uk.ac.ebi.spot.gwas.deposition.domain.DiseaseTrait;
import uk.ac.ebi.spot.gwas.deposition.dto.curation.DiseaseTraitDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.DiseaseTraitIngestDTO;

public interface DiseaseTraitAssemblyService {

    public DiseaseTraitIngestDTO assemble(DiseaseTrait diseaseTrait);

    public DiseaseTraitDto assembleDTO(DiseaseTrait diseaseTrait);


}
