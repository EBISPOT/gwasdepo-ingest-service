package uk.ac.ebi.spot.gwas.deposition.ingest.service.impl;

import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.domain.DiseaseTrait;
import uk.ac.ebi.spot.gwas.deposition.dto.curation.DiseaseTraitDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.DiseaseTraitIngestDTO;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.DiseaseTraitAssemblyService;

@Service
public class DiseaseTraitAssemblyServiceImpl implements DiseaseTraitAssemblyService {

    public DiseaseTraitIngestDTO assemble(DiseaseTrait diseaseTrait) {

        DiseaseTraitIngestDTO diseaseTraitDTO = DiseaseTraitIngestDTO.builder()
                .trait(diseaseTrait.getTrait())
                .mongoSeqId(diseaseTrait.getId())
                .build();


        return diseaseTraitDTO;
    }

    public DiseaseTraitDto assembleDTO(DiseaseTrait diseaseTrait) {

        DiseaseTraitDto diseaseTraitDTO = DiseaseTraitDto.builder()
                .trait(diseaseTrait.getTrait())
                .build();


        return diseaseTraitDTO;
    }
}
