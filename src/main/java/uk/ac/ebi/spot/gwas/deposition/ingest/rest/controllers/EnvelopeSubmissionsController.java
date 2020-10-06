package uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionEnvelopeDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.SubmissionAssemblyService;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.SubmissionService;

import java.util.List;

@RestController
@RequestMapping(value = GeneralCommon.API_V1 + IngestServiceConstants.API_SUBMISSION_ENVELOPES)
public class EnvelopeSubmissionsController {

    private static final Logger log = LoggerFactory.getLogger(EnvelopeSubmissionsController.class);

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private SubmissionAssemblyService submissionAssemblyService;

    /**
     * GET /v1/submission-envelopes
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<SubmissionEnvelopeDto> getSubmissions() {
        log.info("Request to retrieve all submissions.");
        List<Submission> submissions = submissionService.getSubmissions(null);
        log.info("Found {} submissions.", submissions.size());
        List<SubmissionEnvelopeDto> submissionDtos = submissionAssemblyService.assembleEnvelopes(submissions);
        return submissionDtos;
    }

}
