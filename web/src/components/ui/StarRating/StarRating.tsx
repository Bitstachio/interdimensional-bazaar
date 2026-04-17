"use client";
/**
 * StarRating — Visual star rating display and input component.
 *
 * Modes:
 *   - Read-only (default): purely visual, no interaction.
 *   - Interactive: clickable and hoverable stars for rating input. Used
 *     exclusively on the review form for logged-in users.
 */

import { useState } from "react";
import { cn } from "@/lib/utils/cn";
import { Icon } from "@/components/icon";

const TOTAL_STARS = 5;

type StarSize = "sm" | "md";

export type StarRatingProps = {
  rating: number;
  size?: StarSize;
  interactive?: boolean;
  onChange?: (rating: number) => void;
  className?: string;
};

export const StarRating = ({ rating, size = "md", interactive = false, onChange, className }: StarRatingProps) => {
  const [hoveredIndex, setHoveredIndex] = useState<number | null>(null);

  const filledCount = Math.round(Math.min(Math.max(rating, 0), TOTAL_STARS));

  /**
   * In interactive mode, the displayed fill is driven by the hovered index
   * while hovering, and falls back to the selected rating otherwise.
   * In read-only mode, hoverFill always equals filledCount.
   */
  const activeFill = interactive && hoveredIndex !== null ? hoveredIndex + 1 : filledCount;

  return (
    <div
      className={cn("flex items-center gap-0.5", className)}
      role={interactive ? "group" : "img"}
      aria-label={interactive ? "Select a star rating" : `Rating: ${filledCount} out of ${TOTAL_STARS} stars`}
    >
      {Array.from({ length: TOTAL_STARS }, (_: unknown, index: number) => {
        const isFilled = index < activeFill;

        return interactive ? (
          <button
            key={index}
            type="button"
            aria-label={`${index + 1} star`}
            onClick={() => onChange?.(index + 1)}
            onMouseEnter={() => setHoveredIndex(index)}
            onMouseLeave={() => setHoveredIndex(null)}
            onKeyDown={(e: React.KeyboardEvent<HTMLButtonElement>) => {
              if (e.key === "Enter" || e.key === " ") onChange?.(index + 1);
            }}
            className="bg-transparent p-0"
          >
            <Icon
              name="star"
              className={cn(
                "h-6 w-6 cursor-pointer transition-transform duration-150 hover:scale-110",
                isFilled ? "text-accent" : "text-muted",
              )}
            />
          </button>
        ) : (
          <Icon
            key={index}
            name="star"
            className={cn(
              "transition-transform duration-150",
              {
                "h-3.5 w-3.5": size === "sm",
                "h-4 w-4": size === "md",
              },
              isFilled ? "text-accent" : "text-muted",
            )}
          />
        );
      })}
    </div>
  );
};
