import { getSections } from '../../lib/docs';
import { cardStyle, mutedText, palette } from './DocStyles';
import { extractHeadings } from './DocRenderer';

function SectionNav({ activeSection }) {
  const sections = getSections();
  return (
    <aside style={{ ...cardStyle, padding: 14, alignSelf: 'start', position: 'sticky', top: 24 }}>
      <div style={{ color: palette.muted, fontSize: 12, textTransform: 'uppercase', letterSpacing: 1.2, margin: '4px 8px 12px' }}>
        Documentation
      </div>
      <nav style={{ display: 'grid', gap: 4 }}>
        {sections.map((section) => {
          const active = section.key === activeSection;
          return (
            <a
              key={section.key}
              href={`/docs/${section.key}/`}
              style={{
                textDecoration: 'none',
                color: active ? '#ffffff' : palette.soft,
                background: active ? '#1d4ed8' : 'transparent',
                border: active ? '1px solid #3b82f6' : '1px solid transparent',
                borderRadius: 10,
                padding: '10px 12px',
                display: 'flex',
                justifyContent: 'space-between',
                gap: 12
              }}
            >
              <span>{section.title}</span>
              <span style={{ color: active ? '#dbeafe' : palette.muted }}>{section.docs.length}</span>
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
        <p style={{ ...mutedText, margin: 0 }}>Headings will appear here when the selected document has sections.</p>
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

export function DocsShell({ activeSection, children, tocContent }) {
  return (
    <div style={{ display: 'grid', gap: 24 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', gap: 16, alignItems: 'flex-start' }}>
        <div>
          <div style={{ color: '#60a5fa', fontWeight: 700, fontSize: 13, letterSpacing: 1.4, textTransform: 'uppercase' }}>StyxCD Docs</div>
          <h1 style={{ margin: '8px 0 0', fontSize: 36 }}>Documentation Center</h1>
          <p style={{ ...mutedText, maxWidth: 760, margin: '10px 0 0' }}>
            Markdown docs packaged through the orchestrator and presented as a polished dashboard experience.
          </p>
        </div>
        <a href="/docs/" style={{ color: palette.link, textDecoration: 'none', border: `1px solid ${palette.border}`, borderRadius: 999, padding: '9px 13px', background: '#020617' }}>
          Docs home
        </a>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '230px minmax(0, 1fr) 230px', gap: 20, alignItems: 'start' }}>
        <SectionNav activeSection={activeSection} />
        <main>{children}</main>
        <Toc content={tocContent || ''} />
      </div>
    </div>
  );
}

export function DocMeta({ doc }) {
  return (
    <div style={{ display: 'flex', gap: 10, flexWrap: 'wrap', marginBottom: 18 }}>
      <span style={{ background: palette.chip, border: `1px solid ${palette.border}`, borderRadius: 999, padding: '6px 10px', color: palette.soft, fontSize: 13 }}>{doc.section.title}</span>
      <span style={{ background: palette.chip, border: `1px solid ${palette.border}`, borderRadius: 999, padding: '6px 10px', color: palette.soft, fontSize: 13 }}>{doc.status}</span>
      <span style={{ background: palette.chip, border: `1px solid ${palette.border}`, borderRadius: 999, padding: '6px 10px', color: palette.muted, fontSize: 13 }}>{doc.sourcePath}</span>
    </div>
  );
}
