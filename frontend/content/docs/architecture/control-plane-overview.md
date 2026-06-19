# Control Plane Overview

StyxCD is built around an orchestrator-first control plane. The documentation repository remains the source of truth, while the orchestrator packages and exposes the docs for the dashboard.

## Responsibilities

- Accept workflow requests
- Build execution plans
- Track callbacks from Jenkins stages
- Expose documentation and platform state to the UI

## Current publishing shape

```text
styxcd-docs
  -> orchestrator build
  -> docs API
  -> frontend viewer
```

> This is placeholder content. The next step is to replace this dummy document with the real markdown pulled from the docs repository.
