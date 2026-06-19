# StyxCD Kubernetes Stage Architecture: GKE and EKS Parity

Date: 2026-06-17

## Purpose

This document captures the current architectural decision for how StyxCD should model Kubernetes deployment stages across GKE and EKS.

The goal is not to make every cloud provider implementation identical at the command level. The goal is to preserve conceptual parity in the execution plan wherever the work being performed is the same.

In other words:

```text
Same deployment intent
-> Similar execution plan shape
-> Cloud-specific implementation details inside each stage
```

This keeps StyxCD focused on platform abstraction while still allowing each platform to do the work in the correct native way.

---

## Current Guiding Principle

The GKE workflow is treated as the desired reference flow.

The EKS implementation should match the GKE stage model wherever the work is conceptually equivalent, and should introduce EKS-specific stages only where the platform genuinely requires additional setup.

This avoids two bad outcomes:

1. Over-generalizing too early and hiding important platform differences.
2. Letting every cloud provider become a totally different workflow shape.

The balance is:

```text
Conceptual parity where possible.
Implementation-specific behavior where necessary.
```

---

## Existing GKE Flow

The current GKE execution plan is:

```text
GkeCreateNamespace

For each application:
    GkeDeployApplication
    GkeValidateDeployment

GkeCreateIngress
GkeConfigureDns
GkeValidateService
```

The important design detail is that `GkeDeployApplication` currently creates more than just the Deployment object. It creates the application-level Kubernetes resources needed for the app, including:

- Kubernetes Deployment
- Kubernetes Service
- Application secrets
- Environment variables
- Optional GKE BackendConfig for health checks

So the current GKE model is not:

```text
CreateDeployment
CreateService
CreateSecrets
```

as separate stages.

It is:

```text
DeployApplication
```

where application deployment includes the resources that make that application runnable inside Kubernetes.

That is the model EKS should mirror for now.

---

## Proposed EKS Flow

The proposed EKS execution plan is:

```text
EksInstallLoadBalancerController
EksCreateNamespace

For each application:
    EksDeployApplication
    EksValidateDeployment

EksCreateIngress
EksConfigureDns
EksValidateService
```

The only additional stage compared to GKE is:

```text
EksInstallLoadBalancerController
```

This exists because EKS does not provide the same managed ingress behavior out of the box for ALB-based ingress. The AWS Load Balancer Controller is required platform plumbing before an ALB Ingress can work.

---

## GKE / EKS Stage Mapping

| GKE Stage | EKS Stage | Conceptual Match? | Notes |
|---|---|---:|---|
| `GkeCreateNamespace` | `EksCreateNamespace` | Yes | Same Kubernetes responsibility. Ensure the namespace exists before app resources are applied. |
| `GkeDeployApplication` | `EksDeployApplication` | Yes | Same platform responsibility. Create application Kubernetes resources such as Deployment, Service, secrets, env vars. Implementation differs only where cloud-specific annotations or manifests are needed. |
| `GkeValidateDeployment` | `EksValidateDeployment` | Yes | Same Kubernetes validation. Use rollout checks and diagnostic snapshots. |
| `GkeCreateIngress` | `EksCreateIngress` | Yes | Same conceptual responsibility: expose services through Kubernetes Ingress. Implementation differs because EKS uses ALB-specific annotations/class behavior. |
| `GkeConfigureDns` | `EksConfigureDns` | Yes | Same conceptual responsibility: point public DNS names at the ingress endpoint. Implementation differs because GKE uses an ingress IP while EKS uses an ALB hostname/alias target. |
| `GkeValidateService` | `EksValidateService` | Yes | Same final user-facing validation. Check that the deployed app is reachable through public hostnames. |
| None | `EksInstallLoadBalancerController` | No, EKS-specific | Required EKS platform setup for ALB ingress support. This is not application deployment. It is cluster/platform enablement. |

---

## Why `EksInstallLoadBalancerController` Is Separate

The AWS Load Balancer Controller should not be hidden inside `EksCreateIngress`.

`EksCreateIngress` should create the application ingress.

`EksInstallLoadBalancerController` prepares the cluster so ALB ingress can work.

This distinction matters because the controller is platform infrastructure, not application infrastructure.

The stage owns work such as:

