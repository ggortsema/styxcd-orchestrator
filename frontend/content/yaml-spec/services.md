# Services

The `services` block describes cloud or platform resources required by the release.

Services are resources that applications may depend on, but that are not themselves application deployments.

Examples include:

- Message queues
- Notification topics
- Databases
- Buckets
- Caches
- Platform controllers

Services live at the release level because they are provisioned as part of the release environment, not owned exclusively by one application.

```yaml
services:
  - name: johnny-events-queue
    provider: aws
    type: sqs
```

## Design Intent

The `services` block answers:

```text
What supporting resources does this release require?
```

Applications may later reference those services, but the service itself is declared once.

```yaml
services:
  - name: johnny-events-queue
    provider: aws
    type: sqs
    environments:
      - sandbox
```

## AWS Example

```yaml
services:
  - name: johnny-events-queue
    provider: aws
    type: sqs
    environments:
      - sandbox
    config:
      fifo: false
      visibility_timeout_seconds: 30

  - name: johnny-notifications-topic
    provider: aws
    type: sns
    environments:
      - sandbox
```

## Application References

Applications may reference services they require.

```yaml
applications:
  spring:
    - name: johnny-johnny-backend
      services:
        - johnny-events-queue
        - johnny-notifications-topic
```

This allows StyxCD to understand both sides of the relationship:

| Area | Responsibility |
|---|---|
| `services` | Defines resources to provision |
| `applications[*].services` | Defines resources an application consumes |

## Fields

| Field | Required | Description |
|---|---:|---|
| name | Yes | Logical service name |
| provider | Yes | Cloud or platform provider |
| type | Yes | Service type |
| environments | No | Environments where the service should exist |
| config | No | Provider-specific service configuration |

## Provider Notes

Service configuration is provider-specific.

For example, AWS SQS, Google Pub/Sub, and Azure Service Bus may all represent messaging resources, but they do not have identical configuration fields.

The common model should stay small:

```yaml
name:
provider:
type:
environments:
config:
```

Provider-specific details should live under `config`.