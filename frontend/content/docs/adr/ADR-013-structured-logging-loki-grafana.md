# ADR-013: Use environment-agnostic structured logging into Loki/Grafana

## Status
Backlog / Accepted direction

## Context
StyxCD has been moving toward structured logs and observability through Loki and Grafana. Future deployments may run across EC2, ECS, or GKE.

## Decision
Applications and pipeline/stage execution should emit consistent structured JSON logs. Collection should be environment-specific, but the log model should remain consistent.

Potential collectors:

- EC2: Alloy agent
- ECS: CloudWatch / FireLens integration
- GKE: Alloy DaemonSet

Important fields include:

- executionId
- workflow
- stageType/stageName
- lifecycle
- target
- application
- status
- errors[]
- Jenkins metadata

## Consequences
Exceptions and execution-plan errors should flow automatically into Loki/Grafana regardless of runtime.
