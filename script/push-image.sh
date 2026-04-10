#!/usr/bin/env bash
set -euo pipefail

DOCKERHUB_USERNAME="faisalamir0021"
TARGET_IMAGE_NAME="ai-webhook"
TARGET_IMAGE_TAG="latest"
SOURCE_IMAGE_NAME="tel-webhook:latest"

if [ -z "${DOCKERHUB_USERNAME}" ]; then
  echo "Usage: $0 <dockerhub_username> [target_image_name] [target_image_tag]"
  exit 1
fi

TARGET_FULL_IMAGE_NAME="${DOCKERHUB_USERNAME}/${TARGET_IMAGE_NAME}:${TARGET_IMAGE_TAG}"

echo "==> Tag image ${SOURCE_IMAGE_NAME} -> ${TARGET_FULL_IMAGE_NAME}"
sudo docker tag "${SOURCE_IMAGE_NAME}" "${TARGET_FULL_IMAGE_NAME}"

echo "==> Login Docker Hub"
sudo docker login

echo "==> Push image to Docker Hub"
sudo docker push "${TARGET_FULL_IMAGE_NAME}"

echo "==> Done"
echo "Image pushed: ${TARGET_FULL_IMAGE_NAME}"