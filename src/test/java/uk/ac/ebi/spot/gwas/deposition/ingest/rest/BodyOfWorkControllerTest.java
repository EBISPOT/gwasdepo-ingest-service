package uk.ac.ebi.spot.gwas.deposition.ingest.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.BodyOfWorkDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.BodyOfWorkRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto.BodyOfWorkDtoAssembler;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BodyOfWorkControllerTest extends IntegrationTest {

    @Autowired
    private BodyOfWorkRepository bodyOfWorkRepository;

    @Before
    public void setup() {
        super.setup();
        bodyOfWork.setBowId("GCP123456");
        bodyOfWork = bodyOfWorkRepository.insert(bodyOfWork);
    }

    /**
     * GET /v1/bodyofwork/{bodyofworkId}
     */
    @Test
    public void shouldGetBodyOfWork() throws Exception {
        String endpoint = GeneralCommon.API_V1 + IngestServiceConstants.API_BODY_OF_WORK + "/" + bodyOfWork.getBowId();

        String response = mockMvc.perform(get(endpoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BodyOfWorkDto actual = mapper.readValue(response, new TypeReference<BodyOfWorkDto>() {
        });
        assertEquals(BodyOfWorkDtoAssembler.assemble(bodyOfWork), actual);
    }

    /**
     * GET /v1/bodyofwork
     */
    @Test
    public void shouldGetBodyOfWorks() throws Exception {
        String endpoint = GeneralCommon.API_V1 + IngestServiceConstants.API_BODY_OF_WORK;

        String response = mockMvc.perform(get(endpoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<BodyOfWorkDto> actual = mapper.readValue(response, new TypeReference<List<BodyOfWorkDto>>() {
        });
        assertEquals(1, actual.size());
        assertEquals(BodyOfWorkDtoAssembler.assemble(bodyOfWork), actual.get(0));
    }

}
