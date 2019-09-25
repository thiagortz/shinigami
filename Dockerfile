FROM gradle:5.6.2-jdk11 AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM openjdk:11-jre-slim

COPY --from=build /home/gradle/src/build/libs/*.jar /app/pubsub-example.jar
COPY gcloud.json /app/gcloud.json

ENTRYPOINT ["java", "-jar", "/app/pubsub-example.jar"]