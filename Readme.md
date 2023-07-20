## Doctor Labeling API
The Doctor Labeling API is a RESTful web service built using Java, Spring Boot, and Spring Data JPA. It allows users 
to manage medical cases and labels for training data used in ML-based diagnosis.
Users can create, retrieve, update, and delete cases and labels. The API is also Dockerized for easy deployment.

## Technologies Used
- Java 17
- Spring Boot 3.0.4
- PostgreSQL (as the database)
- Lombok (Annotation)
- SpringDoc (for API Documentation)
- Ehcache (for caching)
- Liquibase (for database version control)
- Maven (for build and dependency management)

## Usage
1. Clone the repository.
2. Ensure you have Java 17 and Maven installed on your system.
3. Set up the PostgreSQL database and update the database connection details in the application properties file.
## Docker Setup
To run the Doctor Labeling API using Docker, follow these steps:

Install Docker : Make sure you have Docker installed on your machine. You can download it from the official Docker website for your operating system.

Clone the Repository : Clone this repository to your local machine.

Build the Docker Image : Navigate to the project directory and run the following
command to build the Docker image:

```
mvn clean package
```

```
docker compose up
```


The API will be accessible at http://localhost:8080.

## Unit Testing & Integration Testing
- used the test container approach to create a postgres docker container to perform integration testing for the API endpoints
- Unit testing of the API services.

## API Endpoints
... use swagger ui for details

## Error Handling
... The API handles errors and returns appropriate HTTP status codes for different scenarios. If a resource is not found,
the API returns a 404 Not Found status code. For other errors, it returns a 500 Internal Server Error status code.

## API Documentation with Swagger/OpenAPI
The Doctor Labeling API comes with built-in Swagger/OpenAPI documentation to help you understand and explore the API endpoints.
To access the Swagger UI, follow these steps:

Ensure the API is running.

Open your web browser and navigate to the following URL:

http://localhost:8080/swagger-ui.html

The Swagger UI will be displayed, showing all the API endpoints and their descriptions.
You can interact with the API right from the Swagger UI.

## Postman Client
If you prefer using Postman for API testing and exploration,
we have prepared a Postman collection that includes all the API endpoints.
You can import this collection into your Postman application to get started quickly.

Import the Postman Collection : 

Click on the "Import" button in Postman,
choose "Import From Link," and enter the URL of the Swagger documentation:


The collection will be imported into Postman, and you will have 
access to all the API endpoints ready for testing.

## Database Version Control (Liquibase)
Liquibase is used to manage database version control in this project. The changelog files are stored in the `src/main/resources/db/changelog` directory. Each changelog file represents a specific database version with defined changes. To apply database changes, use the following Maven command: `mvn liquibase:update`.

## Cache Implementation (Ehcache)
Ehcache is integrated into the application for caching purposes.
Caching is applied to certain methods in the `CaseServiceImpl` and `LabelServiceImpl`class to improve performance by reducing database queries.
