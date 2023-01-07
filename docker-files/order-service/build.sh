#!/bin/bash

cd ../../order-service/
./gradlew clean bootJar
cp build/libs/order-service-1.0.jar ../docker-files/order-service/
cd ../docker-files/order-service/
docker build -t yh0921k/order-service:1.0 .
