package org.mycroftai.styxcd.orchestrator.plan;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExecutionPlanService {

    public Map<String, Object> createPlan(Map<String, Object> yml) {
        Map<String, Object> plan = new LinkedHashMap<>();

        plan.put("CloudWorkflowInitialize", Map.of(
                "stagename", "initialize",
                "label", "",
                "VALIDATE_MAP", Map.of(),
                "YML", yml
        ));

        String appName = getFirstSpringAppName(yml);

        plan.put("GradleBuild@" + appName, Map.of(
                "appHostName", appName,
                "stagename", "Build Gradle App - " + appName,
                "label", "",
                "VALIDATE_MAP", Map.of(),
                "YML", yml
        ));

        plan.put("CloudWorkflowCleanup@final", Map.of(
                "stagename", "cleanup",
                "label", "",
                "VALIDATE_MAP", Map.of(),
                "YML", yml
        ));

        return plan;
    }

    @SuppressWarnings("unchecked")
    private String getFirstSpringAppName(Map<String, Object> yml) {
        Map<String, Object> release = (Map<String, Object>) yml.get("release");
        Map<String, Object> applications = (Map<String, Object>) release.get("applications");
        List<Map<String, Object>> springApps = (List<Map<String, Object>>) applications.get("spring");

        return (String) springApps.getFirst().get("name");
    }
}
