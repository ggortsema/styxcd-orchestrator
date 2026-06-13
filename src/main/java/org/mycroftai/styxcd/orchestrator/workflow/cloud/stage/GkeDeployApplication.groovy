package org.mycroftai.styxcd.orchestrator.workflow.cloud.stage

import org.springframework.stereotype.Component

@Component
class GkeDeployApplication {
    Map getParams(yml, paramMap) {
        def params = [:]

        params['stagename'] = 'GKE Deploy Application - ' + paramMap['APP_NAME'] + ' / ' + paramMap['LIFECYCLE'] + ' / ' + paramMap['PLATFORM_NAME']  + ' / ' + paramMap['TARGET_NAME']
        params['label'] = 'dev'
        params['VALIDATE_MAP'] = paramMap['VALIDATE_MAP']
        params['YML'] = yml
        params['LIFECYCLE'] = paramMap['LIFECYCLE']
        params['PLATFORM_NAME'] = paramMap['PLATFORM_NAME']
        params['TARGET_NAME'] = paramMap['TARGET_NAME']
        params['APP_NAME'] = paramMap['APP_NAME']

        return params
    }
}
