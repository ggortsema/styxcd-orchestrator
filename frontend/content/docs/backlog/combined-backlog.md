# Combined StyxCD Roadmap Backlog

This document is the human-readable backlog view generated from a StyxCD `pm_workflow` YAML definition.

## Provider

- **Type:** github
- **Repository:** ggortsema/styxcd-docs
- **Project:** mycroftai engineering roadmap
- **Jenkins Creds:** mycred

## Epics

## Epic: Project Management Workflow Provider

Implement a StyxCD workflow that can create, update, synchronize, and migrate roadmap items across project-management providers using YAML as the durable source of truth.

### Acceptance Criteria

- Project-management workflows can be represented as YAML.
- GitHub Issues and GitHub Projects are supported as the first provider target.
- Epics and child issues can be created from YAML.
- Workflow execution supports dry-run and idempotency.
- Provider issue IDs and URLs can be synchronized back into YAML.
- Provider migration is possible without losing the roadmap-as-code source.

### Issues

#### Define PM Workflow DSL

Define the YAML structure for project-management workflows, including provider configuration, epics, issues, labels, assignees, milestones, acceptance criteria, and provider metadata.

**Acceptance Criteria**

- PM workflow YAML schema is documented.
- Epics and issues are represented clearly.
- Optional fields such as labels, assignees, and milestones are supported.
- Provider-specific metadata can be stored without polluting the portable model.

#### Implement GitHub Issues Provider

Implement the first PM workflow provider using the GitHub API to create roadmap issues in the configured repository.

**Acceptance Criteria**

- GitHub provider can authenticate using Jenkins credentials.
- Provider can create epic issues.
- Provider can create child issues.
- Provider can apply labels, assignees, and milestones when supplied.
- Provider errors are reported clearly.

#### Support Epic and Sub-Issue Creation

Create parent epic issues and attach child issues using GitHub's issue hierarchy or supported relationship model.

**Acceptance Criteria**

- Epic issue is created first.
- Child issues are created under the correct epic.
- Parent-child relationships are preserved.
- Created items are added to the configured project board.

#### Support GitHub Project Assignment

Add created or updated issues to the configured GitHub Project board.

**Acceptance Criteria**

- Project can be identified from YAML configuration.
- Created epics are added to the project.
- Created issues are added to the project.
- Default status can be set when supported.

#### Add PM Workflow Dry-Run Mode

Provide a dry-run mode that previews create, update, no-op, and error actions without modifying the provider.

**Acceptance Criteria**

- Dry-run mode performs no writes.
- Dry-run output lists epics and issues that would be created.
- Dry-run output lists items that would be updated.
- Dry-run output identifies potential duplicates or conflicts.

#### Support PM Workflow Idempotency

Ensure rerunning a PM workflow does not create duplicate epics or issues when matching items already exist.

**Acceptance Criteria**

- Existing epics are detected.
- Existing issues are detected.
- Matching can use provider ID when available.
- Matching can fall back to title when ID is missing.
- Duplicate creation is prevented.
- Dry-run output reports create/update/no-op actions.

#### Synchronize PM Workflow YAML From Provider

Read existing epics and issues from a project-management provider and update local PM workflow YAML with provider identifiers, links, and status metadata.

**Acceptance Criteria**

- Existing provider issues can be fetched.
- Issues are matched to YAML entries by title.
- Provider issue IDs are written back to YAML.
- Provider URLs are written back to YAML.
- Existing YAML descriptions are preserved.
- Missing or renamed issues are reported.

#### Support Provider Metadata in PM Workflow YAML

Store provider-specific identifiers separately so the YAML remains portable across GitHub, Linear, ClickUp, Jira, or future providers.

**Acceptance Criteria**

- Provider-specific IDs are stored under provider metadata.
- Portable fields remain provider-neutral.
- Multiple provider mappings can be represented.
- Provider metadata structure is documented.

#### Support Provider Migration for PM Workflows

Allow the same roadmap YAML to be projected into a different project-management provider.

**Acceptance Criteria**

- PM workflow provider model is abstracted.
- Provider-specific IDs are stored separately.
- YAML can support multiple provider mappings.
- Roadmap can be recreated in a new provider.
- Migration process is documented.

