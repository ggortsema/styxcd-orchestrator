package org.mycroftai.styxcd.orchestrator.execution.plan;

import org.mycroftai.styxcd.orchestrator.workflow.WorkflowPlanner;
import org.mycroftai.styxcd.orchestrator.workflow.cloud.CloudWorkflowPlanner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExecutionPlanService {

    private final List<WorkflowPlanner> workflowPlanners;

    public ExecutionPlanService(List<WorkflowPlanner> workflowPlanners) {
        this.workflowPlanners = workflowPlanners;
    }

    public Map<String, Object> createPlan(Map<String, Object> yml) {
        WorkflowPlanner planner = workflowPlanners.stream()
                .filter(workflowPlanner -> workflowPlanner.workflowName().equals("CloudWorkflow"))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No workflow planner found"));

        return planner.createJsonStageList(yml);
    }
}
