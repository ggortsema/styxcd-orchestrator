import { notFound } from 'next/navigation';
import { getSection, getSections } from '../../../lib/docs';
import { DocsShell } from '../../../components/docs/DocsLayout';
import { cardStyle, mutedText, palette } from '../../../components/docs/DocStyles';

export function generateStaticParams() {
  return getSections().map((section) => ({ section: section.key }));
}

export default async function DocSectionPage({ params }) {
  const { section: sectionKey } = await params;
  const section = getSection(sectionKey);
  if (!section) notFound();

  const tocContent = `## ${section.title}\n## Documents`;

  return (
    <DocsShell activeSection={section.key} tocContent={tocContent}>
      <section style={{ ...cardStyle, padding: 26 }}>
        <div style={{ color: '#60a5fa', fontWeight: 700, fontSize: 13, letterSpacing: 1.4, textTransform: 'uppercase' }}>{section.accent}</div>
        <h1 style={{ margin: '8px 0 10px', fontSize: 34 }}>{section.title}</h1>
        <p style={{ ...mutedText, marginTop: 0 }}>{section.description}</p>
      </section>

      <div style={{ display: 'grid', gap: 14, marginTop: 18 }}>
        {section.docs.map((doc) => (
          <a key={doc.slug} href={`/docs/${section.key}/${doc.slug}/`} style={{ ...cardStyle, display: 'grid', gap: 8, color: palette.text, textDecoration: 'none' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', gap: 12, flexWrap: 'wrap' }}>
              <h2 style={{ margin: 0, fontSize: 21 }}>{doc.title}</h2>
              <span style={{ color: palette.muted }}>{doc.status}</span>
            </div>
            <p style={{ ...mutedText, margin: 0 }}>{doc.sourcePath}</p>
          </a>
        ))}
      </div>
    </DocsShell>
  );
}
