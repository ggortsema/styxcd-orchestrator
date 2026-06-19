# Testing

The `testing` block describes validation that should run as part of a StyxCD workflow.

Testing is part of the release contract because tests help determine whether the desired state has been safely reached.

```yaml
testing:
  behavior:
    enabled: true

  performance:
    enabled: false
```

## Design Intent

The `testing` block answers:

```text
How should StyxCD validate this release?
```

Testing may occur at different points in the workflow:

- Before deployment
- After deployment
- After ingress or DNS is available
- Before promotion
- During rollback validation

## Behavior Tests

Behavior tests validate that the deployed system behaves correctly from a user or API perspective.

```yaml
testing:
  behavior:
    enabled: true
    framework: postman
    source:
      type: repo
      repo: https://github.com/ggortsema/johnny-johnny-tests.git
      branch: main
      path: behavior
    environments:
      - sandbox
```

## Behavior Test Fields

| Field | Required | Description |
|---|---:|---|
| enabled | Yes | Whether behavior tests should run |
| framework | No | Test framework or runner |
| source | No | Where test definitions are stored |
| environments | No | Environments where tests apply |

## Performance Tests

Performance tests validate system capacity, latency, or throughput.

```yaml
testing:
  performance:
    enabled: true
    framework: k6
    source:
      type: repo
      repo: https://github.com/ggortsema/johnny-johnny-tests.git
      branch: main
      path: performance
    thresholds:
      p95_latency_ms: 500
      error_rate_percent: 1
    environments:
      - sandbox
```

## Performance Test Fields

| Field | Required | Description |
|---|---:|---|
| enabled | Yes | Whether performance tests should run |
| framework | No | Test framework or runner |
| source | No | Where test definitions are stored |
| thresholds | No | Required performance thresholds |
| environments | No | Environments where tests apply |

## Starter Example

```yaml
testing:
  behavior:
    enabled: true
    framework: postman
    source:
      type: repo
      repo: https://github.com/ggortsema/johnny-johnny-tests.git
      branch: main
      path: behavior
    environments:
      - sandbox

  performance:
    enabled: true
    framework: k6
    source:
      type: repo
      repo: https://github.com/ggortsema/johnny-johnny-tests.git
      branch: main
      path: performance
    thresholds:
      p95_latency_ms: 500
      error_rate_percent: 1
    environments:
      - sandbox
```

## Notes

The testing model is intentionally early.

Behavior tests and performance tests may eventually become separate execution phases in the StyxCD plan.

For now, the specification treats them as release-level validation requirements.