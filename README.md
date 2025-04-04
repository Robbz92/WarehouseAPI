# Warehouse Service

This is a simple Warehouse Service built with Spring Boot (Reactive/Webflux), Docker and MariaDB 
for keeping track on products and articles for sale.

## Prerequisites

Before running the service, ensure you have the following installed on your machine:
- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/)

## Setup Instruction
1. **Clone the repository**:
   '''bash
   git clone https://github.com/Robbz92/WarehouseAPI.git
   cd WarehouseAPI

2. **Configuration**:
   *Docker*:
    - MYSQL_ROOT_PASSWORD=your_password
    - MYSQL_PASSWORD=your_password
        
  *application.yaml*:
    - password your_password

3. **Generate Docker-image**:
    - mvn clean package
    - docker build -t warehouse .
    - docker run -p 8080:8080 warehouse
    - docker-compose up --build

4. **Start Service**:
    - start docker instance (DockerDeskop/temrinal)
    - maven bootRun
  
## Swagger-page
* url: localhost:8080/webjars/swagger-ui/index.html

