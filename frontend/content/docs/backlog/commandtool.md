# Command Tool Backlog

This document is the human-readable backlog view generated from a StyxCD `pm_workflow` YAML definition.

## Epics

## Epic: StyxCD Command Line Tool

Create a cross-platform command line interface for StyxCD that provides a developer-friendly interface to the orchestrator and workflow execution system.

### Issues

#### Implement UI Parity Command Using Ferry Verb

Create the initial StyxCD CLI command that provides parity with the current Next.js operations UI. The command should submit workflow YAML to the orchestrator and return execution metadata including execution ID, status, Jenkins links, Grafana links, and future execution details.

**Acceptance Criteria**

- CLI supports: styxcd ferry -f workflow.yml
- Workflow YAML can be submitted to the orchestrator.
- Execution ID is returned to the user.
- Execution status is returned to the user.
- Jenkins execution URL is returned when available.
- Grafana log URL is returned when available.
- Response can be displayed as structured JSON.
- Command behavior matches the current operations UI submission flow.

