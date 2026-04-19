import { notFound } from "next/navigation";

export const getCategoryBySlug = async (slug: string) => {
  // TODO: Use appropriate base URL
  const url = `http://localhost:8080/api/categories/slug/${slug}`;
  const res = await fetch(url, { next: { revalidate: 60 } }); // TODO: Revalidate?

  if (res.status === 404) notFound();
  if (!res.ok) throw new Error("Failed to fetch category");
  return res.json();
};
