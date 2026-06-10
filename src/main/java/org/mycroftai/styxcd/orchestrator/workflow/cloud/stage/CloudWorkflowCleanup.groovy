package org.mycroftai.styxcd.orchestrator.workflow.cloud.stage

import org.springframework.stereotype.Component

@Component
class CloudWorkflowCleanup {

    Map getParams(yml, paramMap) {
        def params = [:]

        params['appHostName'] = paramMap['APPHOST_NAME']
        params['stagename'] = "cleanup"
        params['label'] = 'dev'
        params['VALIDATE_MAP'] = paramMap['VALIDATE_MAP']
        params['YML'] = yml

        return params
    }
}
