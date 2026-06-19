# ADR-005: Merge Kubernetes Service creation into GKE application deployment

## Status
Draft / Accepted

## Context
The initial GKE skeleton included both GkeDeployApplication and GkeCreateService. This created naming ambiguity and risked implying that an app could be deployed without its Kubernetes Service.

In Kubernetes, a Service object is normally part of the application manifest set, along with Deployment and related resources.

## Decision
Merge Kubernetes Service creation into GkeDeployApplication.

GkeDeployApplication may eventually apply:

- Deployment
- Service
- ConfigMap/Secret references
- other app-level manifests

## Consequences
The app-scoped GKE flow becomes simpler:

```text
GkeDeployApplication
GkeValidateDeployment
```

This avoids confusing `GkeCreateService` with `GkeValidateService`.
