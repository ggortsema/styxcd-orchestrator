export default function DashboardPage() {
    return (
        <div style={{ display: 'grid', gap: 16 }}>
            <h1 style={{ margin: 0 }}>Dashboard</h1>
            <p style={{ color: '#94a3b8' }}>
                The active execution workspace has moved to the Executions section.
            </p>
            <a href="/Users/grant/dev/ai-ecosystem/styxcd/styxcd-orchestrator/frontend/app/operations/" style={{ color: '#93c5fd' }}>Open Executions</a>
        </div>
    );
}