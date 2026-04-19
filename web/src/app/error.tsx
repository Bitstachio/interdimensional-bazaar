"use client";

import { ROUTES } from "@/lib/constants/routes";
import Link from "next/link";
import { useEffect } from "react";

type ErrorPageProps = {
  error: Error & { digest?: string };
  reset: () => void;
};

/**
 * Route-level error UI (App Router). Must be a Client Component.
 * Catches errors in `page` and nested layouts below this segment, not in the root `layout.tsx`.
 */
const ErrorPage = ({ error, reset }: ErrorPageProps) => {
  useEffect(() => {
    console.error(error);
  }, [error]);

  return (
    <div className="bg-background text-foreground flex min-h-[calc(100vh-4rem)] flex-col items-center justify-center px-6 py-16">
      <div className="border-border bg-surface max-w-md rounded-lg border p-8 text-center shadow-sm">
        <p className="text-muted text-sm tracking-wide uppercase">Something went wrong</p>
        <h1 className="text-strong mt-2 text-2xl font-bold tracking-tight">Dimensional Instability</h1>
        <p className="text-muted mt-3 text-sm leading-relaxed">
          This slice of the multiverse failed to load. You can try again or head back to safer coordinates.
        </p>
        {process.env.NODE_ENV === "development" && (
          <p className="text-muted mt-4 rounded-md bg-black/20 px-3 py-2 text-left font-mono text-xs break-all">
            {error.message}
          </p>
        )}
        <div className="mt-8 flex flex-col gap-3 sm:flex-row sm:justify-center">
          <button
            type="button"
            onClick={reset}
            className="bg-accent text-background hover:bg-accent-hover inline-flex items-center justify-center rounded-md px-4 py-2.5 text-sm font-semibold transition-colors focus:outline-none focus-visible:ring-2 focus-visible:ring-(--focus-ring)"
          >
            Try again
          </button>
          <Link
            href={ROUTES.HOME}
            className="border-border text-foreground hover:bg-elevated inline-flex items-center justify-center rounded-md border px-4 py-2.5 text-sm font-medium transition-colors focus:outline-none focus-visible:ring-2 focus-visible:ring-(--focus-ring)"
          >
            Go home
          </Link>
        </div>
      </div>
    </div>
  );
};

export default ErrorPage;
