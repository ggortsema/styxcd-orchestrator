# StyxCD Session Context - 2026-05-16

## Major Milestone

Tonight StyxCD crossed an important architectural boundary:

```text
Jenkins-generated execution plans
```

moved toward:

```text
orchestrator-generated execution plans
```

Jenkins is now on the path to becoming a pure execution agent.

## What Was Proven

The orchestrator successfully generated an execution plan using:

- Spring service layer
- Groovy workflow planner
- Java stage parameter planners
- PostgreSQL persistence
- JSONB execution plan storage

Jenkins successfully retrieved an execution plan by `executionId` and ran the existing engine from the remotely generated plan.

## Validated Flow

```text
YML
  -> Orchestrator
  -> CloudWorkflowPlanner.groovy
  -> Java stage planners
  -> ExecutionPlanService
  -> persisted executionPlan
  -> GET /executions/{id}/plan
  -> Jenkins parses JSON
  -> Jenkins executes existing engine
```

## Important Execution ID Tested

```text
e79267f6-2b76-472f-844f-af12b3902d21
```

The execution plan contained:

```text
CloudWorkflowInitialize
GradleBuild@styxcd-jenkins
CloudWorkflowCleanup@final
```

## Current Architecture Direction

### Orchestrator Owns

```text
YML parsing
workflow planning
stage parameter generation
execution plan persistence
execution state
future callbacks/events
future dashboard state
```

### Jenkins Owns

```text
execution runtime
running concrete stages
reporting final status
future per-stage event reporting
```

## Key Boundary

```text
Stage planning belongs in orchestrator.
Stage execution belongs in execution agent.
```

## Planner Interface

A Java interface was introduced conceptually:

```java
package org.mycroftai.styxcd.orchestrator.workflow;

import java.util.Map;

public interface WorkflowPlanner {

    String workflowName();

    Map<String, Object> createJsonStageList(Map<String, Object> yml);
}
```

The reason for the interface:

- Java services depend on abstraction
- Groovy planners can implement it
- Spring can inject `List<WorkflowPlanner>`
- future workflow planners become plugin-like beans

## Current Workflow Planner

```text
org.mycroftai.styxcd.orchestrator.workflow.cloud.CloudWorkflowPlanner
```

It should implement:

```text
WorkflowPlanner
```

## Current Stage Planner Package

```text
org.mycroftai.styxcd.orchestrator.workflow.cloud.stage
```

Current Java stage planners:

```text
CloudWorkflowInitializeStagePlanner
CloudWorkflowGradleBuildStagePlanner
CloudWorkflowCleanupStagePlanner
```

These are Spring components.

## Current Plan Service Direction

`ExecutionPlanService` should move under execution-related packages, likely:

```text
org.mycroftai.styxcd.orchestrator.execution.plan
```

Instead of standalone:

```text
org.mycroftai.styxcd.orchestrator.plan
```

Reason:

The plan is now part of the execution lifecycle.

## Package Cleanup Done / Discussed

Removed or planned to remove:

```text
HelloController
dbhello package
yml package/controller
```

Reason:

The system has moved beyond demo/parsing endpoints into real execution orchestration.

## Current Execution API Direction

Primary API should become:

```text
POST /executions
GET /executions/{id}
GET /executions/{id}/plan
PATCH /executions/{id}/status
```

## Temporary Callback Endpoint Needed

Next likely implementation:

```http
PATCH /executions/{id}/status
```

Request:

```json
{
  "status": "SUCCEEDED"
}
```

Initial statuses:

```text
RUNNING
SUCCEEDED
FAILED
```

Jenkins should call this from cleanup/finally.

## Future Callback Model

Eventually replace simple status patch with event model:

```http
POST /executions/{id}/events
```

Future tables:

```text
execution_stage
execution_event
```

Jenkins reports facts.
Orchestrator decides state transitions.

## Callback URL Design Note

Do not permanently hardcode orchestrator URLs inside Jenkins.

Future Jenkins trigger may need to pass:

```text
executionId
planUrl
callbackUrl
eventUrl
auth/token metadata
```

## API Routing Module Note

Future module needed sooner rather than later:

```text
API Routing / Event Ingestion Module
```

Purpose:

```text
accept webhooks/events
parse/normalize input
route based on configuration
trigger orchestrator workflows
```

## Near-Term Next Steps

1. Add execution metadata.
2. Add orchestrator-triggered Jenkins.
3. Add temporary status callback.
4. Continue porting `getParams(...)` methods.
5. Continue porting workflow planners.
6. Delete planning logic from Jenkins after migration.
7. Add HTTPS/nginx soon.
8. Start correlated observability.

## Coding Style Preference Captured

For future generated code:

- compact constructor signatures when readable
- one instruction/action per line inside methods
- avoid unnecessary builder/fluent style
- optimize for operational readability and execution flow
- use explicit code over excessive vertical parameter lists unless needed

Example preferred style:

```java
public ExecutionController(ExecutionService executionService, ExecutionRepository executionRepository) {
    this.executionService = executionService;
    this.executionRepository = executionRepository;
}
```

## Important Conceptual Win

The execution plan is now the stable contract.

This makes future execution agents possible:

```text
Jenkins
XLR
Ansible Tower
ECS workers
Kubernetes runners
standalone Java agents
Python agents
```

The orchestrator becomes the brain.
Execution agents become replaceable runtimes.
