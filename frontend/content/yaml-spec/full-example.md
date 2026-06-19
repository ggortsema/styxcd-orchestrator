# Full Example

This example shows a starter StyxCD workflow for a backend and frontend application deployed to an EKS-like Kubernetes environment.

```yaml
workflow: cloud_workflow

release:
  name: johnny-platform-release
  version: 1.0.0

  desired_state:
    environment: present
    applications: deployed

  applications:

    spring:
      - name: johnny-johnny-backend
        repo: https://github.com/ggortsema/johnny-johnny.git
        branch: main
        version: 1.0.0

        build:
          type: maven-docker
          project_path: .
          module_path: chat-api
          dockerfile: chat-api/Dockerfile
          docker_context: .

        artifacts:
          - type: docker-image
            image: us-east1-docker.pkg.dev/styxcd-sandbox-grant/styxcd-sandbox/johnny-johnny-backend:latest

    node:
      - name: johnny-johnny-ui
        repo: https://github.com/ggortsema/johnny-johnny-ui.git
        branch: main
        version: 1.0.0

        build:
          type: docker
          project_path: .
          dockerfile: Dockerfile
          docker_context: .

        artifacts:
          - type: docker-image
            image: us-east1-docker.pkg.dev/styxcd-sandbox-grant/styxcd-sandbox/johnny-johnny-ui:latest

  environments:
    sandbox:
      - name: johnny-johnny-eks-sandbox

        platform:
          name: eks
          cluster_name: styxcd-sandbox-eks
          region: us-east-1
          namespace: johnny-johnny

        credentials:
          source: jenkins
          id: aws-service-account

        defaults:
          replicas: 1
          service:
            type: ClusterIP

        ingress:
          enabled: true
          name: johnny-johnny-ingress
          class_name: alb
          hosts:
            - host: johnny-johnny.styxcd.com
              routes:
                - path: /
                  path_type: Prefix
                  service: johnny-johnny-ui
                  port: 80

            - host: api.johnny-johnny.styxcd.com
              routes:
                - path: /
                  path_type: Prefix
                  service: johnny-johnny-backend
                  port: 80

        dns:
          enabled: true
          provider: route53
          hosted_zone: styxcd.com
          record_name: johnny-johnny.styxcd.com.
          record_type: A
          ttl: 300
          credentials:
            source: jenkins
            access_key_id: aws-access-key-id
            secret_access_key: aws-secret-access-key

        applications:
          - name: johnny-johnny-backend
            container:
              port: 8080
            service:
              port: 80
              target_port: 8080
            health_check:
              path: /actuator/health
              port: 8080
              type: HTTP
            secrets:
              - env_name: OPENAI_API_KEY
                source:
                  type: jenkins-credential
                  credential_id: openai-api-key

          - name: johnny-johnny-ui
            container:
              port: 3000
            service:
              port: 80
              target_port: 3000
            env:
              - name: NEXT_PUBLIC_API_URL
                value: http://api.johnny-johnny.styxcd.com
```

## Notes

This example is intentionally early.

The EKS and GKE platform models will likely diverge over time. For now, the spec treats both as Kubernetes deployment targets with mostly similar application, service, ingress, and environment configuration.