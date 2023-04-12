package uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto;

import uk.ac.ebi.spot.gwas.deposition.constants.PublicationStatus;
import uk.ac.ebi.spot.gwas.deposition.domain.CorrespondingAuthor;
import uk.ac.ebi.spot.gwas.deposition.domain.Publication;
import uk.ac.ebi.spot.gwas.deposition.dto.CorrespondingAuthorDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.PublicationDto;

public class PublicationDtoAssembler {

    public static Publication disassemble(PublicationDto publicationDto) {
        CorrespondingAuthor correspondingAuthor = publicationDto.getCorrespondingAuthor() != null ?
                new CorrespondingAuthor(publicationDto.getCorrespondingAuthor().getAuthorName(),
                        publicationDto.getCorrespondingAuthor().getEmail()) : null;
        Publication publication = new Publication();
        publication.setJournal(publicationDto.getJournal());
        publication.setTitle(publicationDto.getTitle());
        publication.setFirstAuthor(publicationDto.getFirstAuthor());
        publication.setPublicationDate(publicationDto.getPublicationDate());
        publication.setCorrespondingAuthor(correspondingAuthor);
        publication.setStatus(publicationDto.getStatus() != null ? publicationDto.getStatus() : PublicationStatus.ELIGIBLE.name());
        return publication;
    }

    public static PublicationDto assemble(Publication publication) {
        CorrespondingAuthorDto correspondingAuthor = publication.getCorrespondingAuthor() != null ?
                new CorrespondingAuthorDto(publication.getCorrespondingAuthor().getAuthorName(),
                        publication.getCorrespondingAuthor().getEmail()) : null;

        return new PublicationDto(publication.getPmid(),
                publication.getTitle(),
                publication.getJournal(),
                publication.getFirstAuthor(),
                publication.getPublicationDate(),
                correspondingAuthor,
                publication.getStatus());
    }
}
