package org.mycroftai.styxcd.orchestrator.workflow.cloud.stage;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CloudWorkflowInitializeStagePlanner {

    public Map<String, Object> getParams(
            Map<String, Object> yml,
            Map<String, Object> paramMap
    ) {

        Map<String, Object> params = new LinkedHashMap<>();

        params.put("stagename", "initialize");
        params.put("label", "styxcd-agent");
        params.put("VALIDATE_MAP", paramMap.get("VALIDATE_MAP"));
        params.put("YML", yml);
        params.put("emitAggregateStageMetrics", false);

        return params;
    }
}
