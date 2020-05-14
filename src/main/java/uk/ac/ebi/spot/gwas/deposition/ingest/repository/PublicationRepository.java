package uk.ac.ebi.spot.gwas.deposition.ingest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import uk.ac.ebi.spot.gwas.deposition.domain.Publication;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface PublicationRepository extends MongoRepository<Publication, String> {

    @Query(value = "{}")
    Stream<Publication> findAllByCustomQueryAndStream();

    Optional<Publication> findByPmid(String pmid);

    List<Publication> findByIdIn(List<String> pubIds);
}
