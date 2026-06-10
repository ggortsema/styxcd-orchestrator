package org.mycroftai.styxcd.orchestrator.execution.plan;

import org.mycroftai.styxcd.orchestrator.workflow.Workflow;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExecutionPlanService {

    private final List<Workflow> workflowPlanners;

    public ExecutionPlanService(List<Workflow> workflowPlanners) {
        this.workflowPlanners = workflowPlanners;
    }

    public Map<String, Object> createPlan(Map<String, Object> yml) {

        String workflowName = (String) yml.get("workflow");

        Workflow planner = workflowPlanners.stream()
                .filter(workflowPlanner -> workflowPlanner.workflowName().equals(workflowName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No workflow planner found: " + workflowName));

        return planner.createJsonStageList(yml);
    }
}
