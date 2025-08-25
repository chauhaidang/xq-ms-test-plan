#!/bin/bash

# Exit on error
set -e

echo "Running Bruno API tests..."

# Verify Bruno is installed
if ! command -v bru &> /dev/null; then
    echo "Bruno is not installed. Installing..."
    npm install -g @usebruno/cli
fi

# Run Bruno tests
pwd
cd ./src/test/java/com/xq/testplan/component
bru run . --env sit --reporter-html ./report.html
cd ../../

echo "Bruno tests completed"