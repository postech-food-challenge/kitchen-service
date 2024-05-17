FROM openjdk:12-jdk-alpine

RUN apk add --no-cache bash

WORKDIR /kitchen-ms

CMD ./gradlew run