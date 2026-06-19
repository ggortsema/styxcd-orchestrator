# StyxCD Execution Flow Reference

Date: 2026-05-16

## Current Runtime Flow

```text
1. User submits YML from dashboard or API.
2. Orchestrator parses YML.
3. Orchestrator builds execution plan.
4. Orchestrator saves Execution with status CREATED.
5. Orchestrator calls ExecutionRunner.start(execution).
6. JenkinsExecutionRunner builds STYXCD_REQUEST.
7. Orchestrator triggers Jenkins buildWithParameters.
8. Orchestrator updates execution status to TRIGGERED.
9. Jenkins starts and parses STYXCD_REQUEST.
10. Jenkins fetches execution plan from /executions/{id}/plan.
11. Jenkins splits normal stages from @final stages.
12. Jenkins runs normal stages.
13. Jenkins always runs final stages.
14. Initialize sends STARTED callback.
15. Cleanup sends SUCCESS or FAILED callback.
16. Orchestrator persists final status.
17. Dashboard polls /executions/{id} and shows status changes.
```

## Endpoints

### Create Execution

```http
POST /executions
Content-Type: text/plain
```

Body:

```yaml
workflow: cloud_workflow
release:
  name: styxcd-jenkins-build
  version: 1.0.0

  applications:
    spring:
      - name: styxcd-jenkins
        repo: https://github.com/ggortsema/styxcd-jenkins.git
        branch: main
        build_tool: gradle
```

Result:

```json
{
  "id": "uuid",
  "workflow": "cloud_workflow",
  "status": "TRIGGERED",
  "rawYml": "...",
  "executionPlan": { },
  "createdAt": "..."
}
```

### Get Execution

```http
GET /executions/{id}
```

Used by dashboard polling.

### Get Execution Plan

```http
GET /executions/{id}/plan
```

Used by Jenkins runtime.

### Callback

```http
POST /executions/{id}/callback
Content-Type: application/json
```

Payload:

```json
{
  "executionId": "uuid",
  "status": "STARTED",
  "message": "Jenkins execution started"
}
```

## Jenkins Runtime Parameter

Jenkins job parameter:

```text
STYXCD_REQUEST
```

Example:

```json
{
  "executionId": "uuid",
  "orchestratorUrl": "http://orchestrator.styxcd.com",
  "callbackUrl": "http://orchestrator.styxcd.com/executions/uuid/callback",
  "feature_flags": ["fftest"]
}
```

## Jenkins Job

Current job:

```text
external-input-orchestrator-test-json
```

The Jenkins pipeline script is intentionally thin:

```groovy
library identifier: 'styxcd-jenkins@main',
        retriever: modernSCM([
            $class: 'GitSCMSource',
            remote: 'https://github.com/ggortsema/styxcd-jenkins.git'
        ])

scdj {}
```

## Callback Failure Policy

Current policy:

```text
Callback failures should warn, not fail the Jenkins pipeline.
```

Reason:

- plan fetch failure should fail execution
- callback failure is currently treated as control-plane reporting failure
- future improvements can add retries, event spooling, or reconciliation

## Final Stage Policy

Stages with `@final` run in the Jenkins `finally` flow.

Example:

```text
CloudWorkflowCleanup@final
```

The cleanup stage is responsible for:

- final metrics/logging
- final callback status
- cleanup logic
