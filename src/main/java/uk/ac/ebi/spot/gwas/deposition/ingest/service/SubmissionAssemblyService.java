package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import org.springframework.data.domain.Page;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionEnvelopeDto;

import java.util.List;

public interface SubmissionAssemblyService {

    SubmissionDto assemble(Submission submission);

    List<SubmissionEnvelopeDto> assembleEnvelopes(Page<Submission> submissions);
}
