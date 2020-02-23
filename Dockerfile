# Import base image
FROM openjdk:8u212-jre

# Create log file directory and set permission
RUN groupadd -r gwas-ingest-service && useradd -r --create-home -g gwas-ingest-service gwas-ingest-service
RUN if [ ! -d /var/log/gwas/ ];then mkdir /var/log/gwas/;fi
RUN chown -R gwas-ingest-service:gwas-ingest-service /var/log/gwas

# Move project artifact
ADD target/gwasdepo-ingest-service-*.jar /home/gwas-ingest-service/
USER gwas-ingest-service

# Launch application server
ENTRYPOINT exec $JAVA_HOME/bin/java $XMX $XMS -jar -Dspring.profiles.active=$ENVIRONMENT /home/gwas-ingest-service/gwasdepo-ingest-service-*.jar
