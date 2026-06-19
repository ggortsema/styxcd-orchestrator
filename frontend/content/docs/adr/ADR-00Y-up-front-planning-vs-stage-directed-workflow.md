# ADR-00Y: Up-Front Planning vs Stage-Directed Workflow Execution

## Status

Accepted

## Context

A common pipeline design pattern is to let each stage decide what should happen next.

In that model, a pipeline stage may:

- Inspect current state.
- Decide whether work is needed.
- Decide which downstream stage should run.
- Trigger or skip the next stage.
- Contain workflow branching logic inside the stage implementation.

This can work for small systems, but it creates problems as the platform grows.

When stages decide what comes next, the workflow becomes distributed across many implementation classes. Understanding an execution requires reading the behavior of multiple stages instead of looking at a single execution plan.

Over time this creates several risks:

- Workflow logic becomes hidden inside stage implementation code.
- The same decision may be repeated in multiple stages.
- It becomes difficult to explain why a stage ran.
- It becomes difficult to visualize the full workflow before execution.
- Stage code becomes harder to test because it mixes planning and execution.
- Failures become harder to reason about because the next action may depend on runtime stage behavior.
- The platform can drift toward a chain of imperative scripts rather than a declarative desired-state engine.

StyxCD is intended to be a YAML-driven orchestration platform. The YAML expresses desired state and deployment intent. The orchestrator should evaluate that intent up front and produce a deterministic execution plan before Jenkins begins executing stages.

## Decision

StyxCD will perform workflow planning up front.

The orchestrator is responsible for determining the required stages before execution begins.

Stages should not decide what the next stage should be.

Instead:

- The orchestrator parses the YAML.
- The orchestrator evaluates desired state, target platform, application configuration, ingress, DNS, secrets, and other deployment intent.
- The orchestrator produces a complete execution plan.
- Jenkins receives and executes that plan.
- Each stage performs the implementation work assigned to it.
- Stage output may provide telemetry, status, and discovered values, but it should not redefine the workflow.

In other words:

    Plan once.
    Execute many.

The execution plan is the source of truth for what will run.

## Example

If a target platform contains:

    applications:
      - name: johnny-johnny-backend
      - name: johnny-johnny-ui

    ingress:
      enabled: true

    dns:
      enabled: true

The orchestrator may produce an execution plan such as:

    Initialize
    Create Namespace
    Deploy johnny-johnny-backend
    Validate johnny-johnny-backend
    Deploy johnny-johnny-ui
    Validate johnny-johnny-ui
    Create Ingress
    Configure DNS
    Validate Service
    Cleanup

The deploy stage for the backend should not decide whether the UI should deploy next.

The validate stage should not decide whether ingress should be created.

The ingress stage should not decide whether DNS should run.

Those decisions belong to the planner.

## Rationale

Up-front planning gives StyxCD a cleaner separation of responsibilities.

### Orchestrator Responsibilities

The orchestrator owns planning and decision making.

It determines:

- Which stages are required.
- Which applications are in scope.
- Which target platform is being deployed to.
- Whether ingress is required.
- Whether DNS is required.
- Whether secrets are required.
- Which execution order should be used.
- What parameters each stage needs.

### Jenkins Responsibilities

Jenkins owns execution.

It should:

- Receive the execution plan.
- Instantiate only the stages present in the plan.
- Run those stages in order.
- Report status and telemetry back to the orchestrator.

Jenkins should not be responsible for discovering the workflow dynamically while it runs.

### Stage Responsibilities

Stages own implementation details.

A stage should:

- Assume it is present because the orchestrator determined it is required.
- Execute a specific piece of work.
- Emit logs, telemetry, and outputs.
- Fail clearly when its assigned work cannot be completed.

A stage should not:

- Decide what downstream stage should run.
- Perform global workflow planning.
- Reinterpret the full YAML to make workflow decisions.
- Act as a hidden orchestrator.

## Benefits

This design provides several benefits.

### Deterministic Execution

The full workflow is known before execution starts.

This makes the pipeline easier to reason about, audit, visualize, and debug.

### Better Observability

The execution plan accurately represents actual deployment intent.

Telemetry can be tied directly to the planned stages rather than to skipped or conditional branches buried inside stage code.

### Cleaner Stage Implementations

Stages stay focused on implementation.

For example:

- `GkeDeployApplication` deploys an application.
- `GkeCreateIngress` creates ingress.
- `GkeConfigureDns` configures DNS.

They do not need to contain broad workflow orchestration logic.

### Easier Testing

Planning can be tested independently from execution.

Examples:

- Given YAML with ingress enabled, assert that `GkeCreateIngress` appears in the plan.
- Given YAML with DNS disabled, assert that `GkeConfigureDns` does not appear in the plan.
- Given two target applications, assert that deployment and validation stages are generated for both.

Stage tests can focus on implementation behavior rather than workflow branching.

### Stronger Platform Story

This design allows StyxCD to be described as a dynamic execution platform rather than a static Jenkins pipeline.

StyxCD does not run every possible stage and let unused stages no-op.

Instead, StyxCD plans the required workflow up front and executes only the stages needed to converge the requested desired state.

## Consequences

### Positive Consequences

- The execution plan becomes a first-class artifact.
- The orchestrator becomes the clear owner of workflow decisions.
- Jenkins remains a generic execution engine.
- Stages remain smaller and easier to reason about.
- The UI can eventually display the planned workflow before execution.
- Future policy engines such as Drools can populate planning decisions without changing stage implementations.

### Tradeoffs

- The orchestrator must model workflow decisions explicitly.
- The planner becomes a critical part of the platform.
- Stage parameters must be complete enough for stages to execute without re-planning.
- Some discovered runtime values still need to be passed between stages through stage outputs or execution context.

## Future Direction

Long term, StyxCD may introduce a formal `PlanningDecision` model.

The planning flow may evolve toward:

    YAML
      -> Validation
      -> PlanningDecision objects
      -> Execution Plan
      -> Jenkins execution

A rules engine such as Drools may eventually populate or influence `PlanningDecision` objects.

Even if that happens, the architectural boundary remains the same:

    Planning belongs up front in the orchestrator.
    Execution belongs in Jenkins stages.

## Summary

StyxCD should avoid stage-directed workflow execution.

Stages should not decide what comes next.

The orchestrator should produce a deterministic execution plan up front, and Jenkins should execute that plan exactly.

This keeps StyxCD aligned with its core model:

    Declarative intent in YAML.
    Deterministic planning in the orchestrator.
    Focused execution in Jenkins.
