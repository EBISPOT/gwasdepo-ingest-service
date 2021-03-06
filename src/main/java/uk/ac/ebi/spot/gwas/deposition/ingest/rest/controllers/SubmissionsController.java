package uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;
import uk.ac.ebi.spot.gwas.deposition.domain.Publication;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.PublicationService;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.SubmissionAssemblyService;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.SubmissionService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = GeneralCommon.API_V1 + IngestServiceConstants.API_SUBMISSIONS)
public class SubmissionsController {

    private static final Logger log = LoggerFactory.getLogger(SubmissionsController.class);

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private SubmissionAssemblyService submissionAssemblyService;

    @Autowired
    private PublicationService publicationService;

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
        return submissionAssemblyService.assemble(submission);
    }

    /**
     * GET /v1/submissions
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<SubmissionDto> getSubmissions(@RequestParam(value = IngestServiceConstants.PARAM_PMID, required = false)
                                                      String pmid,
                                              @RequestParam(value = IngestServiceConstants.PARAM_STATUS, required = false)
                                                      String status) {
        log.info("Request to retrieve all submissions - including for PMID: {} | {}", pmid, status);
        String pubId = null;
        if (pmid != null) {
            try {
                Publication publication = publicationService.getPublication(pmid);
                log.info("Found publication: {}", publication.getId());
                pubId = publication.getId();
            } catch (Exception e) {
                log.error("Error when retrieving publication [{}]: {}", pmid, e.getMessage(), e);
            }
        }

        List<Submission> submissions = submissionService.getSubmissions(pubId, status);
        log.info("Found {} submissions.", submissions.size());
        List<SubmissionDto> submissionDtos = new ArrayList<>();
        for (Submission submission : submissions) {
            submissionDtos.add(submissionAssemblyService.assemble(submission));
        }
        return submissionDtos;
    }

    /**
     * PUT /v1/submissions/{submissionId}
     */
    @PutMapping(value = "/{submissionId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SubmissionDto updateSubmission(@PathVariable String submissionId,
                                          @RequestBody SubmissionDto submissionDto) {
        log.info("Request to update status for submission: {}", submissionId);
        Submission submission = submissionService.updateSubmission(submissionId, submissionDto.getStatus());
        log.info("Returning submission: {}", submission.getId());
        return submissionAssemblyService.assemble(submission);
    }
}
