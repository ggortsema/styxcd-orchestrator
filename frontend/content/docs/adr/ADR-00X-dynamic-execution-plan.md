# ADR-00X: Dynamic Execution Plan vs Static Pipeline

## Status

Accepted

## Context

Many CI/CD implementations are built as large static pipelines. Every stage is defined up front and executes during every pipeline run. Stages that are not applicable evaluate runtime conditions and either perform work or exit without taking action.

Example:

- Build Stage
- Terraform Stage
- ECS Deploy Stage
- EKS Deploy Stage
- GKE Deploy Stage
- DNS Stage
- Ingress Stage

Every stage exists in every execution regardless of whether it is required.

This approach is simple to implement initially but has several drawbacks:

- Execution logs contain large numbers of skipped stages.
- Pipeline graphs become noisy and difficult to understand.
- Stage implementations accumulate conditional logic.
- Execution telemetry does not clearly represent actual deployment intent.
- The pipeline structure is fixed and does not accurately represent the desired state being executed.

## Decision

StyxCD will generate a dynamic execution plan before Jenkins execution begins.

The orchestrator is responsible for:

- Parsing the YAML specification.
- Evaluating desired state and platform intent.
- Determining which stages are required.
- Constructing the execution plan.

Jenkins is responsible for:

- Executing the generated plan.
- Instantiating only the stages present in the execution plan.
- Performing the implementation work associated with each stage.

Stages should assume they are required if they are present in the execution plan.

Example:

If the YAML contains:

    ingress:
      enabled: true

the planner adds:

    GkeCreateIngress

to the execution plan.

If the YAML contains:

    ingress:
      enabled: false

the planner does not add the ingress stage at all.

The stage is not instantiated and never executes.

## Consequences

Benefits:

- Execution plans accurately represent deployment intent.
- Jenkins graphs remain concise and meaningful.
- Stage implementations remain focused on execution rather than decision making.
- Telemetry reflects actual work performed.
- New capabilities can be added by extending planning logic rather than increasing conditional complexity within stages.
- The execution plan becomes a first-class artifact that can be visualized, audited, and reasoned about.

Tradeoffs:

- More planning logic exists within the orchestrator.
- Stage inclusion decisions must be modeled explicitly.
- Execution planning becomes a critical platform capability.

## Example

Desired State:

    applications:
      - backend

    ingress:
      enabled: true

    dns:
      enabled: false

Generated Execution Plan:

    Initialize
    Build
    Deploy Application
    Validate Deployment
    Create Ingress
    Cleanup

DNS is not present because it is not required by the desired state.

This behavior is intentional and is considered a core StyxCD architectural principle.
