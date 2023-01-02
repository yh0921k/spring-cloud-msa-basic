#!/bin/bash

cd ../../discovery-service/
./gradlew clean bootJar
cp build/libs/discovery-service-1.0.jar ../docker-files/discovery-service/
cd ../docker-files/discovery-service/
docker build -t yh0921k/discovery-service:1.0 .
