# StyxCD YML Specification

This document describes the current StyxCD declarative YML contract used by the orchestrator to plan CI/CD execution.

The YML file describes desired release intent. The orchestrator is responsible for parsing this file, validating the requested workflow, building an execution plan, persisting execution state, and exposing the plan to runtime agents such as Jenkins.

Jenkins should not derive workflow plans directly from this YML. Jenkins should receive a StyxCD execution request, retrieve the execution plan from the orchestrator, and execute the returned stages.

---

## Example

```yaml
workflow: cloud_workflow

release:
  name: johnny-platform-release
  version: 1.0.0

  desired_state:
    environment: present
    applications: deployed

    # Other valid desired_state examples:
    #
    # environment: absent
    # applications: none
    #
    # environment: ephemeral
    # applications: deployed
    #
    # environment: present
    # applications: built

  applications:
    spring:
      - name: first-app
        repo: someplace
        branch: main
        version: 1.0.0

        build:
          type: maven-docker
          project_path: .
          dockerfile: Dockerfile
          artifact_target: ecr

        runtime:
          container_port: 8080
          health_check_path: /actuator/health

      - name: second-app
        repo: someplace
        branch: main

        build:
          type: maven-docker
          project_path: .
          dockerfile: Dockerfile
          artifact_target: ecr

        runtime:
          container_port: 8080
          health_check_path: /actuator/health

    node:
      - name: first-node-app
        repo: someplace
        branch: main
        version: 1.0.0

        build:
          type: docker
          project_path: .
          dockerfile: Dockerfile
          artifact_target: ecr

        runtime:
          container_port: 80
          health_check_path: /

    python:
      - name: fastapi-app
        repo: someplace
        branch: main
        version: 1.0.0

        build:
          type: python-docker
          framework: fastapi
          project_path: .
          dockerfile: Dockerfile
          artifact_target: ecr

        runtime:
          container_port: 8000
          health_check_path: /health

  test_suites:
    integration:
      repo: someplace
      branch: main

    performance:
      repo: someplace
      branch: main

    penetration:
      repo: someplace
      branch: main

  environments:
    dev:
      - name: dev-a
        platform: eks
        location: us-east-1
        deployment_repo: someplace
        branch: main

      - name: dev-b
        platform: ecs
        location: us-east-2
        deployment_repo: someplace
        branch: main

        ecs:
          launch_type: fargate
          cluster: dev-b-cluster
          desired_count: 1
          assign_public_ip: true

    qa:
      - name: qa-a
        platform: ecs
        location: us-east-1
        deployment_repo: someplace
        branch: main

        ecs:
          launch_type: ec2
          cluster: qa-a-cluster
          desired_count: 2
          capacity_provider: qa-ec2-capacity-provider

        test_overrides:
          skip:
            - performance

    stage:
      - name: stage-a
        platform: aks
        location: eastus
        deployment_repo: someplace
        branch: main

        aks:
          resource_group: stage-platform-rg
          cluster_name: stage-a-aks

    prod:
      - name: prod-a
        platform: eks
        location: us-east-1
        deployment_repo: someplace
        branch: main

        test_overrides:
          skip:
            - performance
            - penetration
```

---

## Top-Level Options

| Option | Type | Required | Default | Description |
|---|---:|:---:|---|---|
| `workflow` | string | no | `cloud_workflow` | Identifies the workflow planner the orchestrator should use. |
| `release` | map | yes | none | Release-level declaration containing applications, environments, tests, and desired state. |

---

## `workflow`

The `workflow` field selects the orchestrator workflow implementation.

Current known workflow:

| Value | Description |
|---|---|
| `cloud_workflow` | Standard cloud build/deploy workflow for applications, environments, and test suites. |

Example:

```yaml
workflow: cloud_workflow
```

---

## `release`

The `release` block describes the release intent.

| Option | Type | Required | Default | Description |
|---|---:|:---:|---|---|
| `name` | string | yes | none | Logical release name. |
| `version` | string | no | none | Release version. Can be used for artifact tagging and traceability. |
| `desired_state` | map | no | workflow-defined | Declares desired environment and application state. |
| `applications` | map | no | `{}` | Application groups to build/deploy. |
| `test_suites` | map | no | `{}` | External test suite definitions. |
| `environments` | map | no | `{}` | Target environments grouped by lifecycle stage. |

Example:

```yaml
release:
  name: johnny-platform-release
  version: 1.0.0
```

---

## `release.desired_state`

The `desired_state` block expresses what the orchestrator should make true.

| Option | Type | Required | Default | Description |
|---|---:|:---:|---|---|
| `environment` | string | no | `present` | Desired infrastructure/environment state. |
| `applications` | string | no | `deployed` | Desired application state. |

