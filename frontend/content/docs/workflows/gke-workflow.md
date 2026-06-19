# GKE Workflow

The GKE workflow creates Kubernetes resources from a planned execution rather than asking every Jenkins stage to decide what comes next.

## Planned stages

- Create namespace
- Create deployment
- Create service
- Configure ingress
- Validate deployment

## Principle

The orchestrator plans the workflow up front. Jenkins executes the plan and reports telemetry back.
