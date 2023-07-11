package uk.ac.ebi.spot.gwas.deposition.ingest.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;
import uk.ac.ebi.spot.gwas.deposition.domain.Publication;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionEnvelopeDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.PublicationService;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.SubmissionService;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto.SubmissionAssembler;

import java.util.List;

@RestController
@RequestMapping(value = GeneralCommon.API_V1 + IngestServiceConstants.API_SUBMISSION_ENVELOPES)
public class EnvelopeSubmissionsController {

    private static final Logger log = LoggerFactory.getLogger(EnvelopeSubmissionsController.class);

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    PublicationService publicationService;

    @Autowired
    private SubmissionAssembler submissionAssembler;

    /**
     * GET /v1/submission-envelopes
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<SubmissionEnvelopeDto> getSubmissions(@RequestParam(value = IngestServiceConstants.PARAM_PMID, required = false) String pmid) {
        log.info("Request to retrieve all submissions.");
        Publication publication = null;
        if(pmid != null) {
            publication = publicationService.getPublication(pmid);
        }
        Pageable wholePage = Pageable.unpaged();
        Page<Submission> submissions = submissionService.getSubmissions(publication != null ? publication.getId() : null, null, wholePage);
        log.info("Found {} submissions.", submissions.getTotalElements());
        return submissionAssembler.assembleEnvelopes(submissions);
    }

}