### `desired_state.environment`

| Value | Description |
|---|---|
| `present` | Environment should exist. |
| `absent` | Environment should be removed or destroyed. |
| `ephemeral` | Environment should be created temporarily for this execution and then cleaned up. |

### `desired_state.applications`

| Value | Description |
|---|---|
| `deployed` | Applications should be built and deployed. |
| `built` | Applications should be built but not deployed. |
| `none` | No application deployment should occur. Useful with environment teardown or infrastructure-only flows. |

Examples:

```yaml
desired_state:
  environment: present
  applications: deployed
```

```yaml
desired_state:
  environment: absent
  applications: none
```

```yaml
desired_state:
  environment: present
  applications: built
```

---

## `release.applications`

The `applications` block groups application declarations by technology family.

Current supported groups:

| Group | Description |
|---|---|
| `spring` | Java/Spring Boot applications. |
| `node` | Node.js or frontend applications. |
| `python` | Python applications such as FastAPI services. |

Each group contains a list of applications.

### Common Application Options

| Option | Type | Required | Default | Description |
|---|---:|:---:|---|---|
| `name` | string | yes | none | Application name. |
| `repo` | string | yes | none | Source repository URL. |
| `branch` | string | no | `main` | Source branch. |
| `version` | string | no | release version | Application version override. |
| `build` | map | yes | none | Build instructions. |
| `runtime` | map | no | `{}` | Runtime metadata used for deployment and health checks. |

Example:

```yaml
applications:
  spring:
    - name: first-app
      repo: someplace
      branch: main
      version: 1.0.0
      build:
        type: maven-docker
        project_path: .
        dockerfile: Dockerfile
        artifact_target: ecr
      runtime:
        container_port: 8080
        health_check_path: /actuator/health
```

---

## `release.applications.*.build`

The `build` block defines how an application artifact or container image should be produced.

| Option | Type | Required | Default | Description |
|---|---:|:---:|---|---|
| `type` | string | yes | none | Build strategy. |
| `framework` | string | no | none | Optional framework hint, such as `fastapi`. |
| `project_path` | string | no | `.` | Path within the repository where build commands should run. |
| `dockerfile` | string | no | `Dockerfile` | Dockerfile path relative to `project_path` or repository root depending on workflow implementation. |
| `artifact_target` | string | no | workflow-defined | Artifact destination, such as `ecr`. |

### Known Build Types

| Value | Description |
|---|---|
| `maven-docker` | Build a Java/Maven project and produce a Docker image. |
| `docker` | Build a Docker image directly. |
| `python-docker` | Build a Python application Docker image. |

Example:

```yaml
build:
  type: python-docker
  framework: fastapi
  project_path: .
  dockerfile: Dockerfile
  artifact_target: ecr
```

---

## `release.applications.*.runtime`

The `runtime` block provides deployment-time application metadata.

| Option | Type | Required | Default | Description |
|---|---:|:---:|---|---|
| `container_port` | integer | no | workflow-defined | Container port exposed by the application. |
| `health_check_path` | string | no | workflow-defined | HTTP health check path. |

Example:

```yaml
runtime:
  container_port: 8080
  health_check_path: /actuator/health
```

---

## `release.test_suites`

The `test_suites` block defines external test repositories or suites that the platform may run as part of a workflow.

| Option | Type | Required | Default | Description |
|---|---:|:---:|---|---|
| `integration` | map | no | none | Integration test suite definition. |
| `performance` | map | no | none | Performance test suite definition. |
| `penetration` | map | no | none | Penetration/security test suite definition. |

### Test Suite Options

| Option | Type | Required | Default | Description |
|---|---:|:---:|---|---|
| `repo` | string | yes | none | Test suite repository URL. |
| `branch` | string | no | `main` | Test suite branch. |

Example:

```yaml
test_suites:
  integration:
    repo: someplace
    branch: main

  performance:
    repo: someplace
    branch: main

  penetration:
    repo: someplace
    branch: main
```

---

## `release.environments`

The `environments` block declares deployment targets grouped by lifecycle stage.

Common lifecycle groups:

| Group | Description |
|---|---|
| `dev` | Development environments. |
| `qa` | QA/test environments. |
| `stage` | Staging/pre-production environments. |
| `prod` | Production environments. |

Each lifecycle group contains a list of environment definitions.

### Common Environment Options

