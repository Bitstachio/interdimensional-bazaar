# Next.js Conventions

This document describes the architectural patterns, project structure, and coding standards used for the web
application. All developers should follow these conventions when adding new features or modules.

Provide this file to your LLM (Claude, GPT, Cursor, etc.) to enforce clean code standards and professional practices.

## Feature-Based Project Structure

The project follows a **feature-based directory structure** where each business or UI domain (e.g., `auth`, `cart`,
`product`) has its own folder with consistent internal organization. Avoid dumping unrelated code into large
`page.tsx` files or defining multiple components in a single file.

### Feature Folder Layout

Each feature should follow a structure similar to the following (names may vary; consistency matters more than exact
labels):

```
src/features/<feature>/
├── components/          # Presentational UI — one component per file
├── hooks/               # Custom hooks (state, effects, data wiring)
├── api/                 # TanStack Query definitions, fetchers, keys (see Data Fetching)
├── types/               # Feature-specific types (use `type`, not `interface`)
├── constants/           # Feature-specific constants
├── utils/               # Pure helpers when not colocated elsewhere
└── errors/              # Feature-specific error classes (optional; may live under src/lib/errors)
```

Routes under `app/` should remain thin: compose feature components and wire route-level concerns only.

### Example: Auth Feature

```
src/features/auth/
├── components/
│   ├── LoginForm.tsx
│   └── LoginFormField.tsx
├── hooks/
│   └── useLogin.ts
├── api/
│   ├── keys.ts
│   ├── loginMutation.ts
│   └── sessionQuery.ts
├── types/
│   └── auth.ts
└── constants/
    └── routes.ts
```

## Layer Responsibilities

### 1. Components (`components/`)

**Purpose**: Render UI and receive data via props.

**Responsibilities**:

- One **exported** component per file. Do not define multiple components in the same module.
- Keep components **dumb**: minimal logic; no data-fetching orchestration, no complex side effects.
- Use **Tailwind** utility classes for styling. Do **not** use inline `style={{ ... }}` except for rare dynamic values
  that cannot be expressed as classes (prefer CSS variables from the theme when possible).

**Guidelines**:

- Use **arrow functions** for components: `export const LoginForm = (props: LoginFormProps) => ...`
- If the component body is only a return, use an **implicit return** (parentheses, no `return` keyword):

```tsx
export const StatusBadge = ({ status }: StatusBadgeProps) => (
  <span className="rounded px-2 py-0.5 text-sm">{status}</span>
);
```

- Name props types as **`ComponentNameProps`** (e.g., `LoginFormProps` for `LoginForm`).

### 2. Hooks (`hooks/`)

**Purpose**: Encapsulate React logic, subscriptions, and composition of TanStack Query hooks.

**Responsibilities**:

- State, `useEffect`, `useMemo`, `useCallback`, and other React lifecycle–related logic belong here—not in fat
  components.
- Hooks may call shared query/mutation hooks from `api/` and expose a simple API to components.

**Example**:

```tsx
// hooks/useLogin.ts
export const useLogin = () => {
  const mutation = useLoginMutation();
  // wire form state, handlers, derived state
  return { mutation /* ... */ };
};
```

### 3. Types, Constants, and Utilities

**Purpose**: Keep components free of clutter.

**Guidelines**:

- **`types/`**: Shared `type` aliases and unions. Prefer **`type` over `interface`** for consistency.
- **`constants/`**: Magic strings, config, and enums used across the feature.
- **`utils/`**: Pure functions; no JSX unless you intentionally colocate a tiny helper with a component (still one
  component per file for exported components).

### 4. Data Fetching (`api/` and centralized query code)

**Purpose**: All client-side server state and mutations go through **TanStack Query**.

**Responsibilities**:

- Maintain a **central place** for query keys, shared options, and factory functions (e.g., `src/lib/query/` or
  `src/features/<feature>/api/`) so definitions are not duplicated.
- Extract **common** query/mutation patterns (default `staleTime`, error mapping, key builders) into reusable helpers.

**Example**:

