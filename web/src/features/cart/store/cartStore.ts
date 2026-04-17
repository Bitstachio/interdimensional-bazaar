/**
 * cartStore — Zustand store for shopping cart state.
 *
 * Business rules:
 *   - Item quantity is capped at the product's available inventory (maxQuantity)
 *   - Low stock warning triggers when a product has 5 or fewer units remaining
 *   - Free shipping applies when the subtotal exceeds $120
 *   - Tax is calculated at a flat 13% of the subtotal
 *   - On login, guest cart is merged with the user's saved cart (see useLogin.ts)
 *     Merged quantities are capped at maxQuantity per item
 *
 * TODO: BACKEND — When the real API is ready:
 *   1. Replace localStorage-only persistence with server cart sync for
 *      logged-in users. Only useLogin.ts and the cart query hooks need
 *      updating — this store's interface stays the same.
 *   2. maxQuantity is currently set at add-to-cart time from product data.
 *      Add a cart validation step at checkout that confirms actual inventory
 *      availability against the backend before placing the order.
 *   3. Free shipping threshold ($120), tax rate (13%), and delivery cost
 *      ($8.99) should be driven by backend config rather than constants.
 */

import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { CartItem, CartState, CartActions } from "@/features/cart/types/cart";

export const FREE_SHIPPING_THRESHOLD = 120;
export const DELIVERY_FEE = 8.99;
export const TAX_RATE = 0.13;
export const LOW_STOCK_THRESHOLD = 5;

type CartStore = CartState & CartActions;

export const useCartStore = create<CartStore>()(
  persist(
    (set, get) => ({
      // State

      /** All items currently in the cart. */
      items: [],

      // Actions

      /**
       * Adds a product to the cart.
       * If the product already exists, increments quantity up to maxQuantity.
       * If the product is new, appends it with quantity 1.
       */
      addItem: (item: CartItem) =>
        set((state: CartStore) => {
          const existing = state.items.find((i: CartItem) => i.id === item.id);

          if (existing) {
            return {
              items: state.items.map((i: CartItem) =>
                i.id === item.id
                  ? {
                      ...i,
                      quantity: Math.min(i.quantity + item.quantity, i.maxQuantity),
                    }
                  : i,
              ),
            };
          }

          return { items: [...state.items, item] };
        }),

      /**
       * Removes a product from the cart entirely by product id.
       */
      removeItem: (id: string) =>
        set((state: CartStore) => ({
          items: state.items.filter((i: CartItem) => i.id !== id),
        })),

      /**
       * Sets the quantity of a specific cart item directly.
       * Quantity is between 1 and the item's maxQuantity.
       * To remove an item, use removeItem instead of setting quantity to 0.
       */
      updateQuantity: (id: string, quantity: number) =>
        set((state: CartStore) => ({
          items: state.items.map((i: CartItem) =>
            i.id === id
              ? {
                  ...i,
                  quantity: Math.min(Math.max(1, quantity), i.maxQuantity),
                }
              : i,
          ),
        })),

      /**
       * Merges a guest cart into the current cart on login.
       * If the same product exists in both, quantities are added together
       * and capped at maxQuantity.
       *
       * TODO: BACKEND — Once server cart sync is implemented, this method
       * should merge the localStorage guest cart into the server cart via
       * an API call, then clear localStorage. Currently merges in-memory only.
       */
      mergeGuestCart: (guestItems: CartItem[]) =>
        set((state: CartStore) => {
          const merged = [...state.items];

          guestItems.forEach((guestItem) => {
            const existing = merged.find((i) => i.id === guestItem.id);

            if (existing) {
              existing.quantity = Math.min(existing.quantity + guestItem.quantity, existing.maxQuantity);
            } else {
              merged.push(guestItem);
            }
          });

          return { items: merged };
        }),

      /**
       * Clears all items from the cart.
       * Called after a successful order placement.
       */
      clearCart: () => set({ items: [] }),

      // Computed values

      getTotalItems: () => get().items.reduce((sum: number, item: CartItem) => sum + item.quantity, 0),
      getSubtotal: () => get().items.reduce((sum: number, item: CartItem) => sum + item.price * item.quantity, 0),

      /**
       * Shipping cost — free if subtotal exceeds FREE_SHIPPING_THRESHOLD,
       * otherwise DELIVERY_FEE.
       *
       * TODO: BACKEND — Replace with shipping cost from backend config.
       */
      getShippingCost: () => {
        const subtotal = get().getSubtotal();
        return subtotal >= FREE_SHIPPING_THRESHOLD ? 0 : DELIVERY_FEE;
      },

      /**
       * Estimated tax at TAX_RATE applied to the subtotal only.
       *
       * TODO: BACKEND — Replace with tax calculation from backend config
       * which may vary by region, product category, or user address.
       */
      getTax: () => parseFloat((get().getSubtotal() * TAX_RATE).toFixed(2)),

      getTotal: () => {
        const { getSubtotal, getShippingCost, getTax } = get();
        return parseFloat((getSubtotal() + getShippingCost() + getTax()).toFixed(2));
      },

      /**
       * Returns true if the given product id has 5 or fewer units remaining.
       * Triggers the low stock warning in CartItem and ProductCard.
       */
      isLowStock: (id: string) => {
        const item = get().items.find((i: CartItem) => i.id === id);
        return item ? item.maxQuantity <= LOW_STOCK_THRESHOLD : false;
      },
    }),
    {
      name: "bazaar-cart",
    },
  ),
);
