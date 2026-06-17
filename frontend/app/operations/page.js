'use client';

import { useEffect, useRef, useState } from 'react';

const API_BASE_URL = 'http://orchestrator.styxcd.com';

const defaultYml = `workflow: cloud_workflow

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

      - name: johnny-johnny-backend-2
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
          name: eks
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
`;

export default function ExecutionsPage() {
    const [activeTab, setActiveTab] = useState('current');
    const [yml, setYml] = useState(defaultYml);
    const [executionId, setExecutionId] = useState('');
    const [status, setStatus] = useState('');
    const [currentLifecycleEvent, setCurrentLifecycleEvent] = useState('');
    const [isRunning, setIsRunning] = useState(false);
    const [consoleLines, setConsoleLines] = useState([]);
    const fileInputRef = useRef(null);

    const log = (message) => {
        setConsoleLines((lines) => [
            ...lines,
            `[${new Date().toLocaleTimeString()}] ${message}`
        ]);
    };

    const loadFile = () => {
        fileInputRef.current?.click();
    };

    const onFileSelected = (event) => {
        const file = event.target.files?.[0];

        if (!file) {
            return;
        }

        const reader = new FileReader();

        reader.onload = (loadEvent) => {
            setYml(loadEvent.target?.result || '');
            log(`Loaded file: ${file.name}`);
        };

        reader.onerror = () => {
            log(`ERROR loading file: ${file.name}`);
        };

        reader.readAsText(file);
        event.target.value = '';
    };

    const loadUrl = async () => {
        const url = window.prompt('Enter raw YAML URL');

        if (!url) {
            return;
        }

        try {
            log(`Loading YAML from URL...`);

            const response = await fetch(url, {
                cache: 'no-store'
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            const text = await response.text();

            setYml(text);
            log(`Loaded YAML from URL`);
        } catch (error) {
            log(`ERROR loading URL: ${error.message}`);
        }
    };

    const clearScreen = () => {
        setYml('');
        setExecutionId('');
        setStatus('');
        setCurrentLifecycleEvent('');
        setConsoleLines([]);
        setIsRunning(false);
    };

    const submitExecution = async () => {
        setIsRunning(true);
        setExecutionId('');
        setStatus('');
        setCurrentLifecycleEvent('');
        setConsoleLines([]);

        try {
            log('Submitting YML to orchestrator...');

            const response = await fetch(`${API_BASE_URL}/executions`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'text/plain'
                },
                body: yml
            });

            if (!response.ok) {
                throw new Error(`Create execution failed: HTTP ${response.status}`);
            }

            const execution = await response.json();

            setExecutionId(execution.id);
            setStatus(execution.status);
            setCurrentLifecycleEvent(execution.currentLifecycleEvent || '');

            log(`Execution created: ${execution.id}`);
            log(`Current status: ${execution.status}`);
            log(`Lifecycle event: ${execution.currentLifecycleEvent || 'N/A'}`);
        } catch (error) {
            log(`ERROR: ${error.message}`);
            setIsRunning(false);
        }
    };

    useEffect(() => {
        if (!executionId || !isRunning) {
            return;
        }

        const interval = setInterval(async () => {
            try {
                const response = await fetch(`${API_BASE_URL}/executions/${executionId}`, {
                    cache: 'no-store'
                });

                if (!response.ok) {
                    throw new Error(`Status poll failed: HTTP ${response.status}`);
                }

                const execution = await response.json();

                setStatus((previousStatus) => {
                    if (previousStatus !== execution.status) {
                        log(`Status changed: ${previousStatus || 'UNKNOWN'} → ${execution.status}`);
                    }

                    return execution.status;
                });

                setCurrentLifecycleEvent((previousEvent) => {
                    const nextEvent = execution.currentLifecycleEvent || '';

                    if (previousEvent !== nextEvent) {
                        log(`Lifecycle event changed: ${previousEvent || 'UNKNOWN'} → ${nextEvent || 'N/A'}`);
                    }

                    return nextEvent;
                });

                if (execution.status === 'SUCCESS' || execution.status === 'FAILED') {
                    setIsRunning(false);
                    log(`Execution finished with status: ${execution.status}`);
                }
            } catch (error) {
                log(`WARNING: ${error.message}`);
            }
        }, 3000);

        return () => clearInterval(interval);
    }, [executionId, isRunning]);

    return (
        <div style={{ display: 'grid', gap: 20 }}>
            <div>
                <h1 style={{ margin: 0 }}>Executions</h1>
                <p style={{ color: '#94a3b8' }}>
                    Submit YML, trigger Jenkins through the orchestrator, and monitor lifecycle state.
                </p>
            </div>

            <div style={tabBarStyle}>
                <button onClick={() => setActiveTab('current')} style={tabStyle(activeTab === 'current')}>
                    Current
                </button>
                <button onClick={() => setActiveTab('history')} style={tabStyle(activeTab === 'history')}>
                    History
                </button>
                <button onClick={() => setActiveTab('failures')} style={tabStyle(activeTab === 'failures')}>
                    Failures
                </button>
            </div>

            {activeTab === 'current' && (
                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 20 }}>
                    <section style={cardStyle}>
                        <h2 style={{ marginTop: 0 }}>Run YML</h2>

                        <textarea
                            value={yml}
                            onChange={(event) => setYml(event.target.value)}
                            style={textareaStyle}
                        />

                        <input
                            ref={fileInputRef}
                            type="file"
                            accept=".yaml,.yml,.txt"
                            style={{ display: 'none' }}
                            onChange={onFileSelected}
                        />

                        <div style={buttonRowStyle}>
                            <button
                                onClick={loadFile}
                                disabled={isRunning}
                                style={secondaryButtonStyle}
                            >
                                Load File
                            </button>

                            <button
                                onClick={loadUrl}
                                disabled={isRunning}
                                style={secondaryButtonStyle}
                            >
                                Load URL
                            </button>

                            <button
                                onClick={clearScreen}
                                style={secondaryButtonStyle}
                            >
                                Clear
                            </button>

                            <button
                                onClick={submitExecution}
                                disabled={isRunning}
                                style={buttonStyle}
                            >
                                {isRunning ? 'Running...' : 'Submit YML'}
                            </button>
                        </div>
                    </section>

                    <section style={cardStyle}>
                        <h2 style={{ marginTop: 0 }}>Execution Status</h2>

                        <div style={statusGridStyle}>
                            <div>
                                <div style={labelStyle}>Execution ID</div>
                                <div style={valueStyle}>{executionId || 'None yet'}</div>
                            </div>

                            <div>
                                <div style={labelStyle}>Status</div>
                                <div style={valueStyle}>{status || 'Idle'}</div>
                            </div>

                            <div>
                                <div style={labelStyle}>Current Lifecycle Event</div>
                                <div style={valueStyle}>{currentLifecycleEvent || 'N/A'}</div>
                            </div>
                        </div>

                        <h3>Console</h3>
                        <pre style={consoleStyle}>
                            {consoleLines.length ? consoleLines.join('\n') : 'Waiting for execution...'}
                        </pre>
                    </section>
                </div>
            )}

            {activeTab === 'history' && (
                <section style={cardStyle}>
                    <h2 style={{ marginTop: 0 }}>Execution History</h2>
                    <p style={{ color: '#94a3b8' }}>
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. This tab will eventually show
                        recent orchestrator runs, statuses, durations, workflows, and quick links to logs.
                    </p>
                </section>
            )}

            {activeTab === 'failures' && (
                <section style={cardStyle}>
                    <h2 style={{ marginTop: 0 }}>Failed Executions</h2>
                    <p style={{ color: '#94a3b8' }}>
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. This tab will eventually show
                        failed executions, failure stages, error messages, and links to Grafana and Jenkins.
                    </p>
                </section>
            )}
        </div>
    );
}

