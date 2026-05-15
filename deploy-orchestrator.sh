#!/usr/bin/env bash
set -euo pipefail

EC2_HOST="ec2-user@orchestrator.styxcd.com"
JAR="target/orchestrator-0.0.1-SNAPSHOT.jar"

echo "Building orchestrator..."
mvn clean package -DskipTests

echo "Uploading jar..."
scp "$JAR" "$EC2_HOST:/tmp/orchestrator.jar"

echo "Deploying and restarting service..."
ssh "$EC2_HOST" '
  set -e

  sudo mv /tmp/orchestrator.jar /opt/orchestrator/releases/orchestrator-0.0.1-SNAPSHOT.jar

  sudo ln -sfn \
    /opt/orchestrator/releases/orchestrator-0.0.1-SNAPSHOT.jar \
    /opt/orchestrator/current.jar

  sudo chown -R ec2-user:ec2-user /opt/orchestrator

  sudo systemctl restart orchestrator

  echo "===== SERVICE STATUS ====="
  sudo systemctl status orchestrator --no-pager -l

  echo "===== LAST 50 LOG LINES ====="
  sudo journalctl -u orchestrator -n 50 --no-pager
'

echo "Deployment complete."
