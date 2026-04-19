/**
 * Outbound HTTP helpers (thin fetch wrapper).
 *
 * Environment:
 * - `API_BASE_URL` — server-only (RSC, Route Handlers, Server Actions). Never exposed to the browser.
 * - `NEXT_PUBLIC_API_BASE_URL` — inlined at build time; use for Client Components / browser `fetch`.
 *   Non-`NEXT_PUBLIC_*` variables are not available in the browser bundle.
 *
 * Example:
 *   import { api } from "@/lib/api";
 *   const category = await api<Category>(`/api/categories/slug/${slug}`, { next: { revalidate: 60 } });
 */

import { notFound } from "next/navigation";

/** Pass-through for Next.js `fetch` extensions (caching, tags). Callers may pass `next` unchanged. */
export type ApiFetchInit = RequestInit & {
  next?: { revalidate?: number | false; tags?: string[] };
};

const TRUNCATE_BODY = 2048;

const trimTrailingSlashes = (s: string): string => {
  return s.replace(/\/+$/, "");
};

const normalizePath = (path: string): string => {
  if (!path || path === "/") return "";
  return path.startsWith("/") ? path : `/${path}`;
};

const resolveApiBaseUrl = (): string => {
  const isServer = typeof window === "undefined";

  if (isServer) {
    const base = process.env.API_BASE_URL;
    if (base) return trimTrailingSlashes(base);
    if (process.env.NODE_ENV === "development") {
      throw new Error(
        "[api] Missing API_BASE_URL. Add it to .env.local (server-side outbound API base, e.g. http://localhost:8080).",
      );
    }
    throw new Error("[api] Missing API_BASE_URL.");
  }

  const publicBase = process.env.NEXT_PUBLIC_API_BASE_URL;
  if (publicBase) return trimTrailingSlashes(publicBase);
  if (process.env.NODE_ENV === "development") {
    throw new Error(
      "[api] Missing NEXT_PUBLIC_API_BASE_URL. Browser code cannot read API_BASE_URL; set NEXT_PUBLIC_API_BASE_URL for client-side requests.",
    );
  }
  throw new Error("[api] Missing NEXT_PUBLIC_API_BASE_URL.");
};

/**
 * Absolute URL for an API path. `path` may be `/api/foo` or `api/foo`; avoids duplicate slashes.
 */
export const apiUrl = (path: string): string => {
  const base = resolveApiBaseUrl();
  const p = normalizePath(path);
  return `${base}${p}`;
};

const looksLikeJsonString = (body: string): boolean => {
  const t = body.trim();
  return (t.startsWith("{") && t.endsWith("}")) || (t.startsWith("[") && t.endsWith("]"));
};

const mergeHeaders = (init?: RequestInit): Headers => {
  const headers = new Headers(init?.headers);
  if (!headers.has("Accept")) {
    headers.set("Accept", "application/json");
  }
  const body = init?.body;
  if (typeof body === "string" && !headers.has("Content-Type") && looksLikeJsonString(body)) {
    headers.set("Content-Type", "application/json");
  }
  return headers;
};

/**
 * `fetch` to the configured API base. Forwards `init` (including Next.js `next` cache options).
 */
export const apiFetch = (path: string, init?: ApiFetchInit): Promise<Response> => {
  const url = apiUrl(path);
  const headers = mergeHeaders(init);
  return fetch(url, { ...init, headers });
};

export class ApiError extends Error {
  constructor(
    message: string,
    readonly status: number,
    readonly bodyText?: string,
  ) {
    super(message);
    this.name = "ApiError";
  }
}

const truncateForMessage = (text: string, max = TRUNCATE_BODY): string => {
  if (text.length <= max) return text;
  return `${text.slice(0, max)}…`;
};

/**
 * JSON response helper: parses `response.json()` on success.
 * On `!response.ok`, throws {@link ApiError} with status and truncated body text (no console logging).
 */
export const api = async <T>(path: string, init?: ApiFetchInit): Promise<T> => {
  const res = await apiFetch(path, init);
  if (res.ok) {
    return res.json() as Promise<T>;
  }

  let bodyText: string | undefined;
  try {
    bodyText = await res.text();
  } catch {
    /* ignore */
  }

  const snippet = bodyText ? truncateForMessage(bodyText) : undefined;
  const message = snippet ? `HTTP ${res.status} ${res.statusText}: ${snippet}` : `HTTP ${res.status} ${res.statusText}`;

  throw new ApiError(message, res.status, bodyText);
};
