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
import uk.ac.ebi.spot.gwas.deposition.domain.User;
import uk.ac.ebi.spot.gwas.deposition.dto.ProvenanceDto;
import uk.ac.ebi.spot.gwas.deposition.exception.EntityNotFoundException;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.UserRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto.ProvenanceDtoAssembler;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.PublicationService;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.SubmissionService;

import java.util.Optional;

@RestController
@RequestMapping(value = GeneralCommon.API_V1 + IngestServiceConstants.API_PROVENANCE)
public class ProvenanceController {

    private static final Logger log = LoggerFactory.getLogger(ProvenanceController.class);

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublicationService publicationService;

    /**
     * GET /v1/provenance
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ProvenanceDto getSubmissionProvenanceForPMID(@RequestParam(value = IngestServiceConstants.PARAM_PMID) String pmid) {
        log.info("Request to retrieve provenance for: {}", pmid);
        Publication publication = publicationService.getPublication(pmid);
        log.info("Found publication: {}", publication.getId());
        Submission submission = submissionService.getSubmissionForPublication(publication.getId());
        log.info("Found submission: {}", submission.getId());
        Optional<User> userOpt = userRepository.findById(submission.getCreated().getUserId());
        if (!userOpt.isPresent()) {
            log.error("Unable to find user: {}", submission.getCreated().getUserId());
            throw new EntityNotFoundException("Unable to find user: " + submission.getCreated().getUserId());
        }

        return ProvenanceDtoAssembler.assemble(submission.getCreated(), userOpt.get());
    }

}
