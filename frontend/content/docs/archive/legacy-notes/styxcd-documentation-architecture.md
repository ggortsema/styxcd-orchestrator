# StyxCD Documentation Architecture

## Status

Accepted

## Date

2026-06-18

# Context

StyxCD requires a documentation solution that supports:

- Architecture documentation
- Architectural Decision Records (ADRs)
- YAML Specification documentation
- Frequently Asked Questions (FAQs)
- Future workflow documentation
- Future platform documentation

The documentation system must integrate naturally with the StyxCD Dashboard while maintaining a simple deployment and security model.

# Decision

StyxCD documentation will be implemented as Markdown-based content rendered directly by the Next.js dashboard.

Documentation will be stored in the frontend source tree and packaged into the existing Spring Boot deployable during the normal build process.

Documentation content will be publicly accessible.

Execution functionality and administrative functions will require authentication.

# Documentation Structure

frontend/
  content/
    docs/
    architecture/
    spec/
    adr/

# Routing Model

Documentation pages are automatically generated from content folders.

Adding a Markdown document to a supported content folder should automatically create a new page on the next build.

# Navigation Model

Navigation is generated dynamically from document metadata/frontmatter.

# Build Model

Add Markdown document
→ Commit to Git
→ Build Dashboard
→ Static Export
→ Package Into Spring Boot
→ Deploy

No database required.
No CMS required.
No separate documentation deployment required.

# Security Model

Public:
- Documentation
- ADRs
- Architecture
- YAML Specification
- FAQs

Authenticated:
- Execute Workflow
- View Execution History
- Administrative Functions
- Credential Management

# Deployment Model

Spring Boot Application

- REST APIs
- Dashboard
- Documentation
- ADRs
- YAML Specification
- Static Assets

The dashboard is exported as static content and packaged into the Spring Boot artifact.

# Alternatives Considered

## Separate Documentation Site (MkDocs)

Rejected because documentation should remain part of the StyxCD Dashboard experience.

## Separate Dashboard Deployment

Rejected because current architecture favors a single deployable and a single security boundary.

# Future Enhancements

- Full-text search
- Versioned specifications
- Authenticated editing
- Git-backed editing workflows
- Documentation approval workflows
- Workflow-specific documentation generation
- AI-assisted documentation search
