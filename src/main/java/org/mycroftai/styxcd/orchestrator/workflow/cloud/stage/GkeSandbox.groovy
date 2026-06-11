package org.mycroftai.styxcd.orchestrator.workflow.cloud.stage

import org.springframework.stereotype.Component

@Component
class GkeSandbox {
    Map getParams(yml, paramMap) {
        def params = [:]

        params['stagename'] = 'GkeSandbox'
        params['label'] = 'dev'
        params['VALIDATE_MAP'] = paramMap['VALIDATE_MAP']
        params['YML'] = yml

        return params
    }
}
