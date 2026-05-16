package org.mycroftai.styxcd.orchestrator.execution;

import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/executions")
public class ExecutionController {

    private final ExecutionService executionService;
    private final ExecutionRepository executionRepository;

    public ExecutionController(
            ExecutionService executionService,
            ExecutionRepository executionRepository
    ) {
        this.executionService = executionService;
        this.executionRepository = executionRepository;
    }

    @PostMapping
    public Execution create(@RequestBody String rawYml) throws Exception {
        return executionService.createExecution(rawYml);
    }

    @GetMapping("/{id}")
    public Execution get(@PathVariable UUID id) {
        return executionRepository.findById(id)
                .orElseThrow();
    }

    @GetMapping("/{id}/plan")
    public Map<String, Object> getPlan(@PathVariable UUID id) {
        return executionRepository.findById(id)
                .orElseThrow()
                .getExecutionPlan();
    }
}
