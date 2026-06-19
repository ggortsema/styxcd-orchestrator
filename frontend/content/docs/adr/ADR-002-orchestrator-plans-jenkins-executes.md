# ADR-002: Keep Jenkins execution-focused and move YAML parsing/planning into orchestrator

## Status
Draft / Accepted

## Context
The original Groovy model had stage classes that both determined parameters and executed stage logic. In the newer architecture, the orchestrator is responsible for planning and Jenkins is responsible for execution.

## Decision
Jenkins should not parse high-level YAML or infer release topology. Jenkins should receive a stage payload that is already ready to execute.

Jenkins stages should primarily:

- print/inspect parameters during skeleton testing
- call build/deploy tooling
- send lifecycle callbacks
- emit structured logs

## Consequences
This creates a clean separation of concerns. It also means stage payloads should eventually include derived fields such as namespace, project ID, cluster name, image tag, service name, ingress name, and DNS hostname so Jenkins does not need to rediscover them.
