FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/WarehouseService-0.0.1-SNAPSHOT.jar /app/warehouse.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/warehouse.jar"]
