package uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;
import uk.ac.ebi.spot.gwas.deposition.domain.Sample;
import uk.ac.ebi.spot.gwas.deposition.dto.SampleDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto.SampleAssemblerV2;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.SampleService;

@Slf4j
@RestController
@RequestMapping(value = GeneralCommon.API_V1 + IngestServiceConstants.API_SUBMISSIONS)
public class SamplesController {

    @Autowired
    private SampleService sampleService;

    @Autowired
    private SampleAssemblerV2 sampleAssembler;

    /**
     * GET /v1/submissions/submissionId/<submissionId>/samples&page=<page>
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{submissionId}/" + IngestServiceConstants.API_SAMPLES,
            produces = MediaTypes.HAL_JSON_VALUE)
    public PagedResources<Resource<SampleDto>> getSubmissions(@PathVariable(IngestServiceConstants.PARAM_SUBMISSIONID) String submissionId,
                                                              PagedResourcesAssembler<Sample> assembler,
                                                              Pageable pageable) {
        log.info("Request to retrieve association for submission: {} - {}", submissionId, pageable.getPageNumber());
        Page<Sample> samples = sampleService.getSampleBySubmission(submissionId, pageable);
        return assembler.toResource(samples, sampleAssembler);
    }

}
