# ADR-011: Refactor future stage registration toward Spring bean registry

## Status
Backlog / Future cleanup

## Context
CloudWorkflow constructor injection is becoming large as more stage helper classes are added.

## Decision
For now, keep explicit constructor injection while building the GKE skeleton.

Later, consider injecting a list of stage planner/helper beans and building a registry:

```text
List<WorkflowStagePlanner> -> Map<String, WorkflowStagePlanner>
```

## Consequences
This future refactor can reduce constructor bloat and make stage registration more scalable. It should not interrupt the current GKE skeleton implementation.
