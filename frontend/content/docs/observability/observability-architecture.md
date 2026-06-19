# StyxCD Observability Architecture

## Summary

StyxCD observability is based on correlated structured events across the orchestrator, Jenkins/runtime stages, Loki, Grafana, and Postgres.

## System of record split

### Postgres

Postgres stores current execution state and indexed metadata:

- execution id
- workflow
- status
- lifecycle event
- submitted YAML
- generated execution plan
- timestamps

### Loki

Loki stores detailed operational event history:

- orchestrator lifecycle events
- Jenkins stage events
- structured JSON log envelopes
- errors and stack traces
- stage-level metadata
- execution and stage correlation identifiers

### Grafana

Grafana provides dashboards and investigative views over Loki and other telemetry sources.

## Correlation model

All logs and callbacks should include enough context to reconstruct a run:

- `executionId`
- `stageName` or stage key
- workflow name
- application name when app-scoped
- environment/lifecycle
- target/platform
- status/event type

## Design direction

The logging architecture should remain environment-agnostic.

A future deployment might run on EC2, ECS, GKE, or EKS. Collection mechanics can vary by runtime, but application and stage logs should use a consistent structured JSON shape so Kharon/RAG and Grafana can reason over executions consistently.

## Related ADR

See `adr/ADR-013-structured-logging-loki-grafana.md`.