#### Generate PM Workflow YAML From Conversation

Use an AI-assisted step to transform roadmap discussion or session notes into PM workflow YAML.

**Acceptance Criteria**

- Conversation/session notes can be converted into PM workflow YAML.
- Generated YAML follows the PM workflow schema.
- Generated YAML can be reviewed before execution.
- Generated YAML can be committed to styxcd-docs.

#### Demonstrate Backlog-as-Code Workflow

Use StyxCD to create or update a real project-management backlog from committed PM workflow YAML.

**Acceptance Criteria**

- PM workflow YAML exists in styxcd-docs.
- Workflow can be executed through StyxCD.
- GitHub issues are created or updated.
- Execution output shows create/update/no-op results.
- Demo is documented.

## Epic: GKE Workflow

Implement Google Kubernetes Engine (GKE) as a first-class deployment target within StyxCD.

### Acceptance Criteria

- GKE is supported as a first-class StyxCD deployment target.
- Applications can be deployed to GKE through a StyxCD workflow.
- Authentication and credential management strategy is defined.
- Deployment validation is supported.
- Rollback capability is supported.
- Workflow DSL supports GKE-specific configuration.
- End-to-end deployment can be demonstrated using StyxCD.

### Issues

#### Implement GKE Deployment Stage

TODO: Define scope and acceptance criteria for Implement GKE Deployment Stage.

#### Define GKE Authentication Strategy

TODO: Define scope and acceptance criteria for Define GKE Authentication Strategy.

#### Deploy Sample Application to GKE

TODO: Define scope and acceptance criteria for Deploy Sample Application to GKE.

#### Add GKE Rollback Support

TODO: Define scope and acceptance criteria for Add GKE Rollback Support.

#### Add GKE Deployment Validation

TODO: Define scope and acceptance criteria for Add GKE Deployment Validation.

#### Define GKE Deployment Model

TODO: Define scope and acceptance criteria for Define GKE Deployment Model.

#### Implement GKE Sandbox Environment Support

TODO: Define scope and acceptance criteria for Implement GKE Sandbox Environment Support.

## Epic: Workflow Templates & Effective YAML

Implement reusable workflow templates and an Effective YAML capability.

### Acceptance Criteria

- Workflow templates are supported.
- Variable substitution is supported.
- Template inheritance/composition is supported.
- Effective YAML can be generated before execution.
- Effective YAML can be viewed in the UI.
- Users can validate resolved workflows prior to execution.

### Issues

#### Design Workflow Template Model

TODO: Define scope and acceptance criteria for Design Workflow Template Model.

#### Implement Template Resolution Engine

TODO: Define scope and acceptance criteria for Implement Template Resolution Engine.

#### Implement Effective YAML Generation

TODO: Define scope and acceptance criteria for Implement Effective YAML Generation.

#### Display Effective YAML in UI

TODO: Define scope and acceptance criteria for Display Effective YAML in UI.

#### Implement Workflow Validation Against Effective YAML

TODO: Define scope and acceptance criteria for Implement Workflow Validation Against Effective YAML.

#### Create Platform Workflow Templates

TODO: Define scope and acceptance criteria for Create Platform Workflow Templates.

#### Support Template Versioning

TODO: Define scope and acceptance criteria for Support Template Versioning.

#### Create Template Catalog UI

TODO: Define scope and acceptance criteria for Create Template Catalog UI.

#### Add Template Resolution Testing Strategy

TODO: Define scope and acceptance criteria for Add Template Resolution Testing Strategy.

#### Design Effective YAML Help Experience

Define how users should understand resolved YAML, template expansion, and final execution inputs before a workflow runs.

**Acceptance Criteria**

- Effective YAML view requirements are documented.
- Help files explain how raw YAML becomes effective YAML.
- Resolved examples show platform defaults, template inheritance, and provider-specific values.
- Effective YAML is positioned as an audit/debugging aid.
- Future RAG retrieval use cases are considered in the documentation format.

## Epic: Platform Security

Implement the security foundation for StyxCD.

### Acceptance Criteria

