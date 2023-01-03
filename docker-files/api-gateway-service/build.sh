#!/bin/bash

cd ../../api-gateway-service/
./gradlew clean bootJar
cp build/libs/api-gateway-service-1.0.jar ../docker-files/api-gateway-service/
cd ../docker-files/api-gateway-service/
docker build -t yh0921k/api-gateway-service:1.0 .
