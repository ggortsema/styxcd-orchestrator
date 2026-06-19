# Environments

The `environments` block describes where applications should run.

```yaml
environments:
  sandbox:
    - name: johnny-johnny-eks-sandbox
```

## Platform

The `platform` block identifies the target runtime.

```yaml
platform:
  name: eks
  cluster_name: styxcd-sandbox-eks
  region: us-east-1
  namespace: johnny-johnny
```

For now, GKE and EKS are treated as mostly similar Kubernetes targets. Platform-specific fields will become stricter as the spec matures.

## Credentials

```yaml
credentials:
  source: jenkins
  id: aws-service-account
```

## Defaults

```yaml
defaults:
  replicas: 1
  service:
    type: ClusterIP
```

## Ingress

```yaml
ingress:
  enabled: true
  name: johnny-johnny-ingress
  class_name: alb
```

## DNS

```yaml
dns:
  enabled: true
  provider: route53
  hosted_zone: styxcd.com
  record_name: johnny-johnny.styxcd.com.
  record_type: A
  ttl: 300
```

## Environment Applications

Environment application entries describe how each application runs in the target platform.

```yaml
applications:
  - name: johnny-johnny-backend
    container:
      port: 8080
    service:
      port: 80
      target_port: 8080
```

## Secrets

```yaml
secrets:
  - env_name: OPENAI_API_KEY
    source:
      type: jenkins-credential
      credential_id: openai-api-key
```

## Environment Variables

```yaml
env:
  - name: NEXT_PUBLIC_API_URL
    value: http://api.johnny-johnny.styxcd.com
```