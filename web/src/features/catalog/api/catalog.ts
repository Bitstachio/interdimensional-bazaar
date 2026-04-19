import { api } from "@/lib/api";
import type { Category, PagedProductsResponse } from "../types";

export const getCategoryBySlug = async (slug: string) =>
  api<Category>(`/api/categories/slug/${slug}`, { next: { revalidate: 60 } });

export const getProductsByCategory = async (categoryId: string) =>
  api<PagedProductsResponse>(`/api/products?category=${categoryId}`, { next: { revalidate: 60 } });
