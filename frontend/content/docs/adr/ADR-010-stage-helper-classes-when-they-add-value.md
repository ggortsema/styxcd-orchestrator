# ADR-010: Use stage helper classes when they encapsulate stage contract logic

## Status
Draft / Accepted

## Context
The original stage classes had two responsibilities: building stage parameters and executing stage code. In the orchestrator, execution moved to Jenkins, leaving some stage classes that only returned parameters already built by the workflow.

## Decision
Stage helper classes are allowed but not mandatory.

Use a stage helper class when it owns meaningful stage contract logic, such as:

- stage key/name construction
- Jenkins label/agent selection
- stage type
- defaults
- derived values
- validation
- parameter shape for a stage

Do not require a class per stage when it only echoes back an already-built map.

## Consequences
The workflow decides when a stage exists. The stage class, when present, decides how that stage is represented. Jenkins decides how the stage executes.

This keeps helper classes useful without requiring ceremony everywhere.
