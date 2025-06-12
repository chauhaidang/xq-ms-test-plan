#!/bin/bash

# Check if version argument is provided
if [ -z "$1" ]; then
  echo "Usage: $0 <version>"
  exit 1
fi

VERSION=$1
IMAGE_NAME="xq-ms-test-plan"

echo $CR_PAT | docker login ghcr.io -u USERNAME --password-stdin
docker push "ghcr.io/chauhaidang/${IMAGE_NAME}:${VERSION}"