```ts
// features/users/api/keys.ts
export const userKeys = {
  all: ["users"] as const,
  detail: (id: string) => [...userKeys.all, "detail", id] as const,
};
```

```ts
// features/users/api/userQuery.ts
export const useUserQuery = (id: string) => useQuery({ queryKey: userKeys.detail(id), queryFn: () => fetchUser(id) });
```

## Error Handling

### Throwing Errors

- Throw **specific error classes** (e.g., `UnauthorizedError`, `ValidationError`) instead of `new Error('...')` with
  only a string.
- **Tests and control flow** should distinguish errors by **`instanceof`** (or a discriminant on the class), not by
  matching message text. Messages are for humans and logging; **type/class** is the contract.

**Example**:

```ts
export class UnauthorizedError extends Error {
  readonly name = "UnauthorizedError";
  constructor(message = "Unauthorized") {
    super(message);
    Object.setPrototypeOf(this, new.target.prototype);
  }
}

// usage
if (response.status === 401) {
  throw new UnauthorizedError("Session expired");
}

// consumer
if (error instanceof UnauthorizedError) {
  // handle auth
}
```

Group base errors under something like `src/lib/errors/` when they are cross-cutting; feature-specific subclasses can
live in `features/<feature>/errors/` or next to the API layer.

## Styling and Theme

### Tailwind and Formatting

- Use **Tailwind CSS** for layout and styling.
- Use the **Prettier Tailwind plugin** so class lists stay sorted and diffs stay stable.

### Theme in `globals.css`

- Define **colors, radii, fonts, and other design tokens** in `app/globals.css` (or your global stylesheet) so themes
  can be swapped or extended in one place. Prefer CSS variables and Tailwind theme integration over scattering
  hex values in components.

**Example (Tailwind v4-style `@theme`; adjust if your project uses v3 + `tailwind.config`):**

```css
/* app/globals.css */
@import "tailwindcss";

@theme {
  --color-brand-primary: oklch(0.55 0.2 250);
  --color-surface: oklch(0.98 0 0);
}
```

Use semantic names (e.g., `--color-brand-primary`) in components via Tailwind utilities mapped to those tokens.

## Code Style

### JavaScript / TypeScript

- Prefer **ES6+** syntax and **arrow functions** for components and local functions. Avoid `function foo()` for
  components; use `const Foo = () => {}`.
- Use **`type`** for props and data shapes, not `interface`.

### Naming

- Components: `PascalCase` file names matching the export (e.g., `LoginForm.tsx` → `LoginForm`).
- Props types: `<ComponentName>Props`.
- Hooks: `use` prefix + descriptive name (`useLogin`, `useProductFilters`).

## Best Practices Summary

| Topic              | Convention                                                              |
| ------------------ | ----------------------------------------------------------------------- |
| Components         | One per file; dumb; Tailwind only; implicit return when body is trivial |
| Logic              | Custom hooks for React logic; TanStack Query for async server state     |
| Types              | `type`; props named `ComponentNameProps`                                |
| API / server state | TanStack Query; centralized keys and shared query/mutation helpers      |
| Errors             | Custom error classes; branch on class/type, not message text            |
| Styling            | Tailwind + Prettier plugin; tokens in `globals.css`                     |

## Creating a New Feature

When adding a new feature (e.g., `checkout`):

1. Create `src/features/checkout/` with `components/`, `hooks/`, `api/`, `types/`, and `constants/` as needed.
2. Add route segments under `app/` that import from `features/checkout` and stay thin.
3. Define TanStack Query keys and hooks under `features/checkout/api/` (or your shared query lib).
4. Introduce feature-specific error classes if the feature has distinct failure modes.
5. Ensure Prettier (with Tailwind) runs on changed files before committing.

## Code Formatting

Run formatting before committing (adjust commands to match your `package.json` scripts):

```bash
pnpm exec prettier --write .
```

Check formatting without writing:

```bash
pnpm exec prettier --check .
```

If the repo uses ESLint with Next.js:

```bash
pnpm exec eslint .
```
