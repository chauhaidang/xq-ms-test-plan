# XQ-MS-TEST-PLAN

> Spring Boot microservice for managing test plan requirements

**Version**: 0.0.1
**Java**: 17
**Spring Boot**: 3.3.0
**Database**: PostgreSQL 16 (H2 for tests)

---

## Overview

XQ-MS-TEST-PLAN is a REST API microservice that manages test requirements for QA teams. It provides centralized requirement documentation with support for categorization (scopes, tags), references, and audit trails.

### Business Purpose

- **Centralize** test requirement documentation
- **Organize** requirements by scope, tags, and references
- **Track** requirement lifecycle with audit trails
- **Enable** integration with test management and CI/CD systems
- **Prevent** duplicate requirements through title uniqueness

### Key Features

- ✅ CRUD operations for test requirements
- ✅ UUID-based resource identification
- ✅ Automatic audit trail (created/modified by/at)
- ✅ Title uniqueness validation
- ✅ OpenAPI/Swagger documentation
- ✅ Comprehensive testing (unit + component tests)
- 🚧 Requirement linking (entity exists, API pending)

---

## Quick Start

### Prerequisites

```bash
# Java 17
java -version

# Set GitHub credentials for private dependencies
export GITHUB_ACTOR=your-github-username
export GITHUB_TOKEN=your-github-token

# PostgreSQL (Docker recommended)
docker --version
```

### Start Service

**Option 1: Using Docker Compose (Recommended)**
```bash
# Build and start PostgreSQL + application
./gradlew clean build
docker-compose up -d
```
Service runs on **http://localhost:8081**

**Option 2: Local PostgreSQL**
```bash
# Start PostgreSQL only
docker-compose -f docker-compose-local.yml up -d

# Set environment variables
export DB_URL=jdbc:postgresql://localhost:5432/xq_testplan_db
export DB_USERNAME=xq_user
export DB_PASSWORD=xq_password

# Run application
./gradlew clean bootRun
```
Service runs on **http://localhost:8080**

See [DATABASE_MIGRATION.md](./DATABASE_MIGRATION.md) for detailed setup instructions.

### View API Documentation

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html (or 8081 for Docker)
- **Database**: PostgreSQL (use pgAdmin or psql)

### Example Request

```bash
curl --request POST \
  --url http://localhost:8080/api/requirement/create \
  --header 'Content-Type: application/json' \
  --data '{
    "title": "Login Authentication Test",
    "description": "Verify user can login with valid credentials",
    "scopes": "authentication,login,security",
    "tags": "critical,smoke-test,p0",
    "ref": "https://jira.example.com/TICKET-123"
  }'
```

---

## Architecture

### Layered Architecture

```
┌─────────────────────────────────────┐
│  Controller Layer                   │  REST endpoints, validation
│  (RequirementsController)           │
├─────────────────────────────────────┤
│  Service Layer                      │  Business logic, transactions
│  (IRequirementsService)             │
├─────────────────────────────────────┤
│  Repository Layer                   │  Data access (JPA)
│  (RequirementsRepository)           │
├─────────────────────────────────────┤
│  Database (PostgreSQL)              │  Persistent database + Flyway
└─────────────────────────────────────┘
```

### Components

- **Main Application**: `TestPlanApplication.java` - Spring Boot app with OpenAPI config
- **Entities**: `Requirements`, `LinkedRequirements` with JPA auditing via `BaseEntity`
- **Service Layer**: `IRequirementsService` interface + `RequirementsServiceImpl`
- **Repository Layer**: Spring Data JPA repositories
- **Controller Layer**: `RequirementsController` with REST endpoints
- **DTOs**: Separate request/response objects for API contracts
- **Exception Handling**: `@ControllerAdvice` with custom exceptions
- **Mapper**: Static utility for entity ↔ DTO conversion

---

## Domain Model

### Requirements Entity

