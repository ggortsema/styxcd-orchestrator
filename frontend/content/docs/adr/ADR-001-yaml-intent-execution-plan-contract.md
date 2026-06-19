# ADR-001: Use YAML intent as input and execution plan map as runtime contract

## Status
Draft / Accepted in current implementation direction

## Context
StyxCD accepts a YAML release definition from the user. The orchestrator parses the YAML and generates a structured execution plan that Jenkins can consume. The execution plan map is the boundary between planning and execution.

Earlier workflow logic mixed YAML interpretation, stage decisions, and Jenkins execution more closely. The current architecture separates those concerns.

## Decision
Use user-authored YAML as the intent document and a generated execution plan map as the stable runtime contract.

The orchestrator owns:

- parsing YAML
- interpreting release/application/environment intent
- deciding which stages exist
- deriving Jenkins-ready stage parameters
- producing the execution plan map

Jenkins owns:

- reading the execution plan
- dispatching stage implementations by stage type/key
- executing shell/Groovy/gcloud/kubectl/terraform/etc.
- sending callbacks and logs

## Consequences
The execution plan becomes the stable internal model. Future hardcoded planners, workflow templates, or a DSL engine can all generate the same plan shape without changing Jenkins execution semantics.

This keeps Jenkins from needing to understand the full YAML shape.
