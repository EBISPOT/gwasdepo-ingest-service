package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import uk.ac.ebi.spot.gwas.deposition.domain.Submission;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionEnvelopeDto;

import java.util.List;

public interface SubmissionAssemblyService {

    SubmissionDto assemble(Submission submission);

    List<SubmissionEnvelopeDto> assembleEnvelopes(List<Submission> submissions);
}
