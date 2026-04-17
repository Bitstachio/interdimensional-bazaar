/**
 * authStore — Zustand store for authentication and user session state.
 *
 * Zustand maintains authentication state in localStorage so users remain
 * logged in across page refreshes until they explicitly log out.
 * Logging out clears the session but does NOT clear cart or wishlist.
 */

import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { User, AuthState, AuthActions } from "@/features/auth/types/auth";

type AuthStore = AuthState & AuthActions;

export const useAuthStore = create<AuthStore>()(
  persist(
    (set) => ({
      // State

      /** The currently authenticated user, or null if guest. */
      user: null,

      /** Whether the user is authenticated. */
      isAuthenticated: false,

      // Actions

      /**
       * Called after a successful login or registration.
       * Sets the user and flips isAuthenticated to true.
       */
      setUser: (user: User) =>
        set({
          user,
          isAuthenticated: true,
        }),

      /**
       * Clears the session on logout.
       * Cart and wishlist stores are NOT cleared here.
       */
      logout: () =>
        set({
          user: null,
          isAuthenticated: false,
        }),

      /**
       * Updates specific fields on the user profile without replacing
       * the entire user object. Used by the account edit page.
       */
      updateUser: (updates: Partial<User>) =>
        set((state: AuthStore) => ({
          user: state.user ? { ...state.user, ...updates } : null,
        })),
    }),
    {
      /**
       * Unique key for localStorage. Prefixed with "bazaar-" to avoid
       * collisions with other apps running on the same origin.
       */
      name: "bazaar-auth",
    },
  ),
);
