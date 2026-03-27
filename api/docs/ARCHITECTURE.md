# Bazaar API - Architecture & Project Structure

## Overview

This document describes the architectural patterns and project structure used in the Bazaar API. All developers should
follow these conventions when adding new features or modules.

## Technology Stack

- **Framework**: Spring Boot 4.0.1
- **Language**: Java 25
- **Build Tool**: Maven
- **Database**: MySQL (with JPA/Hibernate)
- **Code Style**: Google Java Format with AOSP style (100 character line length, 4-space indent)

## Package Structure

The project follows a **feature-based package structure** where each business domain (e.g., `user`, `product`, `order`)
has its own package with consistent internal organization.

### Feature Package Layout

Each feature package should follow this structure:

```
com.bitstachio.bazaarapi.<feature>/
├── controller/      # REST API endpoints
├── domain/          # JPA entities
├── dto/             # Data Transfer Objects (requests/responses)
├── exception/       # Feature-specific exceptions
├── repository/      # Spring Data JPA repositories
└── service/         # Business logic (interface + implementation)
```

### Example: User Feature

```
com.bitstachio.bazaarapi.user/
├── controller/
│   └── UserController.java
├── domain/
│   └── User.java
├── dto/
│   ├── UserCreateRequest.java
│   ├── UserResponse.java
│   └── UserUpdateRequest.java
├── exception/
│   └── UserNotFoundException.java
├── repository/
│   └── UserRepository.java
└── service/
    ├── UserService.java
    └── UserServiceImpl.java
```

## Layer Responsibilities

### 1. Controller Layer (`controller/`)

**Purpose**: Handle HTTP requests and responses

**Responsibilities**:
- Define REST API endpoints
- Validate request parameters
- Delegate business logic to the service layer
- Return appropriate DTOs

**Guidelines**:
- Use `@RestController` annotation
- Define the base path with `@RequestMapping`
- Use constructor injection with `@RequiredArgsConstructor` (Lombok)
- Only interact with the service layer, never directly with repositories
- Return DTOs, never domain entities

**Example**:
```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;

    @PostMapping
    public UserResponse create(@RequestBody UserCreateRequest request) {
        return userService.create(request);
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable UUID id) {
        return userService.getById(id);
    }
}
```

### 2. Service Layer (`service/`)

**Purpose**: Implement business logic

**Structure**:

- Interface defining the contract (e.g., `UserService.java`)
- Implementation class (e.g., `UserServiceImpl.java`)

**Responsibilities**:
- Execute business logic
- Coordinate between repositories
- Handle transactions
- Throw appropriate exceptions
- Convert between domain entities and DTOs

**Guidelines**:
- Use `@Service` annotation on implementation
- Use constructor injection with `@RequiredArgsConstructor`
- Always work with domain entities internally
- Return DTOs to the controller layer
- Throw custom exceptions (not generic RuntimeException)

**Example**:
```java

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;

    @Override
    public UserResponse getById(UUID id) {
        return userRepository
                .findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
```

### 3. Repository Layer (`repository/`)

**Purpose**: Data access abstraction

**Responsibilities**:
- Extend `JpaRepository<Entity, ID>`
- Define custom query methods if needed

**Guidelines**:
- Keep repositories simple
- Use Spring Data JPA method naming conventions
- Add `@Query` annotations only when necessary

**Example**:
```java
public interface UserRepository extends JpaRepository<User, UUID> {
    // Custom queries can be added here if needed
}
```

### 4. Domain Layer (`domain/`)

**Purpose**: JPA entity definitions

**Responsibilities**:
- Map to database tables
- Define relationships between entities
- Contain only data and basic validation

**Guidelines**:
- Use `@Entity` annotation
- Use `@Table(name = "table_name")` to specify table names
- Use Lombok annotations: `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`
  - Do not use `@Data` because it generates `equals()` and `hashCode()` for all fields, which can break JPA proxies and
    cause unexpected behavior
- Use UUIDs for primary keys
- Keep entities free of business logic

**Example**:
```java
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id @GeneratedValue private UUID id;
    private String name;
    private String email;
}
```

