# Platform Targets

Platform target docs describe how StyxCD maps release intent onto a concrete runtime/platform.

Current targets represented here:

- Kubernetes: GKE/EKS stage architecture and parity model
- Terraform: ECS/Fargate app-set hashing strategy

Target docs should explain platform-specific implementation details while preserving the shared StyxCD concepts: intent, execution plan, target-scoped stages, application-scoped stages, validation, and callbacks.
