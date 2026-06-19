# StyxCD Infrastructure Provisioning Specification (SPEC-B)

## Terraform Responsibilities

Terraform manages:
- ECS/Fargate clusters
- ALBs
- Target groups
- Route53
- Security groups
- IAM
- CloudWatch log groups

Terraform backend:
- S3
- encrypted
- versioned
- shared locking

## Ansible Responsibilities

Ansible provisions:
- Jenkins agents
- system packages
- Java
- Docker
- runtime environment

## Jenkins Responsibilities

Jenkins executes:
- Gradle builds
- Maven builds
- Docker builds
- Terraform execution
- deployment stages

## StyxCD Responsibilities

StyxCD:
- generates execution plans
- selects executor labels
- manages workflow sequencing
- tracks execution state

## Executor Model

Base label:
- styxcd-agent

Specialized labels:
- terraform
- docker
- ecs-fargate
- python
- eks

## Future Repository Layout

styxcd-infrastructure/
  terraform/
  ansible/
  docs/
