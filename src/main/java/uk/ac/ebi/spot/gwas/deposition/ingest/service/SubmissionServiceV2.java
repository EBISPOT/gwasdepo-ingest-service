package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import org.springframework.data.domain.Pageable;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;
import uk.ac.ebi.spot.gwas.deposition.dto.StudyDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionEnvelopeDto;

import java.util.List;

public interface SubmissionServiceV2 {

    SubmissionDto assemble(Submission submission);

    List<StudyDto> assembleStudies(String id, Pageable pageable);
}
