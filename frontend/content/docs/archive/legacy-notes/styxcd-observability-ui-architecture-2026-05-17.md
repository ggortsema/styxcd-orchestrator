# StyxCD Observability and UI Architecture Notes - 2026-05-17

## Summary

This session established the first working distributed observability model for StyxCD across the orchestrator, Jenkins shared library, Loki, and Grafana.

The major architectural direction is now:

```text
Orchestrator
  -> owns execution lifecycle, planning, persistence, dispatch, callbacks

Jenkins
  -> owns runtime stage execution
  -> emits stage lifecycle telemetry

Loki
  -> stores structured operational event history

Grafana
  -> provides query/debug/dashboard visibility

Postgres
  -> stores current execution state and indexed execution metadata
```

## Major Decisions

### 1. Loki is the operational event/log store

Loki is used for detailed execution and stage event history.

It stores:

- orchestrator lifecycle events
- Jenkins stage events
- structured JSON envelopes
- error details
- stage metadata
- execution correlation data

Loki is not intended to be the system of record for current execution status. That remains in Postgres.

### 2. Postgres stores current execution state, not full event history yet

The `executions` table remains the fast state source for the dashboard.

Current intended responsibilities:

```text
Postgres:
- execution id
- workflow
- current status
- current lifecycle event
- raw YML
- execution plan
- created timestamp
```

Full event history is intentionally not duplicated in Postgres right now.

Future possible DB shape:

```text
executions
execution_stages
execution_stage_events
```

but this is deferred until dashboard/timeline requirements justify it.

### 3. Execution events and stage events use related but separate envelopes

The design uses:

```text
Execution envelope
  -> main orchestration lifecycle

Stage envelope
  -> individual runtime stage lifecycle

metadata/details
  -> stage-specific context
```

Both are correlated with:

```text
executionId
correlationId
```

### 4. `executionId` stays in the JSON body, not as a Loki label

For now, `executionId` is searchable in the log body:

```logql
{job=~"styxcd-.*"} |= "<executionId>"
```

It is intentionally not a Loki label yet because it is high-cardinality.

Low-cardinality labels are preferred:

```text
job
source
eventType
status
workflow
environment
platform
applicationName
```

This avoids stream/index explosion while still allowing execution-level search.

### 5. Cardinality guidance

In Loki, label cardinality means the number of unique label combinations.

Good labels repeat frequently:

```text
workflow="cloud_workflow"
status="SUCCESS"
source="jenkins"
```

High-cardinality labels create new streams constantly:

```text
executionId="unique-per-run"
buildId="unique-per-build"
containerId="unique-per-container"
```

For StyxCD, execution correlation is extremely important, so this decision can be revisited later, but the current safer design is:

```text
executionId in JSON body
not Loki label
```

### 6. StageWrapper owns stage telemetry

Jenkins stage lifecycle telemetry was moved into `StageWrapper`.

`StageWrapper` emits:

```text
STAGE_STARTED
STAGE_SUCCEEDED
STAGE_FAILED
```

This avoids duplicating telemetry logic in every initialize/cleanup/stage implementation.

### 7. MetricsUtil became the Loki/event utility

Old Splunk-specific logic was removed from active usage.

`MetricsUtil` now:

- builds structured stage event envelopes
- formats Loki push payloads
- sends events directly to Loki
- fails soft if Loki is unavailable

Current flow:

```text
Jenkins -> Loki direct push API
```

Alloy is installed but not used for this path yet.

Future flow may become:

```text
Jenkins -> Alloy -> Loki
```

when enrichment, routing, buffering, or multi-destination needs justify it.

### 8. Orchestrator emits execution lifecycle events

The orchestrator now emits lifecycle events to Loki via:

```text
ExecutionEvent
ExecutionEventError
ExecutionEventFactory
LokiEventPublisher
ExecutionService integration
```

Events include:

```text
EXECUTION_CREATED
EXECUTION_PLAN_CREATED
EXECUTION_DISPATCHED
EXECUTION_CALLBACK_RECEIVED
EXECUTION_SUCCEEDED
EXECUTION_FAILED
EXECUTION_STATUS_UPDATED
```

### 9. `currentLifecycleEvent` added to DB state

The `Execution` entity gained:

```java
@Column(name = "current_lifecycle_event")
private String currentLifecycleEvent;
```

`ExecutionService.publishEvent(...)` updates this field whenever a lifecycle event is published.

This gives the dashboard more detail without adding full event-history tables.

### 10. Grafana/Loki persistence verified

The observability EC2 box is currently running:

```text
Grafana
Loki
Alloy
```

Verified persistent paths:

```text
Grafana: /var/lib/grafana
Alloy:   /var/lib/alloy/data
Loki:    /var/lib/loki
```

