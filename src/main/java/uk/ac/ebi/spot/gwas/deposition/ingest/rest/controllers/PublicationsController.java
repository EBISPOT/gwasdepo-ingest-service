package uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.spot.gwas.deposition.domain.Publication;
import uk.ac.ebi.spot.gwas.deposition.domain.SSTemplateEntryPlaceholder;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.ExtendedPublicationDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.PublicationDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto.ExtendedPublicationDtoAssembler;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto.PublicationDtoAssembler;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.PublicationService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = IngestServiceConstants.API_V1 + IngestServiceConstants.API_PUBLICATIONS)
public class PublicationsController {

    private static final Logger log = LoggerFactory.getLogger(PublicationsController.class);

    @Autowired
    private PublicationService publicationService;

    /**
     * GET /v1/publications
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<PublicationDto> getPublications() {
        log.info("Request to get publications");
        List<Publication> publicationList = publicationService.getPublications();
        log.info("Found {} publications.", publicationList.size());
        List<PublicationDto> publicationDtos = publicationList.stream().map(PublicationDtoAssembler::assemble).collect(Collectors.toList());
        return publicationDtos;
    }

    /**
     * POST /v1/publications
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createPublication(@Valid @RequestBody ExtendedPublicationDto extendedPublicationDto) {
        log.info("Request to create publication: {}.", extendedPublicationDto.getPmid());
        Publication publication = PublicationDtoAssembler.disassemble(ExtendedPublicationDtoAssembler.disassemble(extendedPublicationDto));
        SSTemplateEntryPlaceholder ssTemplateEntryPlaceholder = ExtendedPublicationDtoAssembler.disassembleTemplateEntries(extendedPublicationDto);
        publicationService.createPublication(publication, ssTemplateEntryPlaceholder);
    }

    /**
     * PUT /v1/publications/{pmid}
     */
    @PutMapping(value = "/{pmid}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable String pmid, @Valid @RequestBody ExtendedPublicationDto extendedPublicationDto) {
        log.info("Request to update status of publication: {} - {}.", pmid, extendedPublicationDto.getStatus());
        Publication publication = PublicationDtoAssembler.disassemble(ExtendedPublicationDtoAssembler.disassemble(extendedPublicationDto));
        SSTemplateEntryPlaceholder ssTemplateEntryPlaceholder = ExtendedPublicationDtoAssembler.disassembleTemplateEntries(extendedPublicationDto);
        publicationService.updatePublication(publication, ssTemplateEntryPlaceholder);
    }

    /**
     * GET /v1/publications/{pmid}
     */
    @GetMapping(value = "/{pmid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public PublicationDto getPublication(@PathVariable String pmid) {
        log.info("Request to get publication: {}", pmid);
        Publication publication = publicationService.getPublication(pmid);
        return PublicationDtoAssembler.assemble(publication);
    }

}
