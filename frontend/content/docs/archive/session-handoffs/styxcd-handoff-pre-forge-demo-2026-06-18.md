# StyxCD Session Handoff + Pre-Forge Leadership Demo Plan

Date: 2026-06-18

## Current Situation

The immediate objective is no longer proving that StyxCD can deploy to Kubernetes.

That has already been demonstrated.

The objective is to improve the presentation, usability, documentation, and product story of StyxCD so it can serve as:

- A portfolio project
- A leadership demo
- A foundation for future platform work

---

## What Has Been Proven

### GKE

Successfully demonstrated:

- Execution Plan generation
- Jenkins integration
- GKE deployment workflow
- Namespace creation
- Application deployment
- Service creation
- Ingress and DNS validation path

GKE is currently the reference implementation.

### EKS

Successfully demonstrated:

- Sandbox EKS cluster creation
- Sandbox EKS cluster teardown
- EKS workflow shape inside StyxCD
- Jenkins stage registration
- Execution plan generation

Validated stage lifecycle:

- EksInstallLoadBalancerController
- EksCreateNamespace
- EksDeployApplication
- EksValidateDeployment
- EksCreateIngress
- EksConfigureDns
- EksValidateService

Current EKS work remaining is largely migration and cleanup rather than invention.

---

## Important Architectural Realization

The product is not Kubernetes.

The product is:

Execution Plan
→ Stages
→ Implementation

Examples:

cloud_workflow
→ Deploy infrastructure and applications

pm_workflow
→ Manage backlog and roadmap

Future workflows:

- iac_workflow
- security_workflow
- operations_workflow

The framework itself is becoming the primary value proposition.

---

## New Priority Direction

Shift focus temporarily away from EKS plumbing.

Focus on creating a stronger product demonstration.

---

## Phase 1 - Repository Cleanup

### Goal

Separate UI from orchestrator.

Current state:

- styxcd-orchestrator
  - Spring Boot
  - REST APIs
  - Execution planning
  - Embedded Next.js UI

Target state:

- styxcd-orchestrator
  - Spring Boot APIs only

- styxcd-dashboard
  - Next.js UI

### Before Moving Code

Create a stability tag.

Suggested tag:

- stable-pre-dashboard-split

or

- v0.1.0-foundation

Goal:

"We know this version works."

### After Split

Verify:

- Build succeeds
- Dashboard loads
- API integration works
- Existing deployment workflows continue functioning

---

## Phase 2 - Documentation Consolidation

Create:

docs/
- architecture/
- workflows/
- specifications/
- examples/
- adr/

Initial ADR candidates:

- ADR-001 Execution plans generated up-front
- ADR-002 Stages do not dynamically determine subsequent stages
- ADR-003 Artifacts modeled as lists
- ADR-004 Cloud workflow consumes existing infrastructure
- ADR-005 Workflow engine supports non-deployment workflows

---

## Phase 3 - YAML Specification v0.1

Create first official specification covering:

- Workflow types
- Application types
- Build types
- Artifact types
- Environment types
- Credentials

Publishing strategy:

- GitHub Pages
- Embedded dashboard documentation

Single source of truth:

- styxcd-docs

---

## Phase 4 - Dashboard Improvements

Candidate sections:

- Dashboard
- Executions
- Documentation
- Workflow Catalog
- Examples
- Roadmap
- About

Execution visualization:

✓ Create Namespace
✓ Deploy Backend
✓ Validate Backend
✓ Deploy UI
✓ Validate UI
✓ Create Ingress

### Mobile Strategy

Responsive design only.

Support:

- Desktop
- Tablet
- Phone

---

## Phase 5 - Grafana Integration

Initial targets:

- Deployment Success Rate
- Workflow Executions
- Jenkins Build Status
- Loki Error Counts

Future:

Dedicated observability page.

---

## Phase 6 - PM Workflow

Resume after dashboard and documentation maturity.

Purpose:

Demonstrate StyxCD as a workflow engine, not merely a deployment tool.

---

## Phase 7 - Resume EKS Migration

Move sandbox implementations into:

- EksInstallLoadBalancerController
- EksCreateNamespace
- EksDeployApplication
- EksValidateDeployment
- EksCreateIngress
- EksConfigureDns
- EksValidateService

---

## Leadership Demo Narrative

1. Define desired state using YAML.
2. StyxCD converts YAML into an execution plan.
3. Execution plan becomes stages.
4. Stages execute across platforms.
5. Dashboard visualizes execution.
6. Documentation provides workflow specifications.
7. Future workflows extend the same execution engine.

Key message:

StyxCD is an orchestration framework that happens to deploy cloud workloads today.

The framework is the product.
