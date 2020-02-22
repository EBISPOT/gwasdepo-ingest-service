package uk.ac.ebi.spot.gwas.deposition.ingest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.ac.ebi.spot.gwas.deposition.ingest.constants.IngestServiceConstants;

@Component
public class SystemConfigProperties {

    @Value("${spring.profiles.active}")
    private String activeSpringProfile;

    @Value("${server.name}")
    private String serverName;

    @Value("${server.port}")
    private String serverPort;

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${gwas-ingest-service.db:#{NULL}}")
    private String dbName;

    public String getActiveSpringProfile() {
        return activeSpringProfile;
    }

    public String getServerName() {
        return serverName;
    }

    public String getServerPort() {
        return serverPort;
    }

    public String getMongoUri() {
        return mongoUri;
    }

    public String getDbUser() {
        return System.getenv(IngestServiceConstants.DB_USER);
    }

    public String getDbPassword() {
        return System.getenv(IngestServiceConstants.DB_PASSWORD);
    }

    public String getDbName() {
        return dbName;
    }
}
