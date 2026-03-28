# Spring Boot Conventions (Kotlin)

This document describes the architectural patterns and project structure used for Spring Boot applications written in
Kotlin. All developers should follow these conventions when adding new features or modules.

Provide this file to your LLM (Claude, GPT, Cursor, etc.) to enforce clean code standards and professional practices.

## Package Structure

The project follows a **feature-based package structure** where each business domain (e.g., `user`, `product`, `order`)
has its own package with consistent internal organization.

### Feature Package Layout

Each feature package should follow this structure:

```
com.bitstachio.bazaarapi.<feature>/
├── controller/      # REST API endpoints
├── domain/          # JPA entities
├── dto/             # Data transfer objects (requests / responses)
├── exception/       # Feature-specific exceptions
├── repository/      # Spring Data JPA repositories
└── service/         # Business logic (interface + implementation)
```

### Example: User Feature

```
com.bitstachio.bazaarapi.user/
├── controller/
│   └── UserController.kt
├── domain/
│   └── User.kt
├── dto/
│   ├── UserCreateRequest.kt
│   ├── UserResponse.kt
│   └── UserUpdateRequest.kt
├── exception/
│   └── UserNotFoundException.kt
├── repository/
│   └── UserRepository.kt
└── service/
    ├── UserService.kt
    └── UserServiceImpl.kt
```

## Layer Responsibilities

### 1. Controller Layer (`controller/`)

**Purpose**: Handle HTTP requests and responses.

**Responsibilities**:

- Define REST API endpoints
- Validate request parameters (delegate to DTO validation where appropriate)
- Delegate business logic to the service layer
- Return DTOs only

**Guidelines**:

- Annotate classes with `@RestController`
- Define the base path with `@RequestMapping` (or class-level path on `@RestController`)
- Use **constructor injection**: declare dependencies as `private val` parameters in the primary constructor. Do not use
  field injection (`@Autowired` on properties)
- Controllers interact only with the service layer, never with repositories directly
- Return DTOs, never domain entities

**Example**:

```kotlin
@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {
    @PostMapping
    fun create(@RequestBody @Valid request: UserCreateRequest): UserResponse =
        userService.create(request)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): UserResponse =
        userService.getById(id)
}
```

Prefer **expression bodies** for simple delegating methods (no redundant block + `return`).

### 2. Service Layer (`service/`)

**Purpose**: Implement business logic.

**Structure**:

- Interface defining the contract (e.g., `UserService.kt`)
- Implementation class (e.g., `UserServiceImpl.kt`)

**Responsibilities**:

- Execute business logic
- Coordinate repositories and other services
- Define transaction boundaries where needed (`@Transactional` on the implementation)
- Throw domain-appropriate exceptions
- Map between entities and DTOs

**Guidelines**:

- Annotate the implementation with `@Service`
- Constructor-inject dependencies via the primary constructor (`private val ...`)
- Work with domain entities inside the service; expose DTOs to controllers
- Throw **custom** exceptions (not bare `RuntimeException` or generic `IllegalStateException` without a project-wide
  convention)

**Example**:

```kotlin
@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
) : UserService {

    override fun getById(id: UUID): UserResponse =
        userRepository.findById(id)
            .map { it.toResponse() }
            .orElseThrow { UserNotFoundException(id) }

    private fun User.toResponse(): UserResponse =
        UserResponse(
            id = id!!,
            name = name,
            email = email,
        )
}
```

### 3. Repository Layer (`repository/`)

**Purpose**: Data access abstraction.

**Responsibilities**:

- Extend `JpaRepository<Entity, ID>`
- Declare derived query methods or `@Query` when needed

**Guidelines**:

- Keep repositories thin
- Prefer Spring Data method naming; use `@Query` only when necessary

**Example**:

```kotlin
interface UserRepository : JpaRepository<User, UUID>
```

### 4. Domain Layer (`domain/`)

**Purpose**: JPA entity definitions.

**Responsibilities**:

- Map to database tables
- Define relationships
- Hold persistent state and basic constraints—not business workflows

**Guidelines**:

- Use `@Entity` and `@Table(name = "...")` where table names need to be explicit
- Use **`UUID`** (or your agreed ID strategy) for primary keys consistently
- JPA typically requires **non-final** entity classes for proxies. Use the **Kotlin JPA** plugin (`kotlin("plugin.jpa")`
  / `allOpen` for entities) or mark entity classes `open` per your build setup—do not rely on manual `open` on every
  field
- Prefer **explicit nullable types** (`String?`) where columns are nullable
- Avoid embedding heavy business rules in entities; keep orchestration in services

**Example**:

```kotlin
@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue
    var id: UUID? = null,
    var name: String,
    var email: String,
)
```

Adjust `GenerationType` and nullability to match your schema and Kotlin JPA plugin configuration.

### 5. DTO Layer (`dto/`)

