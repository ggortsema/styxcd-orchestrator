# ADR-0XX Command Line Interface as First-Class Control Plane

## Status

Future

## Context

StyxCD currently exposes a web-based operations interface and an orchestration API.

While the UI is useful for exploration, development, and operational visibility, many platform engineers prefer interacting with deployment systems from a terminal.

Examples include:

- Terraform
- kubectl
- helm
- aws cli
- gcloud
- ansible

A command line interface would provide an alternative control plane experience while reusing the existing orchestrator APIs.

## Decision

StyxCD will eventually provide a dedicated command line interface.

The command line interface will be considered a first-class client of the orchestrator rather than a separate deployment engine.

The CLI will invoke orchestrator APIs and display execution results to the user.

Example:

```bash
styxcd ferry -f workflow.yml
```

Expected response:

```text
Execution Started

Execution ID:
7e6c8d2a

Environment:
sandbox

Application:
johnny-johnny-backend

Links:
Jenkins: https://...
Grafana: https://...
Execution: https://...
```

The CLI will not perform deployments directly.

The orchestrator remains the system of record and execution authority.

## Rationale

### Developer Experience

Many platform engineers spend most of their day in a terminal.

A CLI allows rapid iteration without requiring a browser.

### Consistent Control Plane

The UI and CLI should produce equivalent outcomes.

Both become presentation layers over the same orchestration APIs.

### API Validation

A CLI forces the control plane APIs to remain complete and consumable.

Features that are only available through the UI are considered incomplete.

### Future Automation

A CLI can be embedded into:

- shell scripts
- local workflows
- GitHub Actions
- Jenkins pipelines
- future AI agents

without introducing additional deployment logic.

## Language Selection

The initial implementation language remains undecided.

Candidate languages include:

### Go

Advantages:

- single static binary
- cross-platform distribution
- common choice for platform tooling

### Python

Advantages:

- rapid development
- strong ecosystem
- reuse of future Kharon and Johnny-Johnny capabilities

Language selection will be deferred until implementation begins.

## Consequences

The orchestrator API becomes the primary contract.

Future UI and CLI capabilities should be implemented through shared APIs.

The command line experience should maintain feature parity with the operations UI whenever practical.

## Notes

The command:

```bash
styxcd ferry -f workflow.yml
```

represents the current preferred direction.

The CLI is not a deployment engine.

The CLI is a first-class control plane client.
