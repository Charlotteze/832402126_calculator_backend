# Code Style Standard — Backend (Java)

## Source

This code standard is derived from the official **Google Java Style Guide**.

- Official document: https://google.github.io/styleguide/javaguide.html
- This project follows the core rules below; any deviation is intentional and explained in code comments.

## 1. Naming

| Type | Rule | Example |
|---|---|---|
| Class / Interface | UpperCamelCase | `CalculatorApplication`, `HistoryRepository` |
| Method / Field | lowerCamelCase | `getAllHistory`, `expression` |
| Constants | UPPER_SNAKE_CASE | `MC`, `HTTP_PORT` |
| Package | all lowercase | `com.example.calculator` |

## 2. Formatting

- Indent: 4 spaces (no tabs)
- Braces: K&R style (opening brace on the same line as the statement)
- Line width: at most 100 characters
- One statement per line; one blank line between logical blocks

## 3. Imports

- No wildcard imports (`import java.util.*` is not allowed)
- Order: static imports first, then `java.*`, then third-party, then project classes
- No unused imports

## 4. Comments and Javadoc

- Public classes and public methods carry Javadoc (`/** ... */`)
- Javadoc explains *why* when the code does not make the reason obvious
- Inline comments are kept short and focused

## 5. Constants and magic numbers

- Avoid magic numbers; give them a named constant or a documented meaning

## 6. Error handling

- Use exceptions, never return codes for failures
- Catch the specific exception type, not bare `Exception`
- Business errors (invalid expression, division by zero) are reported as `InvalidExpressionException` and mapped to HTTP 400

## 7. Architecture rules

- Strict layering: `controller` → `service` → `repository`; `model` and `util` are shared
- Controllers contain no business logic; they only parse HTTP input and build responses
- Services are `@Service` and depend on interfaces/repositories, not on other controllers

## Verification

- Code is compiled with `mvn compile`
- API behavior is verified with real HTTP requests (POST /api/calculate, GET /api/history, DELETE /api/history/{id})
