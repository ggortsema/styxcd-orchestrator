package org.mycroftai.styxcd.orchestrator.dbhello;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "db_hello")
public class DbHello {

    @Id
    private Long id;

    private String message;

    protected DbHello() {
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}