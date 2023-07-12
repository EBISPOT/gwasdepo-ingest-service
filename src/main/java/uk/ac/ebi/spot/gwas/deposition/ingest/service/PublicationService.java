package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import uk.ac.ebi.spot.gwas.deposition.domain.Publication;
import uk.ac.ebi.spot.gwas.deposition.domain.SSTemplateEntryPlaceholder;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;

import java.util.List;
import java.util.Optional;

public interface PublicationService {

    List<Publication> getPublications();

    Publication createPublication(Publication publication, SSTemplateEntryPlaceholder ssTemplateEntryPlaceholder);

    void updatePublication(Publication publication, SSTemplateEntryPlaceholder ssTemplateEntryPlaceholder);

    Publication getPublication(String pmid);

    void deletePublication(String pmid);

    void updatePublicationStatus(Publication publication, String status);

    List<Publication> getByIdIn(List<String> pubIds);

    Optional<Publication> getById(String publicationId);

    Publication getPublicationBySubmission(Submission submission);
}
