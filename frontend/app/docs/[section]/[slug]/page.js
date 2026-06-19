import { notFound } from 'next/navigation';
import { getAllDocs, getDoc } from '../../../../lib/docs';
import { DocsShell, DocMeta } from '../../../../components/docs/DocsLayout';
import DocRenderer from '../../../../components/docs/DocRenderer';
import { cardStyle, palette } from '../../../../components/docs/DocStyles';

export function generateStaticParams() {
  return getAllDocs().map((doc) => ({ section: doc.section.key, slug: doc.slug }));
}

export default async function DocDetailPage({ params }) {
  const { section: sectionKey, slug } = await params;
  const doc = getDoc(sectionKey, slug);
  if (!doc) notFound();

  return (
    <DocsShell activeSection={sectionKey} tocContent={doc.content}>
      <div style={{ ...cardStyle, padding: 30 }}>
        <DocMeta doc={doc} />
        <DocRenderer content={doc.content} />
      </div>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 16 }}>
        <a href={`/docs/${sectionKey}/`} style={{ color: palette.link, textDecoration: 'none' }}>← Back to {doc.section.title}</a>
        <span style={{ color: palette.muted }}>Rendered from markdown</span>
      </div>
    </DocsShell>
  );
}
