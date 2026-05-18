package org.mycroftai.styxcd.orchestrator.workflow.cloud.stage;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CloudWorkflowInitializeStagePlanner implements WorkflowStagePlanner {

    @Override
    public String workflowName() {
        return "cloud_workflow";
    }

    @Override
    public String stageType() {
        return "initialize";
    }

    @Override
    public Map<String, Map<String, Object>> getStages(
            Map<String, Object> yml,
            Map<String, Object> paramMap
    ) {
        Map<String, Map<String, Object>> stages = new LinkedHashMap<>();

        stages.put(
                workflowName() + ":" + stageType(),
                getParams(yml, paramMap)
        );

        return stages;
    }

    private Map<String, Object> getParams(
            Map<String, Object> yml,
            Map<String, Object> paramMap
    ) {
        Map<String, Object> params = new LinkedHashMap<>();

        params.put("stagename", "initialize");
        params.put("stageType", stageType());

        params.put("label", "styxcd-agent");

        params.put("VALIDATE_MAP", paramMap.get("VALIDATE_MAP"));
        params.put("YML", yml);

        params.put("emitAggregateStageMetrics", false);

        return params;
    }
}