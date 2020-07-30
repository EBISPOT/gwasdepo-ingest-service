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
import uk.ac.ebi.spot.gwas.deposition.dto.ProvenanceDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.PublicationRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.SubmissionRepository;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProvenanceControllerTest extends IntegrationTest {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    /**
     * GET /v1/provenance
     */
    @Test
    public void shouldGetProvenance() throws Exception {
        Publication publication = publicationRepository.insert(TestUtil.publication());
        Submission submission = new Submission(publication.getId(), SubmissionProvenanceType.PUBLICATION.name(),
                new Provenance(DateTime.now(), user.getId()));
        submission.setCompleted(true);
        submission.setStudies(Arrays.asList(new String[]{study.getId()}));
        submission.setNotes(Arrays.asList(new String[]{note.getId()}));
        submission.setAssociations(Arrays.asList(new String[]{association.getId()}));
        submission.setSamples(Arrays.asList(new String[]{sample.getId()}));
        submission.setDateSubmitted(LocalDate.now());
        submissionRepository.insert(submission);

        String endpoint = GeneralCommon.API_V1 + IngestServiceConstants.API_PROVENANCE + "?" + IngestServiceConstants.PARAM_PMID + "=" + publication.getPmid();

        String response = mockMvc.perform(get(endpoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProvenanceDto actual = mapper.readValue(response, new TypeReference<ProvenanceDto>() {
        });
        assertEquals(user.getName(), actual.getUser().getName());
        assertEquals(user.getEmail(), actual.getUser().getEmail());
    }

}
