# StyxCD Infrastructure Architecture Decisions (ARD-B)

## Session Scope

- Shared Terraform remote state
- ECS/Fargate provisioning strategy
- Jenkins agent provisioning with Ansible
- Remote Jenkins execution
- Capability-based executor labeling
- Infrastructure repository structure

## Key Decisions

### Terraform Owns Platform Infrastructure

Terraform owns:
- ECS clusters
- ALBs
- Target groups
- Route53
- Security groups
- IAM
- CloudWatch log groups

Terraform does NOT own:
- ECR repositories
- Application deployment lifecycle

### ECR Ownership

ECR repositories moved out of Terraform ownership.

StyxCD/Jenkins now own:
- repo validation/creation
- image builds
- image pushes

### Environment Cluster Model

One ECS cluster per environment:
- dev
- qa
- stage
- prod

Multiple unrelated applications may deploy into the same cluster.

### Future Terraform Split

Future split:
- env plans
- app ingress plans
- reusable modules

### Jenkins Execution Model

Built-in node executors set to 0.

All workloads run on remote agents.

### Capability Labels

Base label:
- styxcd-agent

Additional labels:
- terraform
- docker
- ecs-fargate
- python
- eks

Entrypoint:
node('styxcd-agent')

Stage-specific:
node('styxcd-agent && terraform')

### Infrastructure Ownership

Terraform:
- infrastructure desired state

Ansible:
- machine provisioning

Jenkins:
- execution workers

StyxCD:
- orchestration/control plane