- Authentication strategy is defined.
- Service-to-service authentication is implemented.
- Sensitive credentials are managed securely.
- Authorization model is documented.
- Security architecture is documented.

### Issues

#### Evaluate Control Plane Authentication Strategy

TODO: Define scope and acceptance criteria for Evaluate Control Plane Authentication Strategy.

#### Implement Control Plane Authentication

TODO: Define scope and acceptance criteria for Implement Control Plane Authentication.

#### Define Service-to-Service Authentication Model

TODO: Define scope and acceptance criteria for Define Service-to-Service Authentication Model.

#### Secure Jenkins Callback Endpoints

TODO: Define scope and acceptance criteria for Secure Jenkins Callback Endpoints.

#### Implement Secrets Management Strategy

TODO: Define scope and acceptance criteria for Implement Secrets Management Strategy.

#### Define Authorization and RBAC Model

TODO: Define scope and acceptance criteria for Define Authorization and RBAC Model.

#### Secure Workflow Credential Usage

TODO: Define scope and acceptance criteria for Secure Workflow Credential Usage.

#### Audit Security-Sensitive Actions

TODO: Define scope and acceptance criteria for Audit Security-Sensitive Actions.

#### Define Multi-Tenant Security Requirements

TODO: Define scope and acceptance criteria for Define Multi-Tenant Security Requirements.

#### Create Security Architecture Documentation

TODO: Define scope and acceptance criteria for Create Security Architecture Documentation.

#### Implement Session-Based Control Plane Auth

Implement initial control-plane authentication using Spring Security and server-side sessions before introducing more complex OIDC/JWT patterns.

**Acceptance Criteria**

- Spring Security is configured for the orchestrator.
- Protected APIs require authentication.
- Server-side sessions are used for initial implementation.
- Future OIDC/JWT integration remains possible.
- Authentication architecture is documented.

## Epic: Testing Strategy

Establish a comprehensive testing strategy for StyxCD.

### Issues

#### Add YAML Parsing Tests

TODO: Define scope and acceptance criteria for Add YAML Parsing Tests.

#### Add Execution Plan Generation Tests

TODO: Define scope and acceptance criteria for Add Execution Plan Generation Tests.

#### Add Workflow Planner Tests

TODO: Define scope and acceptance criteria for Add Workflow Planner Tests.

#### Add Stage Inclusion Tests

TODO: Define scope and acceptance criteria for Add Stage Inclusion Tests.

#### Add Callback Processing Tests

TODO: Define scope and acceptance criteria for Add Callback Processing Tests.

#### Add Controller/API Tests

TODO: Define scope and acceptance criteria for Add Controller/API Tests.

#### Create Behavior Testing Framework

TODO: Define scope and acceptance criteria for Create Behavior Testing Framework.

#### Create End-to-End Workflow Test Suite

TODO: Define scope and acceptance criteria for Create End-to-End Workflow Test Suite.

#### Add Local Testing Harness

TODO: Define scope and acceptance criteria for Add Local Testing Harness.

#### Add Docker Compose Development Environment

TODO: Define scope and acceptance criteria for Add Docker Compose Development Environment.

## Epic: Dogfooding StyxCD

Deploy and manage StyxCD using StyxCD.

### Issues

#### Deploy StyxCD Using StyxCD

TODO: Define scope and acceptance criteria for Deploy StyxCD Using StyxCD.

#### Create StyxCD Release Workflow

TODO: Define scope and acceptance criteria for Create StyxCD Release Workflow.

#### Create StyxCD Promotion Workflow

TODO: Define scope and acceptance criteria for Create StyxCD Promotion Workflow.

#### Implement StyxCD Self-Update Workflow

TODO: Define scope and acceptance criteria for Implement StyxCD Self-Update Workflow.

#### Validate Upgrade Scenarios

TODO: Define scope and acceptance criteria for Validate Upgrade Scenarios.

#### Document Internal Release Process

TODO: Define scope and acceptance criteria for Document Internal Release Process.

## Epic: Release Management

Establish repeatable release and promotion processes.

### Issues

#### Define Stable Environment Strategy

TODO: Define scope and acceptance criteria for Define Stable Environment Strategy.

