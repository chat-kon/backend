FROM maven:3.8.1-openjdk-17-slim AS dependencies
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

FROM dependencies AS build
COPY src/ /app/src/
RUN mvn package -DskipTests

FROM openjdk:17-jdk-alpine3.14 AS production
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
EXPOSE 5173
CMD ["java", "-jar", "app.jar"]