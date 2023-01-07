#!/bin/bash

cd ../../catalog-service/
./gradlew clean bootJar
cp build/libs/catalog-service-1.0.jar ../docker-files/catalog-service/
cd ../docker-files/catalog-service/
docker build -t yh0921k/catalog-service:1.0 .
