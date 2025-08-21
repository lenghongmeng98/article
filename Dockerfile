FROM gradle:8.5-jdk21-alpine AS build
WORKDIR /app

COPY build.gradle* .
COPY settings.gradle* .
COPY gradle.properties* .

COPY src ./src
RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]