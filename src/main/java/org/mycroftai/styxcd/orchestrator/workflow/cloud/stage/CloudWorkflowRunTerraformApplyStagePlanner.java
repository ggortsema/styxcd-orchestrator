package org.mycroftai.styxcd.orchestrator.workflow.cloud.stage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CloudWorkflowRunTerraformApplyStagePlanner implements WorkflowStagePlanner {

    @Override
    public String workflowName() {
        return "cloud_workflow";
    }

    @Override
    public String stageType() {
        return "terraform_apply";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getParams(
            Map<String, Object> yml,
            Map<String, Object> paramMap
    ) {

        Map<String, Object> params = new LinkedHashMap<>();

        Map<String, Object> release =
                (Map<String, Object>) yml.get("release");

        String environmentClass =
                (String) paramMap.get("ENVIRONMENT_CLASS");

        Map<String, Object> environment =
                (Map<String, Object>) paramMap.get("ENVIRONMENT");

        String environmentName =
                (String) environment.get("name");

        Map<String, Object> platform =
                (Map<String, Object>) environment.get("platform");

        String platformName =
                (String) platform.get("name");

        String launchType =
                (String) platform.get("launch_type");

        String region =
                (String) platform.get("region");

        Map<String, Object> infrastructure =
                (Map<String, Object>) platform.get("infrastructure");

        Map<String, Object> credentials =
                (Map<String, Object>) infrastructure.get("credentials");

        Map<String, Object> aws =
                (Map<String, Object>) credentials.get("aws");

        String awsCredentialsId =
                (String) aws.get("id");

        Map<String, Object> terraform =
                (Map<String, Object>) infrastructure.get("terraform");

        String terraformRepo =
                (String) terraform.get("repo");

        String terraformBranch =
                (String) terraform.get("branch");

        String terraformBasePath =
                (String) terraform.get("path");

        /*
         * Build canonical application-set hash
         */

        List<String> appNames = extractApplicationNames(release);

        Collections.sort(appNames);

        String canonicalString =
                "env=" + environmentName +
                        "|apps=" + String.join(",", appNames);

        String hash =
                sha256(canonicalString).substring(0, 12);

        String terraformResolvedPath =
                terraformBasePath +
                        "/" +
                        environmentName +
                        "-appset-" +
                        hash;

        params.put("stageType", stageType());

        params.put(
                "stagename",
                "Terraform Apply - " + environmentName
        );

        params.put("label", "styxcd-agent");

        params.put("releaseName", release.get("name"));
        params.put("releaseVersion", release.get("version"));

        params.put("environmentClass", environmentClass);
        params.put("environmentName", environmentName);

        params.put("platformName", platformName);
        params.put("launchType", launchType);

        params.put("region", region);

        params.put("awsCredentialsId", awsCredentialsId);

        params.put("terraformRepo", terraformRepo);
        params.put("terraformBranch", terraformBranch);

        params.put("terraformBasePath", terraformBasePath);
        params.put("terraformResolvedPath", terraformResolvedPath);

        params.put("canonicalAppSetString", canonicalString);
        params.put("appSetHash", hash);

        params.put("VALIDATE_MAP", paramMap.get("VALIDATE_MAP"));
        params.put("YML", yml);

        return params;
    }

    @SuppressWarnings("unchecked")
    private List<String> extractApplicationNames(
            Map<String, Object> release
    ) {

        List<String> appNames = new ArrayList<>();

        Map<String, Object> applications =
                (Map<String, Object>) release.get("applications");

        for (Object appGroupObject : applications.values()) {

            List<Map<String, Object>> appGroup =
                    (List<Map<String, Object>>) appGroupObject;

            for (Map<String, Object> app : appGroup) {

                appNames.add((String) app.get("name"));
            }
        }

        return appNames;
    }

    private String sha256(String value) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(value.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();

            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed generating SHA-256 hash",
                    e
            );
        }
    }
}
