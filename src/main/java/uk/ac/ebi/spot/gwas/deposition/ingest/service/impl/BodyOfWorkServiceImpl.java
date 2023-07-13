package uk.ac.ebi.spot.gwas.deposition.ingest.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.constants.BodyOfWorkStatus;
import uk.ac.ebi.spot.gwas.deposition.domain.BodyOfWork;
import uk.ac.ebi.spot.gwas.deposition.domain.BodyOfWorkWatch;
import uk.ac.ebi.spot.gwas.deposition.domain.Publication;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;
import uk.ac.ebi.spot.gwas.deposition.exception.EntityNotFoundException;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.BodyOfWorkRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.BodyOfWorkWatchRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.SubmissionRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.BodyOfWorkService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BodyOfWorkServiceImpl implements BodyOfWorkService {

    private static final Logger log = LoggerFactory.getLogger(BodyOfWorkService.class);

    @Autowired
    private BodyOfWorkRepository bodyOfWorkRepository;

    @Autowired
    private BodyOfWorkWatchRepository bodyOfWorkWatchRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Override
    public List<BodyOfWork> getByBowIdInAndArchived(List<String> bodyOfWorkIds, boolean archived) {
        return bodyOfWorkRepository.findByBowIdInAndArchived(bodyOfWorkIds, archived);
    }

    @Override
    public Optional<BodyOfWork> getByBowIdAndArchived(String bodyOfWorkId, boolean archived) {
        return bodyOfWorkRepository.findByBowIdAndArchived(bodyOfWorkId, archived);
    }

    @Override
    public BodyOfWork retrieveBodyOfWork(String bodyOfWorkId) {
        log.info("Retrieving body of work: {}", bodyOfWorkId);
        Optional<BodyOfWork> optionalBodyOfWork = bodyOfWorkRepository.findByBowIdAndArchived(bodyOfWorkId, false);
        if (!optionalBodyOfWork.isPresent()) {
            log.error("Unable to find body of work with ID: {}", bodyOfWorkId);
            throw new EntityNotFoundException("Unable to find body of work with ID: " + bodyOfWorkId);
        }

        log.info("Returning body of work: {}", optionalBodyOfWork.get().getBowId());
        return optionalBodyOfWork.get();
    }

    @Override
    public List<BodyOfWork> retrieveBodyOfWorks(String status) {
        log.info("Retrieving body of works: {}", status);
        if (status != null) {
            if (status.equalsIgnoreCase(BodyOfWorkStatus.HAS_PMID.name())) {
                List<BodyOfWork> bodyOfWorks = bodyOfWorkRepository.findByArchived(false);
                List<BodyOfWork> result = new ArrayList<>();
                for (BodyOfWork bodyOfWork : bodyOfWorks) {
                    if (bodyOfWork.getPmids() != null) {
                        if (!bodyOfWork.getPmids().isEmpty()) {
                            result.add(bodyOfWork);
                        }
                    }
                }
                return result;
            }
            if (status.equalsIgnoreCase(BodyOfWorkStatus.UPDATED_AFTER_SUBMISSION.name())) {
                List<String> ids = new ArrayList<>();
                List<BodyOfWorkWatch> bodyOfWorkWatches = bodyOfWorkWatchRepository.findByVisited(false);
                for (BodyOfWorkWatch bodyOfWorkWatch : bodyOfWorkWatches) {
                    ids.add(bodyOfWorkWatch.getBowId());
                    bodyOfWorkWatch.setVisited(true);
                    bodyOfWorkWatchRepository.save(bodyOfWorkWatch);
                }
                return bodyOfWorkRepository.findByBowIdInAndArchived(ids, false);
            }

            return bodyOfWorkRepository.findByStatusAndArchived(status, false);
        }
        return bodyOfWorkRepository.findByArchived(false);
    }

    @Override
    public boolean findAndUpdateBasedOnPMID(Publication publication) {
        log.info("Verifying if there are any body of works linked to PMID: {}", publication.getPmid());
        List<BodyOfWork> bodyOfWorks = bodyOfWorkRepository.findByPmidsContainsAndArchived(publication.getPmid(), false);
        log.info("Found {} body of works.", bodyOfWorks.size());
        boolean found = false;
        for (BodyOfWork bodyOfWork : bodyOfWorks) {
            List<Submission> submissionList = submissionRepository.findByBodyOfWorksContainsAndArchived(bodyOfWork.getBowId(), false);

            log.info("Found {} submissions linked to BOW: {}", submissionList.size(), bodyOfWork.getBowId());
            for (Submission submission : submissionList) {
                if (submission.getPublicationId() == null) {
                    submission.setPublicationId(publication.getId());
                    submissionRepository.save(submission);
                    found = true;
                }
            }
        }
        return found;
    }

    @Override
    public boolean bowExistsForPublication(String pmid) {
        log.info("Verifying if there are any body of works linked to PMID: {}", pmid);
        List<BodyOfWork> bodyOfWorks = bodyOfWorkRepository.findByPmidsContainsAndArchived(pmid, false);
        log.info("Found {} body of works.", bodyOfWorks.size());
        return bodyOfWorks.size() != 0;
    }


    @Override
    public BodyOfWork getBodyOfWorkBySubmission(Submission submission){
        Optional<BodyOfWork> bodyOfWorkOptional = this.getByBowIdAndArchived(submission.getBodyOfWorks().get(0), false);
        if (!bodyOfWorkOptional.isPresent()) {
            log.error("Unable to find body of work: {}", submission.getBodyOfWorks());
            throw new EntityNotFoundException("Unable to find body of work: " + submission.getBodyOfWorks().get(0));
        }
        return bodyOfWorkOptional.get();
    }
}
