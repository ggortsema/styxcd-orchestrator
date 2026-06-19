# StyxCD Documentation

This is the cleaned documentation set for StyxCD.

StyxCD is a YAML-driven deployment orchestration platform. The orchestrator accepts release intent, generates an execution plan, persists execution state, and delegates runtime work to execution agents such as Jenkins.

## Start here

1. [Architecture Overview](architecture/overview.md)
2. [Execution Planning](architecture/execution-planning.md)
3. [Control Plane](architecture/control-plane.md)
4. [YAML Contract](specifications/yaml-contract.md)
5. [Kubernetes GKE/EKS Stage Architecture](platform-targets/kubernetes/gke-eks-stage-architecture.md)
6. [Observability](observability/observability-architecture.md)
7. [Architectural Decision Records](adr/README.md)

## Directory layout

```text
adr/                         Architectural decision records; source of truth for decisions.
architecture/                Current architecture and execution model.
specifications/              User/input contracts and infrastructure specifications.
platform-targets/            Platform-specific target behavior such as Kubernetes and Terraform.
observability/               Logging, telemetry, Loki/Grafana, and execution diagnostics.
kharon/                      Future RAG/AI knowledge assistant vision.
backlog/                     Backlog YAML files preserved as-is.
migration/                   Legacy planner code/reference material.
archive/                     Historical notes, handoffs, and superseded drafts.
assets/                      Diagrams and supporting assets.
```

## Cleanup policy

The cleaned structure keeps ADRs and consolidated docs as the authoritative documentation path. Older session handoffs, duplicate architecture notes, and superseded drafts are preserved in `archive/` for traceability but should not be treated as current design guidance unless explicitly referenced by an ADR.
