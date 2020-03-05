package uk.ac.ebi.spot.gwas.deposition.ingest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.spot.gwas.deposition.domain.Manuscript;

import java.util.List;
import java.util.Optional;

public interface ManuscriptRepository extends MongoRepository<Manuscript, String> {

    List<Manuscript> findByArchived(boolean archived);

    Optional<Manuscript> findByIdAndArchived(String manuscriptId, boolean archived);
}
