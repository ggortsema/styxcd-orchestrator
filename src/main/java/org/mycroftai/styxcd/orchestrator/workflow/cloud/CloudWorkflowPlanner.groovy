package org.mycroftai.styxcd.orchestrator.workflow

import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.WorkflowStagePlanner
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.WorkflowStagePlannerRegistry
import org.springframework.stereotype.Component

@Component
class CloudWorkflowPlanner {

    private static final String WORKFLOW_NAME = "cloud_workflow"

    private final WorkflowStagePlannerRegistry workflowStagePlannerRegistry

    CloudWorkflowPlanner(
            WorkflowStagePlannerRegistry workflowStagePlannerRegistry
    ) {
        this.workflowStagePlannerRegistry = workflowStagePlannerRegistry
    }

    Map<String, Object> buildExecutionPlan(Map<String, Object> yml) {

        Map<String, Object> executionPlan = new LinkedHashMap<>()
        Map<String, Object> paramMap = buildBaseParamMap()

        addStage(executionPlan, yml, paramMap, "initialize")

        Map<String, Object> release = yml.release as Map<String, Object>
        Map<String, Object> applications = release?.applications as Map<String, Object>

        List<Map<String, Object>> springApps =
                applications?.spring as List<Map<String, Object>>

        boolean gradleBuildFound =
                springApps?.any { Map<String, Object> app ->
                    Map<String, Object> build = app.build as Map<String, Object>
                    build?.type == "gradle"
                }

        if (gradleBuildFound) {
            addStage(executionPlan, yml, paramMap, "gradle_build")
        }

        Map<String, Object> environments =
                release?.environments as Map<String, Object>

        boolean terraformEnvironmentFound =
                environments?.values()?.any { Object environmentGroup ->

                    List<Map<String, Object>> environmentList =
                            environmentGroup as List<Map<String, Object>>

                    environmentList?.any { Map<String, Object> environment ->

                        Map<String, Object> platform =
                                environment.platform as Map<String, Object>

                        Map<String, Object> infrastructure =
                                platform?.infrastructure as Map<String, Object>

                        infrastructure?.terraform != null
                    }
                }

        if (terraformEnvironmentFound) {
            addStage(executionPlan, yml, paramMap, "terraform_apply")
        }

        addStage(executionPlan, yml, paramMap, "cleanup")

        return executionPlan
    }

    private void addStage(
            Map<String, Object> executionPlan,
            Map<String, Object> yml,
            Map<String, Object> paramMap,
            String stageType
    ) {

        WorkflowStagePlanner planner =
                workflowStagePlannerRegistry
                        .find(WORKFLOW_NAME, stageType)
                        .orElseThrow {
                            new IllegalStateException(
                                    "No stage planner registered for workflowName=${WORKFLOW_NAME}, stageType=${stageType}"
                            )
                        }

        Map<String, Map<String, Object>> stages =
                planner.getStages(yml, paramMap)

        if (!stages) {
            return
        }

        stages.each { String executionPlanKey, Map<String, Object> params ->
            executionPlan.put(executionPlanKey, params)
        }
    }

    private Map<String, Object> buildBaseParamMap() {

        Map<String, Object> paramMap =
                new LinkedHashMap<>()

        paramMap.put("VALIDATE_MAP", true)

        return paramMap
    }
}