package uk.ac.ebi.spot.gwas.deposition.ingest.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;
import uk.ac.ebi.spot.gwas.deposition.constants.PublicationIngestStatus;
import uk.ac.ebi.spot.gwas.deposition.constants.SubmissionProvenanceType;
import uk.ac.ebi.spot.gwas.deposition.domain.Provenance;
import uk.ac.ebi.spot.gwas.deposition.domain.Publication;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;
import uk.ac.ebi.spot.gwas.deposition.dto.CorrespondingAuthorDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.PublicationDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.PublicationIngestEntryRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.PublicationRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.SSTemplateEntryPlaceholderRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.SubmissionRepository;

import java.util.Arrays;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {IntegrationTest.MockTaskExecutorConfig.class})
public class PublicationsControllerDeleteTest extends IntegrationTest {

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private SSTemplateEntryPlaceholderRepository ssTemplateEntryPlaceholderRepository;

    @Autowired
    private PublicationIngestEntryRepository publicationIngestEntryRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Before
    public void setup() {
        super.setup();
    }


    /**
     * DELETE /v1/publications/{pmid}
     */
    @Test
    public void shouldDeletePublication() throws Exception {
        Publication publication = createPublication();

        String response = mockMvc.perform(delete(GeneralCommon.API_V1 +
                IngestServiceConstants.API_PUBLICATIONS + "/" + publication.getPmid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Boolean actual = mapper.readValue(response, new TypeReference<Boolean>() {
        });

        assertTrue(actual);
        assertEquals(0, publicationRepository.findAll().size());
    }

    /**
     * DELETE /v1/publications/{pmid}
     */
    @Test
    public void shouldFailToDeletePublicationWithSubmission() throws Exception {
        Publication publication = createPublication();
        Submission submission = new Submission(publication.getId(), SubmissionProvenanceType.PUBLICATION.name(),
                new Provenance(DateTime.now(), user.getId()));
        submission.setCompleted(true);
        submission.setStudies(Arrays.asList(new String[]{study.getId()}));
        submission.setNotes(Arrays.asList(new String[]{note.getId()}));
        submission.setAssociations(Arrays.asList(new String[]{association.getId()}));
        submission.setSamples(Arrays.asList(new String[]{sample.getId()}));
        submission.setDateSubmitted(LocalDate.now());
        submissionRepository.insert(submission);

        String response = mockMvc.perform(delete(GeneralCommon.API_V1 +
                IngestServiceConstants.API_PUBLICATIONS + "/" + publication.getPmid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Boolean actual = mapper.readValue(response, new TypeReference<Boolean>() {
        });

        assertFalse(actual);
        assertEquals(1, publicationRepository.findAll().size());
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
        return optional.get();
    }
}
