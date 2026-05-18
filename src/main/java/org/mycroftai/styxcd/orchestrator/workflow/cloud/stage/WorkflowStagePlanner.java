package org.mycroftai.styxcd.orchestrator.workflow.cloud.stage;

import java.util.Map;

public interface WorkflowStagePlanner {

    String workflowName();

    String stageType();

    Map<String, Map<String, Object>> getStages(
            Map<String, Object> yml,
            Map<String, Object> paramMap
    );
}
