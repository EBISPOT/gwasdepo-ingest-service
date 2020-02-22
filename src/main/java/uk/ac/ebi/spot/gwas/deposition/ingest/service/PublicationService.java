package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import uk.ac.ebi.spot.gwas.deposition.domain.Publication;
import uk.ac.ebi.spot.gwas.deposition.domain.SSTemplateEntryPlaceholder;

import java.util.List;

public interface PublicationService {

    List<Publication> getPublications();

    void createPublication(Publication publication, SSTemplateEntryPlaceholder ssTemplateEntryPlaceholder);

    void updatePublication(Publication publication, SSTemplateEntryPlaceholder ssTemplateEntryPlaceholder);

    Publication getPublication(String pmid);
}