Loki config uses:

```yaml
common:
  path_prefix: /var/lib/loki
  storage:
    filesystem:
      chunks_directory: /var/lib/loki/chunks
      rules_directory: /var/lib/loki/rules

storage_config:
  tsdb_shipper:
    active_index_directory: /var/lib/loki/index
    cache_location: /var/lib/loki/cache
```

A smoke test confirmed logs survive:

```text
Loki service restart
EC2 stop/start
Grafana Explore query
```

### 11. Useful Grafana queries

All StyxCD logs for one execution:

```logql
{job=~"styxcd-.*"} |= "<executionId>"
```

Jenkins stage events for one execution:

```logql
{job="styxcd-jenkins"} |= "<executionId>"
```

Orchestrator lifecycle events for one execution:

```logql
{job="styxcd-orchestrator"} |= "<executionId>"
```

All Jenkins events:

```logql
{job="styxcd-jenkins"}
```

All orchestrator events:

```logql
{job="styxcd-orchestrator"}
```

By event type:

```logql
{eventType="STAGE_STARTED"}
```

or:

```logql
{eventType="EXECUTION_SUCCEEDED"}
```

### 12. Dashboard layout direction

The frontend evolved toward a platform shell:

```text
Left navigation
  Dashboard
  Operations
  Docs

Main workspace
  page-specific content
```

`/operations` now owns the active execution workspace.

This avoids collision with the backend API route:

```text
/executions
```

which is already used by the orchestrator REST API.

### 13. Frontend route/controller decision

Because the frontend is a static Next.js export served by Spring Boot, Spring needs route forwarding for exported pages.

A Spring controller maps frontend routes like:

```text
/dashboard
/docs
/operations
```

to their static exported HTML files.

The `/executions` frontend route was avoided because it collided with the backend API route.

### 14. Long-term UI strategy

Primary navigation should be a persistent left nav, not top-level tabs or hamburger-only navigation.

Tabs are appropriate inside a section.

Example:

```text
Operations
  Current
  History
  Failures
```

Future sections:

```text
Dashboard
Operations
Docs
Specifications
Observability
Settings
```

### 15. Tailwind/shadcn decision

Do not introduce Tailwind/shadcn immediately.

Planned sequence:

```text
1. Finish observability + dashboard lifecycle event display
2. Build full ECS/Fargate lifecycle pipeline
3. Add Prometheus monitoring for deployed stack and StyxCD infra
4. Migrate UI foundation to Tailwind + shadcn/ui
5. Add richer docs/platform UX
```

Reason: first prove the platform workflow and metrics model, then use Tailwind/shadcn to scale UI components cleanly.

### 16. Prometheus plan

Prometheus should be added after the ECS/Fargate lifecycle path works.

Prometheus should monitor:

```text
Deployed application stack
Orchestrator JVM
Jenkins host/runtime basics
Observability box/node basics
Later: RabbitMQ/Postgres
```

Loki remains logs/events. Prometheus becomes metrics/time-series.

### 17. Long-term retention/search model

Suggested long-term split:

```text
Loki
  -> recent operational logs/events

Postgres
  -> indexed current state and execution summaries

S3/archive
  -> cheap long-term retention

Elastic/OpenSearch/Solr
  -> optional deep historical search later
```

Hadoop is likely unnecessary unless true big-data analytics requirements appear.

### 18. Future local development/testing plan

Future work should include:

```text
Local Docker Compose stack:
- orchestrator
- Postgres
- RabbitMQ
- Jenkins
- Loki
- Grafana
- Alloy
- Prometheus later
```

Also:

```text
Lightweight local profile:
- H2/in-memory DB
- mock execution runner
- fake callbacks
- local observability toggle
```

This supports:

```text
unit tests
behavior tests
integration tests
callback testing
local UI iteration
```

### 19. Deferred failure handling issue

Current known failure case:

```text
Jenkins runtime error
-> cleanup/final callback may not execute
-> orchestrator remains STARTED
-> dashboard never updates to FAILED
```

Future fix may include:

```text
outer pipeline-level try/finally
orchestrator timeout/reconciliation
heartbeat/last-seen timestamps
fallback FAILED callback path
```

## Next Recommended Work

### Immediate next session options

Option A - Finish logging polish:

```text
Add links metadata to Jenkins and orchestrator envelopes:
- grafanaExploreQuery
- executionApiUrl
- jenkinsBuildUrl
```

Option B - Begin ECS/Fargate happy path workflow:

```text
Cloud workflow stages for:
- build app images
- push ECR
- create/validate ECS Fargate cluster
- deploy task/service
- optional teardown
```

Recommended order:

```text
1. Add small links metadata polish if still fresh
2. Then build ECS/Fargate happy path lifecycle
```
