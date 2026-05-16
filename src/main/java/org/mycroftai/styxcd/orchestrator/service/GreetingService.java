package org.mycroftai.styxcd.orchestrator.service;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.mycroftai.styxcd.orchestrator.service.rules.GreetingRequest;
import org.mycroftai.styxcd.orchestrator.service.rules.GreetingResponse;
import org.mycroftai.styxcd.orchestrator.service.rules.GreeterHelper;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    private final KieContainer kieContainer;
    private final GreeterHelper greeterHelper;

    public GreetingService(KieContainer kieContainer, GreeterHelper greeterHelper) {
        this.kieContainer = kieContainer;
        this.greeterHelper = greeterHelper;
    }

    public String greet(String name) {
        GreetingRequest request = new GreetingRequest(name);
        GreetingResponse response = new GreetingResponse(greeterHelper.greet(name));

        KieSession kieSession = kieContainer.newKieSession();
        try {
            kieSession.insert(request);
            kieSession.insert(response);
            kieSession.fireAllRules();
            return response.getMessage();
        } finally {
            kieSession.dispose();
        }
    }
}
