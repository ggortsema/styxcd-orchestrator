# Project Management Workflow Backlog

This document is the human-readable backlog view generated from a StyxCD `pm_workflow` YAML definition.

## Provider

- **Type:** github
- **Repository:** ggortsema/styxcd-docs
- **Project:** mycroftai engineering roadmap
- **Jenkins Creds:** mycred

## Epics

## Epic: Project Management Workflow Provider

Implement a StyxCD workflow that can create, update, synchronize, and migrate roadmap items across project-management providers using YAML as the durable source of truth.

### Acceptance Criteria

- Project-management workflows can be represented as YAML.
- GitHub Issues and GitHub Projects are supported as the first provider target.
- Epics and child issues can be created from YAML.
- Workflow execution supports dry-run and idempotency.
- Provider issue IDs and URLs can be synchronized back into YAML.
- Provider migration is possible without losing the roadmap-as-code source.

### Issues

#### Define PM Workflow DSL

Define the YAML structure for project-management workflows, including provider configuration, epics, issues, labels, assignees, milestones, acceptance criteria, and provider metadata.

**Acceptance Criteria**

- PM workflow YAML schema is documented.
- Epics and issues are represented clearly.
- Optional fields such as labels, assignees, and milestones are supported.
- Provider-specific metadata can be stored without polluting the portable model.

#### Implement GitHub Issues Provider

Implement the first PM workflow provider using the GitHub API to create roadmap issues in the configured repository.

**Acceptance Criteria**

- GitHub provider can authenticate using Jenkins credentials.
- Provider can create epic issues.
- Provider can create child issues.
- Provider can apply labels, assignees, and milestones when supplied.
- Provider errors are reported clearly.

#### Support Epic and Sub-Issue Creation

Create parent epic issues and attach child issues using GitHub's issue hierarchy or supported relationship model.

**Acceptance Criteria**

- Epic issue is created first.
- Child issues are created under the correct epic.
- Parent-child relationships are preserved.
- Created items are added to the configured project board.

#### Support GitHub Project Assignment

Add created or updated issues to the configured GitHub Project board.

**Acceptance Criteria**

- Project can be identified from YAML configuration.
- Created epics are added to the project.
- Created issues are added to the project.
- Default status can be set when supported.

#### Add PM Workflow Dry-Run Mode

Provide a dry-run mode that previews create, update, no-op, and error actions without modifying the provider.

**Acceptance Criteria**

- Dry-run mode performs no writes.
- Dry-run output lists epics and issues that would be created.
- Dry-run output lists items that would be updated.
- Dry-run output identifies potential duplicates or conflicts.

#### Support PM Workflow Idempotency

Ensure rerunning a PM workflow does not create duplicate epics or issues when matching items already exist.

**Acceptance Criteria**

- Existing epics are detected.
- Existing issues are detected.
- Matching can use provider ID when available.
- Matching can fall back to title when ID is missing.
- Duplicate creation is prevented.
- Dry-run output reports create/update/no-op actions.

#### Synchronize PM Workflow YAML From Provider

Read existing epics and issues from a project-management provider and update local PM workflow YAML with provider identifiers, links, and status metadata.

**Acceptance Criteria**

- Existing provider issues can be fetched.
- Issues are matched to YAML entries by title.
- Provider issue IDs are written back to YAML.
- Provider URLs are written back to YAML.
- Existing YAML descriptions are preserved.
- Missing or renamed issues are reported.

#### Support Provider Metadata in PM Workflow YAML

Store provider-specific identifiers separately so the YAML remains portable across GitHub, Linear, ClickUp, Jira, or future providers.

**Acceptance Criteria**

- Provider-specific IDs are stored under provider metadata.
- Portable fields remain provider-neutral.
- Multiple provider mappings can be represented.
- Provider metadata structure is documented.

#### Support Provider Migration for PM Workflows

Allow the same roadmap YAML to be projected into a different project-management provider.

**Acceptance Criteria**

- PM workflow provider model is abstracted.
- Provider-specific IDs are stored separately.
- YAML can support multiple provider mappings.
- Roadmap can be recreated in a new provider.
- Migration process is documented.

#### Generate PM Workflow YAML From Conversation

Use an AI-assisted step to transform roadmap discussion or session notes into PM workflow YAML.

**Acceptance Criteria**

- Conversation/session notes can be converted into PM workflow YAML.
- Generated YAML follows the PM workflow schema.
- Generated YAML can be reviewed before execution.
- Generated YAML can be committed to styxcd-docs.

#### Demonstrate Backlog-as-Code Workflow

Use StyxCD to create or update a real project-management backlog from committed PM workflow YAML.

**Acceptance Criteria**

- PM workflow YAML exists in styxcd-docs.
- Workflow can be executed through StyxCD.
- GitHub issues are created or updated.
- Execution output shows create/update/no-op results.
- Demo is documented.

