package org.mycroftai.styxcd.orchestrator.workflow.cloud.stage;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component

public class CloudWorkflowGradleBuildStagePlanner {

    public Map<String, Object> getParams(
            Map<String, Object> yml,
            Map<String, Object> paramMap
    ) {

        Map<String, Object> params = new LinkedHashMap<>();

        String appHostName = (String) paramMap.get("APPHOST_NAME");

        params.put("appHostName", appHostName);
        params.put("stagename", "Build Gradle App - " + appHostName);
        params.put("label", "");
        params.put("VALIDATE_MAP", paramMap.get("VALIDATE_MAP"));
        params.put("YML", yml);

        return params;
    }
}