| Field | Type | Length | Description |
|-------|------|--------|-------------|
| `reqId` | Long | - | Auto-generated primary key (internal) |
| `uuid` | String | 40 | Unique identifier for external reference |
| `title` | String | 100 | Requirement name (must be unique) |
| `description` | String | 500 | Detailed explanation |
| `scopes` | String | 500 | Comma-separated categories |
| `tags` | String | 200 | Comma-separated labels |
| `references` | String | 500 | URLs/documents |
| `createdAt` | LocalDateTime | - | Auto-populated |
| `createdBy` | String | - | Auto-populated ("ms-test-plan") |
| `updatedAt` | LocalDateTime | - | Auto-updated |
| `updatedBy` | String | - | Auto-updated |

### Business Rules

1. **Title Uniqueness**: No duplicate titles allowed
2. **Mandatory Fields**: All fields required
3. **UUID Immutability**: Generated once at creation
4. **Audit Trail**: All changes tracked automatically

---

## API Endpoints

### REST API

| Method | Endpoint | Description | Status Codes |
|--------|----------|-------------|--------------|
| `POST` | `/api/requirement/create` | Create requirement | 201, 400 |
| `GET` | `/api/requirement/{uuid}` | Get by UUID | 200, 404 |
| `PUT` | `/api/requirement/update?uuid=` | Update requirement | 200, 404, 417 |
| `DELETE` | `/api/requirement/delete?uuid=` | Delete requirement | 200, 404, 417 |
| `DELETE` | `/api/requirement/delete/all` | Delete all (admin) | 200, 417 |
| `GET` | `/api/requirement/all` | List all requirements | 200 |

### Response Format

**Success (Create)**
```json
{
  "statusCode": "201",
  "statusMsg": "Requirement created successfully",
  "uuid": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Error (Validation)**
```json
{
  "apiPath": "/api/requirement/create",
  "errorCode": "BAD_REQUEST",
  "errorMessage": "Requirement with title already exists",
  "errorTime": "2025-10-18T21:30:45"
}
```

---

## Development Commands

### Building and Running

```bash
# Start service (port 8080)
./gradlew clean bootRun

# Build project
./gradlew build

# Clean build
./gradlew clean build
```

### Testing

```bash
# All unit tests (com.xq.testplan.unit.*)
./gradlew test

# Single unit test
./gradlew test --tests "com.xq.testplan.unit.service.IRequirementsServiceTest"

# Integration tests (com.xq.testplan.integration.*)
./gradlew msIntTest

# Component tests (Bruno CLI)
./gradlew msCompTest

# Full test suite
./gradlew xqStartApp
./gradlew msCompTest
./gradlew xqStopApp
```

### Docker Operations

```bash
# Build Docker image
./docker-build.sh

# Run with Docker Compose
docker-compose up

