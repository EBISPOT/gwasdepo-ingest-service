package uk.ac.ebi.spot.gwas.deposition.ingest.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.ac.ebi.spot.gwas.deposition.config.SystemConfigProperties;

import java.util.ArrayList;
import java.util.List;

public class MongoConfig {

    @Configuration
    @EnableMongoRepositories(basePackages = {"uk.ac.ebi.spot.gwas.deposition.ingest.repository"})
    @EnableTransactionManagement
    @Profile({"dev", "test"})
    public static class MongoConfigDev extends AbstractMongoConfiguration {

        @Autowired
        private SystemConfigProperties systemConfigProperties;

        @Autowired
        private IngestServiceConfig ingestServiceConfig;

        @Override
        protected String getDatabaseName() {
            return ingestServiceConfig.getDbName();
        }

        @Bean
        public GridFsTemplate gridFsTemplate() throws Exception {
            return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
        }

        @Override
        public MongoClient mongoClient() {
            String mongoUri = systemConfigProperties.getMongoUri();
            String[] addresses = mongoUri.split(",");
            List<ServerAddress> servers = new ArrayList<>();
            for (String address : addresses) {
                String[] split = address.trim().split(":");
                servers.add(new ServerAddress(split[0].trim(), Integer.parseInt(split[1].trim())));
            }
            return new MongoClient(servers);
        }
    }

    @Configuration
    @EnableMongoRepositories(basePackages = {"uk.ac.ebi.spot.gwas.deposition.ingest.repository"})
    @EnableTransactionManagement
    @Profile({"sandbox","sandbox-migration"})
    public static class MongoConfigSandbox extends AbstractMongoConfiguration {

        @Autowired
        private SystemConfigProperties systemConfigProperties;

        @Autowired
        private IngestServiceConfig ingestServiceConfig;

        @Override
        protected String getDatabaseName() {
            return ingestServiceConfig.getDbName();
        }

        @Bean
        public GridFsTemplate gridFsTemplate() throws Exception {
            return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
        }

        @Override
        public MongoClient mongoClient() {
            String mongoUri = systemConfigProperties.getMongoUri();
            return new MongoClient(new MongoClientURI("mongodb://" + mongoUri));
        }
    }

    @Configuration
    @EnableMongoRepositories(basePackages = {"uk.ac.ebi.spot.gwas.deposition.ingest.repository"})
    @EnableTransactionManagement
    @Profile({"prod"})
    public static class MongoConfigProd extends AbstractMongoConfiguration {

        @Autowired
        private SystemConfigProperties systemConfigProperties;

        @Autowired
        private IngestServiceConfig ingestServiceConfig;

        @Override
        protected String getDatabaseName() {
            return ingestServiceConfig.getDbName();
        }

        @Bean
        public GridFsTemplate gridFsTemplate() throws Exception {
            return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
        }

        @Override
        public MongoClient mongoClient() {
            String mongoUri = systemConfigProperties.getMongoUri();
            String dbUser = systemConfigProperties.getDbUser();
            String dbPassword = systemConfigProperties.getDbPassword();
            String credentials = "";
            if (dbUser != null && dbPassword != null) {
                dbUser = dbUser.trim();
                dbPassword = dbPassword.trim();
                if (!dbUser.equalsIgnoreCase("") &&
                        !dbPassword.equalsIgnoreCase("")) {
                    credentials = dbUser + ":" + dbPassword + "@";
                }
            }

            return new MongoClient(new MongoClientURI("mongodb://" + credentials + mongoUri));
        }
    }

    @Configuration
    @EnableMongoRepositories(basePackages = {"uk.ac.ebi.spot.gwas.deposition.ingest.repository"})
    @EnableTransactionManagement
    @Profile({"gcp-sandbox"})
    public static class MongoConfiGCPSandbox extends AbstractMongoConfiguration {

        @Autowired
        private SystemConfigProperties systemConfigProperties;

        @Autowired
        private IngestServiceConfig gwasDepositionBackendConfig;

        @Override
        protected String getDatabaseName() {
            return gwasDepositionBackendConfig.getDbName();
        }

        @Bean
        public GridFsTemplate gridFsTemplate() throws Exception {
            return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
        }

        @Override
        public MongoClient mongoClient() {
            String mongoUri = systemConfigProperties.getMongoUri();
            String dbUser = systemConfigProperties.getDbUser();
            String dbPassword = systemConfigProperties.getDbPassword();
            String credentials = "";
            if (dbUser != null && dbPassword != null) {
                dbUser = dbUser.trim();
                dbPassword = dbPassword.trim();
                if (!dbUser.equalsIgnoreCase("") &&
                        !dbPassword.equalsIgnoreCase("")) {
                    credentials = dbUser + ":" + dbPassword + "@";
                }
            }

            return new MongoClient(new MongoClientURI("mongodb+srv://" + credentials + mongoUri));
        }
    }
}
