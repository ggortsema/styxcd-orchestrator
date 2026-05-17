package org.mycroftai.styxcd.orchestrator.observability;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class LokiEventPublisher {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${styxcd.observability.loki.enabled:true}")
    private boolean lokiEnabled;

    @Value("${styxcd.observability.loki.url:http://grafana.styxcd.com:3100/loki/api/v1/push}")
    private String lokiUrl;

    public LokiEventPublisher(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restClient = RestClient.create();
    }

    public void publishExecutionEvent(ExecutionEvent event) {

        if (!lokiEnabled) {
            return;
        }

        try {

            String eventJson = objectMapper.writeValueAsString(event);

            Map<String, Object> payload = Map.of(
                    "streams",
                    List.of(
                            Map.of(
                                    "stream",
                                    buildLabels(event),
                                    "values",
                                    List.of(
                                            List.of(
                                                    String.valueOf(Instant.now().toEpochMilli()) + "000000",
                                                    eventJson
                                            )
                                    )
                            )
                    )
            );

            restClient.post()
                    .uri(lokiUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();

        } catch (Exception e) {
            System.err.println("Failed to publish Loki event: " + e.getMessage());
        }
    }

    private Map<String, String> buildLabels(ExecutionEvent event) {

        return Map.of(
                "job", "styxcd-orchestrator",
                "source", safe(event.source()),
                "eventType", safe(event.eventType()),
                "status", safe(event.status()),
                "workflow", safe(event.workflow())
        );
    }

    private String safe(String value) {

        if (value == null || value.isBlank()) {
            return "unknown";
        }

        return value
                .replaceAll("[^a-zA-Z0-9_:.-]", "_")
                .substring(0, Math.min(value.length(), 120));
    }
}
