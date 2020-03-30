#!/bin/bash
docker build --force-rm=true -t gwas-deposition-ingest:latest .
docker tag gwas-deposition-ingest:latest ebispot/gwas-deposition-ingest:latest-sandbox
docker push ebispot/gwas-deposition-ingest:latest-sandbox
