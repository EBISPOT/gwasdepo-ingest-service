package uk.ac.ebi.spot.gwas.deposition.ingest.util;

import uk.ac.ebi.spot.gwas.deposition.domain.Publication;

import java.util.ArrayList;
import java.util.List;

public class PublicationCollector {

    private List<Publication> publicationList;

    public PublicationCollector() {
        publicationList = new ArrayList<>();
    }

    public void add(Publication publication) {
        this.publicationList.add(publication);
    }

    public List<Publication> getPublicationList() {
        return publicationList;
    }
}
