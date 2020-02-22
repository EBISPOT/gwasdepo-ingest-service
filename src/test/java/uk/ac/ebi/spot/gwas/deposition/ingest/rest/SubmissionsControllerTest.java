package uk.ac.ebi.spot.gwas.deposition.ingest.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import uk.ac.ebi.spot.gwas.deposition.constants.Status;
import uk.ac.ebi.spot.gwas.deposition.domain.Provenance;
import uk.ac.ebi.spot.gwas.deposition.domain.Publication;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;
import uk.ac.ebi.spot.gwas.deposition.domain.User;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.PublicationRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.SubmissionRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SubmissionsControllerTest extends IntegrationTest {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    /**
     * GET /v1/submissions/{submissionId}
     */
    @Test
    public void shouldGetSubmission() throws Exception {
        Publication publication = publicationRepository.insert(TestUtil.publication());
        Submission submission = new Submission(publication.getId(), new Provenance(DateTime.now(), user.getId()));
        submission.setCompleted(true);
        submission.setStudies(Arrays.asList(new String[]{study.getId()}));
        submission.setNotes(Arrays.asList(new String[]{note.getId()}));
        submission.setAssociations(Arrays.asList(new String[]{association.getId()}));
        submission.setSamples(Arrays.asList(new String[]{sample.getId()}));
        submission.setDateSubmitted(LocalDate.now());
        submission = submissionRepository.insert(submission);

        String endpoint = IngestServiceConstants.API_V1 + IngestServiceConstants.API_SUBMISSIONS + "/" + submission.getId();

        String response = mockMvc.perform(get(endpoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        SubmissionDto actual = mapper.readValue(response, new TypeReference<SubmissionDto>() {
        });
        assertEquals(publication.getPmid(), actual.getPublication().getPmid());
        assertEquals(1, actual.getStudies().size());
        assertEquals(study.getStudyTag(), actual.getStudies().get(0).getStudyTag());
        assertEquals(1, actual.getAssociations().size());
        assertEquals(association.getStudyTag(), actual.getAssociations().get(0).getStudyTag());
        assertEquals(1, actual.getSamples().size());
        assertEquals(sample.getStudyTag(), actual.getSamples().get(0).getStudyTag());
        assertEquals(1, actual.getNotes().size());
        assertEquals(note.getStudyTag(), actual.getNotes().get(0).getStudyTag());
    }

    /**
     * GET /v1/submissions
     */
    @Test
    public void shouldGetSubmissions() throws Exception {
        Publication publication = publicationRepository.insert(TestUtil.publication());
        Submission submission = new Submission(publication.getId(), new Provenance(DateTime.now(), user.getId()));
        submission.setStudies(Arrays.asList(new String[]{study.getId()}));
        submission.setNotes(Arrays.asList(new String[]{note.getId()}));
        submission.setAssociations(Arrays.asList(new String[]{association.getId()}));
        submission.setSamples(Arrays.asList(new String[]{sample.getId()}));
        submission.setCompleted(true);
        submissionRepository.insert(submission);

        String endpoint = IngestServiceConstants.API_V1 + IngestServiceConstants.API_SUBMISSIONS;

        String response = mockMvc.perform(get(endpoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<SubmissionDto> actual = mapper.readValue(response, new TypeReference<List<SubmissionDto>>() {
        });

        assertEquals(1, actual.size());
        assertEquals(publication.getPmid(), actual.get(0).getPublication().getPmid());
        assertEquals(1, actual.get(0).getStudies().size());
        assertEquals(1, actual.get(0).getAssociations().size());
        assertEquals(1, actual.get(0).getSamples().size());
        assertEquals(1, actual.get(0).getNotes().size());
    }

    /**
     * PUT /v1/submissions/{submissionId}
     */
    @Test
    public void shouldUpdateSubmissionStatus() throws Exception {
        Publication publication = publicationRepository.insert(TestUtil.publication());
        Submission submission = new Submission(publication.getId(), new Provenance(DateTime.now(), user.getId()));
        submission.setCompleted(true);
        submission.setGlobusFolderId(RandomStringUtils.randomAlphanumeric(10));
        submission.setGlobusOriginId(RandomStringUtils.randomAlphanumeric(10));
        submission.setStudies(Arrays.asList(new String[]{study.getId()}));
        submission.setNotes(Arrays.asList(new String[]{note.getId()}));
        submission.setAssociations(Arrays.asList(new String[]{association.getId()}));
        submission.setSamples(Arrays.asList(new String[]{sample.getId()}));
        submission.setDateSubmitted(LocalDate.now());
        submission = submissionRepository.insert(submission);

        String endpoint = IngestServiceConstants.API_V1 + IngestServiceConstants.API_SUBMISSIONS + "/" + submission.getId();

        String response = mockMvc.perform(get(endpoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        SubmissionDto actual = mapper.readValue(response, new TypeReference<SubmissionDto>() {
        });
        assertEquals(publication.getPmid(), actual.getPublication().getPmid());
        assertEquals(1, actual.getStudies().size());
        assertEquals(1, actual.getAssociations().size());
        assertEquals(1, actual.getSamples().size());
        assertEquals(1, actual.getNotes().size());

        SubmissionDto updated = new SubmissionDto(actual.getSubmissionId(),
                actual.getPublication(),
                Status.CURATOR_REVIEW.name(),
                actual.getGlobusFolder(),
                actual.getGlobusOriginId(),
                actual.getStudies(),
                actual.getAssociations(),
                actual.getSamples(),
                actual.getNotes(),
                actual.getDateSubmitted(),
                actual.getCreated());

        response = mockMvc.perform(put(endpoint)
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        actual = mapper.readValue(response, new TypeReference<SubmissionDto>() {
        });
        assertEquals(updated.getPublication(), actual.getPublication());
        assertEquals(updated.getStatus(), actual.getStatus());

        Optional<Submission> subOptional = submissionRepository.findByIdAndArchived(submission.getId(), false);
        assertTrue(subOptional.isPresent());

        Optional<User> userOptional = userRepository.findByEmailIgnoreCase("auto-curator-service@ebi.ac.uk");
        assertTrue(userOptional.isPresent());

        assertEquals(userOptional.get().getId(), subOptional.get().getLastUpdated().getUserId());
        assertEquals(updated.getStatus(), submissionRepository.findById(actual.getSubmissionId()).get().getOverallStatus());
    }
}
