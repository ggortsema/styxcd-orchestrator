
# Johnny-Johnny / MycroftAI Architectural Decisions
Date: 2026-06-03

## Core Product Separation

### StyxCD
Purpose:
- deployment orchestration
- infrastructure workflows
- CI/CD orchestration
- observability-first deployment control plane

Primary stack:
- Java 21 Spring Boot
- Jenkins shared library
- Terraform
- Loki/Grafana
- future RabbitMQ integration

### Johnny-Johnny
Purpose:
- AI-assisted workflow orchestration
- recruiter/email automation
- task/project management agent
- human-in-the-loop operational assistant

Primary stack:
- Spring Boot orchestrator
- Python agent runtime
- OpenAI integration
- GitHub integration
- Gmail integration
- future Obsidian integration

---

# Johnny-Johnny Architecture Direction

## Control Plane
The Spring Boot backend remains the durable orchestrator.

Responsibilities:
- workflow state
- approvals
- retries
- scheduling
- execution tracking
- auditability
- observability
- durable persistence
- API surface

## Python Agent Runtime
Python handles AI-centric execution.

Technology:
- Python
- FastAPI
- OpenAI SDK
- future LangChain experimentation if useful

Responsibilities:
- OpenAI interactions
- semantic reasoning
- Gmail integrations
- GitHub integrations
- markdown/Obsidian handling
- AI task execution

---

# Messaging / REST Direction

## MVP Direction
Initial MVP does NOT require:
- RabbitMQ
- SQS
- distributed orchestration

The first MVP can run:
- scheduled
- manual
- local
- single-process

However:

The Python application WILL still be built using:
- FastAPI
- service-style structure

This preserves future extensibility.

---

# REST vs Messaging Decisions

## Current Decision
Long-term architecture will likely use BOTH.

### REST
Best for:
- synchronous requests
- command/response interactions
- task lookups
- quick semantic operations

Examples:
- issue lookup
- summarize note
- validate task request

### RabbitMQ
Best for:
- asynchronous workflows
- long-running operations
- batch processing
- retryable workflows
- background execution

Examples:
- recruiter inbox processing
- bulk email classification
- scheduled workflows
- asynchronous draft generation

---

# Messaging Philosophy

RabbitMQ transports work.
The orchestrator owns workflow state.

Queues should NOT become:
- the workflow brain
- durable orchestration state

The Spring Boot orchestrator remains authoritative.

---

# Email Agent MVP

## First MVP Scope

Input:
- unread recruiter emails

Processing:
- retrieve emails
- classify/rank opportunities
- generate summaries
- generate suggested replies/drafts

Output:
- ranked recruiter review list
- draft responses for approval

Execution Model:
- daily scheduled execution
- configurable interval
- manual execution possible later

---

# GitHub Project Management Direction

## GitHub Projects
Use:
- one umbrella project board

Project:
- MycroftAI Engineering Roadmap

Purpose:
- operational visibility
- roadmap
- backlog
- flow tracking

---

# Repo Issue Ownership

Repo issues belong near code ownership.

Examples:

- styxcd-orchestrator issues -> StyxCD orchestrator repo
- johnny-johnny issues -> Spring Boot orchestrator repo
- johnny-johnny-agents issues -> Python agent runtime repo

---

# Obsidian Direction

Obsidian will become:
- architectural memory layer
- operational notes system
- long-form thinking environment

Current decision:
- defer setup until Python MVP exists

Planned structure:
- markdown-based
- Git-backed
- private GitHub repo
- local-first with Git sync

---

# Current Immediate Priorities

1. Create GitHub project board
2. Begin Python FastAPI email-agent shell
3. Implement Gmail recruiter-email retrieval
4. Implement ranking/classification
5. Implement draft generation
6. Expand toward task/project agent
7. Add RabbitMQ when asynchronous workflows justify it
