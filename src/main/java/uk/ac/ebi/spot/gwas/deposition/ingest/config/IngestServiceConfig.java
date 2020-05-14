package uk.ac.ebi.spot.gwas.deposition.ingest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IngestServiceConfig {

    @Value("${gwas-ingest-service.auth.enabled}")
    private boolean authEnabled;

    @Value("${gwas-ingest-service.auto-curator-service-account:auto-curator-service@ebi.ac.uk}")
    private String autoCuratorServiceAccount;

    @Value("${gwas-ingest-service.service-environments:#{NULL}}")
    private String serviceEnv;

    @Value("${gwas-ingest-service.db:#{NULL}}")
    private String dbName;

    public String getDbName() {
        return dbName;
    }

    public boolean isAuthEnabled() {
        return authEnabled;
    }

    public String getAutoCuratorServiceAccount() {
        return autoCuratorServiceAccount;
    }

    public List<String> getServiceEnvironments() {
        List<String> list = new ArrayList<>();
        if (serviceEnv != null) {
            String[] envArray = serviceEnv.split(", ");
            for (String env : envArray) {
                env = env.trim();
                if (!"".equalsIgnoreCase(env)) {
                    list.add(env);
                }
            }
        }
        return list;
    }
}
