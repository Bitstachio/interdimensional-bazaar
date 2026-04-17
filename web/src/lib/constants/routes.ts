/**
 * ROUTES — Central registry of all application route paths.
 *
 * Usage:
 *   import { ROUTES } from "@/lib/constants/routes";
 *
 *   router.push(ROUTES.CART);
 *   router.push(ROUTES.PRODUCT("abc123"));
 *   router.push(ROUTES.ADMIN.DASHBOARD);
 */

export const ROUTES = {
  // Public

  /** Home page */
  HOME: "/",

  /** About page */
  ABOUT: "/about",

  /**
   * Product detail page
   * @param id - The unique product identifier.
   * @example ROUTES.PRODUCT("portal-gun-v2")
   */
  PRODUCT: (id: string) => `/products/${id}`,

  // Shopping

  /** Shopping cart page */
  CART: "/cart",

  /** Wishlist page */
  WISHLIST: "/wishlist",

  /**
   * Checkout page, only accessible to authenticated users.
   * Guests are redirected to LOGIN with a callbackUrl param.
   */
  CHECKOUT: "/checkout",

  /**
   * Order confirmation page, shown after a successful order placement.
   * @param orderId - The unique order identifier.
   * @example ROUTES.ORDER_CONFIRMATION("order-789")
   */
  ORDER_CONFIRMATION: (orderId: string) => `/order-confirmation/${orderId}`,

  // Auth

  /** Login page */
  LOGIN: "/login",

  /** Register page */
  REGISTER: "/register",

  /** Authenticated user account page */
  ACCOUNT: "/account",

  // Admin

  ADMIN: {
    /** Admin dashboard, only accessible to users with role "admin". */
    DASHBOARD: "/admin",
  },
} as const;
