# ADR-004: Split GKE stages by target scope and application scope

## Status
Draft / Accepted

## Context
During the GKE skeleton design, not all GKE stages belonged inside the application loop. Some stages describe the target/environment and some describe an application deployed into that target.

## Decision
Use the following GKE skeleton structure.

Target-scoped stages, once per target:

- GkeCreateNamespace
- GkeCreateIngress
- GkeConfigureDns
- GkeValidateService

Application-scoped stages, once per application per target:

- GkeDeployApplication
- GkeValidateDeployment / GkeValidateRollout

Previously considered but removed/merged:

- GkeCreateService, merged into GkeDeployApplication
- GkeAuthenticate, rejected as a stage

## Consequences
The execution order for a target with two apps becomes:

```text
CloudWorkflowInitialize
GradleBuild@app1
GradleBuild@app2
GkeCreateNamespace@target
GkeDeployApplication@app1
GkeValidateDeployment@app1
GkeDeployApplication@app2
GkeValidateDeployment@app2
GkeCreateIngress@target
GkeConfigureDns@target
GkeValidateService@target
CloudWorkflowCleanup@__final__
```
