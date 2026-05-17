package org.mycroftai.styxcd.orchestrator.observability;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.mycroftai.styxcd.orchestrator.execution.Execution;
import org.springframework.stereotype.Component;

@Component
public class ExecutionEventFactory {

    public ExecutionEvent create(
            String eventType,
            Execution execution,
            String status,
            String message
    ) {

        Instant now = Instant.now();

        return new ExecutionEvent(
                "1.0",
                eventType,
                "orchestrator",
                now,

                execution.getId().toString(),
                execution.getId().toString(),

                execution.getWorkflow(),
                null,
                null,

                status,
                message,

                execution.getCreatedAt(),
                null,
                null,

                buildMetadata(execution),
                List.of()
        );
    }

    private Map<String, Object> buildMetadata(Execution execution) {

        return Map.of(
                "runner", "jenkins",
                "createdAt", execution.getCreatedAt().toString()
        );
    }
}
