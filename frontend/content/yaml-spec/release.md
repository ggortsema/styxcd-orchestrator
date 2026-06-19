# Release

The `release` block describes the logical release being executed.

```yaml
release:
  name: johnny-platform-release
  version: 1.0.0
```

## Desired State

`desired_state` describes the outcome StyxCD should plan toward.

```yaml
desired_state:
  environment: present
  applications: deployed
```

## Fields

| Field | Required | Description |
|---|---:|---|
| name | Yes | Logical release name |
| version | Yes | Release version |
| desired_state.environment | Yes | Desired environment state |
| desired_state.applications | Yes | Desired application state |

## Example Desired States

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
  environment: ephemeral
  applications: deployed
```

```yaml
desired_state:
  environment: present
  applications: built
```