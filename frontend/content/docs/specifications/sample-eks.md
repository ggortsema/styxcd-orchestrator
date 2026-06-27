# Sample EKS Workflow

This sample shows a StyxCD cloud workflow targeting an Amazon EKS sandbox environment. It deploys the Johnny-Johnny backend and UI applications to EKS, creates Kubernetes `Deployment` and `Service` resources, configures an ALB-backed ingress, and prepares Route53 DNS records for the public frontend and backend hosts.

```yaml
workflow: cloud_workflow

release:
  name: johnny-platform-release
  version: 1.0.0

  desired_state:
    environment: present
    applications: deployed

    # Other valid desired_state examples:
    #
    # environment: absent
    # applications: none
    #
    # environment: ephemeral
    # applications: deployed
    #
    # environment: present
    # applications: built

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
            image: 359546647832.dkr.ecr.us-east-1.amazonaws.com/johnny-johnny/johnny-johnny-backend:latest

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
            image: 359546647832.dkr.ecr.us-east-1.amazonaws.com/johnny-johnny/johnny-johnny-ui:latest

  environments:
    sandbox:
      - name: johnny-johnny-eks-sandbox
        platform:
          name: eks
          cluster_name: johnny-johnny-dev
          region: us-east-1
          namespace: johnny-johnny

          credentials:
            source: jenkins
            access_key_id: aws-access-key-id
            secret_access_key: aws-secret-access-key

          defaults:
            replicas: 1
            service:
              type: ClusterIP

          ingress:
            enabled: true
            name: johnny-johnny-ingress
            class_name: alb
            hosts:
              - host: johnny-johnny.mycroftai.org
                routes:
                  - path: /
                    path_type: Prefix
                    service: johnny-johnny-ui
                    port: 80

              - host: api.johnny-johnny.mycroftai.org
                routes:
                  - path: /
                    path_type: Prefix
                    service: johnny-johnny-backend
                    port: 80

          dns:
            enabled: true
            provider: route53
            hosted_zone: mycroftai.org
            record_name: johnny-johnny.mycroftai.org.
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
                  value: http://api.johnny-johnny.mycroftai.org
```
