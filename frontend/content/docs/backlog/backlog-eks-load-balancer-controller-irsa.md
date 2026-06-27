# Backlog: Proper AWS Load Balancer Controller IAM Integration

## Summary

The current EKS workflow can install the AWS Load Balancer Controller and create Kubernetes ingress resources, but ALB reconciliation is blocked when the controller lacks the correct AWS IAM permissions.

During the EKS sandbox workflow, the ingress was accepted by Kubernetes and the backend/UI services were valid, but the AWS Load Balancer Controller failed to build the ALB model because it attempted to call Elastic Load Balancing APIs using the EKS node instance role.

The observed failure was:

```text
AccessDenied:
User: arn:aws:sts::<account>:assumed-role/eksctl-...-NodeInstanceRole-...
is not authorized to perform: elasticloadbalancing:DescribeLoadBalancers
```

## Current Behavior

The current `EksInstallLoadBalancerController` stage installs the controller with Helm:

```text
helm upgrade --install aws-load-balancer-controller eks/aws-load-balancer-controller
```

It sets:

```text
clusterName
region
vpcId
```

but it does not create or attach a dedicated IAM role for the controller service account.

Because of that, the controller pod falls back to the worker node IAM role.

## Desired Future Behavior

Implement proper IAM Roles for Service Accounts (IRSA) for the AWS Load Balancer Controller.

The future implementation should:

1. Ensure the EKS cluster has an OIDC provider associated.
2. Create or reuse the AWS Load Balancer Controller IAM policy.
3. Create an IAM role trusted by the controller service account.
4. Annotate the `aws-load-balancer-controller` service account with:

```yaml
eks.amazonaws.com/role-arn: arn:aws:iam::<account>:role/<controller-role>
```

5. Install the Helm chart using the service account binding, for example:

```text
--set serviceAccount.create=false
--set serviceAccount.name=aws-load-balancer-controller
```

6. Validate after install that the service account has the expected IRSA annotation.
7. Validate ingress reconciliation by confirming that an ALB hostname appears on the ingress.

## Temporary Sandbox Workaround

For sandbox only, it is acceptable to attach broad load balancer permissions to the node role to unblock ALB provisioning.

This is not the long-term design.

The long-term design should not rely on the node instance role for controller permissions.

## Why This Matters

StyxCD should model the AWS Load Balancer Controller as shared EKS platform infrastructure.

Installing the controller is not just a Helm operation. It also has a required cloud IAM contract.

This belongs in the EKS environment/platform lifecycle, not in the application deployment lifecycle.

## Acceptance Criteria

- EKS ingress can reconcile successfully without using the node instance role for ALB permissions.
- `kubectl describe ingress` shows successful ALB reconciliation events.
- The ingress receives a populated ALB hostname.
- DNS can create Route53 alias records pointing to the ALB.
- The controller service account has an explicit IAM role annotation.
- The implementation is idempotent and safe to run repeatedly.
