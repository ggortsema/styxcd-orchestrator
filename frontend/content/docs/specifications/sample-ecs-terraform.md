# Sample ECS Terraform Workflow

This sample shows an early StyxCD workflow shape for ECS Fargate backed by Terraform-managed infrastructure. It separates application build metadata from platform infrastructure realization and captures the Terraform repository, environment path, and plan resolution strategy used to create or reconcile the ECS sandbox environment.

```yaml
workflow: cloud_workflow

release:
  name: johnny-platform-release
  version: 1.0.0

  desired_state:
    environment: present
    applications: deployed

  applications:

    spring:
      - name: johnny-johnny-backend
        repo: https://github.com/ggortsema/johnny-johnny.git
        branch: main
        version: 1.0.0

        credentials:
          git:
            source: jenkins
            id: github-styxcd-readonly

        build:
          type: maven-docker
          project_path: .
          module_path: chat-api
          dockerfile: chat-api/Dockerfile
          docker_context: .

        artifact:
          type: docker-image
          registry: ecr
          repository: johnny-johnny/johnny-johnny-backend
          tag: latest

    node:
      - name: johnny-johnny-ui
        repo: https://github.com/ggortsema/johnny-johnny-ui.git
        branch: main
        version: 1.0.0

        credentials:
          git:
            source: jenkins
            id: github-styxcd-readonly

        build:
          type: docker
          project_path: .
          dockerfile: Dockerfile
          docker_context: .

        artifact:
          type: docker-image
          registry: ecr
          repository: johnny-johnny/johnny-johnny-ui
          tag: latest

  environments:

    sandbox:
      - name: johnny-sandbox

        platform:
          name: ecs
          launch_type: fargate
          region: us-east-1

          infrastructure:
            provider: terraform

            credentials:
              aws:
                source: jenkins
                id: aws-styxcd-dev

            terraform:
              repo: https://github.com/ggortsema/styxcd-terraform.git
              branch: main
              path: envs/sandbox/ecs-fargate

              plan:
                resolution_strategy: app_hash
                hash_scope: environment_app_set
                hash_algorithm: sha256
```
