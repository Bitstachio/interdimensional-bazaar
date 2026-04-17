/**
 * Class Name Utility
 *
 * A thin wrapper around the `clsx` library that provides a single, consistent
 * way to compose Tailwind CSS class strings across the entire codebase.
 */
import { clsx, type ClassValue } from "clsx";

export const cn = (...inputs: ClassValue[]): string => clsx(...inputs);
