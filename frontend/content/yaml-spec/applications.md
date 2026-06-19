# Applications

The `applications` block defines source applications and build outputs.

Applications are grouped by application type.

```yaml
applications:
  spring:
    - name: johnny-johnny-backend

  node:
    - name: johnny-johnny-ui
```

## Spring Application

```yaml
spring:
  - name: johnny-johnny-backend
    repo: https://github.com/ggortsema/johnny-johnny.git
    branch: main
    version: 1.0.0

    build:
      type: maven-docker
      project_path: .
      module_path: chat-api
      dockerfile: chat-api/Dockerfile
      docker_context: .

    artifacts:
      - type: docker-image
        image: us-east1-docker.pkg.dev/styxcd-sandbox-grant/styxcd-sandbox/johnny-johnny-backend:latest
```

## Node Application

```yaml
node:
  - name: johnny-johnny-ui
    repo: https://github.com/ggortsema/johnny-johnny-ui.git
    branch: main
    version: 1.0.0

    build:
      type: docker
      project_path: .
      dockerfile: Dockerfile
      docker_context: .

    artifacts:
      - type: docker-image
        image: us-east1-docker.pkg.dev/styxcd-sandbox-grant/styxcd-sandbox/johnny-johnny-ui:latest
```

## Fields

| Field | Required | Description |
|---|---:|---|
| name | Yes | Application name |
| repo | Yes | Source repository |
| branch | Yes | Source branch |
| version | Yes | Application version |
| build.type | Yes | Build strategy |
| artifacts | Yes | Build outputs |