
FROM gradle:8.4-jdk17 AS builder

WORKDIR /app

COPY settings.gradle .

COPY gradle ./gradle

COPY gradlew .

RUN chmod +x ./gradlew

COPY . .

WORKDIR /app/_analises/Analises

RUN ../../gradlew dependencies --no-daemon

RUN ../../gradlew bootJar --no-daemon

WORKDIR /app

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/_analises/Analises/build/libs/*.jar app.jar

EXPOSE 5003

ENTRYPOINT ["java", "-jar", "app.jar"]
