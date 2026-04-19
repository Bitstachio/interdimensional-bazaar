import { ROUTES } from "@/lib/constants/routes";
import Link from "next/link";

/**
 * Shown when `notFound()` is called or the route does not exist.
 * Server Component — keep lightweight; no client JS required.
 */
const NotFoundPage = () => (
  <div className="bg-background text-foreground flex min-h-[calc(100vh-4rem)] flex-col items-center justify-center px-6 py-16">
    <div className="border-border bg-surface max-w-md rounded-lg border p-8 text-center shadow-sm">
      <p className="text-muted text-sm tracking-wide uppercase">404</p>
      <h1 className="text-strong mt-2 text-2xl font-bold tracking-tight">Lost in the Multiverse</h1>
      <p className="text-muted mt-3 text-sm leading-relaxed">
        This page does not exist in any dimension we can reach from here.
      </p>
      <Link
        href={ROUTES.HOME}
        className="bg-accent text-background hover:bg-accent-hover mt-8 inline-flex items-center justify-center rounded-md px-4 py-2.5 text-sm font-semibold transition-colors focus:outline-none focus-visible:ring-2 focus-visible:ring-(--focus-ring)"
      >
        Back to the Bazaar
      </Link>
    </div>
  </div>
);

export default NotFoundPage;
