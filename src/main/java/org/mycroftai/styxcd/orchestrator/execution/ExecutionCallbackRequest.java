package org.mycroftai.styxcd.orchestrator.execution;

public record ExecutionCallbackRequest(String executionId, String status, String message) {}