package uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;
import uk.ac.ebi.spot.gwas.deposition.domain.Study;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;
import uk.ac.ebi.spot.gwas.deposition.dto.StudyDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto.StudyDtoAssembler;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.StudyService;


import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = GeneralCommon.API_V1 + IngestServiceConstants.API_SUBMISSIONS)
public class StudiesController {

    @Autowired
    private StudyService studyService;

    @Autowired
    private StudyDtoAssembler studyAssembler;

    /**
     * GET /v1/submissions/submissionId/<submissionId>/studies&page=<page>
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{submissionId}/" + IngestServiceConstants.API_STUDIES, produces = MediaTypes.HAL_JSON_VALUE)
    public PagedResources<Resource<StudyDto>> getSubmissions(@PathVariable(IngestServiceConstants.PARAM_SUBMISSIONID) String submissionId,
                                                            PagedResourcesAssembler<Study> assembler,
                                                             @PageableDefault(size = 10, page = 0) Pageable pageable) {
        log.info("Request to retrieve studies for submission: {} - {}", submissionId, pageable.getPageNumber());
        Page<Study> studies = studyService.getStudiesBySubmission(submissionId, pageable);
        return assembler.toResource(studies, studyAssembler);
    }




}
