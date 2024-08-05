FROM gradle:8.4.0-jdk21 AS build
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts gradle.properties /app/
COPY src /app/src
RUN gradle build

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/kitchen.jar
ENTRYPOINT ["java", "-jar", "/app/kitchen.jar"]