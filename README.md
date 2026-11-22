# Time Deposit Refactoring Kata - Take-Home Assignment

## XA Bank Time Deposit

### Context
A junior developer implemented domain logic for a time deposit system but did not complete the API functionality. Your task is to refactor the existing codebase to implement all required functionalities based on the provided business requirements, ensuring no breaking changes occur.

### Requirements

1. **API Endpoints**:
    - Create a RESTful API endpoint to update the balances of all time deposits in the database.
    - Create a RESTful API endpoint to retrieve all time deposits.
        - The GET endpoint should return a list of all time deposits with the following schema:
            - `id`
            - `planType`
            - `balance`
            - `days`
            - `withdrawals`

2. **Database Setup**:
    - Store all time deposit plans in a database.
    - Define the following tables:
        - `timeDeposits`:
            - `id`: Integer (primary key)
            - `planType`: String (required)
            - `days`: Integer (required)
            - `balance`: Decimal (required)
        - `withdrawals`:
            - `id`: Integer (primary key)
            - `timeDepositId`: Integer (foreign key, required)
            - `amount`: Decimal (required)
            - `date`: Date (required)

3. **Interest Calculation**:
    - Implement logic to calculate monthly interest based on the plan type:
        - **Basic Plan**: 1% interest
        - **Student Plan**: 3% interest (no interest after 1 year)
        - **Premium Plan**: 5% interest (interest starts after 45 days)
    - No interest is applied for the first 30 days for any existing plans.

4. **Refactoring Constraints**:
    - Do not introduce breaking changes to the shared `TimeDeposit` class or modify the `updateBalance` method signature.
    - Ensure the design is extensible to accommodate future complexities in interest calculations.

5. **Code Quality**:
    - Adhere to SOLID principles, design patterns, and clean code practices where applicable.

6. **AI-Assisted Development**:
    - Set up an AI harness or agent workflow and use it throughout the development for this take-home exercise.
    - Briefly document the tools and setup used (e.g., LLMs, coding assistants, agentic frameworks, configuration).
    - Ensure your AI setup is practical and reproducible.
    - Include any custom rules, system prompts, or agent configurations used.
    - Include a brief summary of which parts of the solution were AI-assisted and why.

### Important Guidelines
- The existing `TimeDepositCalculator.updateBalance` method is functioning correctly. Ensure its behavior remains unchanged after refactoring.
- The final solution must include **exactly two API endpoints**. Do not develop additional endpoints.
- **Do not** create a pull request or a new branch in the ikigai-digital repository. Instead, fork the repository into your own GitHub repository and develop the solution there.
- Handling invalid input or exceptions is not required.
- Use any tools, frameworks, or libraries you find suitable.
- In case of ambiguity, make logical assumptions and justify them in code comments.

### Preferred Stack
- Use an OpenAPI Swagger contract.
- Embrace Hexagonal Architecture.
- Follow atomic commit practices.
- Utilize testcontainers.
- Leverage AI-assisted development tools for code generation, testing, and refactoring.

### Submission Instructions
- Provide clear instructions on how to trigger the endpoints using the Swagger contract.
- Email the link to your public GitHub repository.
---

## Solution (Kotlin)

Built with Claude Code. Tools, setup, rules and what was AI-assisted: [docs/ai-assisted-development.md](docs/ai-assisted-development.md).
Project rules for the agent: [CLAUDE.md](CLAUDE.md).

### Run

Prerequisites: JDK 17+, Maven 3.9+, Docker.

```bash
cd kotlin
docker compose up -d          # PostgreSQL 16 on localhost:5432
mvn spring-boot:run           # Flyway creates the schema and seeds sample data
```

Swagger UI: <http://localhost:8080/swagger-ui.html> · OpenAPI contract: <http://localhost:8080/openapi.yaml>

The API is **contract-first**: [`kotlin/src/main/resources/static/openapi.yaml`](kotlin/src/main/resources/static/openapi.yaml)
is the source of truth. On every build `openapi-generator` turns it into the `TimeDepositsApi` interface and DTOs
(under `target/generated-sources`), and `TimeDepositController` implements that interface. Changing the API means
editing the contract first; code that no longer matches it does not compile.

| Method | Path                      | Description                                                     |
|--------|---------------------------|-----------------------------------------------------------------|
| GET    | `/time-deposits`          | All time deposits: `id`, `planType`, `balance`, `days`, `withdrawals` |
| POST   | `/time-deposits/balances` | Applies one month of interest to every deposit (204 No Content)   |

In Swagger UI, open an operation, click **Try it out**, then **Execute**. Or with curl:

```bash
curl http://localhost:8080/time-deposits
curl -X POST http://localhost:8080/time-deposits/balances
```

### Tests

```bash
mvn test   # needs Docker running (Testcontainers starts PostgreSQL)
```

- `TimeDepositCalculatorTest`: characterization tests pinning the original interest rules.
- `*ServiceTest`: application services against in-memory fakes of the outbound ports (no database, no Spring).
- `TimeDepositApiIntegrationTest`: both endpoints end to end against a real PostgreSQL in Testcontainers.