const cardStyle = {
    padding: 16,
    border: '1px solid #334155',
    borderRadius: 12,
    background: '#111827'
};

const tabBarStyle = {
    display: 'flex',
    gap: 8,
    borderBottom: '1px solid #334155'
};

const tabStyle = (active) => ({
    padding: '10px 14px',
    border: '1px solid #334155',
    borderBottom: active ? '1px solid #111827' : '1px solid #334155',
    borderRadius: '8px 8px 0 0',
    background: active ? '#111827' : '#020617',
    color: active ? '#e2e8f0' : '#94a3b8',
    cursor: 'pointer'
});

const textareaStyle = {
    width: '100%',
    minHeight: 420,
    boxSizing: 'border-box',
    padding: 12,
    borderRadius: 8,
    border: '1px solid #334155',
    background: '#020617',
    color: '#e2e8f0',
    fontFamily: 'Menlo, Monaco, Consolas, monospace',
    fontSize: 13
};

const buttonRowStyle = {
    display: 'flex',
    gap: 8,
    marginTop: 12,
    flexWrap: 'wrap'
};

const buttonStyle = {
    padding: '10px 14px',
    borderRadius: 8,
    border: '1px solid #2563eb',
    background: '#1d4ed8',
    color: 'white',
    cursor: 'pointer'
};

const secondaryButtonStyle = {
    padding: '10px 14px',
    borderRadius: 8,
    border: '1px solid #334155',
    background: '#1e293b',
    color: '#e2e8f0',
    cursor: 'pointer'
};

const statusGridStyle = {
    display: 'grid',
    gap: 12,
    marginBottom: 16
};

const labelStyle = {
    fontSize: 12,
    color: '#94a3b8',
    textTransform: 'uppercase'
};

const valueStyle = {
    marginTop: 4,
    wordBreak: 'break-all'
};

const consoleStyle = {
    minHeight: 260,
    padding: 12,
    borderRadius: 8,
    background: '#020617',
    color: '#bbf7d0',
    overflow: 'auto',
    whiteSpace: 'pre-wrap'
};