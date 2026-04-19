import { getProductsForCategorySlug } from "../categorySlug";

const useCatalog = (slug: string) => {
  return {
    label: "Test Category",
    products: getProductsForCategorySlug(slug),
  };
};

export default useCatalog;