# Component tests in CI (full lifecycle)
./gradlew msCompTestCi
```

---

## Testing Framework

### Unit Tests (JUnit 5 + Mockito)

- **Location**: `src/test/java/com/xq/testplan/unit/`
- **Pattern**: Mock repository, test service logic in isolation
- **Coverage**: All service methods (happy path + exceptions)
- **Run**: `./gradlew test`

### Component Tests (Bruno CLI)

- **Location**: `src/test/java/com/xq/testplan/component/*.bru`
- **Framework**: Bruno CLI for API testing
- **Environment**: SIT (localhost:8081)
- **Report**: HTML at `src/test/java/com/xq/testplan/component/report.html`
- **Script**: `scripts/run-bru-tests.sh` (auto-installs Bruno)
- **Run**: `./gradlew msCompTest`

### Custom Gradle Tasks

Provided by `xq-dev` plugin (v2.0.3):

- `xqStartApp` - Starts app in background for testing
- `xqStopApp` - Stops background app
- `msIntTest` - Runs integration tests
- `msCompTest` - Runs Bruno component tests
- `msCompTestCi` - Full CI lifecycle: `composeUp → msCompTest → composeDown`

---

## Configuration

### Profiles

| Profile | Port | Database | Use Case |
|---------|------|----------|----------|
| `default` | 8080 | PostgreSQL | Local development |
| `component` | 8081 | PostgreSQL (Docker) | Component testing |
| `test` | 8080 | H2 in-memory | Unit tests |
| `integration` | 0 | H2 in-memory | Integration tests |

### Application Config

```yaml
# application.yml (PostgreSQL)
server:
  port: 8080
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/xq_testplan_db}
    username: ${DB_USERNAME:xq_user}
    password: ${DB_PASSWORD:xq_password}
    hikari:
      maximum-pool-size: 10
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
    baseline-on-migrate: true
```

See [DATABASE_MIGRATION.md](./DATABASE_MIGRATION.md) for environment variables and migration details.

### Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| Spring Boot | 3.3.0 | Framework |
| SpringDoc OpenAPI | 2.6.0 | API documentation |
| PostgreSQL Driver | 42.7.3 | Database driver |
| Flyway Core | 10.10.0 | Database migrations |
| H2 Database | 2.2.224 | Test database (test scope) |
| Karate | 1.5.1 | (Declared but unused) |
| `xq-dev` plugin | 2.0.3 | Custom Gradle tasks |
| `xq-api-test-plan` | 1.0.0 | Test utilities |
| `xq-kit-common` | 1.0.0 | Common utilities |

### GitHub Package Registry

Private dependencies require authentication:

```bash
export GITHUB_ACTOR=your-github-username
export GITHUB_TOKEN=your-github-token
```

**Repositories**:
- Plugins: `chauhaidang/xq-plugins`
- Libraries: `chauhaidang/xq-kit-common`

---

## Key Files

| File | Purpose |
|------|---------|
| `build.gradle` | Build config with custom XQ plugin |
| `settings.gradle` | Plugin management + GitHub packages |
| `docker-compose.yml` | Service config (port 8081, component profile) |
| `Dockerfile` | OpenJDK 17 + app.jar |
| `scripts/run-bru-tests.sh` | Bruno test execution |

---

## Known Limitations

### Current Limitations

- ❌ **No pagination** on GET all endpoint
- ❌ **No integration tests** for repository layer
- ❌ **LinkedRequirements API** not implemented (entity exists)
- ❌ **Hardcoded security key** for delete all (should be externalized)
- ❌ **AuditAware** uses service name, not real user
- ❌ **No caching** strategy implemented
- ❌ **Inconsistent parameter style** (path vs query for UUID)

### Future Enhancements

- ✨ Implement LinkedRequirements API for dependency tracking
- ✨ Add pagination/filtering to list endpoint
- ✨ Externalize security configuration
- ✨ Implement real user context for auditing
- ✨ Add caching layer (Redis)
- ✨ Add bulk import/export functionality
- ✨ Database connection pooling metrics and monitoring

---

## Getting Started Checklist

1. ✅ Install Java 17
2. ✅ Set `GITHUB_ACTOR` and `GITHUB_TOKEN` environment variables
3. ✅ Run `./gradlew clean build` to verify setup
4. ✅ Run `./gradlew bootRun` to start service
5. ✅ Access Swagger UI at http://localhost:8080/swagger-ui/index.html
6. ✅ Run `./gradlew test` to verify unit tests
7. ✅ Run `./gradlew msCompTest` to verify component tests

---

## System Context

Part of the **XQ Test Management Ecosystem** with:
- Shared custom libraries (`xq-kit-common`)
- Standardized tooling (`xq-dev` plugin)
- Consistent naming conventions (`xq-svc-*`)
- Integration with test execution systems

```
┌─────────────────────────────────────────┐
│    XQ Test Management Ecosystem         │
├─────────────────────────────────────────┤
│                                          │
│  ┌──────────────────┐                   │
│  │  Test Execution  │                   │
│  │  Service         │                   │
│  └────────┬─────────┘                   │
│           │ GET Requirements             │
│           ↓                              │
│  ┌──────────────────┐                   │
│  │ XQ-MS-TEST-PLAN  │ ← This Service   │
│  │ (Requirements)   │                   │
│  └────────┬─────────┘                   │
│           │ Link Requirements            │
│           ↓                              │
│  ┌──────────────────┐                   │
│  │  Test Case       │                   │
│  │  Service         │                   │
│  └──────────────────┘                   │
│                                          │
└─────────────────────────────────────────┘
```

---

## Support

For project guidance:
- See [CLAUDE.md](./CLAUDE.md) for Claude Code instructions
- See [.claude/agents/](./claude/agents/) for agent configurations
- Contact: David Chau (service.testplan@xq.com)

---

## License

Apache 2.0
