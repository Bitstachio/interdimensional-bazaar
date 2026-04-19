"use client";

import { Icon } from "@/components/icon";
import { ROUTES } from "@/lib/constants/routes";
import { cn } from "@/lib/utils/cn";
import { formatPrice } from "@/lib/utils/currency";
import Link from "next/link";
import { useState, type ReactNode } from "react";
import type { CatalogProduct } from "@/types/catalog";
import { Category } from "../types";

type CatalogViewProps = {
  category: Category;
  /** Loaded on the server — avoids client `fetch`, CORS, and exposing `NEXT_PUBLIC_API_BASE_URL` for this page. */
  products: CatalogProduct[];
};

// TODO: Replace with actual filters from API
const PRICE_FILTERS = ["$0 - $75", "$76 - $150", "$151 - $250", "$251+"] as const;
const REVIEW_FILTERS = ["4 stars & up", "3 stars & up", "2 stars & up"] as const;
const AVAILABILITY_FILTERS = ["In Stock", "Out of Stock"] as const;

const CatalogView = ({ category, products }: CatalogViewProps) => {
  const [showFilters, setShowFilters] = useState(true);
  const label = category.name ?? "Catalog";

  return (
    <div className="bg-background text-foreground min-h-[calc(100vh-4rem)]">
      <div className="mx-auto max-w-7xl px-6 py-8">
        <header className="mb-8">
          <p className="text-muted text-sm tracking-wide uppercase">Catalog</p>
          <h1 className="text-strong mt-1 text-3xl font-bold tracking-tight">{label}</h1>
          <p className="text-muted mt-2 max-w-2xl text-sm">
            {products.length} item{products.length === 1 ? "" : "s"} in this category.
          </p>
        </header>

        <div className="flex flex-col gap-8 lg:flex-row">
          {/* Filters sidebar */}
          <aside
            className={cn(
              "border-border bg-surface w-full shrink-0 rounded-lg border lg:w-64",
              !showFilters && "hidden lg:block",
            )}
          >
            <FilterSection title="Shop By Price">
              {PRICE_FILTERS.map((label) => (
                <label
                  key={label}
                  className="text-foreground hover:text-strong flex cursor-pointer items-center gap-2 py-1.5 text-sm"
                >
                  <input type="checkbox" className="border-border accent-accent rounded" readOnly />
                  {label}
                </label>
              ))}
            </FilterSection>

            <FilterSection title="Reviews">
              {REVIEW_FILTERS.map((label) => (
                <label
                  key={label}
                  className="text-foreground hover:text-strong flex cursor-pointer items-center gap-2 py-1.5 text-sm"
                >
                  <input type="checkbox" className="border-border accent-accent rounded" readOnly />
                  <Icon name="star" className="text-muted h-3.5 w-3.5" />
                  {label}
                </label>
              ))}
            </FilterSection>

            <FilterSection title="Category">
              <p className="text-muted py-1 text-xs">Browsing: {label}</p>
            </FilterSection>

            <FilterSection title="Availability" last>
              {AVAILABILITY_FILTERS.map((label) => (
                <label
                  key={label}
                  className="text-foreground hover:text-strong flex cursor-pointer items-center gap-2 py-1.5 text-sm"
                >
                  <input type="checkbox" className="border-border accent-accent rounded" readOnly />
                  {label}
                </label>
              ))}
            </FilterSection>
          </aside>

          {/* Main column */}
          <div className="min-w-0 flex-1">
            <div className="border-border bg-surface mb-6 flex flex-wrap items-center justify-between gap-4 rounded-lg border px-4 py-3">
              <button
                type="button"
                onClick={() => setShowFilters((v) => !v)}
                className="text-foreground hover:text-strong inline-flex items-center gap-2 text-sm font-medium"
              >
                <Icon name="filter" className="h-4 w-4" />
                {showFilters ? "Hide Filters" : "Show Filters"}
              </button>

              <label className="text-muted flex items-center gap-2 text-sm">
                <span className="whitespace-nowrap">Sort by</span>
                <select
                  // TODO: Invoke API to get sorted products
                  className="border-border bg-elevated text-strong focus:ring-accent rounded-md border px-3 py-1.5 text-sm focus:ring-2 focus:outline-none"
                >
                  <option value="featured">Featured</option>
                  <option value="price-asc">Price: Low to High</option>
                  <option value="price-desc">Price: High to Low</option>
                  <option value="rating">Rating</option>
                </select>
              </label>
            </div>

            <ul className="grid grid-cols-1 gap-6 sm:grid-cols-2 xl:grid-cols-3">
              {products.map((product) => (
                <li key={product.id}>
                  <article className="border-border bg-elevated flex h-full flex-col overflow-hidden rounded-lg border">
                    <Link
                      href={ROUTES.PRODUCT(product.slug ?? "")}
                      className="bg-surface relative block aspect-square"
                    >
                      {/* eslint-disable-next-line @next/next/no-img-element -- external placeholder; avoids remotePatterns config */}
                      <img src={product.imageUrl} alt="" className="h-full w-full object-cover" />
                    </Link>
                    <div className="flex flex-1 flex-col p-4">
                      <div className="flex items-start justify-between gap-2">
                        <Link href={ROUTES.PRODUCT(product.slug ?? "")} className="text-strong group">
                          <h2 className="group-hover:text-accent line-clamp-2 text-base font-semibold">
                            {product.name}
                          </h2>
                        </Link>
                        <button
                          type="button"
                          className="text-muted hover:text-accent shrink-0"
                          aria-label={`Save ${product.name} to wishlist`}
                        >
                          <Icon name="heart" className="h-5 w-5" />
                        </button>
                      </div>
                      <div className="text-muted mt-2 flex items-center gap-1 text-sm">
                        <Icon name="star" className="h-3.5 w-3.5" />
                        <span>{(product.rating ?? 0).toFixed(1)}</span>
                      </div>
                      <p className="text-muted mt-1 text-xs">{product.categoryName}</p>
                      <p className="text-strong mt-3 text-lg font-bold">
                        {formatPrice(product.price ?? 0)}
                      </p>
                      <button
                        type="button"
                        className="border-border text-strong hover:border-accent hover:text-accent mt-4 w-full rounded-full border-2 py-2 text-sm font-semibold transition-colors"
                      >
                        Add to Cart
                      </button>
                    </div>
                  </article>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CatalogView;

const FilterSection = ({ title, children, last }: { title: string; children: ReactNode; last?: boolean }) => {
  const [open, setOpen] = useState(true);
  return (
    <div className={cn("px-4 py-3", !last && "border-border border-b")}>
      <button
        type="button"
        onClick={() => setOpen((o) => !o)}
        className="text-strong flex w-full items-center justify-between text-sm font-semibold"
      >
        {title}
        <Icon name="chevron-down" className={cn("h-4 w-4 transition-transform", open && "rotate-180")} />
      </button>
      {open && <div className="mt-2 space-y-1">{children}</div>}
    </div>
  );
};
