package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import uk.ac.ebi.spot.gwas.deposition.domain.BodyOfWork;
import uk.ac.ebi.spot.gwas.deposition.domain.Publication;

import java.util.List;

public interface BodyOfWorkService {
    BodyOfWork retrieveBodyOfWork(String bodyOfWork);

    List<BodyOfWork> retrieveBodyOfWorks(String status);

    void findAndUpdateBasedOnPMID(Publication publication);
}
