package org.mycroftai.styxcd.orchestrator.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendForwardController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "forward:/dashboard/index.html";
    }

    @GetMapping("/dashboard/")
    public String dashboardSlash() {
        return "forward:/dashboard/index.html";
    }
}