**Purpose**: API contracts separate from persistence.

**Types**:

- `*Request` — input (e.g., `UserCreateRequest`, `UserUpdateRequest`)
- `*Response` — output (e.g., `UserResponse`)

**Guidelines**:

- Use **`data class`** for DTOs
- Apply **`jakarta.validation`** constraints on constructor parameters or properties
- Do not expose internal entity shapes directly on the public API

**Example**:

```kotlin
data class UserCreateRequest(
    @field:NotBlank
    val name: String,
    @field:Email
    val email: String,
)

data class UserResponse(
    val id: UUID,
    val name: String,
    val email: String,
)
```

Use `@field:` constraint targets (or your project’s agreed style) so validation works on properties as expected.

## Exception Handling

Use a centralized **`@RestControllerAdvice`** handler (e.g., `GlobalExceptionHandler`) so controllers stay free of
try/catch for expected failures.

### Throwing Exceptions

- Throw from the **service** layer (or domain rules invoked from services), not from controllers for business failures
- Let exceptions propagate from controllers to the advice

**Example**:

```kotlin
@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
) : UserService {

    override fun getById(id: UUID): UserResponse =
        userRepository.findById(id)
            .map { it.toResponse() }
            .orElseThrow { UserNotFoundException(id) }
}
```

### Base Exception Classes

Map abstract base exceptions to HTTP status codes (same idea as in a Java codebase):

- e.g., `BadRequestException` → 400
- e.g., `ResourceNotFoundException` → 404

Feature-specific types should **extend** the appropriate base so the advice can handle them polymorphically. A catch-all
for `Exception` should return 500 for truly unexpected errors.

**Example**:

```kotlin
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(ex: ResourceNotFoundException): ResponseEntity<ErrorResponse> {
        // build body with 404
        TODO("map to ErrorResponse")
    }
}

open class ResourceNotFoundException(message: String) : RuntimeException(message)

class UserNotFoundException(id: UUID) :
    ResourceNotFoundException("User not found with ID: $id")
```

### Error Response Format

Return a **consistent JSON body** for API errors (status, message, timestamp, optional code). Clients and tests can
assert on **status and structured fields**; avoid relying only on free-form English message text for branching logic in
clients—use a stable **`code`** or exception type on the server side where appropriate.

## Kotlin and Spring Conventions

### Dependency Injection

- **Constructor injection only** via primary constructor parameters (`private val dep: Dep`)
- Do not use `@Autowired` on properties

### Idiomatic Kotlin

- Prefer **`val`** over `var` where mutation is not required
- Use **expression-bodied functions** when the implementation is a single expression
- Use **`data class`** for DTOs and value-like types; use **`object`** or `companion object` for small constants only
  when it improves clarity
- Use **nullable types** deliberately at API and persistence boundaries

### Naming

- Controllers: `<Feature>Controller`
- Services: `<Feature>` + `Service` (interface) and `<Feature>ServiceImpl` (implementation), unless the team agrees on
  a single concrete `@Service` per feature without an interface
- Repositories: `<Feature>Repository`
- Entities: singular nouns (`User`, not `Users`)
- DTOs: `<Feature><Action>Request` / `<Feature>Response`
- Exceptions: `<Feature><Error>Exception`

### Error Handling

- Throw **typed** project exceptions that extend your HTTP-mapping bases
- Include **context** in messages (IDs, field names) for logs and support; keep **machine-readable** distinctions in
  type hierarchy or response `code` fields where clients need to branch

## Creating a New Feature

When adding a new feature (e.g., `product`):

1. Create the package `com.bitstachio.bazaarapi.product`
2. Add the folders: `controller`, `domain`, `dto`, `exception`, `repository`, `service`
3. Implement in a practical order:
   - Domain entity
   - Repository
   - Feature-specific exceptions extending shared bases
   - DTOs
   - Service interface and implementation
   - Controller
4. Register or extend global exception handling if new base types are introduced

## Code Formatting and Static Analysis

Prefer automated formatting and style checks in Gradle (choose tools that match your repo; common choices include
**ktlint** and **Detekt**).

**Format (example with ktlint Gradle plugin):**

```bash
./gradlew ktlintFormat
```

**Check:**

```bash
./gradlew ktlintCheck
```

Run the same checks in CI so main stays consistent.

## Best Practices Summary

| Topic        | Convention                                                                 |
| ------------ | -------------------------------------------------------------------------- |
| Structure    | Feature packages with controller / domain / dto / exception / repository / service |
| Injection    | Primary constructor `private val` dependencies; no field injection         |
| DTOs         | `data class` + Jakarta validation                                        |
| Entities     | JPA plugin / `open` as required; UUID IDs; keep entities thin              |
| Errors       | Custom exceptions + `@RestControllerAdvice`; extend HTTP-mapping bases      |
| Style        | Expression bodies where clear; prefer `val`                                |
