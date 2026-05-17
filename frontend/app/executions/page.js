'use client';

import { useEffect, useState } from 'react';

const API_BASE_URL = 'http://orchestrator.styxcd.com';

const defaultYml = `workflow: 'cloud_workflow'
release:
  name: styxcd-jenkins-build
  version: 1.0.0

  applications:
    spring:
      - name: styxcd-jenkins
        repo: https://github.com/ggortsema/styxcd-jenkins.git
        branch: main
        build_tool: gradle`;

export default function ExecutionsPage() {
    const [activeTab, setActiveTab] = useState('current');
    const [yml, setYml] = useState(defaultYml);
    const [executionId, setExecutionId] = useState('');
    const [status, setStatus] = useState('');
    const [currentLifecycleEvent, setCurrentLifecycleEvent] = useState('');
    const [isRunning, setIsRunning] = useState(false);
    const [consoleLines, setConsoleLines] = useState([]);

    const log = (message) => {
        setConsoleLines((lines) => [
            ...lines,
            `[${new Date().toLocaleTimeString()}] ${message}`
        ]);
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

                        <button
                            onClick={submitExecution}
                            disabled={isRunning}
                            style={buttonStyle}
                        >
                            {isRunning ? 'Running...' : 'Submit YML'}
                        </button>
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

const buttonStyle = {
    marginTop: 12,
    padding: '10px 14px',
    borderRadius: 8,
    border: '1px solid #2563eb',
    background: '#1d4ed8',
    color: 'white',
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