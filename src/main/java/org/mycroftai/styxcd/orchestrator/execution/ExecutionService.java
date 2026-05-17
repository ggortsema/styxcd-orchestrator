package org.mycroftai.styxcd.orchestrator.execution;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.util.Map;
import java.util.UUID;
import org.mycroftai.styxcd.orchestrator.execution.plan.ExecutionPlanService;
import org.mycroftai.styxcd.orchestrator.execution.runner.ExecutionRunner;
import org.mycroftai.styxcd.orchestrator.observability.ExecutionEvent;
import org.mycroftai.styxcd.orchestrator.observability.ExecutionEventFactory;
import org.mycroftai.styxcd.orchestrator.observability.LokiEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ExecutionService {

    private final ExecutionRepository executionRepository;
    private final ExecutionPlanService executionPlanService;
    private final ExecutionRunner executionRunner;
    private final ExecutionEventFactory executionEventFactory;
    private final LokiEventPublisher lokiEventPublisher;
    private final ObjectMapper ymlMapper = new ObjectMapper(new YAMLFactory());

    public ExecutionService(
            ExecutionRepository executionRepository,
            ExecutionPlanService executionPlanService,
            ExecutionRunner executionRunner,
            ExecutionEventFactory executionEventFactory,
            LokiEventPublisher lokiEventPublisher
    ) {
        this.executionRepository = executionRepository;
        this.executionPlanService = executionPlanService;
        this.executionRunner = executionRunner;
        this.executionEventFactory = executionEventFactory;
        this.lokiEventPublisher = lokiEventPublisher;
    }

    public Execution createExecution(String rawYml) throws Exception {
        Map<String, Object> parsedYml = ymlMapper.readValue(
                rawYml,
                new TypeReference<Map<String, Object>>() {}
        );

        String workflow = (String) parsedYml.get("workflow");

        Map<String, Object> executionPlan = executionPlanService.createPlan(parsedYml);

        Execution execution = new Execution(
                UUID.randomUUID(),
                workflow,
                "CREATED",
                rawYml,
                executionPlan
        );

        execution = executionRepository.save(execution);

        publishEvent(
                "EXECUTION_CREATED",
                execution,
                execution.getStatus(),
                "Execution created"
        );

        publishEvent(
                "EXECUTION_PLAN_CREATED",
                execution,
                execution.getStatus(),
                "Execution plan created"
        );

        executionRunner.start(execution);

        execution.setStatus("TRIGGERED");
        execution = executionRepository.save(execution);

        publishEvent(
                "EXECUTION_DISPATCHED",
                execution,
                execution.getStatus(),
                "Execution dispatched to runner"
        );

        return execution;
    }

    public void applyCallback(UUID id, ExecutionCallbackRequest request) {
        Execution execution = executionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Execution not found: " + id));

        publishEvent(
                "EXECUTION_CALLBACK_RECEIVED",
                execution,
                request.status(),
                request.message()
        );

        execution.setStatus(request.status());
        execution = executionRepository.save(execution);

        String eventType = "SUCCESS".equalsIgnoreCase(request.status())
                ? "EXECUTION_SUCCEEDED"
                : "FAILED".equalsIgnoreCase(request.status())
                  ? "EXECUTION_FAILED"
                  : "EXECUTION_STATUS_UPDATED";

        publishEvent(
                eventType,
                execution,
                execution.getStatus(),
                request.message()
        );
    }

    public Execution getExecution(UUID id) {
        return executionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Execution not found: " + id));
    }

    private void publishEvent(
            String eventType,
            Execution execution,
            String status,
            String message
    ) {

        execution.setCurrentLifecycleEvent(eventType);
        executionRepository.save(execution);

        ExecutionEvent event = executionEventFactory.create(
                eventType,
                execution,
                status,
                message
        );

        lokiEventPublisher.publishExecutionEvent(event);
    }
}