package uk.ac.ebi.spot.gwas.deposition.ingest.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.domain.Manuscript;
import uk.ac.ebi.spot.gwas.deposition.exception.EntityNotFoundException;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.ManuscriptRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.ManuscriptService;

import java.util.List;
import java.util.Optional;

@Service
public class ManuscriptServiceImpl implements ManuscriptService {

    private static final Logger log = LoggerFactory.getLogger(ManuscriptService.class);

    @Autowired
    private ManuscriptRepository manuscriptRepository;

    @Override
    public Manuscript retrieveManuscript(String manuscriptId) {
        log.info("Retrieving manuscript: {}", manuscriptId);
        Optional<Manuscript> optionalManuscript = manuscriptRepository.findByIdAndArchived(manuscriptId, false);
        if (!optionalManuscript.isPresent()) {
            log.error("Unable to find manuscript with ID: {}", manuscriptId);
            throw new EntityNotFoundException("Unable to find manuscript with ID: " + manuscriptId);
        }

        log.info("Returning manuscript: {}", optionalManuscript.get().getId());
        return optionalManuscript.get();
    }

    @Override
    public List<Manuscript> retrieveManuscripts() {
        log.info("Retrieving manuscripts.");
        List<Manuscript> manuscripts = manuscriptRepository.findByArchived(false);
        log.info("Found {} manuscripts.", manuscripts.size());
        return manuscripts;
    }
}
