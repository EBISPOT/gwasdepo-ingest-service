package uk.ac.ebi.spot.gwas.deposition.ingest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.spot.gwas.deposition.domain.DiseaseTrait;

import java.util.Optional;

public interface DiseaseTraitRepository extends MongoRepository<DiseaseTrait, String> {


}
