# ADR-008: Use __final__ as reserved final-stage marker

## Status
Draft / Accepted, migration pending in Jenkins as needed

## Context
Cleanup/final stages need to be identifiable as stages executed from Jenkins final/finally behavior.

Using a plain `final` suffix risks accidental matching or ambiguity.

## Decision
Use `__final__` as the reserved marker for stages that should run from final/finally behavior.

Example:

```text
CloudWorkflowCleanup@__final__
```

## Consequences
Jenkins stage dispatch logic must recognize the new marker. Migration should happen in orchestrator and Jenkins together, unless intentionally staged.
