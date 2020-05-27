package uk.ac.ebi.spot.gwas.deposition.ingest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.spot.gwas.deposition.domain.BodyOfWork;

import java.util.List;
import java.util.Optional;

public interface BodyOfWorkRepository extends MongoRepository<BodyOfWork, String> {

    List<BodyOfWork> findByStatusAndArchived(String status, boolean archived);

    List<BodyOfWork> findByArchived(boolean archived);

    Optional<BodyOfWork> findByBowIdAndArchived(String bodyOfWorkId, boolean archived);

    List<BodyOfWork> findByBowIdInAndArchived(List<String> bodyOfWorkIds, boolean archived);

    List<BodyOfWork> findByPmidsContainsAndArchived(String pmid, boolean archived);
}