#### Define Development Environment Strategy

TODO: Define scope and acceptance criteria for Define Development Environment Strategy.

#### Implement Promotion Workflow

TODO: Define scope and acceptance criteria for Implement Promotion Workflow.

#### Implement Version Management Strategy

TODO: Define scope and acceptance criteria for Implement Version Management Strategy.

#### Document Release Process

TODO: Define scope and acceptance criteria for Document Release Process.

#### Add Release Validation Checks

TODO: Define scope and acceptance criteria for Add Release Validation Checks.

#### Define Terraform and Deployment Boundary

Document how StyxCD should separate infrastructure lifecycle from deployment lifecycle when Terraform or external infrastructure tools are involved.

**Acceptance Criteria**

- Long-lived infrastructure responsibilities are identified.
- Release-time deployment responsibilities are identified.
- Fuzzy-boundary resources such as DNS, load balancers, target groups, ECS services, and ingress are discussed.
- Modes are documented: native deploy, Terraform-provisioned foundation, Terraform stage, and external infra contract.
- Decision supports future ECS and Terraform work.

## Epic: Multi-Cloud Support

Expand StyxCD beyond a single cloud provider.

### Issues

#### Complete GKE Vertical Slice

TODO: Define scope and acceptance criteria for Complete GKE Vertical Slice.

#### Expand ECS Deployment Support

TODO: Define scope and acceptance criteria for Expand ECS Deployment Support.

#### Expand EKS Deployment Support

TODO: Define scope and acceptance criteria for Expand EKS Deployment Support.

#### Evaluate AKS Support

TODO: Define scope and acceptance criteria for Evaluate AKS Support.

#### Define Multi-Cloud Abstraction Strategy

TODO: Define scope and acceptance criteria for Define Multi-Cloud Abstraction Strategy.

#### Create Cloud Capability Matrix

TODO: Define scope and acceptance criteria for Create Cloud Capability Matrix.

#### Implement EKS Execution Plan Shape

Add EKS as a first-class StyxCD cloud workflow branch using the same conceptual deployment lifecycle proven by GKE, plus an EKS-specific load balancer controller preparation stage.

**Acceptance Criteria**

- CloudWorkflow emits EKS stages when platform.name is eks.
- EKS execution plan includes load balancer controller installation, namespace creation, per-app deploy/validate stages, ingress, DNS, and service validation.
- Jenkins stage map resolves all EKS stage classes.
- Hello-world EKS stage execution validates end-to-end stage ordering.
- Existing GKE workflow continues to work after EKS planner changes.

#### Implement EKS Stage Logic From Sandbox Code

Move the existing working EKS sandbox deployment logic into discrete StyxCD stages matching the new EKS execution plan.

**Acceptance Criteria**

- EksInstallLoadBalancerController installs or validates AWS Load Balancer Controller support.
- EksCreateNamespace ensures the application namespace exists.
- EksDeployApplication creates deployment, service, secrets, and environment configuration.
- EksValidateDeployment performs rollout validation and captures diagnostics.
- EksCreateIngress creates the ALB-backed Kubernetes ingress.
- EksConfigureDns updates Route53 records for the ALB ingress endpoint.
- EksValidateService validates public application reachability.

#### Create Formal EKS YAML Specification

Define the first formal EKS YAML model after the hello-world execution plan is validated.

**Acceptance Criteria**

- EKS YAML captures AWS region, cluster name, namespace, credentials, ingress class, ALB settings, DNS settings, and application mappings.
- The spec distinguishes portable Kubernetes concepts from EKS-specific implementation options.
- Example EKS YAML deploys Johnny-Johnny backend and UI.
- The EKS YAML can generate the validated EKS execution plan.
- Spec documentation explains differences from the GKE YAML.

#### Document GKE and EKS Stage Parity

Capture the architectural mapping between GKE and EKS stages, including where the workflows align conceptually and where EKS requires provider-specific setup.

**Acceptance Criteria**

