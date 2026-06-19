# StyxCD Control Plane

## Role

The StyxCD control plane is the system of record for release intent, execution plans, and execution state.

It should be treated as a product/service, not as a helper script around Jenkins.

## Responsibilities

- accept execution requests
- store raw submitted YAML
- parse the YAML into internal maps/models
- generate execution plans
- store execution plans
- expose plan retrieval endpoints
- receive runtime callbacks
- maintain current execution state
- expose data for UI/API consumers

## Non-responsibilities

The control plane should not directly run shell commands for platform changes in the current architecture. That belongs to execution runtimes such as Jenkins.

## Runtime boundary

The runtime boundary is the execution plan.

```text
Control plane: planning, state, API
Runtime: execution, logs, callbacks
```

This boundary allows Jenkins to be replaced or supplemented later by other execution agents without rewriting the intent model.
