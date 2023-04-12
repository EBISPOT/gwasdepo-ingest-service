package uk.ac.ebi.spot.gwas.deposition.ingest.service.impl;

import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.domain.EfoTrait;
import uk.ac.ebi.spot.gwas.deposition.dto.curation.EfoTraitDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.EFOTraitIngestDTO;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.EFOTraitAssemblyService;

@Service
public class EFOTraitAssemblyServiceImpl implements EFOTraitAssemblyService {

    public EFOTraitIngestDTO assemble(EfoTrait efoTrait) {

        EFOTraitIngestDTO  efoTraitIngestDTO = EFOTraitIngestDTO.builder()
               .trait(efoTrait.getTrait())
                .shortForm(efoTrait.getShortForm())
                .uri(efoTrait.getUri())
                .mongoSeqId(efoTrait.getId())
                .build();



        return efoTraitIngestDTO;
    }





    public EfoTraitDto assembleDTO(EfoTrait efoTrait) {


        return  EfoTraitDto.builder()
                .trait(efoTrait.getTrait())
                .shortForm(efoTrait.getShortForm())
                .uri(efoTrait.getUri())
                .build();

    }
}
