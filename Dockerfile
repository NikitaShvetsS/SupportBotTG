FROM maven:3.9.9-eclipse-temurin-21 AS builder
LABEL authors="NikitaShvets"

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package

FROM eclipse-temurin:21-jdk AS runner

WORKDIR /app

COPY --from=builder /app/target/SupportBot.jar ./app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]