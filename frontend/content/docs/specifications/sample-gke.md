# Sample GKE Workflow

This sample shows a StyxCD cloud workflow targeting a Google Kubernetes Engine sandbox environment. It deploys the Johnny-Johnny backend and UI applications to GKE, uses Google Artifact Registry images, creates Kubernetes services, configures GCE ingress, and manages public DNS through Route53.

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
      - name: johnny-johnny-gke-sandbox
        platform:
          name: gke
          project_id: styxcd-sandbox-grant
          cluster_name: styxcd-sandbox-gke
          location: us-east1-b
          location_type: zonal
          namespace: johnny-johnny

          credentials:
            source: jenkins
            id: gcp-service-account

          defaults:
            replicas: 1
            service:
              type: ClusterIP

          ingress:
            enabled: true
            name: johnny-johnny-ingress
            class_name: gce
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
