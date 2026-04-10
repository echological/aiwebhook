#!/usr/bin/env bash
set -euo

IMAGE_NAME="tel-webhook:latest"
CONTAINER_NAME="tel-webhook_sit"
MONGODB_URI="mongodb://192.168.59.128:27017"
MONGODB_DB="tel-webhook"
OPENAI_API_KEY=""
OPENAI_BASE_URL="https://api.openai.com/v1"
TELEGRAM_BOT_TOKEN=""
TELEGRAM_BOT_BASE_URL="https://api.telegram.org"
TELEGRAM_BOT_API_KEY=""

echo "==> Build image ${IMAGE_NAME}"
sudo docker build -f Dockerfile.uber -t "${IMAGE_NAME}" .

echo "==> Remove old container if exists"
if sudo docker ps -a --format '{{.Names}}' | grep -xq "${CONTAINER_NAME}"; then
  sudo docker rm -f "${CONTAINER_NAME}"
fi

echo "==> Run container ${CONTAINER_NAME}"
sudo docker run -d --name "${CONTAINER_NAME}" \
  -p 8080:8080 \
  -e MONGODB_URI="${MONGODB_URI}" \
  -e MONGODB_DB="${MONGODB_DB}" \
  -e OPENAI_API_KEY="${OPENAI_API_KEY}" \
  -e OPENAI_BASE_URL="${OPENAI_BASE_URL}" \
  -e TELEGRAM_BOT_TOKEN="${TELEGRAM_BOT_TOKEN}" \
  -e TELEGRAM_BOT_BASE_URL="${TELEGRAM_BOT_BASE_URL}" \
  -e TELEGRAM_BOT_API_KEY="${TELEGRAM_BOT_API_KEY}" \
  "${IMAGE_NAME}"

echo "==> Done. Logs:"
sudo docker ps | grep "${CONTAINER_NAME}"