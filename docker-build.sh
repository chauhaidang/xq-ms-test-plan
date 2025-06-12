#!/bin/bash

IMAGE_NAME="xq-ms-test-plan"
docker images rm "$IMAGE_NAME:latest" -f || true
docker build -t "$IMAGE_NAME:latest" .