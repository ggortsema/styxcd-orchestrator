import { getAllDocs, getSections } from '../../lib/docs';
import { cardStyle, mutedText, palette } from '../../components/docs/DocStyles';

export default function DocsPage() {
  const sections = getSections();
  const docs = getAllDocs();

  return (
    <div style={{ display: 'grid', gap: 24 }}>
      <section style={{ ...cardStyle, padding: 28 }}>
        <div style={{ color: '#60a5fa', fontWeight: 700, fontSize: 13, letterSpacing: 1.4, textTransform: 'uppercase' }}>StyxCD Documentation</div>
        <h1 style={{ margin: '10px 0 10px', fontSize: 42, lineHeight: 1.05 }}>Docs dashboard</h1>
        <p style={{ ...mutedText, maxWidth: 820, fontSize: 17 }}>
          A polished markdown viewer for ADRs, architecture, specifications, workflows, observability, Kharon, backlog, and archive material. These pages use dummy docs now; the next step is replacing this data with markdown packaged from the docs repository by the orchestrator build.
        </p>
        <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap', marginTop: 22 }}>
          <span style={{ background: '#082f49', border: '1px solid #075985', color: '#bae6fd', borderRadius: 999, padding: '7px 11px' }}>Markdown source</span>
          <span style={{ background: '#1e1b4b', border: '1px solid #4338ca', color: '#c7d2fe', borderRadius: 999, padding: '7px 11px' }}>Orchestrator-backed soon</span>
          <span style={{ background: '#052e16', border: '1px solid #166534', color: '#bbf7d0', borderRadius: 999, padding: '7px 11px' }}>{docs.length} dummy docs</span>
        </div>
      </section>

      <section style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: 16 }}>
        {sections.map((section) => (
          <a key={section.key} href={`/docs/${section.key}/`} style={{ ...cardStyle, display: 'grid', gap: 12, textDecoration: 'none', color: palette.text }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', gap: 12 }}>
              <div style={{ color: '#93c5fd', fontWeight: 700 }}>{section.accent}</div>
              <div style={{ color: palette.muted }}>{section.docs.length} doc</div>
            </div>
            <h2 style={{ margin: 0, fontSize: 22 }}>{section.title}</h2>
            <p style={{ ...mutedText, margin: 0 }}>{section.description}</p>
          </a>
        ))}
      </section>
    </div>
  );
}
