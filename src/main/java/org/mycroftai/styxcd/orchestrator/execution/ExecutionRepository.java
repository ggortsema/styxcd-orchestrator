package org.mycroftai.styxcd.orchestrator.execution;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExecutionRepository extends JpaRepository<Execution, UUID> {
}
