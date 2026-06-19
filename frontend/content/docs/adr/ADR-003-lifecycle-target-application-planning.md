# ADR-003: Model cloud workflow with lifecycle -> target -> application planning

## Status
Draft / Accepted

## Context
The GKE skeleton proved that the release YAML needs to be interpreted across lifecycle, target, and application dimensions.

The current lifecycle order is fixed:

- sandbox
- dev
- qa
- stage
- prod

## Decision
For cloud workflows, plan stages using the general structure:

```text
for lifecycle
  for target
    generate target-scoped stages
    for application
      generate application-scoped stages
    generate target-level validation/exposure stages
```

## Consequences
This prevents target-scoped work such as namespace creation, ingress, DNS, or environment-level service validation from running once per application.

It also makes lifecycle ordering explicit and predictable.
