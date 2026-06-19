
# StyxCD Decision Notes - 2026-05-18

## Decisions Finalized

### Keep workflow planners in Groovy
Reason:
- YAML ergonomics
- orchestration readability
- fast iteration

### Keep stage planners in Java
Reason:
- typed contracts
- Spring registry integration
- testability
- long-term maintainability

### Remove YAML orchestration from Jenkins
Reason:
- cleaner separation of concerns
- future runtime portability
- improved execution determinism

### Use namespaced execution keys
Format:
```text
workflow:stage_type:target
```

Reason:
- collision prevention
- observability clarity
- scalable orchestration identifiers

### StageWrapper performs normalization
Reason:
- stable implementation lookup
- preserve concrete execution identity

### Temporarily use json instead of jsonb
Reason:
- preserve ordering during refactor
- avoid premature execution-order metadata implementation

### Future execution plan becomes ordered list
Reason:
- explicit execution semantics
- future DAG support
- persistence portability
- deterministic replay

---

# Immediate Next Steps

1. Add TerraformApply Jenkins stage
2. Wire terraform_apply planner into workflow
3. Validate terraform execution-plan flow
4. Echo Terraform params in Jenkins
5. Add Terraform repo checkout/init/apply later

