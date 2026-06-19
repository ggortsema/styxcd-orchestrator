# ADR-006: Keep deployment validation and service validation separate

## Status
Draft / Accepted

## Context
There was discussion about whether GkeValidateService should be merged into or renamed to deployment validation.

The conclusion was that deployment health and externally reachable service health are different checks.

## Decision
Keep two validation concepts:

- GkeValidateDeployment or GkeValidateRollout: validates internal Kubernetes deployment health.
- GkeValidateService: validates externally reachable application/service health.

GkeValidateDeployment/GkeValidateRollout should check things like:

- Deployment exists
- pods are ready
- rollout status succeeds

GkeValidateService should check things like:

- ingress is reachable
- DNS resolves
- HTTP health endpoint succeeds
- expected status/body is returned

## Consequences
This produces clearer failure diagnostics.

Example:

```text
GkeValidateDeployment = SUCCESS
GkeValidateService = FAILED
```

This means Kubernetes deployed the workload, but users may not be able to reach it.
