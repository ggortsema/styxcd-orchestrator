package org.mycroftai.styxcd.orchestrator.workflow.cloud.stage;

import java.util.LinkedHashMap;
import java.util.List;
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
    @SuppressWarnings("unchecked")
    public Map<String, Map<String, Object>> getStages(
            Map<String, Object> yml,
            Map<String, Object> paramMap
    ) {
        Map<String, Map<String, Object>> stages = new LinkedHashMap<>();

        Map<String, Object> release = (Map<String, Object>) yml.get("release");
        Map<String, Object> applications = (Map<String, Object>) release.get("applications");

        if (applications == null) {
            return stages;
        }

        List<Map<String, Object>> springApps =
                (List<Map<String, Object>>) applications.get("spring");

        if (springApps == null) {
            return stages;
        }

        for (Map<String, Object> app : springApps) {
            Map<String, Object> build = (Map<String, Object>) app.get("build");

            if (build == null) {
                continue;
            }

            String buildType = (String) build.get("type");

            if (!"gradle".equals(buildType)) {
                continue;
            }

            String appHostName = (String) app.get("name");

            Map<String, Object> stageParamMap = new LinkedHashMap<>(paramMap);
            stageParamMap.put("APPHOST_NAME", appHostName);

            stages.put(
                    stageType() + ":" + appHostName,
                    getParams(yml, stageParamMap, app)
            );
        }

        return stages;
    }

    private Map<String, Object> getParams(
            Map<String, Object> yml,
            Map<String, Object> paramMap,
            Map<String, Object> app
    ) {
        Map<String, Object> params = new LinkedHashMap<>();

        String appHostName = (String) paramMap.get("APPHOST_NAME");

        params.put("appHostName", appHostName);
        params.put("appName", appHostName);

        params.put("stagename", "Build Gradle App - " + appHostName);
        params.put("stageType", stageType());
        params.put("label", "styxcd-agent");

        params.put("VALIDATE_MAP", paramMap.get("VALIDATE_MAP"));

        params.put("repo", app.get("repo"));
        params.put("branch", app.getOrDefault("branch", "main"));
        params.put("buildCommand", "./gradlew clean test");
        params.put("preWorkspaceStashName", appHostName + "-pre-workspace");
        params.put("workspaceStashName", appHostName + "-workspace");

        return params;
    }
}