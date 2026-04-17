/**
 * Button — Base button component used throughout the application.
 *
 * Variants:
 *   - primary: filled background, used for main CTAs (Add to Cart, Checkout)
 *   - outline: transparent with border, used for secondary actions (Favourite)
 *   - ghost: no background or border, used for icon buttons and controls
 *
 * Sizes:
 *   - default: standard height for most buttons
 *   - small: compact height for cart quantity controls
 */

import { cn } from "@/lib/utils/cn";

type ButtonVariant = "primary" | "outline" | "ghost";
type ButtonSize = "default" | "small";

export type ButtonProps = {
  variant?: ButtonVariant;
  size?: ButtonSize;
  disabled?: boolean;
  className?: string;
  onClick?: () => void;
  type?: "button" | "submit" | "reset";
  "aria-label"?: string;
  children: React.ReactNode;
};

export const Button = ({
  variant = "primary",
  size = "default",
  disabled = false,
  className,
  onClick,
  type = "button",
  "aria-label": ariaLabel,
  children,
}: ButtonProps) => (
  <button
    type={type}
    disabled={disabled}
    onClick={onClick}
    aria-label={ariaLabel}
    className={cn(
      // Base styles applied to all variants
      "inline-flex cursor-pointer items-center justify-center rounded-full font-semibold transition-all duration-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-(--focus-ring) disabled:cursor-not-allowed disabled:opacity-50",

      // Size variants
      {
        "h-11 px-6 text-sm": size === "default",
        "h-8 px-3 text-xs": size === "small",
      },

      // Visual variants
      {
        // Primary
        "bg-strong text-background hover:opacity-90 active:opacity-80": variant === "primary",

        // Outline
        "border-border text-foreground hover:border-strong hover:text-strong border bg-transparent":
          variant === "outline",

        // Ghost
        "text-muted hover:text-foreground bg-transparent": variant === "ghost",
      },

      className,
    )}
  >
    {children}
  </button>
);
