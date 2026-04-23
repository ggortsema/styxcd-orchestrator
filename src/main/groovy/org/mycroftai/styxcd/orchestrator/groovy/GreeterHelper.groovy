package org.mycroftai.styxcd.orchestrator.groovy

import org.springframework.stereotype.Component

@Component
class GreeterHelper {

    String greet(String name) {
        return "Hello, ${name}!"
    }
}
