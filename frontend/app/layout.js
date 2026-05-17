export const metadata = {
  title: 'StyxCD Orchestrator',
  description: 'Static Next.js frontend served by Spring Boot'
};

const navLinkStyle = {
  display: 'block',
  padding: '10px 12px',
  borderRadius: 8,
  color: '#cbd5e1',
  textDecoration: 'none'
};

export default function RootLayout({ children }) {
  return (
      <html lang="en">
      <body style={{ fontFamily: 'Arial, sans-serif', margin: 0, background: '#0f172a', color: '#e2e8f0' }}>
      <div style={{ display: 'grid', gridTemplateColumns: '240px 1fr', minHeight: '100vh' }}>
        <aside style={{ borderRight: '1px solid #334155', padding: 20, background: '#020617' }}>
          <div style={{ fontWeight: 700, marginBottom: 24 }}>MycroftAI StyxCD</div>

          <nav style={{ display: 'grid', gap: 8 }}>
            <a href="/" style={navLinkStyle}>Dashboard</a>
            <a href="/operations/" style={navLinkStyle}>Operations</a>
            <a href="/docs/" style={navLinkStyle}>Docs</a>
          </nav>
        </aside>

        <main style={{ padding: 24 }}>
          {children}
        </main>
      </div>
      </body>
      </html>
  );
}