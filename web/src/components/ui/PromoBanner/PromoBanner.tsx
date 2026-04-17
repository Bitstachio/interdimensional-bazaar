/**
 * PromoBanner — Promotional announcement bar.
 *
 * Behaviour:
 *   - Always visible, not dismissible
 *   - Accepts a message prop for flexibility across different promotions
 *   - Full width, centered text
 */

import { cn } from "@/lib/utils/cn";

export type PromoBannerProps = {
  message: string;
  className?: string;
};

export const PromoBanner = ({ message, className }: PromoBannerProps) => (
  <div
    className={cn("bg-elevated text-strong w-full px-4 py-3 text-center text-sm font-bold tracking-widest", className)}
  >
    {message}
  </div>
);
