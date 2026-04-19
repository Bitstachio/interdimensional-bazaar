import { getCategoryBySlug, getProductsByCategory } from "@/features/catalog/api/catalog";
import CatalogView from "@/features/catalog/components/CatalogView";
import { withNextRouting } from "@/lib/routing";
import type { Metadata } from "next";
import { cache } from "react";

type CatalogCategoryPageProps = {
  params: Promise<{ categorySlug: string }>;
};

export const resolveCategory = cache((slug: string) => withNextRouting(getCategoryBySlug(slug)));

export const generateMetadata = async ({ params }: CatalogCategoryPageProps): Promise<Metadata> => {
  const { categorySlug } = await params;
  const category = await resolveCategory(categorySlug);

  return {
    title: `${category.name} - Interdimensional Bazaar`,
    description: category.description ?? `Browse ${category.name} products`,
  };
};

const CatalogCategoryPage = async ({ params }: CatalogCategoryPageProps) => {
  const { categorySlug } = await params;
  const category = await resolveCategory(categorySlug);
  const products = (await withNextRouting(getProductsByCategory(category.id))).content ?? [];

  return <CatalogView category={category} products={products} />;
};

export default CatalogCategoryPage;
