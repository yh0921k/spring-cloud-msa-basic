#!/bin/bash

cd ../../user-service/
./gradlew clean bootJar
cp build/libs/user-service-1.0.jar ../docker-files/user-service/
cd ../docker-files/user-service/
docker build -t yh0921k/user-service:1.0 .
