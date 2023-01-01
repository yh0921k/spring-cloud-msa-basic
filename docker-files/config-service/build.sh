#!/bin/bash

cd ../../config-service/
./gradlew clean bootJar
cd ..

cp ./config-service/build/libs/config-service-1.0.jar ./docker-files/config-service/
cp ./keystore/apiEncryptionKey.jks ./docker-files/config-service/
cd ./docker-files/config-service/
docker build -t yh0921k/config-service:1.0 .
