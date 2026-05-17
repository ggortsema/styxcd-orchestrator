package org.mycroftai.styxcd.orchestrator.web;

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

    @GetMapping({"/operations", "/operations/"})
    public String operations() {
        return "forward:/operations/index.html";
    }
}