| Option | Type | Required | Default | Description |
|---|---:|:---:|---|---|
| `name` | string | yes | none | Environment name. |
| `platform` | string | yes | none | Target platform such as `eks`, `ecs`, or `aks`. |
| `location` | string | yes | none | Cloud region or location. |
| `deployment_repo` | string | no | none | Repository containing deployment manifests or IaC definitions. |
| `branch` | string | no | `main` | Deployment repository branch. |
| `ecs` | map | no | none | ECS-specific settings. Required when `platform: ecs` needs ECS-specific behavior. |
| `aks` | map | no | none | AKS-specific settings. Required when `platform: aks` needs AKS-specific behavior. |
| `test_overrides` | map | no | none | Environment-specific test behavior overrides. |

### Known Platform Values

| Value | Description |
|---|---|
| `eks` | Amazon Elastic Kubernetes Service. |
| `ecs` | Amazon Elastic Container Service. Use `ecs.launch_type` to distinguish Fargate vs EC2. |
| `aks` | Azure Kubernetes Service. |

Example:

```yaml
environments:
  dev:
    - name: dev-a
      platform: eks
      location: us-east-1
      deployment_repo: someplace
      branch: main
```

---

## `release.environments.*.ecs`

The `ecs` block configures ECS-specific behavior.

| Option | Type | Required | Default | Description |
|---|---:|:---:|---|---|
| `launch_type` | string | yes | none | ECS launch type, usually `fargate` or `ec2`. |
| `cluster` | string | yes | none | ECS cluster name. |
| `desired_count` | integer | no | `1` | Desired number of running tasks. |
| `assign_public_ip` | boolean | no | workflow-defined | Whether Fargate tasks should receive a public IP. Most relevant for public-subnet Fargate demos. |
| `capacity_provider` | string | no | none | ECS capacity provider. Most relevant for ECS on EC2. |

### `ecs.launch_type`

| Value | Description |
|---|---|
| `fargate` | Run ECS tasks on AWS Fargate. |
| `ec2` | Run ECS tasks on ECS-managed EC2 capacity. |

Fargate example:

```yaml
platform: ecs
location: us-east-2
ecs:
  launch_type: fargate
  cluster: dev-b-cluster
  desired_count: 1
  assign_public_ip: true
```

EC2 example:

```yaml
platform: ecs
location: us-east-1
ecs:
  launch_type: ec2
  cluster: qa-a-cluster
  desired_count: 2
  capacity_provider: qa-ec2-capacity-provider
```

---

## `release.environments.*.aks`

The `aks` block configures Azure Kubernetes Service-specific behavior.

| Option | Type | Required | Default | Description |
|---|---:|:---:|---|---|
| `resource_group` | string | yes | none | Azure resource group containing the AKS cluster. |
| `cluster_name` | string | yes | none | AKS cluster name. |

Example:

```yaml
platform: aks
location: eastus
aks:
  resource_group: stage-platform-rg
  cluster_name: stage-a-aks
```

---

## `release.environments.*.test_overrides`

The `test_overrides` block allows an environment to override the default platform test policy.

| Option | Type | Required | Default | Description |
|---|---:|:---:|---|---|
| `skip` | list | no | `[]` | Test suite names to skip in this environment. |

Example:

```yaml
test_overrides:
  skip:
    - performance
    - penetration
```

---

## Feature Flags

Feature flags may be provided to the Jenkins runtime request envelope or, in the future, as part of orchestrator metadata.

Preferred JSON request format:

```json
{
  "executionId": "e79267f6-2b76-472f-844f-af12b3902d21",
  "orchestratorUrl": "http://orchestrator.styxcd.com",
  "callbackUrl": "http://orchestrator.styxcd.com/executions/e79267f6-2b76-472f-844f-af12b3902d21/callback",
  "feature_flags": [
    "fftest"
  ]
}
```

Current known feature flags:

| Flag | Description |
|---|---|
| `fftest` | Test feature flag used to validate feature flag plumbing. |

---

## Planning and Execution Responsibilities

### Orchestrator Responsibilities

The orchestrator owns:

- YML parsing
- YML validation
- workflow-specific planning
- execution plan creation
- persistence
- execution status
- callback endpoint ownership
- future dashboard/API visibility

### Jenkins Runtime Responsibilities

Jenkins owns:

- receiving `STYXCD_REQUEST`
- parsing the execution request JSON
- fetching `/executions/{executionId}/plan` from the orchestrator
- executing the returned stage plan
- running final/cleanup stages
- sending logs/status/callbacks back to the orchestrator

---

## Notes

- `platform: ecs` should be paired with `ecs.launch_type` to distinguish `fargate` from `ec2`.
- `desired_state` is intentionally declarative. The orchestrator determines the concrete stages needed to reach that state.
- Environment-specific test behavior should generally be controlled by platform policy, with `test_overrides` used only when necessary.
- Stage names using suffixes such as `@final` are execution-plan details, not YML authoring concerns.
