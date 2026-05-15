[condition][]A deployment request is for environment "{env}"=deployment: DeploymentRequest(environment == "{env}")
[condition][]The application risk level is "{risk}"=Application(riskLevel == "{risk}")
[condition][]The application is customer facing=Application(customerFacing == true)

[consequence][]Require approval from "{group}"=deployment.requireApproval("{group}");
[consequence][]Mark deployment as "{status}"=deployment.setStatus("{status}");
[consequence][]Require deployment strategy "{strategy}"=deployment.requireStrategy("{strategy}");
[consequence][]Add policy message "{message}"=deployment.addPolicyMessage("{message}");