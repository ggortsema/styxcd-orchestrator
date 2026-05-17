package org.mycroftai.styxcd.orchestrator.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendRouteController {

    @GetMapping({"/dashboard", "/dashboard/"})
    public String dashboard() {
        return "forward:/dashboard/index.html";
    }

    @GetMapping({"/docs", "/docs/"})
    public String docs() {
        return "forward:/docs/index.html";
    }

    @GetMapping({"/executions", "/executions/"})
    public String executions() {
        return "forward:/executions/index.html";
    }
}
