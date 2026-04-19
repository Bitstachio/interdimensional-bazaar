import { getCategoryBySlug } from "@/features/catalog/api/catalog";
import CatalogView from "@/features/catalog/components/CatalogView";
import type { Metadata } from "next";

type CatalogCategoryPageProps = {
  params: Promise<{ categorySlug: string }>;
};

export const generateMetadata = async ({ params }: CatalogCategoryPageProps): Promise<Metadata> => {
  const { categorySlug } = await params;
  const category = await getCategoryBySlug(categorySlug);

  return {
    title: `${category.name} - Interdimensional Bazaar`,
    description: category.description ?? `Browse ${category.name} products`,
  };
};

const CatalogCategoryPage = async ({ params }: CatalogCategoryPageProps) => {
  const { categorySlug } = await params;
  return <CatalogView slug={categorySlug} />;
};

export default CatalogCategoryPage;
