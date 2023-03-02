package uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;
import uk.ac.ebi.spot.gwas.deposition.domain.EfoTrait;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.EFOTraitIngestDTO;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.EFOTraitAssemblyService;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.EfoTraitService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = GeneralCommon.API_V1 + IngestServiceConstants.API_EFO_TRAITS)
public class EFOTraitsController {

    @Autowired
    EfoTraitService efoTraitService;

    @Autowired
    EFOTraitAssemblyService efoTraitAssemblyService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EFOTraitIngestDTO> getEfoTraits() {
        List<EfoTrait> efoTraits =  efoTraitService.getEfoTraits();
        return efoTraits.stream().map(efoTraitAssemblyService::assemble).collect(Collectors.toList());

    }
}
