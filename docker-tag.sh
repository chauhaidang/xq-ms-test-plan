#!/bin/bash

# Check if version argument is provided
if [ -z "$1" ]; then
  echo "Usage: $0 <version>"
  exit 1
fi

VERSION=$1
IMAGE_NAME="xq-ms-test-plan"

# Tag the Docker image
docker tag ${IMAGE_NAME}:latest "ghcr.io/chauhaidang/${IMAGE_NAME}:${VERSION}"

# Print success message
echo "Docker image tagged as ${IMAGE_NAME}:${VERSION}"