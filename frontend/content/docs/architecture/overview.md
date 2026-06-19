# StyxCD Architecture Overview

## Purpose

StyxCD is a control-plane-first CI/CD orchestration system.

Users describe release intent in YAML. The StyxCD orchestrator interprets that intent, creates an execution plan, stores state, and exposes the plan to a runtime executor such as Jenkins.

The core architectural rule is:

```text
YAML intent -> Orchestrator planning -> Execution plan -> Runtime execution -> Callbacks/telemetry
```

## Core components

### StyxCD Orchestrator

The orchestrator owns planning and state:

- accept submitted YAML
- parse release/application/environment intent
- validate syntax and eventually semantic rules
- generate an execution plan
- persist raw YAML, execution plan, and execution status
- expose plan retrieval APIs
- receive callbacks from execution runtimes

### Jenkins execution runtime

Jenkins executes already-planned stages. It should not infer high-level release topology from the YAML.

Jenkins owns:

- checking out/building source code
- running Maven/Gradle/Docker/Terraform/kubectl/gcloud/aws commands
- dispatching stage implementations by stage type
- reporting `STARTED`, `SUCCESS`, and `FAILED` callbacks
- emitting correlated structured logs

### Execution plan contract

The execution plan is the stable boundary between orchestrator and runtime.

This allows future planner implementations, rule engines, workflow DSLs, or alternate execution runtimes to generate or consume the same contract without changing the overall system model.

## Current reference flow

```text
POST /executions
  -> orchestrator parses YAML
  -> orchestrator creates execution plan
  -> orchestrator persists execution state
  -> Jenkins receives executionId
  -> Jenkins calls GET /executions/{id}/plan
  -> Jenkins executes stages
  -> Jenkins sends callbacks
  -> orchestrator updates execution status
  -> logs/telemetry flow to Loki/Grafana
```

## Design principles

- The orchestrator plans; Jenkins executes.
- Stages should not decide the global workflow.
- Planning happens up front so the full execution graph can be inspected before runtime.
- Runtime stages can perform conditional implementation details, but they should not rewrite the release plan.
- ADRs are authoritative for decisions.
- Historical notes belong in `archive/`, not the main reading path.
