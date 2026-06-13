package org.mycroftai.styxcd.orchestrator.workflow.cloud

import org.mycroftai.styxcd.orchestrator.workflow.Workflow
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.CloudWorkflowCleanup
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.CloudWorkflowInitialize
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.GkeSandbox
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.GradleBuild
import org.springframework.stereotype.Component

@Component
class CloudWorkflow implements Workflow {

    private final CloudWorkflowInitialize cloudWorkflowInitialize
    private final CloudWorkflowCleanup cloudWorkflowCleanup
    private final GradleBuild gradleBuild
    private final GkeSandbox gkeSandbox

    CloudWorkflow(
            CloudWorkflowInitialize cloudWorkflowInitialize,
            CloudWorkflowCleanup cloudWorkflowCleanup,
            GradleBuild gradleBuild,
            GkeSandbox gkeSandbox
    ) {
        this.cloudWorkflowInitialize = cloudWorkflowInitialize
        this.cloudWorkflowCleanup = cloudWorkflowCleanup
        this.gradleBuild = gradleBuild
        this.gkeSandbox = gkeSandbox
    }

    @Override
    String workflowName() {
        return 'cloud_workflow'
    }

    @Override
    Map<String, Object> createJsonStageList(Map<String, Object> yml) {

        def paramMap = [:]
        def jsonOutput = [:]

        paramMap['VALIDATE_MAP'] = preprocessYml(yml)

        jsonOutput['CloudWorkflowInitialize'] =
                cloudWorkflowInitialize.getParams(yml, paramMap)

        yml?.release?.applications?.spring?.each {
            paramMap = [:]
            paramMap['APPHOST_NAME'] = it?.name
            paramMap['VALIDATE_MAP'] = preprocessYml(yml)

            if (it?.build_tool == 'gradle') {
                jsonOutput["GradleBuild@${paramMap['APPHOST_NAME']}"] =
                        gradleBuild.getParams(yml, paramMap)
            }
        }

        def envList = ['sandbox', 'dev', 'qa', 'stage', 'prod']

        envList.each { lifecycle ->
            yml.release?.environments?."${lifecycle}"?.each { target ->
                yml.release?.applications?.spring?.each { app ->
                    paramMap['APPHOST_NAME'] = app?.name
                    if(!app?.skip_deploy) {
                        if(target?.platform?.name == 'gke') {
                            jsonOutput["GkeSandbox@${paramMap['APPHOST_NAME']}${lifecycle}${target?.name}"] = gkeSandbox.getParams(yml, paramMap)
                        }
                    }
                }
            }
        }

        jsonOutput['CloudWorkflowCleanup@final'] =
                cloudWorkflowCleanup.getParams(yml, paramMap)

        return jsonOutput
    }

    private Map preprocessYml(yml) {
        def validateMap = [:]
        return validateMap
    }
}