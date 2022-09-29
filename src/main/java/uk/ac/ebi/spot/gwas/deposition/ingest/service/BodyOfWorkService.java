package uk.ac.ebi.spot.gwas.deposition.ingest.service;

import uk.ac.ebi.spot.gwas.deposition.domain.BodyOfWork;
import uk.ac.ebi.spot.gwas.deposition.domain.Publication;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;

import java.util.List;
import java.util.Optional;

public interface BodyOfWorkService {

    List<BodyOfWork> getByBowIdInAndArchived(List<String> bodyOfWorkIds, boolean archived);

    Optional<BodyOfWork> getByBowIdAndArchived(String bodyOfWorkId, boolean archived);

    BodyOfWork retrieveBodyOfWork(String bodyOfWork);

    List<BodyOfWork> retrieveBodyOfWorks(String status);

    boolean findAndUpdateBasedOnPMID(Publication publication);

    boolean bowExistsForPublication(String pmid);

    BodyOfWork getBodyOfWorkBySubmission(Submission submission);

}
