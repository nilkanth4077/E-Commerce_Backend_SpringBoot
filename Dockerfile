FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim
COPY --from=build target/e-commerce-0.0.1-SNAPSHOT.jar e-commerce.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "e-commerce.jar"]