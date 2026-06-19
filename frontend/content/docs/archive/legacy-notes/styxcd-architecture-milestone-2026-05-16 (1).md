# StyxCD Architecture Milestone - Orchestrator-Controlled Jenkins Runtime

Date: 2026-05-16

## Summary

This session completed a major StyxCD architecture transition.

StyxCD now has a functioning control-plane loop:

```text
Dashboard
→ Orchestrator
→ persisted execution plan
→ Jenkins runtime trigger
→ Jenkins execution
→ lifecycle callbacks
→ persisted execution status
→ dashboard polling
```

The system moved from Jenkins-centered orchestration to an orchestrator-centered control plane.

## Final Responsibility Boundary

### Orchestrator Responsibilities

The orchestrator is now responsible for:

- accepting YML input
- parsing YML
- generating execution plans
- persisting execution records
- exposing execution plans to runtimes
- triggering execution runners
- accepting lifecycle callbacks
- maintaining execution status
- serving dashboard/API state

### Jenkins Responsibilities

Jenkins is now responsible for:

- receiving a single `STYXCD_REQUEST` parameter
- parsing the request envelope
- fetching the execution plan from the orchestrator
- executing stages from the execution plan
- sending lifecycle callbacks
- acting as a runtime agent only

Jenkins no longer owns:

- YML interpretation
- workflow planning
- stage parameter generation
- environment expansion
- execution state persistence

## Execution State Flow

Current execution statuses:

```text
CREATED
TRIGGERED
STARTED
SUCCESS
FAILED
```

Flow:

```text
POST /executions
→ CREATED
→ orchestrator triggers Jenkins
→ TRIGGERED
→ Jenkins initialize callback
→ STARTED
→ Jenkins cleanup callback
→ SUCCESS or FAILED
```

## Execution Plan Contract

Jenkins fetches plans from:

```http
GET /executions/<built-in function id>/plan
```

Plan shape:

```text
Map<String, Object>
```

Stage keys may include suffix metadata:

```text
CloudWorkflowInitialize
GradleBuild@styxcd-jenkins
CloudWorkflowCleanup@final
```

Jenkins strips suffixes before stage lookup:

```text
GradleBuild@styxcd-jenkins → GradleBuild
CloudWorkflowCleanup@final → CloudWorkflowCleanup
```

The `@final` suffix is used to place stages into final/cleanup execution.

## Runtime Request Contract

Jenkins receives a single multiline string parameter:

```text
STYXCD_REQUEST
```

Example:

```json
{
  "executionId": "855e2c90-8cc8-4148-a2e3-683f047d0178",
  "orchestratorUrl": "http://orchestrator.styxcd.com",
  "callbackUrl": "http://orchestrator.styxcd.com/executions/855e2c90-8cc8-4148-a2e3-683f047d0178/callback",
  "feature_flags": [
    "fftest"
  ]
}
```

## Callback Contract

Jenkins calls:

```http
POST /executions/<built-in function id>/callback
```

Payload:

```json
{
  "executionId": "855e2c90-8cc8-4148-a2e3-683f047d0178",
  "status": "STARTED",
  "message": "Jenkins execution started"
}
```

The callback DTO is implemented as a Java record.

## Java Record Decision

Records are used for immutable API DTOs such as:

```java
public record ExecutionCallbackRequest(
        String executionId,
        String status,
        String message
) {
}
```

Decision:

- Records are preferred for request/response DTOs and future event contracts.
- Records are not preferred for JPA entities.
- JPA entities remain normal classes with controlled mutation methods.

## Feature Flags

The Jenkins `FeatureFlags` class was refactored to support multiple input shapes:

- CSV string
- JSON array/list
- map of flag name to boolean
- null/no flags

Preferred future contract:

```json
"feature_flags": ["fftest"]
```

The constructor logic was kept inline because Jenkins CPS has problems with constructors calling instance helper methods.

## Legacy Planner Code

The old Jenkins workflow and `getParams()` planning logic was removed from the active Jenkins runtime codebase.

A copy was archived in the docs repository legacy area for later replication inside the orchestrator.

Removed from active Jenkins code:

- workflow files
- workflow maps
- stage `getParams()` methods
- Jenkins-side execution plan generation

Remaining in active Jenkins code:

- StageMap
- StageWrapper
- executable stage classes
- FeatureFlags
- runtime callbacks
- runtime execution helpers

## Dashboard Milestone

A static Next.js dashboard page was added.

Current behavior:

```text
YML textarea
→ Submit YML
→ POST /executions
→ display execution id/status
→ poll GET /executions/<built-in function id>
→ update console/status until SUCCESS or FAILED
```

Static Next.js can support this because API polling happens in the browser.

Future dashboard update option:

- short term: polling
- medium term: Server-Sent Events
- long term, if needed: WebSockets

SSE is preferred over WebSockets for most StyxCD status updates because execution updates are primarily one-way:

```text
orchestrator → browser
```

## Execution Runner Abstraction

The orchestrator now uses an execution runner seam.

Current concept:

```java
public interface ExecutionRunner {
    void start(Execution execution);
}
```

Current implementation:

```text
JenkinsExecutionRunner
```

Future implementations may include:

- message-driven runner
- RabbitMQ-based dispatcher
- parallel stage group runner
- ECS task runner
- GitHub Actions runner
- Temporal-like worker model

The design intentionally avoids naming the abstraction after Jenkins because future execution may involve:

```text
serial stages
parallel fan-out
join/wait logic
message-driven dispatch
multi-runtime execution
```

## Jenkins Trigger

The orchestrator triggers Jenkins using a Spring HTTP client.

Configuration is environment-backed:

```yaml
styxcd:
  jenkins:
    base-url: http://jenkins.styxcd.com:8080
    job-name: external-input-orchestrator-test-json
    username: ${JENKINS_USERNAME}
    api-token: ${JENKINS_API_TOKEN}
```

Systemd environment variables were added to:

```text
/etc/systemd/system/orchestrator.service
```

Then reloaded with:

```bash
sudo systemctl daemon-reload
sudo systemctl restart orchestrator
```

## Current Working End-to-End Loop

Validated successfully:

```text
Dashboard submits YML
→ Orchestrator creates execution
→ Orchestrator generates execution plan
→ Orchestrator persists execution
→ Orchestrator triggers Jenkins
→ Jenkins receives STYXCD_REQUEST
→ Jenkins fetches execution plan
→ Jenkins runs stages
→ Jenkins sends STARTED callback
→ Jenkins sends SUCCESS callback
→ Orchestrator persists final status
→ Dashboard polling observes status changes
```

This is the first fully working StyxCD control-plane loop.
