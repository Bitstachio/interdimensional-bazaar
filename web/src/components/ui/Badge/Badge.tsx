/**
 * Badge — Numeric indicator overlaid on Navbar icons.
 *
 * Behaviour:
 *   - Renders nothing when count is 0.
 *   - Caps display at 99 — shows "99+" for counts above that threshold
 *     to prevent the badge from overflowing its container
 */

import { cn } from "@/lib/utils/cn";

export type BadgeProps = {
  count: number;
  className?: string;
};

const MAX_DISPLAY_COUNT = 99;

export const Badge = ({ count, className }: BadgeProps) => {
  if (count <= 0) return null;

  return (
    <span
      className={cn(
        "bg-accent text-background absolute -top-1.5 -right-1.5 flex h-4 min-w-4 items-center justify-center rounded-full px-1 text-[10px] font-bold",
        className,
      )}
    >
      {count > MAX_DISPLAY_COUNT ? "99+" : count}
    </span>
  );
};
