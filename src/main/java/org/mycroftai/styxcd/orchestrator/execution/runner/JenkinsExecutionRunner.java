package org.mycroftai.styxcd.orchestrator.execution.runner;

import org.mycroftai.styxcd.orchestrator.execution.Execution;
import org.springframework.stereotype.Component;

@Component
public class JenkinsExecutionRunner implements ExecutionRunner {

    @Override
    public void start(Execution execution) {
        // TODO: Build STYXCD_REQUEST and trigger Jenkins buildWithParameters.
        System.out.println("TODO: Trigger Jenkins for execution " + execution.getId());
    }
}
