package uk.ac.ebi.spot.gwas.deposition.ingest.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import uk.ac.ebi.spot.gwas.deposition.domain.CorrespondingAuthor;
import uk.ac.ebi.spot.gwas.deposition.domain.Manuscript;
import uk.ac.ebi.spot.gwas.deposition.domain.Provenance;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.ManuscriptDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.ManuscriptRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto.ManuscriptDtoAssembler;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ManuscriptControllerTest extends IntegrationTest {

    @Autowired
    private ManuscriptRepository manuscriptRepository;

    private Manuscript manuscript;

    @Before
    public void setup() {
        super.setup();
        manuscript = manuscriptRepository.insert(new Manuscript(
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10),
                LocalDate.now(),
                LocalDate.now(),
                new CorrespondingAuthor(RandomStringUtils.randomAlphanumeric(10),
                        RandomStringUtils.randomAlphanumeric(10)),
                new Provenance(DateTime.now(),
                        RandomStringUtils.randomAlphanumeric(10))
        ));
    }

    /**
     * GET /v1/manuscripts/{manuscriptId}
     */
    @Test
    public void shouldGetManuscript() throws Exception {
        String endpoint = IngestServiceConstants.API_V1 + IngestServiceConstants.API_MANUSCRIPTS + "/" + manuscript.getId();

        String response = mockMvc.perform(get(endpoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ManuscriptDto actual = mapper.readValue(response, new TypeReference<ManuscriptDto>() {
        });
        assertEquals(ManuscriptDtoAssembler.assemble(manuscript), actual);
    }

    /**
     * GET /v1/manuscripts
     */
    @Test
    public void shouldGetManuscripts() throws Exception {
        String endpoint = IngestServiceConstants.API_V1 + IngestServiceConstants.API_MANUSCRIPTS;

        String response = mockMvc.perform(get(endpoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ManuscriptDto> actual = mapper.readValue(response, new TypeReference<List<ManuscriptDto>>() {
        });
        assertEquals(1, actual.size());
        assertEquals(ManuscriptDtoAssembler.assemble(manuscript), actual.get(0));
    }

}
