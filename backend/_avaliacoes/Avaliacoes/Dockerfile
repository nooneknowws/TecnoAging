
FROM gradle:8.4-jdk17 AS builder

WORKDIR /app

COPY settings.gradle .

COPY gradle ./gradle

COPY gradlew .

RUN chmod +x ./gradlew

COPY . .

WORKDIR /app/_avaliacoes/Avaliacoes

RUN ../../gradlew dependencies --no-daemon

RUN ../../gradlew bootJar --no-daemon

WORKDIR /app

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/_avaliacoes/Avaliacoes/build/libs/*.jar app.jar

EXPOSE 5002

ENTRYPOINT ["java", "-jar", "app.jar"]
