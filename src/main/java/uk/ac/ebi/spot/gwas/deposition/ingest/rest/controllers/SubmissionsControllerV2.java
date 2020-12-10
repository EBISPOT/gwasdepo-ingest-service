package uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.SubmissionAssemblyServiceV2;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.SubmissionService;

@RestController
@RequestMapping(value = GeneralCommon.API_V2 + IngestServiceConstants.API_SUBMISSIONS)
public class SubmissionsControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(SubmissionsControllerV2.class);

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private SubmissionAssemblyServiceV2 submissionAssemblyServiceV2;

    /**
     * GET /v1/submissions/{submissionId}
     */
    @GetMapping(value = "/{submissionId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SubmissionDto getSubmission(@PathVariable String submissionId) {
        log.info("Request to retrieve submission: {}", submissionId);
        Submission submission = submissionService.getSubmission(submissionId);
        log.info("Returning submission: {}", submission.getId());
        return submissionAssemblyServiceV2.assemble(submission);
    }

}
