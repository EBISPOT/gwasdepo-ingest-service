package uk.ac.ebi.spot.gwas.deposition.ingest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.spot.gwas.deposition.domain.EfoTrait;

import java.util.List;

public interface EfoTraitRepository extends MongoRepository<EfoTrait, String> {

}