### 5. DTO Layer (`dto/`)

**Purpose**: Data Transfer Objects for API communication

**Types**:
- `*Request` - Input DTOs (e.g., `UserCreateRequest`, `UserUpdateRequest`)
- `*Response` - Output DTOs (e.g., `UserResponse`)

**Responsibilities**:
- Define API contract
- Separate internal domain model from external API
- Provide data validation

**Guidelines**:
- Use Java records for DTOs to ensure immutability
- Apply Lombok `@Builder` when constructing response records
- Annotate record components with `jakarta.validation` constraints
- Never expose internal entity structure directly

**Example**:
```java
public record UserCreateRequest(String name, String email) {}

@Builder
public record UserResponse(UUID id, String name, String email) {}
```

## Exception Handling

The Bazaar API uses a centralized exception handling strategy with a `GlobalExceptionHandler`.

### Throwing Exceptions

Exceptions must be thrown from the **service layer**, not the controller. Controllers should allow exceptions to
propagate to the global handler:

```java
// Service layer - throw exceptions here
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    @Override
    public UserResponse getById(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}

// Controller - let exceptions propagate
@RestController
@RequiredArgsConstructor
public class UserController {
    
    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable UUID id) {
        return userService.getById(id);  // No try-catch needed
    }
}
```

### Base Exception Classes

Abstract base exceptions in the global exception package map to HTTP status codes:

- `BadRequestException` → 400 Bad Request
- `ResourceNotFoundException` → 404 Not Found
- More exceptions to be added in the future...

The global exception handler catches these base exceptions and automatically returns the appropriate HTTP status code.
All feature-specific exceptions should extend these base classes to get mapped to the correct status code via
polymorphism. If a custom exception does not extend one of the specified base exceptions, it will be caught by
`@ExceptionHandler(Exception.class)` and result in an internal server error (HTTP status 500).

**Example**:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles ResourceNotFoundException and all subclasses (e.g., UserNotFoundException)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        // Construct the error response with a 404 status code
    }
}

// Base exception for "not found" errors - maps to 404 status code
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

// Feature-specific exception that inherits 404 status code mapping
public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(UUID id) {
        super("User not found with ID: " + id);
    }
}
```

### Error Response Format

All errors return a consistent JSON structure:

```json
{
  "status": 404,
  "message": "User not found with ID: 123e4567-e89b-12d3-a456-426614174000",
  "timestamp": "2024-01-15T10:30:00"
}
```

## Best Practices

### Code Style

- Use **Google Java Format with AOSP style** via `fmt-maven-plugin`
- Run `mvn fmt:format` before committing
- 100-character line length (Google standard)
- 4-space indentation (AOSP style)

### Dependency Injection

- Always use constructor injection
- Use Lombok's `@RequiredArgsConstructor` for final fields
- Never use field injection (`@Autowired` on fields)

### Naming Conventions

- Controllers: `<Feature>Controller`
- Services: `<Feature>Service` (interface) and `<Feature>ServiceImpl` (implementation)
- Repositories: `<Feature>Repository`
- Entities: Use singular nouns (e.g., `User`, not `Users`)
- DTOs: `<Feature><Action>Request/Response`
- Exceptions: `<Feature><Error>Exception`

### Error Handling

- Always throw custom exceptions, never generic `RuntimeException`
- Provide meaningful error messages with context
- Use constructor parameters to include relevant IDs or data

## Creating a New Feature

When adding a new feature (e.g., "Product"):

1. Create the feature package: `com.bitstachio.bazaarapi.product`
2. Create all six sub-packages: `controller`, `domain`, `dto`, `exception`, `repository`, `service`
3. Implement in this order:
   - Domain entity (`Product.java`)
   - Repository interface (`ProductRepository.java`)
   - Custom exceptions (`ProductNotFoundException.java`)
   - DTOs (request/response classes)
   - Service interface and implementation
   - Controller

## Code Formatting

Run formatting before committing:

```bash
mvn fmt:format
```

Check if the code is properly formatted:

```bash
mvn fmt:check
```

## Questions or Suggestions?

If you have questions about the architecture or suggestions for improvements, please discuss with the team.
