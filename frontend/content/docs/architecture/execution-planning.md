# StyxCD Execution Planning

## Summary

Execution planning is the central StyxCD abstraction.

The orchestrator receives declarative YAML and produces a runtime-ready stage plan. The plan should contain enough derived data that Jenkins can execute without understanding the full YAML topology.

## Planning dimensions

Cloud workflows are planned across three dimensions:

```text
lifecycle -> target -> application
```

Example lifecycle order:

```text
sandbox -> dev -> qa -> stage -> prod
```

For each lifecycle and target, the orchestrator creates target-scoped stages and application-scoped stages.

## Target-scoped stages

Target-scoped stages run once per environment target.

Examples:

- create namespace
- create ingress
- configure DNS
- validate externally reachable service

## Application-scoped stages

Application-scoped stages run once per application per target.

Examples:

- build application artifact/image
- deploy application manifests
- validate deployment rollout

## Dynamic execution plan

A dynamic execution plan means the orchestrator only includes stages that are required by the YAML intent and target platform.

This is different from a static pipeline where every possible stage exists and each stage decides whether to no-op.

Preferred model:

```text
The orchestrator decides whether a stage belongs in the plan.
The stage executes the work it was given.
```

## Why up-front planning matters

Up-front planning makes StyxCD more inspectable and product-like:

- users can see what will happen before it happens
- UI can show planned stages
- audit logs can capture intent and planned execution
- future validation/rules can reject bad plans before runtime
- Jenkins remains a runtime agent rather than the brain of the system

## Related ADRs

- `adr/ADR-001-yaml-intent-execution-plan-contract.md`
- `adr/ADR-002-orchestrator-plans-jenkins-executes.md`
- `adr/ADR-003-lifecycle-target-application-planning.md`
- `adr/ADR-00X-dynamic-execution-plan.md`
- `adr/ADR-00Y-up-front-planning-vs-stage-directed-workflow.md`
