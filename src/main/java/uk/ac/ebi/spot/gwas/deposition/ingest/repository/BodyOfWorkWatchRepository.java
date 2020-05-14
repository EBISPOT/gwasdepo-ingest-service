package uk.ac.ebi.spot.gwas.deposition.ingest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.spot.gwas.deposition.domain.BodyOfWorkWatch;

import java.util.List;
import java.util.Optional;

public interface BodyOfWorkWatchRepository extends MongoRepository<BodyOfWorkWatch, String> {

    Optional<BodyOfWorkWatch> findByBowId(String bowId);

    List<BodyOfWorkWatch> findByVisited(boolean visited);
}
