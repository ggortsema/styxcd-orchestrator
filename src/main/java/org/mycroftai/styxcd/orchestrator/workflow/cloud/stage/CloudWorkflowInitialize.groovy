package org.mycroftai.styxcd.orchestrator.workflow.cloud.stage

import org.springframework.stereotype.Component

@Component
class CloudWorkflowInitialize {

    public Map getParams(yml, paramMap) {
        def params = [:]
        params['stagename'] = 'initialize cloud workflow'
        params['label'] = 'dev'
        params['VALIDATE_MAP'] = paramMap['VALIDATE_MAP']
        params['YML'] = yml
        return params
    }
}
