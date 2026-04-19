import { mockCatalog } from "./mock";

/** URL segment for a category, e.g. "Potions" → "potions" */
export const categoryNameToSlug = (name: string) => {
  return name
    .toLowerCase()
    .trim()
    .replace(/&/g, "and")
    .replace(/[^a-z0-9]+/g, "-")
    .replace(/^-|-$/g, "");
};

export const getDistinctCategorySlugs = () => {
  const seen = new Set<string>();
  for (const item of mockCatalog.content) {
    seen.add(categoryNameToSlug(item.categoryName));
  }
  return [...seen].sort();
};

export const getProductsForCategorySlug = (slug: string) => {
  return mockCatalog.content.filter((p) => categoryNameToSlug(p.categoryName) === slug);
};

export const resolveCategoryLabel = (slug: string) => {
  for (const item of mockCatalog.content) {
    if (categoryNameToSlug(item.categoryName) === slug) {
      return item.categoryName;
    }
  }
  return null;
};
