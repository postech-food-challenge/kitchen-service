FROM gradle:8.4.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar -x test --no-daemon

FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/kitchen.jar
ENTRYPOINT ["java","-jar","/app/kitchen.jar"]