- Documentation includes a GKE/EKS stage mapping table.
- Documentation explains why EksInstallLoadBalancerController is separate from EksCreateIngress.
- Documentation explains the funnel model: cluster capability, namespace, app resources, ingress, DNS, validation.
- Documentation captures the principle that platform abstraction means common lifecycle plus provider-specific stage implementation.
- Document is stored in styxcd-docs.

#### Evaluate AKS Stage Mapping

Use the GKE and EKS stage model as the reference point for evaluating how AKS maps into the StyxCD execution lifecycle.

**Acceptance Criteria**

- AKS stage mapping is compared against GKE and EKS.
- Any AKS-specific platform preparation stages are identified.
- Planner impact is documented before implementation.
- AKS evaluation avoids premature over-abstraction.
- Decision is captured in architecture docs.

#### Map ECS Native Deployment Lifecycle

Revisit ECS without Terraform first to discover the native ECS deployment lifecycle and compare it to Kubernetes platform workflows.

**Acceptance Criteria**

- ECS native deployment responsibilities are identified.
- Task definition, service update, load balancer, target group, DNS, and validation responsibilities are mapped.
- StyxCD ECS stages are proposed based on working implementation behavior.
- Terraform is deferred until native lifecycle boundaries are understood.
- ECS mapping is documented for later comparison.

## Epic: Validation & Deployment Verification

Provide standardized deployment validation across platforms.

### Issues

#### Define Validation Contract

TODO: Define scope and acceptance criteria for Define Validation Contract.

#### Implement Validation Framework

TODO: Define scope and acceptance criteria for Implement Validation Framework.

#### Add Health Check Validation

TODO: Define scope and acceptance criteria for Add Health Check Validation.

#### Add Smoke Test Validation

TODO: Define scope and acceptance criteria for Add Smoke Test Validation.

#### Add Deployment Verification Reporting

TODO: Define scope and acceptance criteria for Add Deployment Verification Reporting.

#### Add Validation Dashboard Support

TODO: Define scope and acceptance criteria for Add Validation Dashboard Support.

## Epic: SLO-Aware Promotion

Support promotion decisions using measurable deployment outcomes.

### Issues

#### Define Measurement Contract

TODO: Define scope and acceptance criteria for Define Measurement Contract.

#### Capture Validation Metrics

TODO: Define scope and acceptance criteria for Capture Validation Metrics.

#### Store Validation Results

TODO: Define scope and acceptance criteria for Store Validation Results.

#### Evaluate SLO Thresholds

TODO: Define scope and acceptance criteria for Evaluate SLO Thresholds.

#### Support Promotion Gates

TODO: Define scope and acceptance criteria for Support Promotion Gates.

#### Document SLO Promotion Model

TODO: Define scope and acceptance criteria for Document SLO Promotion Model.

## Epic: Artifact Management

Support enterprise artifact promotion and governance patterns.

### Issues

#### Define Artifact Metadata Model

TODO: Define scope and acceptance criteria for Define Artifact Metadata Model.

#### Track Digest-Based Deployments

TODO: Define scope and acceptance criteria for Track Digest-Based Deployments.

#### Support Registry Replication

TODO: Define scope and acceptance criteria for Support Registry Replication.

#### Store Promotion History

TODO: Define scope and acceptance criteria for Store Promotion History.

#### Implement Artifact Audit Trail

TODO: Define scope and acceptance criteria for Implement Artifact Audit Trail.

#### Document Enterprise Artifact Strategy

TODO: Define scope and acceptance criteria for Document Enterprise Artifact Strategy.

#### Evaluate Build Once Promote Everywhere Pattern

Explore an enterprise artifact-management model where images are built once, tracked by digest, and promoted or replicated across cloud-native registries.

**Acceptance Criteria**

- Canonical artifact metadata model is proposed.
- Digest-based deployment support is evaluated.
- Registry replication/promotion use cases are documented.
- Cost and complexity tradeoffs are captured.
- Feature is positioned as optional enterprise governance, not the default deployment path.

## Epic: Observability

Provide operational visibility into StyxCD.

### Issues

#### Add Prometheus Metrics

TODO: Define scope and acceptance criteria for Add Prometheus Metrics.

#### Add Grafana Dashboards

TODO: Define scope and acceptance criteria for Add Grafana Dashboards.

