import { redirect } from 'next/navigation';
import { getFirstSpecPage } from '../../lib/yamlSpec';

export default function YamlSpecIndexPage() {
  const firstPage = getFirstSpecPage();
  redirect(firstPage ? `/yaml-spec/${firstPage.slug}/` : '/docs/');
}
