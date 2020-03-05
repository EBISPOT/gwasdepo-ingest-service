package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import uk.ac.ebi.spot.gwas.deposition.domain.Manuscript;

import java.util.List;

public interface ManuscriptService {
    Manuscript retrieveManuscript(String manuscriptId);

    List<Manuscript> retrieveManuscripts();
}