#### Expand Structured Logging

TODO: Define scope and acceptance criteria for Expand Structured Logging.

#### Track Workflow Metrics

TODO: Define scope and acceptance criteria for Track Workflow Metrics.

#### Track Stage Metrics

TODO: Define scope and acceptance criteria for Track Stage Metrics.

#### Add Deployment Analytics

TODO: Define scope and acceptance criteria for Add Deployment Analytics.

#### Design Environment-Agnostic Log Collection

Design a log collection architecture that works across EC2, ECS, GKE, EKS, and future runtimes while preserving consistent structured application logging.

**Acceptance Criteria**

- Per-runtime collector options are evaluated.
- Alloy patterns for EC2 and Kubernetes are documented.
- ECS/CloudWatch/FireLens integration options are evaluated.
- Structured JSON logging contract is defined.
- Exceptions and execution-plan errors can flow to Loki/Grafana regardless of runtime.

## Epic: Documentation & Knowledge Base

Create a comprehensive documentation experience for StyxCD.

### Issues

#### Create Architecture Documentation

TODO: Define scope and acceptance criteria for Create Architecture Documentation.

#### Create Workflow Authoring Guide

TODO: Define scope and acceptance criteria for Create Workflow Authoring Guide.

#### Create Platform Guides

TODO: Define scope and acceptance criteria for Create Platform Guides.

#### Create Troubleshooting Guide

TODO: Define scope and acceptance criteria for Create Troubleshooting Guide.

#### Create Knowledge Base Structure

TODO: Define scope and acceptance criteria for Create Knowledge Base Structure.

#### Create Example Workflow Library

TODO: Define scope and acceptance criteria for Create Example Workflow Library.

#### Define Human and RAG Documentation Strategy

Decide how StyxCD documentation should serve both human readers today and future retrieval-augmented generation use cases.

**Acceptance Criteria**

- Human-readable documentation categories are defined.
- RAG-oriented documentation structure is defined.
- Document metadata conventions are proposed.
- Architecture decisions, YAML specs, examples, troubleshooting guides, and session handoffs are categorized.
- Guidelines explain what belongs in narrative docs versus machine-retrievable reference docs.

#### Formalize YAML Specification Documentation

Create a formal documentation model for StyxCD YAML specs, including cloud workflow specs, PM workflow specs, examples, and help files.

**Acceptance Criteria**

- YAML fields are documented with purpose, required/optional status, examples, and provider-specific notes.
- GKE and EKS examples are included.
- Spec help files are designed to be readable by humans and useful for future assistant retrieval.
- Versioning strategy for YAML specs is documented.
- Effective YAML documentation requirements are captured.

#### Clean Up Documentation Structure

Organize existing and future StyxCD docs into a clean structure that supports product documentation, architecture history, implementation notes, and AI retrieval.

**Acceptance Criteria**

- Repository documentation folders are proposed.
- Architecture docs, ADRs, specs, examples, backlog YAML, and handoff docs have clear homes.
- Redundant or stale documents are identified.
- A naming convention for dated session/context docs is defined.
- Documentation cleanup plan is added to the roadmap.

#### Create Architecture Decision Records

Introduce lightweight ADRs for major StyxCD design decisions such as execution-plan stages, platform abstraction boundaries, Terraform boundaries, and documentation strategy.

**Acceptance Criteria**

- ADR template is created.
- GKE/EKS stage parity decision is captured as an ADR.
- Native ECS before Terraform decision is captured as an ADR.
- Stable orchestration plus replaceable implementations principle is captured.
- ADRs are stored in styxcd-docs.

## Epic: Local Development Experience

Improve developer productivity and testing.

### Issues

#### Add In-Memory Database Mode

TODO: Define scope and acceptance criteria for Add In-Memory Database Mode.

#### Create Docker Compose Environment

TODO: Define scope and acceptance criteria for Create Docker Compose Environment.

#### Support Local Workflow Execution

TODO: Define scope and acceptance criteria for Support Local Workflow Execution.

#### Improve Developer Setup Documentation

TODO: Define scope and acceptance criteria for Improve Developer Setup Documentation.

#### Create Local Demo Environment

