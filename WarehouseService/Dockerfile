FROM openjdk:21-jdk-slim

# wait-for-it script
COPY wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

WORKDIR /app

COPY build/libs/WarehouseService-0.0.1-SNAPSHOT.jar /app/warehouse.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "/app/wait-for-it.sh mariadb:3306 -- java -jar /app/warehouse.jar"]