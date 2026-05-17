package org.mycroftai.styxcd.orchestrator.observability;

import java.util.Map;

public record ExecutionEventError(
        String code,
        String message,
        String severity,
        Boolean retryable,
        String exceptionType,
        Map<String, Object> details
) {
}
