# API Client Auto-Generation & Publishing Setup

## ✅ Setup Complete!

Your microservice now automatically generates and publishes Java API clients to GitHub Packages when the API version changes.

---

## 📋 What's Been Configured

### 1. **Version-Based Triggering**
- Monitors: `api/test-plan-api.yaml`
- Trigger: When the `version` field changes (e.g., 1.0.0 → 1.1.0)
- Branch: `main` branch only
- Also supports: Manual workflow dispatch

### 2. **Java Client Generation**
- **Generator**: OpenAPI Generator CLI
- **HTTP Client**: Spring RestTemplate
- **Package**: `com.xq.testplan:testplan-api-client`
- **Features**:
  - Type-safe API methods
  - Lombok builders
  - Jakarta EE validation
  - Java 8+ date/time
  - Jackson serialization

### 3. **GitHub Packages Publishing**
- **Registry**: GitHub Packages (Maven)
- **Automatic**: Published when version changes
- **Authentication**: Uses `GITHUB_TOKEN` automatically

---

## 🚀 How to Use

### Step 1: Update API Version

Edit `api/test-plan-api.yaml`:

```yaml
info:
  title: TestPlan microservice
  version: 1.1.0  # ← Change this version
```

### Step 2: Commit and Push

```bash
git add api/test-plan-api.yaml
git commit -m "Bump API version to 1.1.0"
git push origin main
```

### Step 3: Watch the Workflow

Go to: **Actions** → **Generate and Publish API Client**

The workflow will:
1. ✅ Detect version change (1.0.0 → 1.1.0)
2. ✅ Generate Java client
3. ✅ Build JAR file
4. ✅ Publish to GitHub Packages
5. ✅ Post release notes

---

## 📦 Using the Published Client

### In Your Gradle Project

#### 1. Configure GitHub Packages Repository

Add to `build.gradle`:

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

#### 2. Add Dependency

```groovy
dependencies {
    implementation 'com.xq.testplan:testplan-api-client:1.1.0'
}
```

#### 3. Configure Authentication

**Option A:** Add to `~/.gradle/gradle.properties`:

```properties
gpr.user=YOUR_GITHUB_USERNAME
gpr.token=YOUR_PERSONAL_ACCESS_TOKEN
```

**Option B:** Use environment variables:

```bash
export GITHUB_ACTOR=your-github-username
export GITHUB_TOKEN=your-personal-access-token
```

**Create GitHub Token:**
1. Go to: Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Generate new token with `read:packages` scope
3. Use this token as `gpr.token` or `GITHUB_TOKEN`

#### 4. Use the Client

```java
import com.xq.testplan.client.ApiClient;
import com.xq.testplan.client.api.RestApisTestPlanApi;
import com.xq.testplan.client.model.Requirement;
import com.xq.testplan.client.model.Response;

// Configure client
ApiClient apiClient = new ApiClient();
apiClient.setBasePath("http://localhost:8081");

// Create API instance
RestApisTestPlanApi api = new RestApisTestPlanApi(apiClient);

// Create requirement
Requirement req = new Requirement()
    .title("Test Login")
    .description("Verify login functionality")
    .scopes("auth,security")
    .tags("critical")
    .ref("JIRA-123");

Response response = api.createRequirement(req);
System.out.println("Created: " + response.getUuid());

// Fetch requirement
Requirement fetched = api.fetchRequirement(response.getUuid());

// List all
ListRequirementsDto all = api.getAllRequirements();
System.out.println("Total requirements: " + all.getTotal());

// Update
req.setDescription("Updated description");
api.updateRequirement(response.getUuid(), req);

// Delete
api.deleteRequirement(response.getUuid());
```

---

## 🧪 Local Testing (Before Pushing)

Test client generation locally without publishing:

```bash
cd client-generator
./generate-java-client.sh
```

This generates the client in `generated-clients/java/`.

To use locally:

```bash
cd generated-clients/java
mvn install
```

Now you can use it in local projects without publishing to GitHub Packages.

---

## 📁 Files Created

```
xq-ms-test-plan/
├── .github/
│   └── workflows/
│       └── publish-api-client.yml    # GitHub Actions workflow
│
├── api/
│   └── test-plan-api.yaml            # OpenAPI spec (UPDATED)
│
├── client-generator/
│   ├── README.md                     # Client generator docs
│   ├── config-java.json              # OpenAPI Generator config
│   └── generate-java-client.sh       # Local generation script
│
├── generated-clients/
│   ├── .gitignore                    # Ignore generated files
│   ├── README.md                     # Generated clients info
│   └── java/                         # Generated (not committed)
│
└── API-CLIENT-SETUP.md               # This file
```

