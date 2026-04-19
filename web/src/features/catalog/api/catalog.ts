import { api } from "@/lib/api";
import type { Category, PagedProductsResponse } from "@/types/catalog";

export const getCategoryBySlug = async (slug: string) =>
  api<Category>(`/api/categories/slug/${slug}`, { next: { revalidate: 60 } });

export const getProductsByCategory = async (categoryId: string) =>
  api<PagedProductsResponse>(`/api/products?categoryId=${categoryId}`, { next: { revalidate: 60 } });
