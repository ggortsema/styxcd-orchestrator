export default function HomePage() {
    return (
        <div style={{ display: 'grid', gap: 16 }}>
            <h1 style={{ margin: 0 }}>StyxCD Control Plane</h1>
            <p style={{ color: '#94a3b8' }}>
                Orchestrate builds, deployments, observability, and workflow execution from a single control plane.
            </p>

            <div style={{ padding: 16, border: '1px solid #334155', borderRadius: 12, background: '#111827' }}>
                <h2 style={{ marginTop: 0 }}>Getting Started</h2>
                <p>
                    Use the Executions section to submit YML, trigger the orchestrator, and watch current lifecycle status.
                </p>
                <a href="/operations/" style={{ color: '#93c5fd' }}>Open Operations</a>
            </div>
        </div>
    );
}