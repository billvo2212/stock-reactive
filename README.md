# Stock Price Spring Boot Reactive Application

This application is a Spring Boot-based reactive system designed to manage stock prices, utilizing PostgreSQL as its database.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17**: The application is built using Java 17.
- **Docker Engine or Docker Desktop**: Required to build and run the application in Docker containers.
- **Spring Boot Application Folder**: Ensure you have the Spring Boot application folder on your local machine.

## Building and Running the Application

To build and run the application along with the PostgreSQL database, follow these steps:

1. **Navigate to the Application Directory**

   Open a terminal and navigate to the directory containing the Spring Boot application:

   ```bash
   cd /path/to/your/spring-boot-application
   ```
2. **Build and Start the Services**

    Builds Docker image for the Spring Boot application and Starts PostgreSQL database service using:
    
    ```bash
    docker-compose up --build
    ```
3. **Access the Application**
    using:
    ```
    http:localhost:8080
    ```
4. **Stop Application**
    using:
    ```bash
    docker-compose down
    ```
#   s t o c k - r e a c t i v e  
 