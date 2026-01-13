# Import base image
FROM eclipse-temurin:8-jre-alpine

# Create log file directory and set permission
RUN addgroup -S gwas-ingest-service && adduser -S -G gwas-ingest-service -h /home/gwas-ingest-service gwas-ingest-service
RUN if [ ! -d /var/log/gwas/ ];then mkdir /var/log/gwas/;fi
RUN chown -R gwas-ingest-service:gwas-ingest-service /var/log/gwas

# Move project artifact
ADD target/gwasdepo-ingest-service-*.jar /home/gwas-ingest-service/
USER gwas-ingest-service

# Launch application server
ENTRYPOINT exec $JAVA_HOME/bin/java $XMX $XMS -jar -Dspring.profiles.active=$ENVIRONMENT /home/gwas-ingest-service/gwasdepo-ingest-service-*.jar
