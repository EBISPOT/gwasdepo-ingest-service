package uk.ac.ebi.spot.gwas.deposition.ingest.config;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.spot.gwas.deposition.scheduler.config.QuartzSchedulerJobConfig;

import javax.annotation.PostConstruct;

@Configuration
public class IngestQuartzConfig {

    @Autowired
    private QuartzSchedulerJobConfig quartzSchedulerJobConfig;

    @PostConstruct
    private void initialize() throws SchedulerException {
        quartzSchedulerJobConfig.initializeJobs();
    }
}
