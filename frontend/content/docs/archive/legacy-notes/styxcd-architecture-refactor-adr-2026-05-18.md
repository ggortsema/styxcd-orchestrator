
# StyxCD Architecture Decision Record
# Execution Plan Refactor and Stage Planner Architecture

Date: 2026-05-18

---

# Context

Original architecture relied heavily on Jenkins shared-library orchestration logic.

Problems:
- Jenkins stages parsed YAML directly
- orchestration logic spread across Jenkins
- workflow semantics tightly coupled to runtime execution
- difficult to unit test planners
- difficult to support future non-Jenkins execution engines
- stage expansion logic duplicated

Goal:
Move orchestration semantics into orchestrator while keeping Jenkins as execution runtime.

---

# Final Architecture

## Orchestrator Responsibilities

The orchestrator now owns:

- YAML semantic interpretation
- workflow ordering
- stage planner selection
- execution-plan generation
- stage expansion
- runtime parameter resolution

---

## Jenkins Responsibilities

Jenkins now owns:

- execution runtime
- workspace lifecycle
- stash/unstash
- tool execution
- metrics/logging emission
- report publishing

Jenkins stages no longer interpret orchestration semantics.

---

# Workflow Planner Layer

Implemented in Groovy.

Responsibilities:
- determine stage ordering
- evaluate high-level workflow conditions
- invoke stage planners

Example:
```text
initialize
gradle_build
terraform_apply
cleanup
```

Workflow planner no longer expands concrete execution instances.

---

# Stage Planner Layer

Implemented in Java.

Responsibilities:
- expand stage families into concrete executable stages
- resolve runtime parameters

Example:
```text
gradle_build
```

expands into:

```text
cloud_workflow:gradle_build:styxcd-jenkins
cloud_workflow:gradle_build:billing-service
```

---

# Execution Key Structure

Current execution key format:

```text
workflow:stage_type:target
```

Examples:

```text
cloud_workflow:initialize
cloud_workflow:gradle_build:styxcd-jenkins
cloud_workflow:terraform_apply:johnny-sandbox
```

Benefits:
- namespace safety
- workflow isolation
- observability clarity
- retry targeting
- future DAG support

---

# StageWrapper Normalization

StageWrapper resolves concrete execution keys to stable implementation keys.

Example:

```text
cloud_workflow:gradle_build:styxcd-jenkins
```

normalizes to:

```text
cloud_workflow:gradle_build
```

for StageMap lookup.

---

# Current Persistence Model

Execution plan currently persisted as JSON.

Reason:
PostgreSQL jsonb reorders object keys.

Temporary solution:
- use json instead of jsonb
- preserve insertion order during rapid iteration phase

Long-term target:
Execution plans become ordered lists rather than map structures.

---

# Long-Term Direction

Future execution-plan model should support:
- DAG execution
- parallel stages
- retries
- insert-before/after
- visualization
- execution replay
- AI-assisted planning
- policy mutation layers

Current refactor lays the foundation for:
- non-Jenkins runners
- execution replay
- orchestration auditability
- semantic workflow engines
- future Drools-driven planning decisions