- Associate IAM OIDC provider
- Ensure/load AWS Load Balancer Controller IAM policy
- Create/update IAM service account
- Add/update Helm repo
- Install/upgrade AWS Load Balancer Controller
- Validate controller rollout
- Verify `IngressClass alb`

This stage may eventually be skipped if the cluster is already prepared, but for the sandbox implementation it can be kept idempotent and safe to run.

---

## Why Service Creation Stays Inside DeployApplication For Now

Although Kubernetes Service is its own resource, the current GKE design creates Deployment and Service together inside `GkeDeployApplication`.

For parity, EKS should do the same for now.

The reason is practical:

```text
Application deployment = make this app runnable inside the cluster.
```

That includes:

- Container image
- Replicas
- Ports
- Environment variables
- Secrets
- Kubernetes Service

Splitting Service into its own stage may make sense later, but doing it now would make EKS diverge from the GKE reference flow for no immediate benefit.

The first EKS refactor should favor matching the proven GKE workflow.

---

## Ingress Responsibility

`CreateIngress` should own Kubernetes routing configuration.

For GKE, this means creating an Ingress that routes host/path rules to services.

For EKS, this means creating an Ingress that routes host/path rules to services using the AWS ALB ingress class/controller behavior.

Conceptually, both stages answer the same question:

```text
How should traffic entering the cluster be routed to services?
```

The stage should not own public DNS updates. That belongs to DNS configuration.

---

## DNS Responsibility

`ConfigureDns` should own public name resolution.

For GKE, the DNS stage reads the ingress IP and creates Route53 records pointing at that IP.

For EKS, the DNS stage should read the ALB hostname from the ingress and create Route53 alias records pointing at the ALB.

Conceptually, both stages answer the same question:

```text
How should public hostnames point to the deployed application entrypoint?
```

The implementation differs because:

```text
GKE ingress -> IP address
EKS ALB ingress -> AWS load balancer hostname + canonical hosted zone ID
```

That is a provider-specific implementation detail inside the DNS stage, not a reason to change the execution-plan concept.

---

## Validation Responsibility

There are two validation stages:

```text
ValidateDeployment
ValidateService
```

They serve different purposes.

`ValidateDeployment` checks whether the Kubernetes Deployment rolled out successfully.

This is inside-cluster/application-health validation.

`ValidateService` checks whether the deployed application is reachable through the exposed service/ingress/DNS path.

This is outside-in/user-facing validation.

Keeping both is useful because failures are easier to understand:

```text
Deployment failed
-> likely image, pod, env, secret, readiness, or scheduling problem

Service validation failed
-> likely service, ingress, DNS, load balancer, or application routing problem
```

---

## Current Recommendation

Use this as the first EKS implementation target:

```text
EksInstallLoadBalancerController
EksCreateNamespace

For each application:
    EksDeployApplication
    EksValidateDeployment

EksCreateIngress
EksConfigureDns
EksValidateService
```

Do not add cluster creation or teardown to this flow yet.

Cluster build/teardown should remain a separate infrastructure lifecycle concern, possibly later represented as:

```text
EksCreateCluster
EksDestroyCluster
```

or as a separate sandbox/provisioning workflow.

The immediate goal is deploying Johnny-Johnny to an existing EKS cluster using the same conceptual flow already proven with GKE.

---

## First Coding Slice

The first coding slice should be intentionally small:

```text
1. Update orchestrator parsing so EKS targets produce EKS stage names.
2. Send parameter maps to Jenkins.
3. Implement hello-world EKS stages.
4. Echo parsed params in each stage.
5. Confirm execution order.
```

Only after that should working sandbox commands be moved into each stage.

This preserves the same successful pattern used for GKE:

```text
Parse YAML
-> build execution plan
-> run placeholder stages
-> move real implementation into stages one at a time
```

---

## Architectural Takeaway

The platform abstraction is not that every cloud runs the same commands.

The platform abstraction is that StyxCD can represent the same deployment intent as a similar execution plan across clouds.

GKE and EKS both need:

```text
Namespace
Application deployment
Deployment validation
Ingress
DNS
Service validation
```

EKS additionally needs:

```text
Load balancer controller installation
```

That difference should be explicit, isolated, and understandable.
