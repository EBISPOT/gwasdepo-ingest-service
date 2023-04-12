package uk.ac.ebi.spot.gwas.deposition.ingest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.spot.gwas.deposition.domain.EfoTrait;

public interface EfoTraitRepository extends MongoRepository<EfoTrait, String> {

}
