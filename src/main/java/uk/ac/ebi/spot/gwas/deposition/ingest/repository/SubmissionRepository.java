package uk.ac.ebi.spot.gwas.deposition.ingest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends MongoRepository<Submission, String> {

    Optional<Submission> findByIdAndArchived(String id, boolean archived);

    Optional<Submission> findByPublicationIdAndArchived(String id, boolean archived);

    Page<Submission> findByArchived(boolean archived, Pageable pageable);

    Page<Submission> findByOverallStatusAndArchived(String status, boolean archived, Pageable pageable);

    List<Submission> findByBodyOfWorksContainsAndArchived(String bowId, boolean archived);
}
