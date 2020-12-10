package uk.ac.ebi.spot.gwas.deposition.ingest.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;
import uk.ac.ebi.spot.gwas.deposition.constants.SubmissionProvenanceType;
import uk.ac.ebi.spot.gwas.deposition.domain.Provenance;
import uk.ac.ebi.spot.gwas.deposition.domain.Publication;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.BodyOfWorkRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.PublicationRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.SubmissionRepository;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SubmissionsControllerV2Test extends IntegrationTest {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private BodyOfWorkRepository bodyOfWorkRepository;

    /**
     * GET /v1/submissions/{submissionId}
     */
    @Test
    public void shouldGetSubmission() throws Exception {
        Publication publication = publicationRepository.insert(TestUtil.publication());
        Submission submission = new Submission(publication.getId(), SubmissionProvenanceType.PUBLICATION.name(),
                new Provenance(DateTime.now(), user.getId()));
        submission.setCompleted(true);
        submission.setStudies(Arrays.asList(new String[]{study.getId()}));
        submission.setNotes(Arrays.asList(new String[]{note.getId()}));
        submission.setAssociations(Arrays.asList(new String[]{association.getId()}));
        submission.setSamples(Arrays.asList(new String[]{sample.getId()}));
        submission.setDateSubmitted(LocalDate.now());
        submission = submissionRepository.insert(submission);

        String endpoint = GeneralCommon.API_V2 + IngestServiceConstants.API_SUBMISSIONS + "/" + submission.getId();

        String response = mockMvc.perform(get(endpoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        SubmissionDto actual = mapper.readValue(response, new TypeReference<SubmissionDto>() {
        });
        assertNull(actual.getBodyOfWork());
        assertEquals(publication.getPmid(), actual.getPublication().getPmid());
        assertNull(actual.getStudies());
        assertNull(actual.getAssociations());
        assertNull(actual.getSamples());
        assertNull(actual.getNotes());
    }

    /**
     * GET /v1/submissions/{submissionId}
     */
    @Test
    public void shouldGetSubmissionWithBodyOfWork() throws Exception {
        bodyOfWork.setBowId("GCP123456");
        bodyOfWork = bodyOfWorkRepository.insert(bodyOfWork);
        Submission submission = new Submission(bodyOfWork.getBowId(), SubmissionProvenanceType.BODY_OF_WORK.name(),
                new Provenance(DateTime.now(), user.getId()));
        submission.setCompleted(true);
        submission.setDateSubmitted(LocalDate.now());
        submission = submissionRepository.insert(submission);

        String endpoint = GeneralCommon.API_V2 + IngestServiceConstants.API_SUBMISSIONS + "/" + submission.getId();

        String response = mockMvc.perform(get(endpoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        SubmissionDto actual = mapper.readValue(response, new TypeReference<SubmissionDto>() {
        });
        assertNull(actual.getPublication());
        assertEquals(bodyOfWork.getBowId(), actual.getBodyOfWork().getBodyOfWorkId());
        assertNull(actual.getStudies());
        assertNull(actual.getAssociations());
        assertNull(actual.getSamples());
        assertNull(actual.getNotes());
    }

}
