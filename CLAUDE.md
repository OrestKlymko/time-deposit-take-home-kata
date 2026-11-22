# CLAUDE.md

Project rules for Claude Code. The solution lives in `kotlin/`; the other language folders are untouched.

## Commands (run from `kotlin/`)

- `mvn clean test` — all tests; needs Docker (Testcontainers starts PostgreSQL).
- `docker compose up -d && mvn spring-boot:run` — run the app; Swagger UI at http://localhost:8080/swagger-ui.html.
- Run `mvn clean` after moving or renaming packages: stale classes in `target/` cause duplicate-entity errors.

## Hard constraints (from the kata README)

- Do not change the shared `TimeDeposit` class (fields, types, package) or the `TimeDepositCalculator.updateBalance` signature.
  `TimeDepositCalculator()` must keep working without arguments.
- Exactly two endpoints. Do not add more.
- The behaviour of `updateBalance` must not change: `TimeDepositCalculatorTest` is the characterization suite and must stay green after every refactor.
- Ambiguities are resolved with a logical assumption, justified in a code comment.

## Architecture (hexagonal)

- `domain` — business rules only, no Spring. Interest rules are one `Interest` strategy per `PlanType` in `domain/interest`.
- `application` — inbound ports (`*UseCase`) and their output models, outbound ports, one service per use case.
  Services orchestrate steps and transactions; formulas and `if`s on business data belong in the domain.
- `adapter/inbound/web` — `controller/` implements the generated API interface; `mapper/WebMapper` maps to DTOs.
- `adapter/outbound/persistence` — `adapter/`, `mapper/`, `entity/`, `repository/`. JPA types never leave this package.
- Dependencies point inward: adapters → application → domain.
- Do not name packages `in`/`out`: `in` is a Kotlin keyword and breaks the generated sources. Use `inbound`/`outbound`.

## API contract (contract-first)

- `src/main/resources/static/openapi.yaml` is the source of truth. Change it first; the controller interface and DTOs are generated at build time.
- Never hand-write request mappings or DTOs for these endpoints.

## Database

- Flyway owns the schema; Hibernate only validates (`ddl-auto: validate`).
- Once a migration is released it is immutable: changes go into a new `V<n>__*.sql`.
- Money is `NUMERIC(19,2)` / `BigDecimal` in the database; `TimeDeposit.balance` stays `Double`, converted in the persistence mapper.

## Tests

- Domain: plain unit tests. Application services: unit tests against in-memory fakes of the outbound ports (no Spring, no DB).
- Adapters: end-to-end tests with Testcontainers PostgreSQL.

## Working agreement

- Propose options with trade-offs before structural changes; the developer decides.
- Do not commit or push unless explicitly asked. Commits are atomic: one logical change each.
- Match the surrounding code style: Kotlin official style, sparse comments that explain *why*.
