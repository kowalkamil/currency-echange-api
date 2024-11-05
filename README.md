# Currency Exchange API

## Overview
The Currency Exchange API is a RESTful service designed to handle currency exchange. The service supports registering and logging user, also it supports exchanging currency for user.

## Features
- **Register user**: Allows to register new user
- **Login user**: Allows to user login
- **Get user details**: Return user details.
- **Exchange currency**: Allows users to exchange currency

## API Documentation
The API is documented using Swagger and can be accessed via the `/swagger-ui.html` endpoint.

### Endpoints

1. **Register user**
    - **URL**: `/api/v1/account/register`
    - **Method**: `POST`
    - **Request Body**:
      ```json
      {
        "name": "string",
        "lastName": "string",
        "password": "string",
        "startingBalancePln": "number"
      }
      ```
2. **Login user**
    - **URL**: `/api/v1/account/login`
    - **Method**: `POST`
    - **Request Body**:
      ```json
      {
        "username": "string",
        "password": "string"
      }
      ```

3. **Get user details**
    - **URL**: `/api/v1/user`
    - **Method**: `GET`

4. **UExchange currency**
    - **URL**: `/api/v1/user/exchange`
    - **Method**: `POST`
   - **Request Params**:
       - `currency`: currency enum.
       - `amount`: amount.

## Resilience4j Integration
The service uses Resilience4j. The following resilience patterns are implemented:

- **Circuit Breaker**: Protects the service from external API failures by stopping further requests to the API when failures are detected.
- **Retry**: Automatically retries failed requests to the external API before triggering the circuit breaker.

These configurations are specified in the `application.yaml` file:

```yaml
resilience4j:
  circuitbreaker:
    instances:
      geoLocationService:
        sliding-window-size: 10
        minimum-number-of-calls: 5
        failure-rate-threshold: 50
        wait-duration-in-open-state: 10s
  retry:
    instances:
      geoLocationServiceRetry:
        max-attempts: 3
        wait-duration: 500ms
```

## Running the Application

To run the application locally using Docker:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/currency-exchange-api.git
   cd currency-exchange-api
   mvn clean package
   ```

2. **Ensure Docker and Docker Compose are installed** on your machine.

3. **Build and run the Docker containers**:
   Use the following command to build the Docker image for the application and start the PostgreSQL container along with the application container.
   ```bash
   docker-compose up --build
   ```

4. **Access the application**:
   Once the containers are running, you can access the API documentation via Swagger at:
   [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

5. **Stopping the containers**:
   To stop the running containers, use:
   ```bash
   docker-compose down
   ```

### Docker Configuration Details

- **PostgreSQL Container**:
    - Image: `postgres:15`
    - Environment:
        - `POSTGRES_USER`: `user`
        - `POSTGRES_PASSWORD`: `userpassword`
        - `POSTGRES_DB`: `currency_exchange_db`
    - Ports: `5432:5432`
    - Volume: `postgres-data:/var/lib/postgresql/data`

- **Spring Boot Application Container**:
    - Build context: The current directory (which contains the Dockerfile)
    - Environment:
        - `SPRING_DATASOURCE_URL`: `jdbc:postgresql://postgres:5432/currency_exchange_db`
        - `SPRING_DATASOURCE_USERNAME`: `user`
        - `SPRING_DATASOURCE_PASSWORD`: `userpassword`
    - Ports: `8080:8080`
    - Dependency: The application container waits for the PostgreSQL container to be ready before starting.

### Notes

- The `application.yaml` is configured to use the database connection provided by the Docker container.
- All necessary database changes will be applied automatically using Flyway on application startup.
- Ensure that Docker Desktop or Docker Engine is running before executing the `docker-compose up` command.
- For local development without Docker, consider using the `application-dev.yaml` profile, which connects to a locally hosted PostgreSQL instance.