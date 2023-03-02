package uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;
import uk.ac.ebi.spot.gwas.deposition.domain.DiseaseTrait;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.DiseaseTraitIngestDTO;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto.DiseaseTraitAssembler;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.DiseaseTraitService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = GeneralCommon.API_V1 + IngestServiceConstants.API_DISEASE_TRAITS)
public class DiseaseTraitController {

    @Autowired
    DiseaseTraitService diseaseTraitService;

    @Autowired
    DiseaseTraitAssembler diseaseTraitAssembler;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DiseaseTraitIngestDTO> getDiseaseTraits() {

        List<DiseaseTrait> pagedDiseaseTraits =  diseaseTraitService.getDiseaseTraits();

       return  pagedDiseaseTraits.stream().map(diseaseTrait -> diseaseTraitAssembler.assemble(diseaseTrait))
                .collect(Collectors.toList());

    }

}
