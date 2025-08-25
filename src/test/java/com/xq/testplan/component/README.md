# Test Plan SIT Environment

Service Integration Testing (SIT) environment for the Test Requirements Management API.

## Environment Overview

This directory contains the SIT-specific configurations and setup for testing the Test Requirements Management service.

## Configuration

- Environment: SIT
- Gateway URL: `http://localhost:8080`


## Setup Instructions

1. Download Bruno CLI and Bruno UI
2. Go to terminal and run the following command:
   ```bash
   cd sit/xq-ms-test-plan-sit
   bru run . --env sit --reporter-html ./report.html
   cd ../../
   ```