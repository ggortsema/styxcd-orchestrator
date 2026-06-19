import { palette } from './DocStyles';

function slugify(text) {
  return String(text)
    .toLowerCase()
    .replace(/[^a-z0-9\s-]/g, '')
    .trim()
    .replace(/\s+/g, '-');
}

function parseInline(text) {
  const parts = String(text).split(/(`[^`]+`|\*\*[^*]+\*\*)/g);
  return parts.map((part, index) => {
    if (part.startsWith('`') && part.endsWith('`')) {
      return (
        <code key={index} style={{ background: '#020617', color: '#bfdbfe', border: `1px solid ${palette.border}`, borderRadius: 6, padding: '2px 6px' }}>
          {part.slice(1, -1)}
        </code>
      );
    }
    if (part.startsWith('**') && part.endsWith('**')) {
      return <strong key={index}>{part.slice(2, -2)}</strong>;
    }
    return part;
  });
}

function renderTable(lines, key) {
  const rows = lines
    .filter((line) => !/^\s*\|?\s*-{3,}/.test(line.replace(/\|/g, ' | ')))
    .map((line) => line.trim().replace(/^\|/, '').replace(/\|$/, '').split('|').map((cell) => cell.trim()));

  const [header, ...body] = rows;
  return (
    <div key={key} style={{ overflowX: 'auto', margin: '20px 0' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', border: `1px solid ${palette.border}`, borderRadius: 12 }}>
        <thead>
          <tr>
            {header.map((cell, index) => (
              <th key={index} style={{ textAlign: 'left', padding: 12, background: '#020617', borderBottom: `1px solid ${palette.border}`, color: palette.soft }}>
                {parseInline(cell)}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {body.map((row, rowIndex) => (
            <tr key={rowIndex}>
              {row.map((cell, cellIndex) => (
                <td key={cellIndex} style={{ padding: 12, borderTop: `1px solid ${palette.border}`, color: palette.text }}>
                  {parseInline(cell)}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export function extractHeadings(markdown) {
  return String(markdown)
    .split('\n')
    .filter((line) => /^#{2,3}\s+/.test(line))
    .map((line) => {
      const depth = line.startsWith('###') ? 3 : 2;
      const title = line.replace(/^#{2,3}\s+/, '').trim();
      return { depth, title, id: slugify(title) };
    });
}

export default function DocRenderer({ content }) {
  const lines = String(content || '').split('\n');
  const nodes = [];
  let index = 0;

  while (index < lines.length) {
    const line = lines[index];

    if (!line.trim()) {
      index += 1;
      continue;
    }

    if (line.startsWith('```')) {
      const language = line.replace('```', '').trim();
      const code = [];
      index += 1;
      while (index < lines.length && !lines[index].startsWith('```')) {
        code.push(lines[index]);
        index += 1;
      }
      index += 1;
      nodes.push(
        <div key={nodes.length} style={{ margin: '22px 0', border: `1px solid ${palette.border}`, borderRadius: 14, overflow: 'hidden', background: '#020617' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', padding: '9px 12px', borderBottom: `1px solid ${palette.border}`, color: palette.muted, fontSize: 12 }}>
            <span>{language || 'code'}</span>
            <span>copy-ready</span>
          </div>
          <pre style={{ margin: 0, padding: 16, overflowX: 'auto', color: '#dbeafe', lineHeight: 1.6 }}>
            <code>{code.join('\n')}</code>
          </pre>
        </div>
      );
      continue;
    }

    if (line.startsWith('|') && lines[index + 1] && lines[index + 1].includes('---')) {
      const tableLines = [];
      while (index < lines.length && lines[index].startsWith('|')) {
        tableLines.push(lines[index]);
        index += 1;
      }
      nodes.push(renderTable(tableLines, nodes.length));
      continue;
    }

    if (line.startsWith('# ')) {
      const title = line.replace(/^#\s+/, '').trim();
      nodes.push(<h1 key={nodes.length} id={slugify(title)} style={{ fontSize: 38, lineHeight: 1.1, margin: '0 0 18px' }}>{title}</h1>);
      index += 1;
      continue;
    }

    if (line.startsWith('## ')) {
      const title = line.replace(/^##\s+/, '').trim();
      nodes.push(<h2 key={nodes.length} id={slugify(title)} style={{ fontSize: 24, margin: '32px 0 12px', paddingTop: 8 }}>{title}</h2>);
      index += 1;
      continue;
    }

    if (line.startsWith('### ')) {
      const title = line.replace(/^###\s+/, '').trim();
      nodes.push(<h3 key={nodes.length} id={slugify(title)} style={{ fontSize: 18, margin: '26px 0 10px', color: palette.soft }}>{title}</h3>);
      index += 1;
      continue;
    }

    if (line.startsWith('> ')) {
      const quoteLines = [];
      while (index < lines.length && lines[index].startsWith('> ')) {
        quoteLines.push(lines[index].replace(/^>\s?/, ''));
        index += 1;
      }
      nodes.push(
        <blockquote key={nodes.length} style={{ margin: '22px 0', padding: '14px 16px', borderLeft: '4px solid #60a5fa', background: 'rgba(30,64,175,0.16)', borderRadius: 12, color: palette.soft }}>
          {quoteLines.map((quote, quoteIndex) => <p key={quoteIndex} style={{ margin: quoteIndex ? '10px 0 0' : 0 }}>{parseInline(quote)}</p>)}
        </blockquote>
      );
      continue;
    }

    if (/^-\s+/.test(line)) {
      const items = [];
      while (index < lines.length && /^-\s+/.test(lines[index])) {
        items.push(lines[index].replace(/^-\s+/, '').trim());
        index += 1;
      }
      nodes.push(
        <ul key={nodes.length} style={{ margin: '14px 0 20px', paddingLeft: 22, color: palette.text, lineHeight: 1.7 }}>
          {items.map((item, itemIndex) => <li key={itemIndex}>{parseInline(item)}</li>)}
        </ul>
      );
      continue;
    }

    const paragraph = [line.trim()];
    index += 1;
    while (index < lines.length && lines[index].trim() && !/^#|^-\s+|^>\s+|^```|^\|/.test(lines[index])) {
      paragraph.push(lines[index].trim());
      index += 1;
    }
    nodes.push(<p key={nodes.length} style={{ color: palette.soft, lineHeight: 1.75, margin: '0 0 16px' }}>{parseInline(paragraph.join(' '))}</p>);
  }

  return <article>{nodes}</article>;
}
