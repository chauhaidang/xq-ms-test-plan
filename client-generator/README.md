# API Client Generation

This directory contains configuration and scripts for automatically generating API clients from the OpenAPI specification.

## 📋 Overview

When the API version in `api/test-plan-api.yaml` changes, a GitHub Actions workflow automatically:
1. Detects the version change
2. Generates a Java client library
3. Builds the client as a JAR
4. Publishes to GitHub Packages (Maven repository)

## 🚀 Quick Start

### Local Generation (Testing)

Generate and build the client locally:

```bash
cd client-generator
./generate-java-client.sh
```

This will:
- Extract the API version from the YAML file
- Generate Java client code
- Build the JAR file
- Output the JAR location

### Using the Generated Client

#### Option 1: Publish to Local Maven Repository

```bash
cd generated-clients/java
./gradlew publishToMavenLocal
```

Then add to your `build.gradle`:

```groovy
dependencies {
    implementation 'com.xq.testplan:testplan-api-client:1.0.0'
}
```

#### Option 2: Use from GitHub Packages

Add GitHub Packages repository to your `build.gradle`:

```groovy
repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/YOUR_ORG/xq-ms-test-plan")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.token") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}
```

Configure authentication in `~/.gradle/gradle.properties`:

```properties
gpr.user=YOUR_GITHUB_USERNAME
gpr.token=YOUR_GITHUB_TOKEN
```

Add the dependency:

```groovy
dependencies {
    implementation 'com.xq.testplan:testplan-api-client:1.0.0'
}
```

## 🔄 Automatic Publishing Workflow

### Trigger Conditions

The workflow runs when:
- ✅ A commit modifies `api/test-plan-api.yaml`
- ✅ The `version` field in the YAML has changed
- ✅ Commit is on the `main` branch
- ✅ Manual trigger via GitHub Actions UI

### Version Detection

The workflow compares:
```yaml
# Previous commit
info:
  version: 1.0.0

# Current commit
info:
  version: 1.1.0  # ← Version changed!
```

If the version changed → Generate and publish client
If the version unchanged → Skip (no-op)

## 📦 Generated Client Features

The Java client includes:

- ✅ **Type-safe API methods** - All endpoints from OpenAPI spec
- ✅ **Model classes** - DTOs with validation annotations
- ✅ **Builder pattern** - Lombok builders for all models
- ✅ **Jakarta EE** - Uses Jakarta validation (not javax)
- ✅ **RestTemplate** - Spring RestTemplate HTTP client
- ✅ **Jackson** - JSON serialization/deserialization
- ✅ **Bean Validation** - Automatic request validation
- ✅ **Java 8+** - Modern Java date/time APIs

## 📁 Directory Structure

```
client-generator/
├── README.md                    # This file
├── config-java.json            # OpenAPI Generator config for Java
├── generate-java-client.sh     # Local generation script
└── (future: config-typescript.json)

generated-clients/
└── java/                       # Generated Java client (gitignored)
    ├── src/
    ├── pom.xml
    └── target/
```

## 🛠️ Configuration Files

### `config-java.json`

OpenAPI Generator configuration:

```json
{
  "groupId": "com.xq.testplan",
  "artifactId": "testplan-api-client",
  "apiPackage": "com.xq.testplan.client.api",
  "modelPackage": "com.xq.testplan.client.model",
  "library": "resttemplate",
  "java8": true,
  "useJakartaEe": true
}
```

## 📝 Usage Example

```java
import com.xq.testplan.client.ApiClient;
import com.xq.testplan.client.api.RequirementsApi;
import com.xq.testplan.client.model.Requirement;
import com.xq.testplan.client.model.Response;

// Configure client
ApiClient apiClient = new ApiClient();
apiClient.setBasePath("http://localhost:8081");

// Create API instance
RequirementsApi api = new RequirementsApi(apiClient);

// Create a requirement
Requirement requirement = Requirement.builder()
    .title("Login Authentication Test")
    .description("Verify user can login with valid credentials")
    .scopes("authentication,login,security")
    .tags("critical,smoke-test,p0")
    .ref("https://jira.example.com/TICKET-123")
    .build();

Response response = api.createRequirement(requirement);
System.out.println("Created requirement: " + response.getUuid());

// Fetch requirement
Requirement fetched = api.fetchRequirement(response.getUuid());
System.out.println("Title: " + fetched.getTitle());

// List all requirements
ListRequirementsDto allRequirements = api.getAllRequirements();
System.out.println("Total: " + allRequirements.getTotal());
```

## 🔐 GitHub Packages Authentication

### For GitHub Actions (Automatic)

The workflow uses `GITHUB_TOKEN` automatically - no configuration needed.

### For Local Development

1. Create a Personal Access Token (PAT):
   - Go to GitHub Settings → Developer settings → Personal access tokens
   - Generate new token with `read:packages` scope

2. Configure in `~/.gradle/gradle.properties`:

```properties
gpr.user=YOUR_GITHUB_USERNAME
gpr.token=YOUR_PERSONAL_ACCESS_TOKEN
```

Or use environment variables:

```bash
export GITHUB_ACTOR=your-username
export GITHUB_TOKEN=your-token
```

## 🔧 Customization

### Change Java Client Library

Edit `config-java.json`:

```json
{
  "library": "resttemplate"  // or: "webclient", "okhttp-gson", "retrofit2"
}
```

### Add Additional Annotations

```json
{
  "additionalModelTypeAnnotations": "@lombok.Builder @lombok.Data @CustomAnnotation"
}
```

### Change Package Names

```json
{
  "apiPackage": "com.yourcompany.api",
  "modelPackage": "com.yourcompany.model"
}
```

## 🐛 Troubleshooting

### "Version unchanged" - Client not published

Check that you updated the version in `api/test-plan-api.yaml`:

```yaml
info:
  version: 1.1.0  # ← Increment this
```

### Authentication failed when downloading client

Ensure your GitHub token has `read:packages` permission and is configured in `~/.gradle/gradle.properties` or as environment variables.

### Build fails with "package javax.* does not exist"

The client uses Jakarta EE (not javax). Ensure your project uses Jakarta dependencies:

```xml
<dependency>
  <groupId>jakarta.validation</groupId>
  <artifactId>jakarta.validation-api</artifactId>
</dependency>
```

## 📚 Resources

- [OpenAPI Generator Documentation](https://openapi-generator.tech/)
- [GitHub Packages Maven](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry)
- [OpenAPI Specification](https://swagger.io/specification/)
