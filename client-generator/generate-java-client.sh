#!/bin/bash

# Script to generate Java API client locally for testing
set -e

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}🔧 Generating Java API Client${NC}"

# Get API version from YAML
API_VERSION=$(grep -A1 "^info:" ../api/test-plan-api.yaml | grep "version:" | awk '{print $2}' | tr -d '"' | tr -d "'")
echo -e "${BLUE}📌 API Version: ${API_VERSION}${NC}"

# Check if OpenAPI Generator is installed
if ! command -v openapi-generator-cli &> /dev/null; then
    echo -e "${RED}❌ OpenAPI Generator CLI not found${NC}"
    echo -e "${BLUE}Installing via npm...${NC}"
    npm install -g @openapitools/openapi-generator-cli
fi

# Update version in config
CONFIG_FILE="config-java.json"
sed "s/\"dynamic\"/\"$API_VERSION\"/g" "$CONFIG_FILE" > config-java-temp.json

# Clean previous generation
OUTPUT_DIR="../generated-clients/java"
if [ -d "$OUTPUT_DIR" ]; then
    echo -e "${BLUE}🧹 Cleaning previous generation...${NC}"
    rm -rf "$OUTPUT_DIR"
fi

# Generate Java client
echo -e "${BLUE}📦 Generating Java client...${NC}"
openapi-generator-cli generate \
  -i ../api/test-plan-api.yaml \
  -g java \
  -o "$OUTPUT_DIR" \
  -c config-java-temp.json \
  --additional-properties=licenseName=Apache-2.0

# Cleanup temp config
rm -f config-java-temp.json

echo -e "${GREEN}✅ Java client generated successfully!${NC}"
echo -e "${BLUE}📁 Output location: $OUTPUT_DIR${NC}"

# Patch build.gradle with necessary dependencies
echo -e "${BLUE}🔧 Patching build.gradle...${NC}"
./patch-build-gradle.sh

# Build the client
echo -e "${BLUE}🔨 Building Java client with Gradle...${NC}"
cd "$OUTPUT_DIR"

# Make gradlew executable
chmod +x gradlew

# Build
./gradlew clean build -x test

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Build successful!${NC}"
    JAR_FILE=$(find build/libs -name "*.jar" -not -name "*javadoc*" -not -name "*sources*" | head -n 1)
    echo -e "${BLUE}📦 JAR file: $JAR_FILE${NC}"
else
    echo -e "${RED}❌ Build failed${NC}"
    exit 1
fi

echo ""
echo -e "${GREEN}🎉 All done!${NC}"
echo -e "${BLUE}To use the client locally in your project:${NC}"
echo ""
echo -e "  ${BLUE}1. Publish to local Maven repository:${NC}"
echo -e "     ${BLUE}./gradlew publishToMavenLocal${NC}"
echo ""
echo -e "  ${BLUE}2. Add to your build.gradle:${NC}"
echo -e "     ${BLUE}dependencies {${NC}"
echo -e "       ${BLUE}implementation 'com.xq.testplan:testplan-api-client:${API_VERSION}'${NC}"
echo -e "     ${BLUE}}${NC}"
