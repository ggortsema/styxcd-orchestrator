package org.mycroftai.styxcd.orchestrator.workflow.cloud

import org.mycroftai.styxcd.orchestrator.workflow.Workflow
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.CloudWorkflowCleanup
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.CloudWorkflowInitialize
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.EKSWorkflowClusterBuild
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.EksConfigureDns
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.EksCreateIngress
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.EksCreateNamespace
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.EksDeployApplication
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.EksInstallLoadBalancerController
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.EksValidateDeployment
import org.mycroftai.styxcd.orchestrator.workflow.cloud.stage.EksValidateService
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
    private final EksInstallLoadBalancerController eksInstallLoadBalancerController
    private final GkeCreateNamespace gkeCreateNamespace
    private final EksCreateNamespace eksCreateNamespace
    private final GkeDeployApplication gkeDeployApplication
    private final EksDeployApplication eksDeployApplication
    private final GkeCreateIngress gkeCreateIngress
    private final EksCreateIngress eksCreateIngress
    private final GkeConfigureDns gkeConfigureDns
    private final EksConfigureDns eksConfigureDns
    private final GkeValidateService gkeValidateService
    private final EksValidateService eksValidateService
    private final GkeValidateDeployment gkeValidateDeployment
    private final EksValidateDeployment eksValidateDeployment
    private final EKSWorkflowClusterBuild eksWorkflowClusterBuild

    CloudWorkflow(
            CloudWorkflowInitialize cloudWorkflowInitialize,
            CloudWorkflowCleanup cloudWorkflowCleanup,
            GradleBuild gradleBuild,
            GkeCreateNamespace gkeCreateNamespace,
            GkeDeployApplication gkeDeployApplication,
            GkeCreateIngress gkeCreateIngress,
            GkeConfigureDns gkeConfigureDns,
            GkeValidateService gkeValidateService,
            GkeValidateDeployment gkeValidateDeployment,
            EksInstallLoadBalancerController eksInstallLoadBalancerController,
            EksCreateNamespace eksCreateNamespace,
            EksDeployApplication eksDeployApplication,
            EksCreateIngress eksCreateIngress,
            EksConfigureDns eksConfigureDns,
            EksValidateService eksValidateService,
            EksValidateDeployment eksValidateDeployment,
            EKSWorkflowClusterBuild eksWorkflowClusterBuild
    ) {
        this.cloudWorkflowInitialize = cloudWorkflowInitialize
        this.cloudWorkflowCleanup = cloudWorkflowCleanup
        this.gradleBuild = gradleBuild
        this.gkeCreateNamespace = gkeCreateNamespace
        this.gkeDeployApplication = gkeDeployApplication
        this.gkeCreateIngress = gkeCreateIngress
        this.gkeConfigureDns = gkeConfigureDns
        this.gkeValidateService = gkeValidateService
        this.gkeValidateDeployment = gkeValidateDeployment
        this.eksInstallLoadBalancerController = eksInstallLoadBalancerController
        this.eksCreateNamespace = eksCreateNamespace
        this.eksDeployApplication = eksDeployApplication
        this.eksCreateIngress = eksCreateIngress
        this.eksConfigureDns = eksConfigureDns
        this.eksValidateService = eksValidateService
        this.eksValidateDeployment = eksValidateDeployment
        this.eksWorkflowClusterBuild = eksWorkflowClusterBuild
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
                paramMap.remove('APP_NAME')
                paramMap['LIFECYCLE'] = lifecycle
                paramMap['PLATFORM_NAME'] = target?.platform?.name
                paramMap['TARGET_NAME'] = target?.name
                paramMap['VALIDATE_MAP'] = preprocessYml

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
                    paramMap.remove('APP_NAME')
                    jsonOutput["GkeCreateIngress@${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = gkeCreateIngress.getParams(yml, paramMap)
                    jsonOutput["GkeConfigureDns@${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = gkeConfigureDns.getParams(yml, paramMap)
                    jsonOutput["GkeValidateService@${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = gkeValidateService.getParams(yml, paramMap)
                }

                if (target?.platform?.name == 'eks') {

                    jsonOutput["EksInstallLoadBalancerController@${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = eksInstallLoadBalancerController.getParams(yml, paramMap)
                    jsonOutput["EksCreateNamespace@${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = eksCreateNamespace.getParams(yml, paramMap)

                    target?.platform?.applications?.each { targetApp ->

                        def appName = targetApp?.name
                        def app = yml.release?.applications?.values()?.flatten()?.find { it?.name == appName }

                        //TODO move this into preprocess yml
                        if (!app) {
                            throw new RuntimeException("EKS target application '${appName}' was listed for target '${paramMap['TARGET_NAME']}' but no matching release application was found.")
                        }

                        paramMap['APP_NAME'] = appName

                        jsonOutput["EksDeployApplication@${paramMap['APP_NAME']}${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = eksDeployApplication.getParams(yml, paramMap)
                        jsonOutput["EksValidateDeployment@${paramMap['APP_NAME']}${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = eksValidateDeployment.getParams(yml, paramMap)
                    }
                    paramMap.remove('APP_NAME')
                    jsonOutput["EksCreateIngress@${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = eksCreateIngress.getParams(yml, paramMap)
                    jsonOutput["EksConfigureDns@${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = eksConfigureDns.getParams(yml, paramMap)
                    jsonOutput["EksValidateService@${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = eksValidateService.getParams(yml, paramMap)
                }
                if (target?.platform?.name == 'aks') {
                    //TODO this needs to be remoed it is just a way to trigger the eks cluster build for now
                    jsonOutput["EKSWorkflowClusterBuild@${paramMap['PLATFORM_NAME']}${paramMap['LIFECYCLE']}${paramMap['TARGET_NAME']}"] = eksWorkflowClusterBuild.getParams(yml, paramMap)
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