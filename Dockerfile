FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/e-commerce-0.0.1-SNAPSHOT.jar e-commerce.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "e-commerce.jar"]