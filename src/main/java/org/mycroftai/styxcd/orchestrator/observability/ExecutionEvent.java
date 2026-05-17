package org.mycroftai.styxcd.orchestrator.observability;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ExecutionEvent(
        String eventVersion,
        String eventType,
        String source,
        Instant timestamp,

        String correlationId,
        String executionId,

        String workflow,
        String releaseName,
        String releaseVersion,

        String status,
        String message,

        Instant startedAt,
        Instant endedAt,
        Long elapsedMs,

        Map<String, Object> metadata,
        List<ExecutionEventError> errors
) {
}
