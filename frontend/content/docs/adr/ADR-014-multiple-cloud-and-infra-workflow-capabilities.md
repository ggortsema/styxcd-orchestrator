# ADR-014: Treat Terraform/ECS/GKE deployment paths as separate workflow capabilities

## Status
Draft / Accepted direction

## Context
StyxCD is evolving from a single cloud deployment path into multiple deployment and infrastructure capabilities:

- ECS Fargate
- EKS/GKE
- Terraform apply
- infrastructure setup/teardown

## Decision
Model these as workflow/stage capabilities that can be combined through the orchestrator execution plan rather than as one monolithic deployment process.

Terraform apply remains a stage/workflow capability. GKE deployment remains a platform-specific application deployment capability.

## Consequences
This keeps the architecture open to multiple platforms while preserving a consistent execution model.
