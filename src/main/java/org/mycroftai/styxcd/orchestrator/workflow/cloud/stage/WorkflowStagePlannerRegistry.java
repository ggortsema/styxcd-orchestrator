package org.mycroftai.styxcd.orchestrator.workflow.cloud.stage;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class WorkflowStagePlannerRegistry {

    private final Map<String, WorkflowStagePlanner> planners;

    public WorkflowStagePlannerRegistry(List<WorkflowStagePlanner> planners) {
        this.planners = planners.stream()
                .collect(Collectors.toMap(
                        planner -> key(planner.workflowName(), planner.stageType()),
                        planner -> planner
                ));
    }

    public Optional<WorkflowStagePlanner> find(String workflowName, String stageType) {
        return Optional.ofNullable(planners.get(key(workflowName, stageType)));
    }

    private String key(String workflowName, String stageType) {
        return workflowName + ":" + stageType;
    }
}
