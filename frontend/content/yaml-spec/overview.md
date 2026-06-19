# YAML Specification

The StyxCD YAML specification describes the desired workflow state.

A workflow author describes what should exist, what should be built, and where it should run. StyxCD turns that YAML into an execution plan.

```yaml
workflow: cloud_workflow
```

The current core areas are:

- `workflow`
- `release`
- `applications`
- `environments`