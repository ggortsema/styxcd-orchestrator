export const metadata = {
  title: 'Orchestrator Dashboard',
  description: 'Static Next.js frontend served by Spring Boot'
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body style={{ fontFamily: 'Arial, sans-serif', margin: 0, background: '#0f172a', color: '#e2e8f0' }}>
        <header style={{ padding: '16px 24px', borderBottom: '1px solid #334155' }}>
          <strong>MycroftAI StyxCD Orchestrator</strong>
        </header>
        <main style={{ padding: '24px' }}>{children}</main>
      </body>
    </html>
  );
}
