# Mono Repo Auto Versioning

## Project Purpose

A multi-module Maven project demonstrating:
- **Selective builds** — only build submodules affected by changes
- **Automatic versioning** — version modules and their dependents automatically, removing the need for manual version tracking

## Project Structure

- `mono-repo/` — the Maven multi-module project root (aggregator POM)
  - `common-models/` — shared domain models and DTOs (no internal deps)
  - `common-utils/` — shared utilities and helpers (no internal deps)
  - `common-security/` — security config and auth utilities (depends on `common-utils`)
  - `service-user/` — user management (depends on `common-models`, `common-security`)
  - `service-order/` — order management (depends on `common-models`, `common-utils`)
  - `service-product/` — product catalog (depends on `common-models`, `common-utils`)
  - `service-payment/` — payment processing (depends on `common-models`, `common-security`)
  - `service-notification/` — notifications (depends on `common-models`, `common-utils`)

## Conventions

- groupId: `com.example.monorepo`, version: `1.0.0-SNAPSHOT`
- Java 17, Spring Boot 3.2.5 parent
- Services use `spring-boot-starter-web` + `spring-boot-maven-plugin`
- Common lib versions managed via `<dependencyManagement>` in the parent POM
