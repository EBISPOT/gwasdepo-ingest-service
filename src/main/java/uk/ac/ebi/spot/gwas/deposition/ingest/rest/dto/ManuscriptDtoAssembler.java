package uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto;

import uk.ac.ebi.spot.gwas.deposition.domain.Manuscript;
import uk.ac.ebi.spot.gwas.deposition.dto.CorrespondingAuthorDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.ManuscriptDto;

public class ManuscriptDtoAssembler {

    public static ManuscriptDto assemble(Manuscript manuscript) {
        return new ManuscriptDto(manuscript.getId(),
                manuscript.getTitle(),
                manuscript.getJournal(),
                manuscript.getFirstAuthor(),
                manuscript.getSubmissionDate(),
                manuscript.getAcceptanceDate(),
                manuscript.getCorrespondingAuthor() != null ?
                        new CorrespondingAuthorDto(manuscript.getCorrespondingAuthor().getAuthorName(),
                                manuscript.getCorrespondingAuthor().getEmail()) : null,
                manuscript.getStatus());
    }
}