TODO: Define scope and acceptance criteria for Create Local Demo Environment.

## Epic: UI Enhancements

Expand StyxCD usability and workflow authoring experience.

### Issues

#### Load YAML From File

TODO: Define scope and acceptance criteria for Load YAML From File.

#### Load YAML From URL

TODO: Define scope and acceptance criteria for Load YAML From URL.

#### Create Workflow Templates UI

TODO: Define scope and acceptance criteria for Create Workflow Templates UI.

#### Display Effective YAML

TODO: Define scope and acceptance criteria for Display Effective YAML.

#### Improve Mobile Experience

TODO: Define scope and acceptance criteria for Improve Mobile Experience.

#### Add Execution Filtering

TODO: Define scope and acceptance criteria for Add Execution Filtering.

#### Add Execution Comparison View

TODO: Define scope and acceptance criteria for Add Execution Comparison View.

#### Add Template Catalog UI

TODO: Define scope and acceptance criteria for Add Template Catalog UI.

#### Remember YAML Editor State

Improve the StyxCD UI by remembering the user's last submitted YAML and editor/layout state.

**Acceptance Criteria**

- Last submitted YAML can be restored from local storage.
- Editor/layout preferences can be remembered.
- Clear editor behavior remains available.
- Draft restore behavior is predictable and documented.
- Feature does not interrupt current workflow execution.

## Epic: Planning & Decision Engine

Evolve workflow planning into a semantic decision system.

### Issues

#### Define PlanningDecision Model

TODO: Define scope and acceptance criteria for Define PlanningDecision Model.

#### Integrate Drools Decision Engine

TODO: Define scope and acceptance criteria for Integrate Drools Decision Engine.

#### Separate Planning From Execution

TODO: Define scope and acceptance criteria for Separate Planning From Execution.

#### Implement Decision Rules Framework

TODO: Define scope and acceptance criteria for Implement Decision Rules Framework.

#### Document Planning Architecture

TODO: Define scope and acceptance criteria for Document Planning Architecture.

#### Rename Validation Map Concepts

Revisit naming around VALIDATE_MAP, paramMap, and stageSpecificMap to better reflect execution planning, workflow context, and stage telemetry.

**Acceptance Criteria**

- Candidate names are reviewed: PlanningDecision/DecisionMap, ExecutionContext, StageTelemetry.
- Current naming pain points are documented.
- Renaming plan avoids disrupting current implementation flow.
- Decision is captured in architecture documentation.
- Code changes are deferred until timing is appropriate.

## Epic: Kharon Assistant

Build assistant capabilities that help users retrieve contextual project knowledge, understand StyxCD decisions, and navigate implementation history.

### Acceptance Criteria

- Assistant can retrieve relevant StyxCD context from approved documentation sources.
- Assistant responses can cite source documents.
- Project knowledge is organized for both human use and RAG retrieval.
- Assistant helps with implementation handoffs, spec lookup, and architectural decision recall.

### Issues

#### Create RAG Context Retrieval Layer

Create a retrieval layer that can index StyxCD documentation, specs, architecture decisions, backlog YAML, and session handoffs so Kharon can recover project context accurately.

**Acceptance Criteria**

- Supported source document types are identified.
- Chunking and metadata strategy is defined.
- Retrieval results can include source references.
- Initial corpus includes architecture docs, YAML specs, backlog YAML, and handoff documents.
- Assistant can answer contextual questions about prior decisions without relying only on conversation memory.

#### Define Kharon Project Memory Sources

Define which repositories, documents, and generated artifacts should become trusted sources for Kharon contextual answers.

**Acceptance Criteria**

- Trusted source list is documented.
- styxcd-docs is treated as the primary project knowledge source.
- Generated session handoffs have a clear lifecycle.
- Deprecated or experimental documents are marked clearly.
- Source freshness and authority rules are documented.

## Generated Metadata

- **Date:** 2026-06-17
- **Name:** combined-styxcd-roadmap-backlog
- **Notes:** Combined PM workflow backlog with existing StyxCD roadmap and new items from recent EKS, documentation, RAG, ECS, Terraform, and assistant discussions.

