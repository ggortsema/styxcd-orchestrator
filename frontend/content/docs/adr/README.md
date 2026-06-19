# Architectural Decision Records

ADRs are the source of truth for StyxCD design decisions.

## Current decisions

- ADR-0001: Control plane and execution runtime separation
- ADR-001: YAML intent and execution plan contract
- ADR-002: Orchestrator plans, Jenkins executes
- ADR-003: Lifecycle → target → application planning
- ADR-004: GKE target/application stage scopes
- ADR-005: Merge Kubernetes Service creation into deploy application
- ADR-006: Separate deployment validation from service validation
- ADR-007: Use `APP_NAME`, not `APPHOST_NAME`
- ADR-008: Use `__final__` as final-stage marker
- ADR-009: Authentication is utility behavior, not a stage
- ADR-00X: Dynamic execution plan vs static pipeline
- ADR-00Y: Up-front planning vs stage-directed workflow execution
- ADR-010: Stage helper classes when they add value
- ADR-011: Future stage registry refactor
- ADR-012: Stable execution plan with future workflow DSL/productization
- ADR-013: Structured logging into Loki/Grafana
- ADR-014: Multiple cloud and infrastructure workflow capabilities
- ADR-015: Project-management workflow as future non-deployment capability

## Numbering note

`ADR-00X` and `ADR-00Y` are preserved because they already exist and capture important architectural decisions. Future cleanup can renumber them, but that should be done intentionally in a separate commit to avoid breaking references.
