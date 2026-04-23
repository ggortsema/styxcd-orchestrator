async function getGreeting() {
  try {
    const response = await fetch('http://localhost:8080/hello?name=world', { cache: 'no-store' });
    if (!response.ok) {
      return 'Backend call failed';
    }
    return response.text();
  } catch (error) {
    return 'Backend unavailable during static export';
  }
}

export default async function HomePage() {
  const greeting = await getGreeting();

  return (
    <div style={{ display: 'grid', gap: 16 }}>
      <h1 style={{ margin: 0 }}>Dashboard Home</h1>
      <p>This frontend is exported by Next.js and served as static content by Spring Boot.</p>
      <div style={{ padding: 16, border: '1px solid #334155', borderRadius: 12, background: '#111827' }}>
        <div style={{ fontSize: 14, opacity: 0.8 }}>Sample backend response</div>
        <div style={{ fontSize: 24, marginTop: 8 }}>{greeting}</div>
      </div>
      <a href="/dashboard/" style={{ color: '#93c5fd' }}>Go to dashboard page</a>
    </div>
  );
}
