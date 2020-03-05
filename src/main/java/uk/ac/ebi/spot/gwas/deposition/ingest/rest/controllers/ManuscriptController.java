package uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.spot.gwas.deposition.domain.Manuscript;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.ManuscriptDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto.ManuscriptDtoAssembler;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.ManuscriptService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = IngestServiceConstants.API_V1 + IngestServiceConstants.API_MANUSCRIPTS)
public class ManuscriptController {

    private static final Logger log = LoggerFactory.getLogger(PublicationsController.class);

    @Autowired
    private ManuscriptService manuscriptService;

    /**
     * GET /v1/manuscripts/{manuscriptId}
     */
    @GetMapping(value = "/{manuscriptId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ManuscriptDto getManuscript(@PathVariable String manuscriptId) {
        log.info("Request to get manuscript: {}", manuscriptId);
        Manuscript manuscript = manuscriptService.retrieveManuscript(manuscriptId);
        log.info("Returning manuscript: {}", manuscript.getId());
        return ManuscriptDtoAssembler.assemble(manuscript);
    }

    /**
     * GET /v1/manuscripts
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ManuscriptDto> getManuscripts() {
        log.info("Request to retrieve manuscripts.");
        List<Manuscript> manuscripts = manuscriptService.retrieveManuscripts();
        List<ManuscriptDto> manuscriptDtos = manuscripts.stream().map(ManuscriptDtoAssembler::assemble).collect(Collectors.toList());
        return manuscriptDtos;
    }

}
