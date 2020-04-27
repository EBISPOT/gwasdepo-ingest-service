package uk.ac.ebi.spot.gwas.deposition.ingest.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.constants.PublicationIngestStatus;
import uk.ac.ebi.spot.gwas.deposition.domain.Publication;
import uk.ac.ebi.spot.gwas.deposition.domain.PublicationIngestEntry;
import uk.ac.ebi.spot.gwas.deposition.domain.SSTemplateEntryPlaceholder;
import uk.ac.ebi.spot.gwas.deposition.exception.EntityNotFoundException;
import uk.ac.ebi.spot.gwas.deposition.exception.PublicationAlreadyExistsException;
import uk.ac.ebi.spot.gwas.deposition.ingest.config.IngestServiceConfig;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.PublicationIngestEntryRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.PublicationRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.SSTemplateEntryPlaceholderRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.PublicationService;
import uk.ac.ebi.spot.gwas.deposition.ingest.util.PublicationCollector;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class PublicationServiceImpl implements PublicationService {

    private static final Logger log = LoggerFactory.getLogger(PublicationService.class);

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private SSTemplateEntryPlaceholderRepository ssTemplateEntryPlaceholderRepository;

    @Autowired
    private PublicationIngestEntryRepository publicationIngestEntryRepository;

    @Autowired
    private IngestServiceConfig ingestServiceConfig;

    @Override
    public List<Publication> getPublications() {
        log.info("Retrieving all publications ...");
        Stream<Publication> publicationStream = publicationRepository.findAllByCustomQueryAndStream();
        PublicationCollector publicationCollector = new PublicationCollector();
        publicationStream.forEach(publication -> publicationCollector.add(publication));
        publicationStream.close();
        log.info("Found {} publications.", publicationCollector.getPublicationList().size());
        return publicationCollector.getPublicationList();
    }

    @Override
    public void createPublication(Publication publication, SSTemplateEntryPlaceholder ssTemplateEntryPlaceholder) {
        log.info("Creating publication with PMID: {}", publication.getPmid());
        Optional<Publication> optionalPublication = publicationRepository.findByPmid(publication.getPmid());
        if (optionalPublication.isPresent()) {
            log.error("Unable to find create publication. Publication [{}] already exists.", publication.getPmid());
            throw new PublicationAlreadyExistsException("Unable to find create publication. Publication [" + publication.getPmid() + "] already exists.");
        }

        publication = publicationRepository.insert(publication);
        log.info("Publication [{}] created: {}", publication.getPmid(), publication.getId());
        if (ssTemplateEntryPlaceholder != null) {
            ssTemplateEntryPlaceholder.setPmid(publication.getPmid());
            ssTemplateEntryPlaceholderRepository.insert(ssTemplateEntryPlaceholder);
        }

        for (String env : ingestServiceConfig.getServiceEnvironments()) {
            publicationIngestEntryRepository.insert(new PublicationIngestEntry(publication.getId(), PublicationIngestStatus.CREATED.name(), env));
        }
    }

    @Override
    public void updatePublication(Publication publication, SSTemplateEntryPlaceholder ssTemplateEntryPlaceholder) {
        log.info("Updating publication with PMID: {}", publication.getPmid());

        Optional<Publication> optionalPublication = publicationRepository.findByPmid(publication.getPmid());
        if (!optionalPublication.isPresent()) {
            log.error("Unable to find publication with PMID: {}", publication.getPmid());
            throw new EntityNotFoundException("Unable to find publication with PMID: " + publication.getPmid());
        }

        Publication existing = optionalPublication.get();
        existing.setStatus(publication.getStatus());
        existing = publicationRepository.save(existing);

        if (ssTemplateEntryPlaceholder != null) {
            if (ssTemplateEntryPlaceholder.getSsTemplateEntries() != null) {
                ssTemplateEntryPlaceholder.setPmid(existing.getPmid());
                Optional<SSTemplateEntryPlaceholder> ssTemplateEntryPlaceholderOptional = ssTemplateEntryPlaceholderRepository.findByPmid(publication.getPmid());
                if (ssTemplateEntryPlaceholderOptional.isPresent()) {
                    ssTemplateEntryPlaceholderRepository.delete(ssTemplateEntryPlaceholderOptional.get());
                }
                ssTemplateEntryPlaceholderRepository.insert(ssTemplateEntryPlaceholder);
            }
        }
        for (String env : ingestServiceConfig.getServiceEnvironments()) {
            publicationIngestEntryRepository.insert(new PublicationIngestEntry(existing.getId(), PublicationIngestStatus.UPDATED.name(), env));
        }
    }

    @Override
    public Publication getPublication(String pmid) {
        log.info("Retrieving publication with PMID: {}", pmid);
        Optional<Publication> optionalPublication = publicationRepository.findByPmid(pmid);
        if (!optionalPublication.isPresent()) {
            log.error("Unable to find publication with PMID: {}", pmid);
            throw new EntityNotFoundException("Unable to find publication with PMID: " + pmid);
        }

        log.info("Found publication [{}] for PMID: {}", optionalPublication.get().getId(), pmid);
        return optionalPublication.get();
    }
}
