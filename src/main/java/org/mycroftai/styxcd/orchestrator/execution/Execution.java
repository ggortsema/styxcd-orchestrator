package org.mycroftai.styxcd.orchestrator.execution;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "executions")
public class Execution {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "workflow")
    private String workflow;

    @Column(name = "status")
    private String status;

    @Column(name = "current_lifecycle_event")
    private String currentLifecycleEvent;

    @Column(name = "raw_yml", columnDefinition = "text")
    private String rawYml;

    @Type(JsonType.class)
    @Column(name = "execution_plan", columnDefinition = "jsonb")
    private Map<String, Object> executionPlan;

    @Column(name = "created_at")
    private Instant createdAt;

    protected Execution() {
    }

    public Execution(UUID id, String workflow, String status, String rawYml, Map<String, Object> executionPlan) {
        this.id = id;
        this.workflow = workflow;
        this.status = status;
        this.rawYml = rawYml;
        this.executionPlan = executionPlan;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getWorkflow() {
        return workflow;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentLifecycleEvent() {
        return currentLifecycleEvent;
    }

    public void setCurrentLifecycleEvent(String currentLifecycleEvent) {
        this.currentLifecycleEvent = currentLifecycleEvent;
    }

    public String getRawYml() {
        return rawYml;
    }

    public Map<String, Object> getExecutionPlan() {
        return executionPlan;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {

        return "Execution{" +
                "id=" + id +
                ", workflow='" + workflow + '\'' +
                ", status='" + status + '\'' +
                ", currentLifecycleEvent='" + currentLifecycleEvent + '\'' +
                ", createdAt=" + createdAt +
                ", executionPlan=" + executionPlan +
                '}';
    }
}