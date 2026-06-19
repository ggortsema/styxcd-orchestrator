# Editing the StyxCD YAML Specification

## Overview

The StyxCD YAML Specification is published from content stored in the `styxcd-docs` repository.

The specification viewer is intentionally simple:

```text
Markdown Files
      +
Navigation YAML
      ↓
Frontend Renderer
      ↓
Published Specification
```

No CMS is required.
No content is edited through the UI.
Git remains the source of truth.

---

# Repository Structure

The specification content lives in:

```text
styxcd-docs/
└── yaml-spec/
```

Example:

```text
yaml-spec/
├── spec-nav.yml
├── overview.md
├── release.md
├── applications.md
├── environments.md
└── hello-world.md
```

---

# Navigation

The left-hand navigation is controlled by:

```text
yaml-spec/spec-nav.yml
```

Example:

```yaml
title: YAML Specification

items:
  - title: Overview
    path: overview

  - title: Release
    path: release

  - title: Applications
    path: applications

  - title: Environments
    path: environments
```

Each navigation entry references a markdown file.

Example:

```yaml
- title: Release
  path: release
```

maps to:

```text
yaml-spec/release.md
```

---

# Creating a New Page

## Step 1

Add a navigation entry:

```yaml
- title: Artifacts
  path: artifacts
```

## Step 2

Create:

```text
yaml-spec/artifacts.md
```

Example:

````md
# Artifacts

Artifacts represent deployable outputs produced by a build.

```yaml
artifacts:
  - type: docker-image
```
````

## Step 3

Commit and push:

```bash
git add .
git commit -m "Add artifacts specification"
git push
```

---

# Markdown Basics

## Headings

```md
# Page Title

## Section

### Subsection
```

## Lists

```md
- Item One
- Item Two
- Item Three
```

## Code Blocks

````md
```yaml
release:
  name: hello-world
```
````

Language hints may be used:

```text
yaml
bash
json
java
groovy
```

---

# Tables

```md
| Field | Description |
|---------|-------------|
| name | Release name |
| version | Release version |
```

---

# Publishing Changes

```bash
git add .
git commit -m "Update YAML specification"
git push
```

Rebuild StyxCD:

```bash
mvn clean package
```

The build process:

1. Clones `styxcd-docs`
2. Copies documentation content
3. Copies YAML specification content
4. Builds the Next.js frontend
5. Packages the static site into the Spring Boot application

---

# Design Philosophy

The YAML Specification is treated as a product.

The goal is to keep content creation simple:

```text
Markdown
+
Navigation YAML
+
Git
```

Specification authors should focus on describing the contract, not maintaining a documentation platform.
