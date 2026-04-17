"use client";

/**
 * Navbar — Primary navigation bar for the entire application.
 *
 * Structure (left to right):
 *   1. Logo — links to home/PLP
 *   2. Nav links — Categories dropdown, New Arrivals, Clearance
 *   3. Search bar — full width input with search icon
 *   4. Wishlist icon — with badge count, links to /wishlist
 *   5. Cart icon — with badge count, links to /cart
 *   6. Account icon — links to /account (logged in) or /login (guest)
 *   7. Theme toggle — dark/light switch
 */

import Logo from "@/assets/images/logo.svg";
import Link from "next/link";
import { useState } from "react";
import { cn } from "@/lib/utils/cn";
import { Icon } from "@/components/icon";
import { Badge } from "@/components/ui/Badge/Badge";
import { ROUTES } from "@/lib/constants/routes";
import { useCartStore } from "@/features/cart/store/cartStore";
import { useWishlistStore } from "@/features/wishlist/store/wishlistStore";
import { useAuthStore } from "@/features/auth/store/authStore";

const CATEGORIES = [
  "Travel & Exploration",
  "Gadgets & Enhancements",
  "Weapons & Power Gears",
  "Everyday & Utility",
] as const;

export const Navbar = () => {
  const [isCategoriesOpen, setIsCategoriesOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");

  const cartCount = useCartStore((state) => state.getTotalItems());
  const wishlistCount = useWishlistStore((state) => state.getTotalItems());
  const { isAuthenticated } = useAuthStore();

  const handleSearchSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!searchQuery.trim()) return;
    window.location.href = `${ROUTES.HOME}?search=${encodeURIComponent(searchQuery.trim())}`;
  };

  return (
    <header className="border-border bg-surface border-b">
      <nav className="mx-auto flex max-w-7xl items-center gap-6 px-6 py-3">
        {/* Logo */}
        <Link href={ROUTES.HOME} className="shrink-0">
          <Logo className="h-16 w-auto" aria-label="Bazaar — Rick and Morty Interdimensional Shop" />
        </Link>

        {/* Nav Links */}
        <div className="flex items-center gap-1">
          {/* Categories dropdown */}
          <div className="relative">
            <button
              type="button"
              onClick={() => setIsCategoriesOpen((prev) => !prev)}
              className="text-foreground hover:text-strong flex items-center gap-1 rounded-md px-3 py-2 text-sm font-medium transition-colors"
            >
              <Icon
                name="chevron-down"
                className={cn("h-4 w-4 transition-transform duration-200", isCategoriesOpen && "rotate-180")}
              />
              Categories
            </button>

            {/* Dropdown menu */}
            {isCategoriesOpen && (
              <div className="border-border bg-elevated absolute top-full left-0 z-50 mt-1 w-56 rounded-md border py-1 shadow-lg">
                {CATEGORIES.map((category: string) => (
                  <Link
                    key={category}
                    href={`${ROUTES.HOME}?category=${encodeURIComponent(category)}`}
                    onClick={() => setIsCategoriesOpen(false)}
                    className="text-foreground hover:bg-surface hover:text-strong block px-4 py-2 text-sm transition-colors"
                  >
                    {category}
                  </Link>
                ))}
              </div>
            )}
          </div>

          <span className="text-border">|</span>

          <Link
            href={`${ROUTES.HOME}?sort=new`}
            className="text-foreground hover:text-strong rounded-md px-3 py-2 text-sm font-medium transition-colors"
          >
            New Arrivals
          </Link>

          <span className="text-border">|</span>

          <Link
            href={`${ROUTES.HOME}?sort=clearance`}
            className="text-foreground hover:text-strong rounded-md px-3 py-2 text-sm font-medium transition-colors"
          >
            Clearance
          </Link>
        </div>

        {/* Search Bar */}
        <form
          onSubmit={handleSearchSubmit}
          className="border-border bg-elevated focus-within:border-accent flex flex-1 items-center rounded-full border px-4 py-2 transition-colors"
        >
          <Icon name="search" className="text-muted h-4 w-4 shrink-0" />
          <input
            type="text"
            value={searchQuery}
            onChange={(e: React.ChangeEvent<HTMLInputElement>) => setSearchQuery(e.target.value)}
            placeholder="Search"
            className="text-foreground placeholder:text-muted ml-2 flex-1 bg-transparent text-sm focus:outline-none"
          />
        </form>

        {/* Icon Actions */}
        <div className="flex items-center gap-4">
          {/* Wishlist */}
          <Link
            href={ROUTES.WISHLIST}
            aria-label="Wishlist"
            className="text-foreground hover:text-strong relative transition-colors"
          >
            <Icon name="heart" className="h-6 w-6" />
            <Badge count={wishlistCount} />
          </Link>

          {/* Cart */}
          <Link
            href={ROUTES.CART}
            aria-label="Cart"
            className="text-foreground hover:text-strong relative transition-colors"
          >
            <Icon name="cart" className="h-6 w-6" />
            <Badge count={cartCount} />
          </Link>

          {/* Account */}
          <Link
            href={isAuthenticated ? ROUTES.ACCOUNT : ROUTES.LOGIN}
            aria-label="Account"
            className="text-foreground hover:text-strong relative transition-colors"
          >
            <Icon name="account" className="h-6 w-6" />
          </Link>
        </div>
      </nav>
    </header>
  );
};
