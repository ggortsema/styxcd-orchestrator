import { notFound } from 'next/navigation';
import { getSpecPage, getSpecPages } from '../../../lib/yamlSpec';
import { YamlSpecShell } from '../../../components/yaml-spec/YamlSpecLayout';
import DocRenderer from '../../../components/docs/DocRenderer';
import { cardStyle, palette } from '../../../components/docs/DocStyles';

export function generateStaticParams() {
  return getSpecPages().map((page) => ({ slug: page.slug }));
}

export default async function YamlSpecPage({ params }) {
  const { slug } = await params;
  const page = getSpecPage(slug);
  if (!page) notFound();

  return (
    <YamlSpecShell activeSlug={page.slug} tocContent={page.content}>
      <div style={{ ...cardStyle, padding: 30 }}>
        <div style={{ display: 'flex', gap: 10, flexWrap: 'wrap', marginBottom: 18 }}>
          <span style={{ background: '#2e1065', border: '1px solid #7c3aed', borderRadius: 999, padding: '6px 10px', color: '#ddd6fe', fontSize: 13 }}>YAML Specification</span>
          <span style={{ background: palette.chip, border: `1px solid ${palette.border}`, borderRadius: 999, padding: '6px 10px', color: palette.muted, fontSize: 13 }}>{page.sourcePath}</span>
        </div>
        <DocRenderer content={page.content} />
      </div>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 16 }}>
        <a href="/yaml-spec/" style={{ color: palette.link, textDecoration: 'none' }}>← YAML Specification home</a>
        <span style={{ color: palette.muted }}>Rendered from spec markdown</span>
      </div>
    </YamlSpecShell>
  );
}
