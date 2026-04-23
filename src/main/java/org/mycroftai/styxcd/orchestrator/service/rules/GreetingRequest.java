package org.mycroftai.styxcd.orchestrator.service.rules;

public class GreetingRequest {

    private final String name;

    public GreetingRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
