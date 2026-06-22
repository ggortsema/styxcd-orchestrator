# ADR-000X - YAML Specification as a First-Class Product Area

## Status

Accepted

## Decision

YAML Specification is a dedicated top-level product area separate from Documentation.

Navigation:

- Operations
- Documentation
- YAML Specification

## Rationale

Documentation explains concepts.

YAML Specification defines the platform contract.

Separating the two improves discoverability and allows the specification to evolve as a product artifact.

## Consequences

Benefits:

- Clear separation of responsibilities
- Cleaner navigation model
- Easier future tutorials and examples
- Stable publishing workflow

Tradeoffs:

- Additional viewer maintenance
- Separate navigation structure

## Implementation

Specification content is stored in:

styxcd-docs/yaml-spec

Navigation is controlled by:

spec-nav.yml

Pages are authored in Markdown.
