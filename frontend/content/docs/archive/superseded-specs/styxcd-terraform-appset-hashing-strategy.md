# StyxCD Terraform App-Set Hashing Strategy

## Purpose

This document defines the Terraform app-set hashing strategy used by StyxCD to deterministically resolve Terraform environment projects for ECS Fargate deployments.

The goal is to:

- Avoid collisions between app-set combinations
- Support large application sets cleanly
- Keep Terraform paths deterministic
- Avoid extremely long directory names
- Support future caching, lineage, and drift tracking

---

# Canonical App-Set Strategy

The Terraform lookup path is derived from:

1. Environment class
2. Platform type
3. Environment name
4. Canonicalized application set hash

The application set hash is generated from:

- Environment name
- Alphabetically sorted application names

---

# Canonicalization Process

## Example Application Set

Applications:

- johnny-johnny-backend
- johnny-johnny-ui

## Step 1 — Sort Alphabetically

Sorted:

- johnny-johnny-backend
- johnny-johnny-ui

## Step 2 — Create Canonical String

Canonical string:

env=johnny-sandbox|apps=johnny-johnny-backend,johnny-johnny-ui

## Step 3 — Generate SHA-256 Hash

Example:

a84c91f2d712b73fcb0e8e42cdb0f97d2e10f5b8d4b6b3ef4cbbf57d34b8711

## Step 4 — Use Short Hash Prefix

For readability, StyxCD uses the first 12 characters:

a84c91f2d712

---

# Final Terraform Path

Final lookup path:

envs/sandbox/ecs-fargate/johnny-sandbox-appset-a84c91f2d712

---

# Naming Rules

Terraform environment folders follow:

{environment-name}-appset-{hash12}

Examples:

johnny-sandbox-appset-a84c91f2d712

billing-dev-appset-93bc771ad0ff

platform-qa-appset-f11ab98ce224

---

# Why Hashing Is Used

Hashing provides:

- Deterministic lookup
- Compact naming
- Collision resistance
- Stable app-set identity
- Future support for caching and drift tracking

---

# Important Design Decision

The hash identifies:

- The environment + application set combination

The hash DOES NOT identify:

- Terraform runtime plan files
- Terraform state files
- Terraform execution runs

Terraform plan files remain ephemeral runtime artifacts and are not committed to Git.

---

# Future Enhancements

Potential future enhancements:

- Include application versions in canonical string
- Include infrastructure provider configuration
- Include runtime configuration
- Include Terraform module versions
- Add plan caching keyed by hash
- Drift detection based on hash lineage