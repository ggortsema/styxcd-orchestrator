package org.mycroftai.styxcd.orchestrator.workflow;

import java.util.Map;

public interface Workflow {

    String workflowName();

    Map<String, Object> createJsonStageList(Map<String, Object> yml);
}
