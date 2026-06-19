# ADR-009: Treat GKE authentication as utility behavior, not a workflow stage

## Status
Draft / Accepted

## Context
The GKE skeleton initially considered whether authentication should be represented as a stage.

Authentication is necessary for GKE commands, but it is not itself a release lifecycle step that users should reason about as part of application deployment.

## Decision
Do not create a GkeAuthenticate stage.

Authentication should be handled as utility/setup behavior inside Jenkins/shared library support code or inside the stages that need it.

## Consequences
The user-facing workflow remains focused on deployment lifecycle concepts rather than implementation mechanics.
