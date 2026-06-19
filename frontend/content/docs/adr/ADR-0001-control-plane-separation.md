# ADR-0001: StyxCD Control Plane and Execution Runtime Separation

## Status
Accepted

## Context

StyxCD is evolving from a Jenkins-centric orchestration engine into a dedicated orchestration and control plane platform.

Historically:
- Jenkins parsed the submitted yml
- Jenkins generated the execution map
- Jenkins executed the stages

This tightly coupled:
- orchestration logic
- persistence
- validation
- execution runtime

The new architecture separates these concerns.

## Decision

The orchestrator (styxcd-orchestrator) becomes the authoritative control plane responsible for:

- accepting raw yml
- parsing yml into flexible map structures
- generating execution plans
- persisting execution state
- persisting raw submitted yml
- exposing execution retrieval APIs
- eventually handling validation, rules, events, metrics, and dashboards

Jenkins becomes an execution runtime only.

### New Flow

```text
POST /executions
    ↓
parse yml
    ↓
generate execution plan
    ↓
persist execution + plan
    ↓
return executionId

Jenkins job
    ↓
receives executionId
    ↓
GET /executions/{id}/plan
    ↓
execute stages
```

## Execution Plan Contract

Execution plans are persisted as ordered maps keyed by unique stage names.

Example:

```json
{
  "CloudWorkflowInitialize": {...},
  "GradleBuild@first-app": {...},
  "CloudWorkflowCleanup@final": {...}
}
```

### Important Semantics

- keys must remain unique
- @final indicates cleanup/final execution semantics
- stage params remain compatible with existing Jenkins execution engine
- execution plans contain the parsed YML structure for compatibility during migration

## YML Parsing Strategy

The platform intentionally avoids rigid DTO mapping of the entire yml spec.

Instead:

```text
raw yml
  → flexible Map structure
  → workflow-specific mappers
  → Drools facts
  → rules/validation
  → execution plan
```

Reasons:
- avoids brittle DTO explosion
- supports multiple workflow types
- allows workflow-specific validation
- keeps orchestration flexible

## Persistence Strategy

Execution persistence currently stores:

- id
- workflow
- status
- raw_yml
- execution_plan (jsonb)
- created_at

The raw yml is preserved as source-of-truth intent.

## Packaging Strategy

The project uses package-by-feature/domain instead of layered packaging.

Example:

```text
execution/
  Execution.java
  ExecutionRepository.java
  ExecutionService.java
  ExecutionController.java
```

This aligns with:
- DDD-inspired modularity
- vertical slice architecture
- orchestration domain cohesion

## Future Work

Planned future modules:

- styxcd-planner
- styxcd-core
- styxcd-jenkins
- customer-specific workflow modules

Planned future features:

- Drools validation
- workflow mappers
- RabbitMQ events
- Grafana/Loki observability
- dashboard UI
- plugin workflow architecture
- Flyway migrations
