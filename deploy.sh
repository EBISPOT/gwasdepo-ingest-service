#!/bin/bash

kubectl delete deploy gwas-ingest-service -n gwas
kubectl apply -f /home/tudor/Desktop/_deployment_plans_/gwas-ingest-service-deployment.yaml
