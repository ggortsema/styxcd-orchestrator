# ADR-015: Capture project-management workflow as a future non-deployment capability

## Status
Future backlog idea

## Context
StyxCD's orchestrator model can apply to non-deployment workflows as well. A future project-management workflow could read YAML, markdown, session notes, Jenkins failures, or TODOs and create/update tasks in systems such as GitHub Issues, Jira, ClickUp, or Linear.

## Decision
Treat project-management workflow as a separate future capability, not part of the current GKE implementation.

Potential behavior:

- parse messy notes/docs/YAML comments
- deduplicate work items
- group/prioritize work
- generate acceptance criteria
- create or update provider-specific tasks

## Consequences
This reinforces that StyxCD is becoming an orchestration platform, not only a deployment tool. It also provides future evidence for workflow abstraction/productization if multiple workflow types converge on the same execution plan contract.
