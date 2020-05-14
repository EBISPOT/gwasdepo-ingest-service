package uk.ac.ebi.spot.gwas.deposition.ingest.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;
import uk.ac.ebi.spot.gwas.deposition.constants.PublicationIngestStatus;
import uk.ac.ebi.spot.gwas.deposition.constants.PublicationStatus;
import uk.ac.ebi.spot.gwas.deposition.domain.Publication;
import uk.ac.ebi.spot.gwas.deposition.domain.SSTemplateEntryPlaceholder;
import uk.ac.ebi.spot.gwas.deposition.dto.CorrespondingAuthorDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.ExtendedPublicationDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.PublicationDto;
import uk.ac.ebi.spot.gwas.deposition.dto.summarystats.SSTemplateEntryDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.PublicationIngestEntryRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.PublicationRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.SSTemplateEntryPlaceholderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {IntegrationTest.MockTaskExecutorConfig.class})
public class PublicationsControllerTest extends IntegrationTest {

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private SSTemplateEntryPlaceholderRepository ssTemplateEntryPlaceholderRepository;

    @Autowired
    private PublicationIngestEntryRepository publicationIngestEntryRepository;

    @Before
    public void setup() {
        super.setup();
    }

    /**
     * POST /v1/publications
     */
    @Test
    public void shouldCreatePublication() throws Exception {
        createPublication();
    }

    /**
     * POST /v1/publications
     */
    @Test
    public void shouldCreatePublicationWithSSEntries() throws Exception {
        Publication publication = TestUtil.publication();
        List<SSTemplateEntryDto> ssTemplateEntryDtos = new ArrayList<>();
        ssTemplateEntryDtos.add(new SSTemplateEntryDto(RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10),
                false));

        ExtendedPublicationDto extendedPublicationDto = new ExtendedPublicationDto(publication.getPmid(),
                publication.getTitle(),
                publication.getJournal(),
                publication.getFirstAuthor(),
                publication.getPublicationDate(),
                new CorrespondingAuthorDto(publication.getCorrespondingAuthor().getAuthorName(),
                        publication.getCorrespondingAuthor().getEmail()),
                null,
                ssTemplateEntryDtos);

        mockMvc.perform(post(GeneralCommon.API_V1 +
                IngestServiceConstants.API_PUBLICATIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(extendedPublicationDto)))
                .andExpect(status().isCreated());

        Optional<Publication> optional = publicationRepository.findByPmid(publication.getPmid());
        assertTrue(optional.isPresent());

        Optional<SSTemplateEntryPlaceholder> ssTemplateEntryPlaceholderOptional = ssTemplateEntryPlaceholderRepository.findByPmid(publication.getPmid());
        assertTrue(ssTemplateEntryPlaceholderOptional.isPresent());
        assertEquals(1, ssTemplateEntryPlaceholderOptional.get().getSsTemplateEntries().size());
    }

    /**
     * GET /v1/publications
     */
    @Test
    public void shouldGetPublications() throws Exception {
        Publication publication = createPublication();
        String response = mockMvc.perform(get(GeneralCommon.API_V1 +
                IngestServiceConstants.API_PUBLICATIONS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<PublicationDto> actual = mapper.readValue(response, new TypeReference<List<PublicationDto>>() {
        });
        assertEquals(1, actual.size());
        assertEquals(publication.getPmid(), actual.get(0).getPmid());
        assertEquals(publication.getTitle(), actual.get(0).getTitle());
        assertEquals(publication.getFirstAuthor(), actual.get(0).getFirstAuthor());
        assertEquals(publication.getJournal(), actual.get(0).getJournal());
    }

    /**
     * GET /v1/publications/{pmid}
     */
    @Test
    public void shouldGetPublication() throws Exception {
        Publication publication = createPublication();

        String response = mockMvc.perform(get(GeneralCommon.API_V1 +
                IngestServiceConstants.API_PUBLICATIONS + "/" + publication.getPmid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PublicationDto actual = mapper.readValue(response, new TypeReference<PublicationDto>() {
        });

        assertEquals(publication.getPmid(), actual.getPmid());
        assertEquals(publication.getTitle(), actual.getTitle());
        assertEquals(publication.getJournal(), actual.getJournal());
    }

    /**
     * PUT /v1/publications/{publicationId}
     */
    @Test
    public void shouldUpdatePublication() throws Exception {
        Publication publication = createPublication();
        List<SSTemplateEntryDto> ssTemplateEntryDtos = new ArrayList<>();
        ssTemplateEntryDtos.add(new SSTemplateEntryDto(RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10),
                false));

        ExtendedPublicationDto extendedPublicationDto = new ExtendedPublicationDto(publication.getPmid(),
                publication.getTitle(),
                publication.getJournal(),
                publication.getFirstAuthor(),
                publication.getPublicationDate(),
                new CorrespondingAuthorDto(publication.getCorrespondingAuthor().getAuthorName(),
                        publication.getCorrespondingAuthor().getEmail()),
                PublicationStatus.CURATION_STARTED.name(),
                ssTemplateEntryDtos);

        mockMvc.perform(put(GeneralCommon.API_V1 +
                IngestServiceConstants.API_PUBLICATIONS + "/" + extendedPublicationDto.getPmid())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(extendedPublicationDto)))
                .andExpect(status().isOk());

        Optional<Publication> optional = publicationRepository.findByPmid(publication.getPmid());
        assertTrue(optional.isPresent());
        assertEquals(PublicationStatus.CURATION_STARTED.name(), optional.get().getStatus());

        Optional<SSTemplateEntryPlaceholder> ssTemplateEntryPlaceholderOptional = ssTemplateEntryPlaceholderRepository.findByPmid(publication.getPmid());
        assertTrue(ssTemplateEntryPlaceholderOptional.isPresent());
        assertEquals(1, ssTemplateEntryPlaceholderOptional.get().getSsTemplateEntries().size());

        assertEquals(1, publicationIngestEntryRepository.findByStatus(PublicationIngestStatus.UPDATED.name()).size());
    }

    private Publication createPublication() throws Exception {
        Publication publication = TestUtil.publication();
        PublicationDto publicationDto = new PublicationDto(publication.getPmid(),
                publication.getTitle(),
                publication.getJournal(),
                publication.getFirstAuthor(),
                publication.getPublicationDate(),
                new CorrespondingAuthorDto(publication.getCorrespondingAuthor().getAuthorName(),
                        publication.getCorrespondingAuthor().getEmail()),
                null);

        mockMvc.perform(post(GeneralCommon.API_V1 +
                IngestServiceConstants.API_PUBLICATIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(publicationDto)))
                .andExpect(status().isCreated());

        Optional<Publication> optional = publicationRepository.findByPmid(publication.getPmid());
        assertTrue(optional.isPresent());
        assertTrue(ssTemplateEntryPlaceholderRepository.findAll().isEmpty());

        assertEquals(1, publicationIngestEntryRepository.findByStatus(PublicationIngestStatus.CREATED.name()).size());
        return publication;
    }
}
