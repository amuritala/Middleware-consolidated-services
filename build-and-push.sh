#!/usr/bin/env bash

set -Eeuo pipefail

IMAGE_NAME="docker.io/techstackimagerepo/consolidated-service"
IMAGE_TAG="${CONSOLIDATED_TAG:-1.5}"
SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"

cd "$SCRIPT_DIR"

if ! command -v docker >/dev/null 2>&1; then
  echo "Docker is required. Install/start Docker Desktop and try again." >&2
  exit 1
fi

if command -v /usr/libexec/java_home >/dev/null 2>&1; then
  DETECTED_JAVA_HOME="$(/usr/libexec/java_home -v 17 2>/dev/null || true)"
  if [[ -n "$DETECTED_JAVA_HOME" ]]; then
    JAVA_HOME="$DETECTED_JAVA_HOME"
    export JAVA_HOME
    export PATH="${JAVA_HOME}/bin:${PATH}"
  fi
fi

if ! command -v java >/dev/null 2>&1; then
  echo "Java 17 is required to build this service." >&2
  exit 1
fi

JAVA_VERSION="$(java -version 2>&1 | awk -F '"' '/version/ { print $2; exit }')"
if [[ "${JAVA_VERSION%%.*}" != "17" ]]; then
  echo "Java 17 is required; found ${JAVA_VERSION:-unknown}. Set JAVA_HOME to a Java 17 installation." >&2
  exit 1
fi

echo "Building and pushing ${IMAGE_NAME}:${IMAGE_TAG} for linux/amd64"

bash ./mvnw -DskipTests \
  "-Dconsolidated.image.tag=${IMAGE_TAG}" \
  compile jib:build

echo "Done! Pushed ${IMAGE_NAME}:${IMAGE_TAG}"
