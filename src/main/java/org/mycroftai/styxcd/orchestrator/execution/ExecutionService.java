package org.mycroftai.styxcd.orchestrator.execution;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.mycroftai.styxcd.orchestrator.execution.plan.ExecutionPlanService;
import org.mycroftai.styxcd.orchestrator.execution.runner.ExecutionRunner;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class ExecutionService {

    private final ExecutionRepository executionRepository;
    private final ExecutionPlanService executionPlanService;
    private final ExecutionRunner executionRunner;
    private final ObjectMapper ymlMapper = new ObjectMapper(new YAMLFactory());

    public ExecutionService(ExecutionRepository executionRepository, ExecutionPlanService executionPlanService, ExecutionRunner executionRunner) {
        this.executionRepository = executionRepository;
        this.executionPlanService = executionPlanService;
        this.executionRunner = executionRunner;
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

        executionRunner.start(execution);

        execution.setStatus("TRIGGERED");

        return executionRepository.save(execution);
    }

    public void applyCallback(UUID id, ExecutionCallbackRequest request) {
        Execution execution = executionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Execution not found: " + id));

        execution.setStatus(request.status());

        executionRepository.save(execution);
    }

    public Execution getExecution(UUID id) {
        return executionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Execution not found: " + id));
    }
}