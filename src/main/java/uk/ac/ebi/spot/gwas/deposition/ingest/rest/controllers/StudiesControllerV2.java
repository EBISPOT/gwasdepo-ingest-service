package uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;
import uk.ac.ebi.spot.gwas.deposition.dto.StudyDto;
import uk.ac.ebi.spot.gwas.deposition.exception.EntityNotFoundException;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto.StudyDtoAssembler;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.StudyService;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.SubmissionAssemblyServiceV2;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.SubmissionService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = GeneralCommon.API_V2 + IngestServiceConstants.API_STUDIES)
public class StudiesControllerV2 {

    @Autowired
    private StudyService studyService;
    @Autowired
    private StudyDtoAssembler studyDtoAssembler;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private SubmissionAssemblyServiceV2 submissionAssemblyServiceV2;

    /**
     * GET /v1/studies?submissionId=<submissionId>&page=<page>
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<StudyDto> getSubmission(@RequestParam(value = IngestServiceConstants.PARAM_SUBMISSIONID) String submissionId,
                                        @PageableDefault(size = 100) Pageable pageable) {
        log.info("Request to retrieve studies for submission: {} - {}", submissionId, pageable.getPageNumber());
        Submission submission = submissionService.getSubmission(submissionId);
        log.info("Returning studies for submission: {}", submission.getId());
        return submissionAssemblyServiceV2.assembleStudies(submission.getId(), pageable);
    }

    @GetMapping("{studyAccessionId}")
    public ResponseEntity<StudyDto> getOneStudyByAccessionId(@PathVariable("studyAccessionId") String studyAccessionId) {
        return studyService.getStudy(studyAccessionId)
                .map(studyDtoAssembler::assemble)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Accession %s not found", studyAccessionId)));
    }
}