---

## 🔄 Workflow Behavior

### Scenario 1: Version Changed
```
Commit changes api/test-plan-api.yaml (version: 1.0.0 → 1.1.0)
    ↓
Push to main branch
    ↓
GitHub Actions detects version change
    ↓
Generate Java client
    ↓
Build JAR
    ↓
Publish to GitHub Packages
    ↓
✅ Client available: com.xq.testplan:testplan-api-client:1.1.0
```

### Scenario 2: Version Unchanged
```
Commit changes api/test-plan-api.yaml (version stays 1.0.0)
    ↓
Push to main branch
    ↓
GitHub Actions detects NO version change
    ↓
⏭️  Skip generation and publishing
```

### Scenario 3: Non-version Changes
```
Add new endpoint but keep version 1.0.0
    ↓
⚠️  Client NOT published (version must change!)
```

**Best Practice:** Increment version whenever you modify the API spec.

---

## 🎯 Versioning Strategy

Follow Semantic Versioning (SemVer):

- **Major (x.0.0)**: Breaking changes (removed fields, changed types)
- **Minor (1.x.0)**: New features (new endpoints, optional fields)
- **Patch (1.0.x)**: Bug fixes (documentation, typos)

Examples:
```yaml
# Add new endpoint
version: 1.0.0 → 1.1.0

# Fix typo in description
version: 1.0.0 → 1.0.1

# Remove required field
version: 1.0.0 → 2.0.0
```

---

## 🔐 Security Notes

### GitHub Token Permissions

The workflow requires:
- ✅ `contents: read` - Read repository
- ✅ `packages: write` - Publish to GitHub Packages

These are automatically granted to `GITHUB_TOKEN`.

### Personal Access Tokens

For downloading packages, users need tokens with:
- ✅ `read:packages` - Download from GitHub Packages

---

## 🐛 Troubleshooting

### Problem: Workflow doesn't trigger

**Solution:**
- Ensure you changed the `version` field in `api/test-plan-api.yaml`
- Ensure you pushed to `main` branch
- Check workflow file exists: `.github/workflows/publish-api-client.yml`

### Problem: "Failed to publish artifact"

**Solution:**
- Check GitHub Packages permissions are enabled for your repository
- Verify `GITHUB_TOKEN` has `packages: write` permission

### Problem: Can't download client in another project

**Solution:**
- Create Personal Access Token with `read:packages` scope
- Add token to `~/.m2/settings.xml`
- Ensure repository URL matches: `https://maven.pkg.github.com/YOUR_ORG/xq-ms-test-plan`

### Problem: Build fails with "javax.* not found"

**Solution:**
The client uses Jakarta EE. Add to your project:

```xml
<dependency>
  <groupId>jakarta.validation</groupId>
  <artifactId>jakarta.validation-api</artifactId>
  <version>3.0.2</version>
</dependency>
```

---

## 📊 Monitoring

### View Published Packages

Go to: Repository → Packages (right sidebar)

You'll see: `testplan-api-client` with all published versions

### View Workflow Runs

Go to: Repository → Actions → "Generate and Publish API Client"

Each run shows:
- ✅ Version change detection
- ✅ Generation logs
- ✅ Build output
- ✅ Publish confirmation

---

## 🔮 Future Enhancements

### TypeScript/JavaScript Client

To add TypeScript client generation:

1. Create `client-generator/config-typescript.json`
2. Add TypeScript generation job to workflow
3. Publish to GitHub Packages NPM registry

### Automated Changelog

Generate changelog from API spec differences:
- Compare old vs new spec
- List added/removed endpoints
- Document breaking changes

### Version Tagging

Automatically create Git tags when publishing:
```bash
git tag -a api-v1.1.0 -m "API version 1.1.0"
```

---

## ✅ Next Steps

1. **Test Locally:**
   ```bash
   cd client-generator
   ./generate-java-client.sh
   ```

2. **Update API Version:**
   ```bash
   # Edit api/test-plan-api.yaml
   # Change version: 1.0.0 → 1.0.1
   ```

3. **Commit and Push:**
   ```bash
   git add .
   git commit -m "Setup API client auto-generation"
   git push origin main
   ```

4. **Watch Workflow Run:**
   Go to GitHub → Actions tab

5. **Use the Client:**
   Add dependency to your consuming projects

---

## 📚 Resources

- [OpenAPI Generator](https://openapi-generator.tech/)
- [GitHub Packages Maven](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry)
- [Semantic Versioning](https://semver.org/)
- [Client Generator README](client-generator/README.md)

---

🎉 **Setup Complete!** Your API clients will now be automatically generated and published on version changes.
