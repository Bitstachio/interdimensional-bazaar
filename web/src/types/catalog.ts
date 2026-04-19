import type { components } from "./schema";

type Schemas = components["schemas"];

export type Category = Schemas["CategoryResponse"];

export type CatalogPageInfo = Schemas["PageMetadata"];

export type Product = Schemas["ProductResponse"];
/** @alias Product — same as {@link Product} */
export type CatalogProduct = Product;
export type PagedProductsResponse = Schemas["PagedModelProductResponse"];
export type ProductReview = Schemas["ReviewResponse"];
export type ProductCreateInput = Schemas["ProductCreateRequest"];
export type ProductUpdateInput = Schemas["ProductUpdateRequest"];
export type CategoryCreateInput = Schemas["CategoryCreateRequest"];
export type CategoryUpdateInput = Schemas["CategoryUpdateRequest"];
