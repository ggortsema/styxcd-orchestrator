export default function DashboardPage() {
  return (
    <div style={{ display: 'grid', gap: 16 }}>
      <h1 style={{ margin: 0 }}>Dashboard</h1>
      <p>This page is part of the static Next.js export bundled into the Spring Boot app.</p>
      <ul>
        <li><a href="/hello?name=world" style={{ color: '#93c5fd' }}>/hello?name=world</a></li>
        <li><a href="/hello?name=john" style={{ color: '#93c5fd' }}>/hello?name=john</a></li>
      </ul>
      <a href="/" style={{ color: '#93c5fd' }}>Back home</a>
    </div>
  );
}
