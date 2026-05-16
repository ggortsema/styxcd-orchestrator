package org.mycroftai.styxcd.orchestrator.workflow.cloud

import org.mycroftai.styxcd.orchestrator.workflow.WorkflowPlanner
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.CloudWorkflowCleanupStagePlanner
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.CloudWorkflowGradleBuildStagePlanner
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.CloudWorkflowInitializeStagePlanner
import org.springframework.stereotype.Component

@Component
class CloudWorkflowPlanner implements WorkflowPlanner {

    private final CloudWorkflowInitializeStagePlanner initializeStagePlanner
    private final CloudWorkflowGradleBuildStagePlanner gradleBuildStagePlanner
    private final CloudWorkflowCleanupStagePlanner cleanupStagePlanner

    CloudWorkflowPlanner(CloudWorkflowInitializeStagePlanner initializeStagePlanner, CloudWorkflowGradleBuildStagePlanner gradleBuildStagePlanner, CloudWorkflowCleanupStagePlanner cleanupStagePlanner) {
        this.initializeStagePlanner = initializeStagePlanner
        this.gradleBuildStagePlanner = gradleBuildStagePlanner
        this.cleanupStagePlanner = cleanupStagePlanner
    }

    @Override
    String workflowName() {
        return 'CloudWorkflow'
    }

    @Override
    Map<String, Object> createJsonStageList(Map<String, Object> yml) {
        Map<String, Object> paramMap = [:]
        Map<String, Object> jsonOutput = new LinkedHashMap<>()

        paramMap['VALIDATE_MAP'] = preprocessYml(yml)

        jsonOutput['CloudWorkflowInitialize'] = initializeStagePlanner.getParams(yml, paramMap)

        yml.release.applications.spring.each { app ->
            paramMap = [:]
            paramMap['APPHOST_NAME'] = app?.name
            paramMap['VALIDATE_MAP'] = preprocessYml(yml)

            if (app.build_tool == 'gradle') {
                jsonOutput["GradleBuild@${paramMap['APPHOST_NAME']}"] = gradleBuildStagePlanner.getParams(yml, paramMap)
            }
        }

        jsonOutput['CloudWorkflowCleanup@final'] = cleanupStagePlanner.getParams(yml, paramMap)

        return jsonOutput
    }

    private Map<String, Object> preprocessYml(Map<String, Object> yml) {
        Map<String, Object> validateMap = [:]

        return validateMap
    }
}