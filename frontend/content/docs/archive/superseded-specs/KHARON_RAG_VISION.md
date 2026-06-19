# Kharon: AI Knowledge Assistant for StyxCD

## Status

Future Vision / Backlog Item

## Overview

Kharon is the proposed AI-powered knowledge assistant for StyxCD.

The initial implementation will focus on Retrieval-Augmented Generation (RAG) to provide contextual answers based on StyxCD documentation, architecture records, workflow specifications, deployment history, and operational knowledge.

The long-term vision is to evolve Kharon from a passive documentation assistant into an intelligent deployment advisor capable of helping users understand workflows, troubleshoot failures, and eventually recommend remediation actions.

## Why Kharon?

Before the project was named StyxCD, its original working name was **Kharon**.

The name originates from Greek mythology, where Kharon (commonly spelled Charon) serves as the ferryman who guides travelers across the River Styx.

As the StyxCD platform evolved, the project name changed, but the original name remains strongly connected to the platform's identity and mythology.

The symbolism is particularly fitting:

- StyxCD guides deployments through complex delivery workflows.
- Kharon guides users through platform knowledge and operational decisions.
- Users cross from uncertainty to understanding with Kharon's assistance.

## Goals

The primary goal of Kharon is to make organizational and platform knowledge easily accessible through natural language.

Examples:

- What stages currently exist in Cloud Workflow?
- Why was the Planner architecture introduced?
- How does terraform_apply work?
- What backlog items exist for GKE support?
- Explain the StyxCD logging architecture.

## Phase 1: Knowledge Assistant

Data sources may include:

- Architecture documents (ARDs)
- Architecture Decision Records (ADRs)
- Product specifications
- Workflow specifications
- Session context documents
- Roadmaps
- Backlog items
- Runbooks

## Proposed Technical Architecture

- Spring Boot
- Spring AI
- OpenAI
- PostgreSQL
- PGVector

Document → Chunking → Embeddings → PGVector → Semantic Search → Context Assembly → LLM Response

## Phase 2: Workflow Intelligence

Potential capabilities:

- Explain execution plans
- Explain stage inclusion decisions
- Describe workflow structure
- Summarize deployment intent from YAML

## Phase 3: Operational Intelligence

Potential data sources:

- Deployment executions
- Jenkins logs
- Validation results
- Metrics
- Deployment history
- Incident runbooks

Examples:

- Why did deployment 123 fail?
- Have we seen this error before?
- Show similar failures.

## Phase 4: Advisory Platform

Potential features:

- Suggested remediation actions
- Deployment readiness analysis
- Risk identification
- SLO-aware deployment guidance

Human approval remains required for execution.

## Relationship to Johnny-Johnny

Kharon is intentionally focused on StyxCD and deployment-platform knowledge.

Johnny-Johnny represents a broader AI platform vision that may eventually include personal knowledge management, agents, email automation, and long-term memory.

## Vision Statement

Kharon helps users navigate the knowledge, workflows, and operational history of StyxCD.

Just as the ferryman guided travelers across the River Styx, Kharon guides users through the growing body of platform knowledge, helping transform information into understanding and eventually understanding into action.
