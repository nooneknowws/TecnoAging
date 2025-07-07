
FROM gradle:8.4-jdk17 AS builder

WORKDIR /app

COPY settings.gradle .

COPY gradle ./gradle

COPY gradlew .

RUN chmod +x ./gradlew

COPY . .

WORKDIR /app/_testes/Testes

RUN ../../gradlew dependencies --no-daemon

RUN ../../gradlew bootJar --no-daemon

WORKDIR /app

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/_testes/Testes/build/libs/*.jar app.jar

EXPOSE 5004

ENTRYPOINT ["java", "-jar", "app.jar"]
