# Generated API Clients

This directory contains auto-generated API client libraries.

⚠️ **Do not edit files in this directory manually** - they are automatically generated from `api/test-plan-api.yaml`.

## Contents

When generated, you'll find:

- `java/` - Java client library (Maven project)
- (future: `typescript/` - TypeScript/JavaScript client library)

## Generation

Clients are generated automatically by GitHub Actions when the API version changes, or manually using:

```bash
cd ../client-generator
./generate-java-client.sh
```

See [client-generator/README.md](../client-generator/README.md) for details.
