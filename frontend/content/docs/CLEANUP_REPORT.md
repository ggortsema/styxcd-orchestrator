# StyxCD Docs Cleanup Report

Date: 2026-06-18

## What changed

- Removed `.git` internals from the deliverable ZIP.
- Removed `.DS_Store` files.
- Moved ADRs into `adr/` and made them the decision source of truth.
- Created consolidated current architecture docs under `architecture/`.
- Moved current contracts/specifications into `specifications/`.
- Moved GKE/EKS and Terraform target docs into `platform-targets/`.
- Moved Kharon/RAG vision into `kharon/`.
- Preserved backlog YAML files unchanged under `backlog/`.
- Preserved legacy Jenkins planner code under `migration/jenkins-planner-legacy/`.
- Archived session handoffs and older overlapping notes under `archive/`.

## Files from original ZIP considered

- `ADR-0001-control-plane-separation.md`
- `ADR-001-yaml-intent-execution-plan-contract.md`
- `ADR-002-orchestrator-plans-jenkins-executes.md`
- `ADR-003-lifecycle-target-application-planning.md`
- `ADR-004-gke-target-and-application-scopes.md`
- `ADR-005-merge-kubernetes-service-into-deploy-application.md`
- `ADR-006-separate-deployment-validation-and-service-validation.md`
- `ADR-007-app-name-not-apphost-name.md`
- `ADR-008-final-stage-marker.md`
- `ADR-009-authentication-is-utility-not-stage.md`
- `ADR-00X-dynamic-execution-plan.md`
- `ADR-00Y-up-front-planning-vs-stage-directed-workflow.md`
- `ADR-010-stage-helper-classes-when-they-add-value.md`
- `ADR-011-stage-registry-future-refactor.md`
- `ADR-012-future-workflow-dsl-productization.md`
- `ADR-013-structured-logging-loki-grafana.md`
- `ADR-014-multiple-cloud-and-infra-workflow-capabilities.md`
- `ADR-015-project-management-workflow-future-capability.md`
- `KHARON_RAG_VISION.md`
- `README.md`
- `architectural_decisions_2026_06_03.md`
- `architecture-api-routing-module.md`
- `architecture-current-next-steps.md`
- `architecture-execution-agent-callbacks.md`
- `architecture-planner-migration.md`
- `architecture-stage-param-planners.md`
- `architecture-workflow-planner-contract.md`
- `backlog/commandtool.yml`
- `backlog/styxcd-combined-backlog-2026-06-17.yml`
- `backlog/styxcd-pm-workflow-backlog.yml`
- `backlog/styxcd-project-management-workflow.yml`
- `cicd_architecture.drawio`
- `control-plane.md`
- `event-routing-module.md`
- `execution-lifecycle.md`
- `execution-planning-architecture.md`
- `future-agent-model.md`
- `migration/jenkins-planner-legacry/INFO.md`
- `migration/jenkins-planner-legacry/stages/StageMap.groovy`
- `migration/jenkins-planner-legacry/stages/StageWrapper.groovy`
- `migration/jenkins-planner-legacry/stages/stagesimpl/BuildECSFargateCluster.groovy`
- `migration/jenkins-planner-legacry/stages/stagesimpl/CloudWorkflowCleanup.groovy`
- `migration/jenkins-planner-legacry/stages/stagesimpl/CloudWorkflowInitialize.groovy`
- `migration/jenkins-planner-legacry/stages/stagesimpl/DeployECSFargateApplications.groovy`
- `migration/jenkins-planner-legacry/stages/stagesimpl/DestroyECSFargateCluster.groovy`
- `migration/jenkins-planner-legacry/stages/stagesimpl/DummyWorkflowBody.groovy`
- `migration/jenkins-planner-legacry/stages/stagesimpl/DummyWorkflowCleanup.groovy`
- `migration/jenkins-planner-legacry/stages/stagesimpl/DummyWorkflowInitialize.groovy`
- `migration/jenkins-planner-legacry/stages/stagesimpl/EKSWorkflowBuildImages.groovy`
- `migration/jenkins-planner-legacry/stages/stagesimpl/EKSWorkflowCleanup.groovy`
- `migration/jenkins-planner-legacry/stages/stagesimpl/EKSWorkflowClusterBuild.groovy`
- `migration/jenkins-planner-legacry/stages/stagesimpl/EKSWorkflowClusterTeardown.groovy`
- `migration/jenkins-planner-legacry/stages/stagesimpl/EKSWorkflowDeployImages.groovy`
- `migration/jenkins-planner-legacry/stages/stagesimpl/EKSWorkflowInitialize.groovy`
- `migration/jenkins-planner-legacry/stages/stagesimpl/EKSWorkflowLoadBalancingController.groovy`
- `migration/jenkins-planner-legacry/stages/stagesimpl/GradleBuild.groovy`
- `migration/jenkins-planner-legacry/stages/stagesimpl/ScaleDownECSFargateApplications.groovy`
- `migration/jenkins-planner-legacry/workflow/CloudWorkflow.groovy`
- `migration/jenkins-planner-legacry/workflow/DummyWorkflow.groovy`
- `migration/jenkins-planner-legacry/workflow/EKSWorkflow.groovy`
- `migration/jenkins-planner-legacry/workflow/WorkflowMap.groovy`
- `observability-strategy.md`
- `session-context-2026-05-16.md`
- `styxcd-architecture-milestone-2026-05-16 (1).md`
- `styxcd-architecture-refactor-adr-2026-05-18.md`
- `styxcd-ard-b.md`
- `styxcd-decision-notes-2026-05-18.md`
- `styxcd-documentation-architecture.md`
- `styxcd-execution-flow-reference-2026-05-16.md`
- `styxcd-gke-eks-stage-architecture-2026-06-17.md`
- `styxcd-handoff-pre-forge-demo-2026-06-18.md`
- `styxcd-lessons-learned.md`
- `styxcd-observability-next-steps-2026-05-16.md`
- `styxcd-observability-ui-architecture-2026-05-17.md`
- `styxcd-spec-b.md`
- `styxcd-terraform-appset-hashing-strategy.md`
- `styxcd-yml-specification.md`

## Cleanup judgment

No user-authored docs were permanently deleted from the cleaned repo package except generated/system files like `.DS_Store` and embedded `.git` objects. Older docs were moved into archive folders so they no longer clutter the main reading path.
