import { api } from "@/lib/api";
import { Category } from "../types";

export const getCategoryBySlug = async (slug: string) =>
  api<Category>(`/api/categories/slug/${slug}`, { next: { revalidate: 60 } });
