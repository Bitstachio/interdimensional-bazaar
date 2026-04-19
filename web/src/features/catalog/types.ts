import { mockCatalog } from "./mock";

export type CatalogProduct = (typeof mockCatalog.content)[number];

export type Category = {
  id: string;
  name: string;
  slug: string;
  description?: string;
  createdAt: string;
  // TODO: Add `updatedAt` if appropriate
};
