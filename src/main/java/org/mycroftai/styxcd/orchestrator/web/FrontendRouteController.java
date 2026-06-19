package org.mycroftai.styxcd.orchestrator.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FrontendRouteController {

    @GetMapping({"/dashboard", "/dashboard/"})
    public String dashboard() {
        return "forward:/dashboard/index.html";
    }

    @GetMapping({"/operations", "/operations/"})
    public String operations() {
        return "forward:/operations/index.html";
    }

    @GetMapping({"/docs", "/docs/"})
    public String docs() {
        return "forward:/docs/index.html";
    }

    @GetMapping({"/docs/{section:[^\\.]+}", "/docs/{section:[^\\.]+}/"})
    public String docsSection(@PathVariable String section) {
        return "forward:/docs/" + section + "/index.html";
    }

    @GetMapping({"/docs/{section:[^\\.]+}/{page:[^\\.]+}", "/docs/{section:[^\\.]+}/{page:[^\\.]+}/"})
    public String docsPage(
            @PathVariable String section,
            @PathVariable String page
    ) {
        return "forward:/docs/" + section + "/" + page + "/index.html";
    }

    @GetMapping({"/yaml-spec", "/yaml-spec/"})
    public String yamlSpec() {
        return "forward:/yaml-spec/index.html";
    }

    @GetMapping({"/yaml-spec/{page:[^\\.]+}", "/yaml-spec/{page:[^\\.]+}/"})
    public String yamlSpecPage(@PathVariable String page) {
        return "forward:/yaml-spec/" + page + "/index.html";
    }
}