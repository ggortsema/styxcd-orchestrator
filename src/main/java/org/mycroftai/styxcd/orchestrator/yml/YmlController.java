package org.mycroftai.styxcd.orchestrator.yml;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.mycroftai.styxcd.orchestrator.plan.ExecutionPlanService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/yml")
public class YmlController {

    private final ObjectMapper ymlMapper =
            new ObjectMapper(new YAMLFactory());

    private final ExecutionPlanService executionPlanService;

    public YmlController(ExecutionPlanService executionPlanService) {
        this.executionPlanService = executionPlanService;
    }

    @PostMapping("/parse")
    public Map<String, Object> parse(@RequestBody String yml) throws Exception {
        return ymlMapper.readValue(
                yml,
                new TypeReference<Map<String, Object>>() {}
        );
    }

    @PostMapping("/plan")
    public Map<String, Object> plan(@RequestBody String yml) throws Exception {
        Map<String, Object> parsedYml = parse(yml);
        return executionPlanService.createPlan(parsedYml);
    }
}