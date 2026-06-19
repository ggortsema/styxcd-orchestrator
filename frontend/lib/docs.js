import fs from 'fs';
import path from 'path';

const docsRoot = path.join(process.cwd(), 'content', 'docs');

const sectionOrder = [
  'architecture',
  'adr',
  'specifications',
  'workflows',
  'observability',
  'kharon',
  'backlog',
  'archive'
];

const sectionMeta = {
  architecture: {
    title: 'Architecture',
    description: 'System design, control-plane boundaries, and product architecture decisions.',
    accent: 'Control Plane'
  },
  adr: {
    title: 'ADRs',
    description: 'Architecture decision records that capture durable StyxCD design decisions.',
    accent: 'Decisions'
  },
  specifications: {
    title: 'Specifications',
    description: 'Formal contracts for YAML, execution plans, callbacks, and docs APIs.',
    accent: 'Contracts'
  },
  workflows: {
    title: 'Workflows',
    description: 'Workflow guides for GKE, EKS, ECS, and future platform targets.',
    accent: 'Delivery'
  },
  observability: {
    title: 'Observability',
    description: 'Logging, metrics, tracing, validation, and future SLO-aware behavior.',
    accent: 'Telemetry'
  },
  kharon: {
    title: 'Kharon',
    description: 'RAG, release intelligence, and historical execution context for StyxCD.',
    accent: 'Reasoning'
  },
  backlog: {
    title: 'Backlog',
    description: 'Planning docs, epics, stories, and implementation roadmap material.',
    accent: 'Planning'
  },
  archive: {
    title: 'Archive',
    description: 'Historical notes, handoffs, and superseded material preserved for context.',
    accent: 'History'
  }
};

function titleFromMarkdown(content, fallback) {
  const heading = String(content).split('\n').find((line) => line.startsWith('# '));
  return heading ? heading.replace(/^#\s+/, '').trim() : fallback;
}

function titleFromSlug(slug) {
  return slug.split('-').map((part) => part.charAt(0).toUpperCase() + part.slice(1)).join(' ');
}

function readSectionDocs(sectionKey) {
  const sectionPath = path.join(docsRoot, sectionKey);
  if (!fs.existsSync(sectionPath)) return [];

  return fs
    .readdirSync(sectionPath)
    .filter((file) => file.endsWith('.md'))
    .sort()
    .map((file) => {
      const slug = file.replace(/\.md$/, '');
      const sourcePath = `${sectionKey}/${file}`;
      const content = fs.readFileSync(path.join(sectionPath, file), 'utf8');
      return {
        slug,
        title: titleFromMarkdown(content, titleFromSlug(slug)),
        status: sectionKey === 'archive' ? 'Archived' : 'Draft',
        sourcePath,
        lastUpdated: 'Dummy content',
        format: 'markdown',
        content
      };
    });
}

export function getSections() {
  return sectionOrder.map((key) => ({
    key,
    ...sectionMeta[key],
    docs: readSectionDocs(key)
  }));
}

export function getSection(section) {
  const meta = sectionMeta[section];
  if (!meta) return null;
  return { key: section, ...meta, docs: readSectionDocs(section) };
}

export function getDoc(section, slug) {
  const group = getSection(section);
  if (!group) return null;
  const doc = group.docs.find((item) => item.slug === slug);
  return doc ? { ...doc, section: group } : null;
}

export function getAllDocs() {
  return getSections().flatMap((section) =>
    section.docs.map((doc) => ({ ...doc, section }))
  );
}
