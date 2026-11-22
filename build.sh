#!/bin/bash

# Variables
IMAGE_NAME="consolidated-service"
VERSION=v1
REGISTRY="docker.io/techstackimagerepo"  # e.g., docker.io/username, myregistry.azurecr.io
FULL_IMAGE_NAME="${REGISTRY}/${IMAGE_NAME}:${VERSION}"
LATEST_TAG="${REGISTRY}/${IMAGE_NAME}:1.0"

# Build JAR
echo "Building JAR..."
./mvnw clean package -DskipTests

# Build Docker image
echo "Building Docker image..."
docker build -t ${FULL_IMAGE_NAME} -t ${LATEST_TAG} .

# Tag image
echo "Tagging image..."
docker tag ${FULL_IMAGE_NAME} ${LATEST_TAG}

# Push to registry
echo "Pushing to registry..."
docker push ${FULL_IMAGE_NAME}
docker push ${LATEST_TAG}

echo "Done! Image pushed: ${FULL_IMAGE_NAME}"