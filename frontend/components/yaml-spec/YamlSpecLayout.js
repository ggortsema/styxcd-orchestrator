import { getSpecNav } from '../../lib/yamlSpec';
import { extractHeadings } from '../docs/DocRenderer';
import { cardStyle, mutedText, palette } from '../docs/DocStyles';

function SpecNav({ activeSlug }) {
  const nav = getSpecNav();

  return (
    <aside style={{ ...cardStyle, padding: 14, alignSelf: 'start', position: 'sticky', top: 24 }}>
      <div style={{ color: palette.muted, fontSize: 12, textTransform: 'uppercase', letterSpacing: 1.2, margin: '4px 8px 12px' }}>
        Specification
      </div>
      <nav style={{ display: 'grid', gap: 4 }}>
        {nav.items.map((item) => {
          const active = item.path === activeSlug;
          return (
            <a
              key={item.path}
              href={`/yaml-spec/${item.path}/`}
              style={{
                textDecoration: 'none',
                color: active ? '#ffffff' : palette.soft,
                background: active ? 'linear-gradient(135deg, #7c3aed, #2563eb)' : 'transparent',
                border: active ? '1px solid #8b5cf6' : '1px solid transparent',
                borderRadius: 10,
                padding: '10px 12px',
                display: 'flex',
                justifyContent: 'space-between',
                gap: 12
              }}
            >
              <span>{item.title}</span>
              <span style={{ color: active ? '#ede9fe' : palette.muted }}>→</span>
            </a>
          );
        })}
      </nav>
    </aside>
  );
}

function Toc({ content }) {
  const headings = extractHeadings(content);
  return (
    <aside style={{ ...cardStyle, padding: 14, alignSelf: 'start', position: 'sticky', top: 24 }}>
      <div style={{ color: palette.muted, fontSize: 12, textTransform: 'uppercase', letterSpacing: 1.2, marginBottom: 12 }}>
        On this page
      </div>
      {headings.length === 0 ? (
        <p style={{ ...mutedText, margin: 0 }}>Add ## headings to build the page outline.</p>
      ) : (
        <nav style={{ display: 'grid', gap: 8 }}>
          {headings.map((heading) => (
            <a key={heading.id} href={`#${heading.id}`} style={{ color: heading.depth === 3 ? palette.muted : palette.soft, textDecoration: 'none', paddingLeft: heading.depth === 3 ? 12 : 0, fontSize: heading.depth === 3 ? 13 : 14 }}>
              {heading.title}
            </a>
          ))}
        </nav>
      )}
    </aside>
  );
}

function ExamplePanel({ content }) {
  const match = String(content || '').match(/```ya?ml\n([\s\S]*?)```/);
  const yaml = match ? match[1].trim() : 'workflow: cloud_workflow\n\nrelease:\n  name: hello-world\n  version: 0.1.0';

  return (
    <aside style={{ ...cardStyle, padding: 0, alignSelf: 'start', position: 'sticky', top: 24, overflow: 'hidden' }}>
      <div style={{ padding: '13px 14px', borderBottom: `1px solid ${palette.border}`, background: '#020617' }}>
        <div style={{ color: '#c4b5fd', fontSize: 12, textTransform: 'uppercase', letterSpacing: 1.2, fontWeight: 700 }}>Sticky example</div>
        <div style={{ color: palette.muted, fontSize: 13, marginTop: 4 }}>Copy-ready YAML shape</div>
      </div>
      <pre style={{ margin: 0, padding: 16, overflowX: 'auto', color: '#dbeafe', lineHeight: 1.6, fontSize: 13 }}>
        <code>{yaml}</code>
      </pre>
    </aside>
  );
}

export function YamlSpecShell({ activeSlug, children, tocContent }) {
  return (
    <div style={{ display: 'grid', gap: 24 }}>
      <section style={{ ...cardStyle, padding: 28, background: 'linear-gradient(135deg, rgba(88,28,135,0.38), rgba(2,6,23,0.96) 52%, rgba(30,64,175,0.28))' }}>
        <div style={{ color: '#c4b5fd', fontWeight: 700, fontSize: 13, letterSpacing: 1.4, textTransform: 'uppercase' }}>StyxCD Product Contract</div>
        <h1 style={{ margin: '10px 0 10px', fontSize: 42, lineHeight: 1.05 }}>YAML Specification</h1>
        <p style={{ ...mutedText, maxWidth: 820, fontSize: 17 }}>
          A dedicated specification viewer for workflow authors. The content comes from Git, while this UI turns the contract into a navigable product surface.
        </p>
        <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap', marginTop: 22 }}>
          <span style={{ background: '#2e1065', border: '1px solid #7c3aed', color: '#ddd6fe', borderRadius: 999, padding: '7px 11px' }}>Git-backed spec</span>
          <span style={{ background: '#082f49', border: '1px solid #075985', color: '#bae6fd', borderRadius: 999, padding: '7px 11px' }}>Examples first</span>
          <span style={{ background: '#052e16', border: '1px solid #166534', color: '#bbf7d0', borderRadius: 999, padding: '7px 11px' }}>Orchestrator contract</span>
        </div>
      </section>

      <div style={{ display: 'grid', gridTemplateColumns: '230px minmax(0, 1fr) 310px', gap: 20, alignItems: 'start' }}>
        <SpecNav activeSlug={activeSlug} />
        <main>{children}</main>
        <div style={{ display: 'grid', gap: 20 }}>
          <ExamplePanel content={tocContent || ''} />
          <Toc content={tocContent || ''} />
        </div>
      </div>
    </div>
  );
}
