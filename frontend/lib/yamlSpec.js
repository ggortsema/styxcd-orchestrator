import fs from 'fs';
import path from 'path';

const specRoot = path.join(process.cwd(), 'content', 'yaml-spec');
const navFile = path.join(specRoot, 'spec-nav.yml');

function titleFromMarkdown(content, fallback) {
  const heading = String(content).split('\n').find((line) => line.startsWith('# '));
  return heading ? heading.replace(/^#\s+/, '').trim() : fallback;
}

function titleFromSlug(slug) {
  return slug.split('-').map((part) => part.charAt(0).toUpperCase() + part.slice(1)).join(' ');
}

function readMarkdown(slug) {
  const filePath = path.join(specRoot, `${slug}.md`);
  if (!fs.existsSync(filePath)) return null;
  const content = fs.readFileSync(filePath, 'utf8');
  return {
    slug,
    title: titleFromMarkdown(content, titleFromSlug(slug)),
    sourcePath: `yaml-spec/${slug}.md`,
    content
  };
}

function parseSpecNavLine(line) {
  const titleMatch = line.match(/^\s*-?\s*title:\s*(.+)\s*$/);
  if (titleMatch) return { key: 'title', value: titleMatch[1].trim().replace(/^['\"]|['\"]$/g, '') };

  const pathMatch = line.match(/^\s*path:\s*(.+)\s*$/);
  if (pathMatch) return { key: 'path', value: pathMatch[1].trim().replace(/^['\"]|['\"]$/g, '') };

  return null;
}

function parseSpecNav() {
  if (!fs.existsSync(navFile)) {
    return {
      title: 'YAML Specification',
      items: [
        { title: 'Overview', path: 'overview' },
        { title: 'Hello World', path: 'hello-world' }
      ]
    };
  }

  const lines = fs.readFileSync(navFile, 'utf8').split('\n');
  const nav = { title: 'YAML Specification', items: [] };
  let currentItem = null;

  for (const line of lines) {
    const parsed = parseSpecNavLine(line);
    if (!parsed) continue;

    if (line.trim().startsWith('title:') && nav.items.length === 0 && !currentItem) {
      nav.title = parsed.value;
      continue;
    }

    if (line.trim().startsWith('- title:')) {
      currentItem = { title: parsed.value };
      nav.items.push(currentItem);
      continue;
    }

    if (parsed.key === 'path' && currentItem) {
      currentItem.path = parsed.value;
    }
  }

  nav.items = nav.items.filter((item) => item.title && item.path);
  return nav;
}

export function getSpecNav() {
  const nav = parseSpecNav();
  return {
    ...nav,
    items: nav.items.map((item) => ({
      ...item,
      doc: readMarkdown(item.path)
    })).filter((item) => item.doc)
  };
}

export function getSpecPages() {
  return getSpecNav().items.map((item) => item.doc);
}

export function getSpecPage(slug) {
  return getSpecPages().find((page) => page.slug === slug) || null;
}

export function getFirstSpecPage() {
  return getSpecPages()[0] || null;
}
