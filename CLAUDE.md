# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

XQ-MS-TEST-PLAN is a Spring Boot microservice (version 3.3.0) that provides a REST API for managing test plan requirements. It uses H2 in-memory database, JPA for data persistence, and includes comprehensive testing with both unit tests and API component tests using Bruno.

## Architecture

- **Main Application**: `TestPlanApplication.java` - Spring Boot application with OpenAPI documentation
- **Entities**: `Requirements` and `LinkedRequirements` with JPA auditing support via `BaseEntity`
- **Service Layer**: `IRequirementsService` interface with `RequirementsServiceImpl` implementation
- **Repository Layer**: Spring Data JPA repositories for data access
- **Controller Layer**: `RequirementsController` providing REST endpoints
- **DTOs**: Request/response objects for API communication
- **Exception Handling**: Global exception handler with custom exceptions

## Development Commands

### Building and Running
- **Start service**: `./gradlew clean bootRun` (runs on port 8080)
- **Build project**: `./gradlew build`
- **Clean build**: `./gradlew clean build`

### Testing
- **Unit tests**: `./gradlew test` (runs tests matching `com.xq.testplan.unit.*`)
- **Integration tests**: `./gradlew msIntTest` (runs tests matching `com.xq.testplan.integration.*`)
- **Component tests**: `./gradlew msCompTest` (runs Bruno API tests via script)
- **Full test suite**:
  ```bash
  ./gradlew xqStartApp
  ./gradlew msCompTest
  ./gradlew xqStopApp
  ```

### Docker Operations
- **Build image**: `./docker-build.sh`
- **Run with Docker Compose**: `docker-compose up`
- **Component tests in CI**: `./gradlew msCompTestCi` (includes Docker Compose lifecycle)

## Testing Framework

### Unit Tests
- Uses JUnit 5 with standard Spring Boot testing annotations
- Located in `src/test/java/com/xq/testplan/unit/`

### Component Tests
- Uses Bruno CLI for API testing
- Test files located in `src/test/java/com/xq/testplan/component/`
- Bruno tests run against live service instance
- Generates HTML reports at `./report.html`
- Environment configuration in `environments/sit.bru`

### Custom Gradle Tasks
- `xqStartApp`: Starts the application for testing
- `xqStopApp`: Stops the application after testing
- `msIntTest`: Runs integration tests
- `msCompTest`: Runs Bruno component tests
- `msCompTestCi`: Runs component tests with Docker Compose lifecycle

## Configuration

### Profiles
- **Default**: Uses H2 in-memory database on port 8080
- **Component**: Used for component testing (port 8081 in Docker)

### Key Dependencies
- Spring Boot 3.3.0 (Web, JPA, Validation, Actuator)
- SpringDoc OpenAPI 2.6.0
- H2 Database 2.2.224
- Karate 1.5.1 for testing
- Custom XQ plugins and libraries from GitHub packages

### GitHub Package Registry
Requires environment variables for private package access:
- `GITHUB_ACTOR`: GitHub username
- `GITHUB_TOKEN`: GitHub personal access token

## API Documentation
- OpenAPI/Swagger UI available at: `http://localhost:8080/swagger-ui/index.html`
- H2 Console available at: `http://localhost:8080/h2-console`

## Key Files
- `build.gradle`: Main build configuration with custom XQ plugin
- `settings.gradle`: Plugin management and GitHub package registry setup
- `docker-compose.yml`: Service configuration for component testing
- `scripts/run-bru-tests.sh`: Bruno test execution script