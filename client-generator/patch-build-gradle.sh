#!/bin/bash

# Script to patch generated build.gradle with necessary dependencies
set -e

BUILD_GRADLE="../generated-clients/java/build.gradle"

if [ ! -f "$BUILD_GRADLE" ]; then
    echo "Error: build.gradle not found at $BUILD_GRADLE"
    exit 1
fi

echo "📝 Patching build.gradle with Hibernate Validator dependency..."

# Add hibernate_validator_version to ext block if not already present
if ! grep -q "hibernate_validator_version" "$BUILD_GRADLE"; then
    sed -i.bak '/bean_validation_version = /a\
    hibernate_validator_version = "8.0.1.Final"
' "$BUILD_GRADLE"
fi

# Add Hibernate Validator dependency if not already present
if ! grep -q "org.hibernate.validator:hibernate-validator" "$BUILD_GRADLE"; then
    sed -i.bak '/jakarta.validation:jakarta.validation-api/a\
    implementation "org.hibernate.validator:hibernate-validator:$hibernate_validator_version"
' "$BUILD_GRADLE"
fi

# Clean up backup file
rm -f "$BUILD_GRADLE.bak"

echo "✅ build.gradle patched successfully"
