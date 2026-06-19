# StyxCD ADR Pack - Working Draft

Date: 2026-06-13

This is a rough working set of architectural decision records captured from recent StyxCD design sessions. These are intentionally draft-quality notes for later cleanup and consolidation.

## Included ADRs

- ADR-001: Use YAML intent as input and execution plan map as runtime contract
- ADR-002: Keep Jenkins execution-focused and move YAML parsing/planning into orchestrator
- ADR-003: Model cloud workflow with lifecycle -> target -> application planning
- ADR-004: Split GKE stages by target scope and application scope
- ADR-005: Merge Kubernetes Service creation into GKE application deployment
- ADR-006: Keep deployment validation and service validation separate
- ADR-007: Rename APPHOST_NAME to APP_NAME in the common contract
- ADR-008: Use __final__ as reserved final-stage marker
- ADR-009: Keep authentication as utility behavior, not a workflow stage
- ADR-010: Allow stage helper classes when they encapsulate stage contract logic
- ADR-011: Refactor future stage registration toward Spring bean registry
- ADR-012: Keep execution plan stable while allowing future workflow DSL/productization
- ADR-013: Use environment-agnostic structured logging into Loki/Grafana
- ADR-014: Treat Terraform/ECS/GKE deployment paths as separate workflow capabilities
- ADR-015: Capture project-management workflow as a future non-deployment capability

