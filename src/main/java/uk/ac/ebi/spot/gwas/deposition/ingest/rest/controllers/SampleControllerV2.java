package uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping(value = GeneralCommon.API_V2 + IngestServiceConstants.API_STUDIES)
public class SampleControllerV2 {

    @Autowired
    private SampleService sampleService;

    @Autowired
    private SampleAssemblerV2 sampleAssemblerV2;

    /**
     * GET /v2/studies/<studyAccessionId>/samples
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{studyAccessionId}/" + IngestServiceConstants.API_SAMPLES, produces = MediaTypes.HAL_JSON_VALUE)
    public PagedResources<Resource<SampleDto>> getSubmissions(@PathVariable("studyAccessionId") String studyAccessionId,
                                                              PagedResourcesAssembler<Sample> assembler,
                                                              @PageableDefault(size = 10, page = 0) Pageable pageable) {

        log.info("Request to retrieve sample for study: {} - {}", studyAccessionId, pageable.getPageNumber());
        Page<Sample> samples = sampleService.getSamplesByAccessionId(studyAccessionId, pageable);
        return assembler.toResource(samples, sampleAssemblerV2);
    }

    /**
     * GET /v1/submissions/<submissionId>/studyTag/associations&page=<page>&size=<size>
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{submissionId}/" + IngestServiceConstants.PARAM_STUDY_TAG + IngestServiceConstants.API_SAMPLES,
            produces = MediaTypes.HAL_JSON_VALUE)
    public PagedResources<Resource<SampleDto>> getSamples(@PathVariable(IngestServiceConstants.PARAM_SUBMISSIONID) String submissionId,
                                                          @RequestParam(value = IngestServiceConstants.PARAM_STUDY_TAG,
                                                                  required = true) String studyTag,
                                                              PagedResourcesAssembler<Sample> assembler,
                                                          @PageableDefault(size = 10, page = 0) Pageable pageable) {
        log.info("Request to retrieve association for submission: {} - {}", submissionId, pageable.getPageNumber());
        Page<Sample> samples = sampleService.getSampleBySubmissionAndStudyTag(submissionId, studyTag, pageable);
        return assembler.toResource(samples, sampleAssemblerV2);
    }


}
