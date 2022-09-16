package uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto;

import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.domain.EfoTrait;
import uk.ac.ebi.spot.gwas.deposition.dto.curation.EfoTraitDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.EFOTraitIngestDTO;

@Service
public class EFOTraitAssembler {

    public EFOTraitIngestDTO assemble(EfoTrait efoTrait) {
        return EFOTraitIngestDTO.builder()
               .trait(efoTrait.getTrait())
                .shortForm(efoTrait.getShortForm())
                .uri(efoTrait.getUri())
                .mongoSeqId(efoTrait.getId())
                .build();
    }

    public EfoTraitDto assembleDTO(EfoTrait efoTrait) {
        return  EfoTraitDto.builder()
                .trait(efoTrait.getTrait())
                .shortForm(efoTrait.getShortForm())
                .uri(efoTrait.getUri())
                .build();
    }
}
