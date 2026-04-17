"use client";

/**
 * Providers — Root provider wrapper for the entire application.
 *
 * Adding new providers:
 * If a new library requires a context provider (e.g. a toast library, an
 * analytics provider), add it here — not in layout.tsx. Keep layout.tsx thin.
 */

import { useState } from "react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { ThemeProvider } from "@/features/theme/components/ThemeProvider";

type ProvidersProps = {
  children: React.ReactNode;
};

export const Providers = ({ children }: ProvidersProps) => {
  /**
   * Default options:
   * - staleTime: 60s — data is considered fresh for 60 seconds after fetching,
   *   preventing unnecessary refetches when navigating between pages.
   * - retry: 1 — failed requests are retried once before surfacing an error,
   *   balancing resilience against hanging the UI on persistent failures.
   */
  const [queryClient] = useState(
    () =>
      new QueryClient({
        defaultOptions: {
          queries: {
            staleTime: 60 * 1000,
            retry: 1,
          },
        },
      }),
  );

  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider>{children}</ThemeProvider>
      {process.env.NODE_ENV === "development" && <ReactQueryDevtools initialIsOpen={false} />}
    </QueryClientProvider>
  );
};
