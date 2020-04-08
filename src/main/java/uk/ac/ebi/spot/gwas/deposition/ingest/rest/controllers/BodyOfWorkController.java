package uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;
import uk.ac.ebi.spot.gwas.deposition.domain.BodyOfWork;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.BodyOfWorkDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto.BodyOfWorkDtoAssembler;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.BodyOfWorkService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = GeneralCommon.API_V1 + IngestServiceConstants.API_BODY_OF_WORK)
public class BodyOfWorkController {

    private static final Logger log = LoggerFactory.getLogger(PublicationsController.class);

    @Autowired
    private BodyOfWorkService bodyOfWorkService;

    /**
     * GET /v1/bodyofwork/{bodyofworkId}
     */
    @GetMapping(value = "/{bodyofworkId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public BodyOfWorkDto getBodyOfWork(@PathVariable String bodyofworkId) {
        log.info("Request to get body of work: {}", bodyofworkId);
        BodyOfWork bodyOfWork = bodyOfWorkService.retrieveBodyOfWork(bodyofworkId);
        log.info("Returning body of work: {}", bodyOfWork.getBowId());
        return BodyOfWorkDtoAssembler.assemble(bodyOfWork);
    }

    /**
     * GET /v1/bodyofwork
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<BodyOfWorkDto> getBodyOfWorks() {
        log.info("Request to retrieve body of works.");
        List<BodyOfWork> facetedBodyOfWorks = bodyOfWorkService.retrieveBodyOfWorks();
        return facetedBodyOfWorks.stream().map(BodyOfWorkDtoAssembler::assemble).collect(Collectors.toList());
    }
}
