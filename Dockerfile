FROM maven:3.9-eclipse-temurin-21 as builder

WORKDIR /build

COPY pom.xml .
RUN mvn dependency:resolve

COPY src ./src

RUN mvn clean package -DskipTests=true

FROM eclipse-temurin:21-jre-slim

WORKDIR /app

COPY --from=builder /build/target/bank-monolithic3-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
