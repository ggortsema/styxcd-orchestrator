package org.mycroftai.styxcd.orchestrator.dbhello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbHelloController {

    private final DbHelloRepository repository;

    public DbHelloController(DbHelloRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/dbhello")
    public DbHello hello() {
        return repository.findAll().stream()
                .findFirst()
                .orElseThrow();
    }
}
