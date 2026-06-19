# StyxCD Lessons Learned and Design Decisions

## Purpose

This document captures important lessons learned while building StyxCD and explains why certain design decisions were made.

## Build From Git, Not From Local Workstations

A deployment was tested successfully from a local deployment script, but the code had not been pushed to Git. This resulted in version drift between what was running and what Git reported.

**Lesson:** Build from Git. Git is the source of truth.

## Execution Plans Instead Of Hardcoded Pipelines

Users express intent in YAML. The orchestrator translates intent into an execution plan.

Example:

```yaml
gke: true
```

becomes:

```text
Initialize
Gradle Build
GKE Deploy
Cleanup
```

## Workflow DSL Is A Permanent Concept

There will always be a mapping between:

User Intent → Execution Plan

The implementation may change, but the concept remains.

## Stage Wrappers

Cross-cutting concerns such as logging, metrics, callbacks, and error handling belong in wrappers rather than being duplicated in every stage.

## Structured Logging

Structured JSON logs are emitted to Loki with execution and stage correlation information.

## Jenkins Library As Software

The Jenkins executor is a Gradle project with source code, tests, versioning, and releases. It is treated as a software product rather than a collection of scripts.

## Mock Jenkins Runtime For Testing

A mock Steps implementation allows Spock tests to execute pipeline logic outside Jenkins.

## Versioning Strategy

Early releases may evolve in lockstep. Long-term compatibility should be based on execution-plan contracts.

## Dogfooding

The best way to validate a deployment platform is to deploy itself with the platform.

## Templates And Effective YAML

Future direction:

1. Select template
2. Supply overrides
3. View effective YAML
4. Execute workflow

Inspired by Maven's Effective POM.

## Stable And Development Environments

Maintain separate deployments:

- Stable
- Development

This allows experimentation without impacting demonstrations.

## Platform Engineering Over Pipeline Engineering

Prefer:

- Reusability
- Testability
- Observability
- Versioning
- Extensibility
