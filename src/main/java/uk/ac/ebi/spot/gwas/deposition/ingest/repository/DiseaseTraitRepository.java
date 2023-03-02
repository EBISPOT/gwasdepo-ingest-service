package uk.ac.ebi.spot.gwas.deposition.ingest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.spot.gwas.deposition.domain.DiseaseTrait;

public interface DiseaseTraitRepository extends MongoRepository<DiseaseTrait, String> {


}
