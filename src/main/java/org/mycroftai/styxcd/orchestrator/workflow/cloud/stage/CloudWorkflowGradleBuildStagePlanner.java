package org.mycroftai.styxcd.orchestrator.workflow.cloud.stage;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CloudWorkflowGradleBuildStagePlanner implements WorkflowStagePlanner {

    @Override
    public String workflowName() {
        return "cloud_workflow";
    }

    @Override
    public String stageType() {
        return "gradle_build";
    }

    @Override
    public Map<String, Object> getParams(
            Map<String, Object> yml,
            Map<String, Object> paramMap
    ) {

        Map<String, Object> params = new LinkedHashMap<>();

        String appHostName = (String) paramMap.get("APPHOST_NAME");

        params.put("appHostName", appHostName);

        /*
         * Human readable execution-plan display name
         */
        params.put("stagename", "Build Gradle App - " + appHostName);

        /*
         * Canonical stage type identifier
         */
        params.put("stageType", stageType());

        params.put("label", "styxcd-agent");
        params.put("VALIDATE_MAP", paramMap.get("VALIDATE_MAP"));
        params.put("YML", yml);

        return params;
    }
}