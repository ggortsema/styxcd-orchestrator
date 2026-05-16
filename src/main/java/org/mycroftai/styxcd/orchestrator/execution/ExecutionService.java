package org.mycroftai.styxcd.orchestrator.execution;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.mycroftai.styxcd.orchestrator.execution.plan.ExecutionPlanService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class ExecutionService {

    private final ExecutionRepository executionRepository;
    private final ExecutionPlanService executionPlanService;
    private final ObjectMapper ymlMapper = new ObjectMapper(new YAMLFactory());

    public ExecutionService(
            ExecutionRepository executionRepository,
            ExecutionPlanService executionPlanService
    ) {
        this.executionRepository = executionRepository;
        this.executionPlanService = executionPlanService;
    }

    public Execution createExecution(String rawYml) throws Exception {
        Map<String, Object> parsedYml = ymlMapper.readValue(
                rawYml,
                new TypeReference<Map<String, Object>>() {}
        );

        String workflow = (String) parsedYml.get("workflow");

        Map<String, Object> executionPlan =
                executionPlanService.createPlan(parsedYml);

        Execution execution = new Execution(
                UUID.randomUUID(),
                workflow,
                "CREATED",
                rawYml,
                executionPlan
        );

        return executionRepository.save(execution);
    }
}
