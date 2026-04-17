/**
 * wishlistStore — Zustand store for wishlist state.
 *
 * Wishlist state must persist across page refreshes for both guests and
 * logged-in users, and must be accessible from multiple places.
 *
 * Business rules enforced by this store:
 *   - Any user (guest or logged-in) can add items to the wishlist
 *   - Wishlist persists in localStorage for both guests and logged-in users
 *   - On login, guest wishlist merges with the user's saved wishlist —
 *     duplicates are ignored
 *   - Logging out does NOT clear the wishlist
 *
 * TODO: BACKEND — When the real API is ready:
 *   1. For logged-in users, sync wishlist to the server on every change
 *      so it is accessible across devices. Only the query hooks and
 *      useLogin.ts need updating — this store's interface stays the same.
 *   2. mergeGuestWishlist should call a backend merge endpoint rather than
 *      operating purely in localStorage.
 */

import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { WishlistItem, WishlistState, WishlistActions } from "@/features/wishlist/types/wishlist";

type WishlistStore = WishlistState & WishlistActions;

export const useWishlistStore = create<WishlistStore>()(
  persist(
    (set, get) => ({
      // State

      /** All items currently in the wishlist. */
      items: [],

      // Actions

      /**
       * Adds a product to the wishlist.
       * If the product is already in the wishlist, ignore.
       */
      addItem: (item: WishlistItem) =>
        set((state: WishlistStore) => {
          const alreadyExists = state.items.some((i: WishlistItem) => i.id === item.id);

          if (alreadyExists) return state;

          return { items: [...state.items, item] };
        }),

      /**
       * Removes a product from the wishlist by product id.
       */
      removeItem: (id: string) =>
        set((state: WishlistStore) => ({
          items: state.items.filter((i: WishlistItem) => i.id !== id),
        })),

      /**
       * Toggles a product in the wishlist.
       * If it exists, removes it. If it doesn't, adds it.
       * Used by the wishlist heart button.
       */
      toggleItem: (item: WishlistItem) => {
        const exists = get().items.some((i: WishlistItem) => i.id === item.id);

        if (exists) {
          get().removeItem(item.id);
        } else {
          get().addItem(item);
        }
      },

      /**
       * Returns true if the given product id is in the wishlist.
       * Used to render the heart icon in its filled or outlined state.
       */
      isInWishlist: (id: string) => get().items.some((i: WishlistItem) => i.id === id),

      /**
       * Merges a guest wishlist into the current wishlist on login.
       * Duplicates are ignored.
       *
       * TODO: BACKEND — Once server wishlist sync is implemented, this
       * method should merge the localStorage guest wishlist into the
       * server wishlist via an API call, then clear localStorage.
       */
      mergeGuestWishlist: (guestItems: WishlistItem[]) =>
        set((state: WishlistStore) => {
          const existingIds = new Set(state.items.map((i: WishlistItem) => i.id));

          const newItems = guestItems.filter((i: WishlistItem) => !existingIds.has(i.id));

          return { items: [...state.items, ...newItems] };
        }),

      /**
       * Clears all items from the wishlist.
       * Not called on logout.
       * Available for explicit user action (e.g. "Clear Wishlist" button).
       */
      clearWishlist: () => set({ items: [] }),

      /**
       * Total number of items in the wishlist.
       * Used for the Navbar wishlist icon badge count.
       */
      getTotalItems: () => get().items.length,
    }),
    {
      name: "bazaar-wishlist",
    },
  ),
);
