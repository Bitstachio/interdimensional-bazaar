/**
 * themeStore — Zustand store for managing the application theme.
 *
 * Zustand keeps the user's theme preference in localStorage
 * so it survives page refreshes.
 */

import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { Theme, ThemeState, ThemeActions } from "@/features/theme/types/theme";

type ThemeStore = ThemeState & ThemeActions;

export const useThemeStore = create<ThemeStore>()(
  persist(
    (set) => ({
      // State

      /** Current active theme. Defaults to dark. */
      theme: "dark" as Theme,

      // Actions

      /** Toggles between dark and light theme. */
      toggleTheme: () =>
        set((state: ThemeStore) => ({
          theme: state.theme === "dark" ? "light" : "dark",
        })),

      /**
       * Sets a specific theme directly.
       * Used by ThemeProvider on initial mount from localStorage. */
      setTheme: (theme: Theme) => set({ theme }),
    }),
    {
      /** Unique localStorage key. */
      name: "bazaar-theme",
    },
  ),
);
