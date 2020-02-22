package uk.ac.ebi.spot.gwas.deposition.ingest.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.ac.ebi.spot.gwas.deposition.domain.*;
import uk.ac.ebi.spot.gwas.deposition.ingest.Application;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {Application.class})
public abstract class IntegrationTest {

    @Configuration
    public static class MockTaskExecutorConfig {

        @Bean
        public TaskExecutor taskExecutor() {
            return new SyncTaskExecutor();
        }
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private AssociationRepository associationRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    protected UserRepository userRepository;

    protected MockMvc mockMvc;

    protected ObjectMapper mapper;

    protected Study study;

    protected Note note;

    protected Sample sample;

    protected Association association;

    protected User user;

    @Before
    public void setup() {
        mongoTemplate.getDb().drop();
        mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        user = userRepository.insert(TestUtil.user());
        userRepository.insert(new User("Auto Curator", "auto-curator-service@ebi.ac.uk"));
        createPrerequisites();
    }

    private void createPrerequisites() {
        study = new Study();
        study.setStudyTag(RandomStringUtils.randomAlphanumeric(10));
        study.setGenotypingTechnology(RandomStringUtils.randomAlphanumeric(10));
        study.setArrayManufacturer(RandomStringUtils.randomAlphanumeric(10));
        study.setArrayInformation(RandomStringUtils.randomAlphanumeric(10));
        study.setImputation(false);
        study.setVariantCount(10);
        study.setStatisticalModel(RandomStringUtils.randomAlphanumeric(10));
        study.setStudyDescription(RandomStringUtils.randomAlphanumeric(10));
        study.setTrait(RandomStringUtils.randomAlphanumeric(10));
        study.setEfoTrait(RandomStringUtils.randomAlphanumeric(10));
        study.setBackgroundTrait(RandomStringUtils.randomAlphanumeric(10));
        study.setBackgroundEfoTrait(RandomStringUtils.randomAlphanumeric(10));
        study.setSummaryStatisticsFile(RandomStringUtils.randomAlphanumeric(10));
        study.setSummaryStatisticsAssembly(RandomStringUtils.randomAlphanumeric(10));
        study = studyRepository.insert(study);

        note = new Note();
        note.setNote(RandomStringUtils.randomAlphanumeric(10));
        note.setNoteSubject(RandomStringUtils.randomAlphanumeric(10));
        note.setStatus(RandomStringUtils.randomAlphanumeric(10));
        note.setStudyTag(RandomStringUtils.randomAlphanumeric(10));
        note = noteRepository.insert(note);

        sample = new Sample();
        sample.setStudyTag(RandomStringUtils.randomAlphanumeric(10));
        sample.setStage(RandomStringUtils.randomAlphanumeric(10));
        sample.setSize(10);
        sample.setCases(10);
        sample.setControls(10);
        sample.setSampleDescription(RandomStringUtils.randomAlphanumeric(10));
        sample.setAncestryCategory(RandomStringUtils.randomAlphanumeric(10));
        sample.setAncestry(RandomStringUtils.randomAlphanumeric(10));
        sample.setAncestryDescription(RandomStringUtils.randomAlphanumeric(10));
        sample.setCountryRecruitement(RandomStringUtils.randomAlphanumeric(10));
        sample = sampleRepository.insert(sample);

        association = new Association();
        association.setStudyTag(RandomStringUtils.randomAlphanumeric(10));
        association.setHaplotypeId(RandomStringUtils.randomAlphanumeric(10));
        association.setVariantId(RandomStringUtils.randomAlphanumeric(10));
        association.setPvalue("10.0");
        association.setPvalueText(RandomStringUtils.randomAlphanumeric(10));
        association.setProxyVariant(RandomStringUtils.randomAlphanumeric(10));
        association.setEffectAllele(RandomStringUtils.randomAlphanumeric(10));
        association.setOtherAllele(RandomStringUtils.randomAlphanumeric(10));
        association.setEffectAlleleFrequency(10.0);
        association.setOddsRatio(10.0);
        association.setBeta(10.0);
        association.setBetaUnit(RandomStringUtils.randomAlphanumeric(10));
        association.setCiLower(10.0);
        association.setCiUpper(10.0);
        association.setStandardError(10.0);
        association = associationRepository.insert(association);

    }

}
