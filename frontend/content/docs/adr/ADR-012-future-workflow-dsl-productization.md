# ADR-012: Keep execution plan stable while allowing future workflow DSL/productization

## Status
Future product evolution idea, not current backlog

## Context
StyxCD can currently support multiple workflows by adding orchestrator code, such as:

- cloud_workflow
- project_management_workflow
- infrastructure workflow
- Akamai workflow

This is powerful for the builder, but customers should not need to compile the product to add or compose workflows.

## Decision
Do not build a workflow DSL now.

Keep the execution plan as the stable runtime contract so that future workflow definitions, templates, or DSL engines can generate the same plan.

Possible future maturity path:

1. hardcoded workflows
2. configurable workflows using known stage types
3. plugin/provider stage packs
4. full workflow DSL/engine with scopes, conditions, dependencies, and templates

## Consequences
Current work should continue to build real workflows first. Later productization can extract abstractions from actual repeated patterns instead of guessing.
