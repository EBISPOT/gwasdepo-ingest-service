package uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.domain.DiseaseTrait;
import uk.ac.ebi.spot.gwas.deposition.dto.curation.DiseaseTraitDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.DiseaseTraitIngestDTO;

@Component
public class DiseaseTraitAssembler {

    public DiseaseTraitIngestDTO assemble(DiseaseTrait diseaseTrait) {
        return DiseaseTraitIngestDTO.builder()
                .trait(diseaseTrait.getTrait())
                .mongoSeqId(diseaseTrait.getId())
                .build();
    }

    public DiseaseTraitDto assembleDTO(DiseaseTrait diseaseTrait) {

        return DiseaseTraitDto.builder()
                .trait(diseaseTrait.getTrait())
                .build();
    }
}
