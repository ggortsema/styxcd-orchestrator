package org.mycroftai.styxcd.orchestrator.workflow.cloud

import org.mycroftai.styxcd.orchestrator.workflow.Workflow
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.CloudWorkflowCleanup
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.CloudWorkflowInitialize
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.GkeConfigureDns
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.GkeCreateIngress
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.GkeCreateNamespace
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.GkeDeployApplication
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.GkeValidateDeployment
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.GkeValidateService
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.GradleBuild
import org.springframework.stereotype.Component

@Component
class CloudWorkflow implements Workflow {

    private final CloudWorkflowInitialize cloudWorkflowInitialize
    private final CloudWorkflowCleanup cloudWorkflowCleanup
    private final GradleBuild gradleBuild
    private final GkeCreateNamespace gkeCreateNamespace
    private final GkeDeployApplication gkeDeployApplication
    private final GkeCreateIngress gkeCreateIngress
    private final GkeConfigureDns gkeConfigureDns
    private final GkeValidateService gkeValidateService
    private final GkeValidateDeployment gkeValidateDeployment

    CloudWorkflow(
            CloudWorkflowInitialize cloudWorkflowInitialize,
            CloudWorkflowCleanup cloudWorkflowCleanup,
            GradleBuild gradleBuild,
            GkeCreateNamespace gkeCreateNamespace,
            GkeDeployApplication gkeDeployApplication,
            GkeCreateIngress gkeCreateIngress,
            GkeConfigureDns gkeConfigureDns,
            GkeValidateService gkeValidateService,
            GkeValidateDeployment gkeValidateDeployment
    ) {
        this.cloudWorkflowInitialize = cloudWorkflowInitialize
        this.cloudWorkflowCleanup = cloudWorkflowCleanup
        this.gradleBuild = gradleBuild
        this.gkeCreateNamespace = gkeCreateNamespace
        this.gkeDeployApplication = gkeDeployApplication
        this.gkeCreateIngress = gkeCreateIngress
        this.gkeConfigureDns =  gkeConfigureDns
        this.gkeValidateService = gkeValidateService
        this.gkeValidateDeployment = gkeValidateDeployment
    }

    @Override
    String workflowName() {
        return 'cloud_workflow'
    }

    @Override
    Map<String, Object> createJsonStageList(Map<String, Object> yml) {

        def paramMap = [:]
        def jsonOutput = [:]
        def preprocessYml = this.preprocessYml(yml)

        paramMap['VALIDATE_MAP'] = preprocessYml

        jsonOutput['CloudWorkflowInitialize'] = cloudWorkflowInitialize.getParams(yml, paramMap)

        yml?.release?.applications?.spring?.each {

            paramMap = [:]
            paramMap['APP_NAME'] = it?.name
            paramMap['VALIDATE_MAP'] = preprocessYml

            if (it?.build_tool == 'gradle') {

                jsonOutput["GradleBuild@${paramMap['APP_NAME']}"] = gradleBuild.getParams(yml, paramMap)

            }
        }

        def envList = ['sandbox', 'dev', 'qa', 'stage', 'prod']

        envList.each { lifecycle ->

            //TODO create different maps for different parts of the lifecycle to avoid mutability errors
            paramMap = [:]
            paramMap['LIFECYCLE'] = lifecycle

            yml.release?.environments?."${lifecycle}"?.each { target ->

                paramMap['LIFECYCLE'] = lifecycle
                paramMap['PLATFORM_NAME'] = target?.platform?.name
                paramMap['TARGET_NAME'] = target?.name
                paramMap['VALIDATE_MAP'] = preprocessYml

//                if(target?.platform?.name == 'gke') {
//
//                    jsonOutput["GkeCreateNamespace@${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = gkeCreateNamespace.getParams(yml, paramMap)
//
//                    yml.release?.applications?.spring?.each { app ->
//
//                        paramMap['APP_NAME'] = app?.name
//                        jsonOutput["GkeDeployApplication@${paramMap['APP_NAME']}${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = gkeDeployApplication.getParams(yml, paramMap)
//                        jsonOutput["GkeValidateDeployment@${paramMap['APP_NAME']}${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = gkeValidateDeployment.getParams(yml, paramMap)
//
//                    }
//
//                    jsonOutput["GkeCreateIngress@${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = gkeCreateIngress.getParams(yml, paramMap)
//                    jsonOutput["GkeConfigureDns@${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = gkeConfigureDns.getParams(yml, paramMap)
//                    jsonOutput["GkeValidateService@${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = gkeValidateService.getParams(yml, paramMap)
//
//                }

                if (target?.platform?.name == 'gke') {

                    jsonOutput["GkeCreateNamespace@${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = gkeCreateNamespace.getParams(yml, paramMap)

                    target?.platform?.applications?.each { targetApp ->

                        def appName = targetApp?.name
                        def app = yml.release?.applications?.values()?.flatten()?.find { it?.name == appName }

                        //TODO move this into preprocess yml
                        if (!app) {
                            throw new RuntimeException("GKE target application '${appName}' was listed for target '${paramMap['TARGET_NAME']}' but no matching release application was found.")
                        }

                        paramMap['APP_NAME'] = appName

                        jsonOutput["GkeDeployApplication@${paramMap['APP_NAME']}${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = gkeDeployApplication.getParams(yml, paramMap)
                        jsonOutput["GkeValidateDeployment@${paramMap['APP_NAME']}${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = gkeValidateDeployment.getParams(yml, paramMap)
                    }

                    jsonOutput["GkeCreateIngress@${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = gkeCreateIngress.getParams(yml, paramMap)
                    jsonOutput["GkeConfigureDns@${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = gkeConfigureDns.getParams(yml, paramMap)
                    jsonOutput["GkeValidateService@${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = gkeValidateService.getParams(yml, paramMap)
                }
            }
        }

        jsonOutput['CloudWorkflowCleanup@__final__'] = cloudWorkflowCleanup.getParams(yml, paramMap)

        return jsonOutput
    }

    private Map preprocessYml(yml) {
        def validateMap = [:]
        return validateMap
    }
}