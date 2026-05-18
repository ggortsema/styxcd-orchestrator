package org.mycroftai.styxcd.orchestrator.execution.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mycroftai.styxcd.orchestrator.execution.Execution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@ConditionalOnProperty(
        name = "styxcd.jenkins.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class JenkinsExecutionRunner implements ExecutionRunner {

    @Value("${styxcd.jenkins.base-url}")
    private String baseUrl;

    @Value("${styxcd.jenkins.job-name}")
    private String jobName;

    @Value("${styxcd.jenkins.username}")
    private String username;

    @Value("${styxcd.jenkins.api-token}")
    private String apiToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void start(Execution execution) {
        try {
            String callbackUrl = String.format(
                    "http://orchestrator.styxcd.com/executions/%s/callback",
                    execution.getId()
            );

            Map<String, Object> request = new HashMap<>();
            request.put("executionId", execution.getId().toString());
            request.put("orchestratorUrl", "http://orchestrator.styxcd.com");
            request.put("callbackUrl", callbackUrl);

            String requestJson = objectMapper.writeValueAsString(request);

            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(username, apiToken);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String body = "STYXCD_REQUEST=" + requestJson;

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            String url = String.format(
                    "%s/job/%s/buildWithParameters",
                    baseUrl,
                    jobName
            );

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            System.out.println("Triggered Jenkins job. Status: " + response.getStatusCode());

        } catch (Exception e) {
            throw new RuntimeException("Failed to trigger Jenkins job", e);
        }
    }
}
