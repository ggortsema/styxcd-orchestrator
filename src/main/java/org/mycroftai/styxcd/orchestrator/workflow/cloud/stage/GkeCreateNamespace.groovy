package org.mycroftai.styxcd.orchestrator.workflow.cloud.stage

import org.springframework.stereotype.Component

@Component
class GkeCreateNamespace {
    Map getParams(yml, paramMap) {
        def params = [:]

        params['stagename'] = 'GKE Create Namespace: ' + paramMap['LIFECYCLE'] + ' / ' + paramMap['PLATFORM_NAME']  + ' / ' + paramMap['TARGET_NAME']
        params['label'] = 'dev'
        params['VALIDATE_MAP'] = paramMap['VALIDATE_MAP']
        params['YML'] = yml
        params['LIFECYCLE'] = paramMap['LIFECYCLE']
        params['PLATFORM_NAME'] = paramMap['PLATFORM_NAME']
        params['TARGET_NAME'] = paramMap['TARGET_NAME']

        return params
    }
}

