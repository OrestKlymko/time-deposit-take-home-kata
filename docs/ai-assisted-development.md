# AI-assisted development

## Tools and setup

| What | Used for |
|------|----------|
| [Claude Code](https://docs.claude.com/en/docs/claude-code) (CLI) | The agent: reads the repo, edits files, runs Maven, Docker and curl, and reports back. |
| Claude Opus 5.5, medium effort | Model behind the agent. |
| Claude Code advisor (a second, stronger reviewer model) | Consulted before larger changes and before declaring a task done. |
| Claude Artifacts | A private, interactive learning guide to hexagonal architecture, kept in sync with the code as it changed. |

No custom agents or MCP servers were needed. The project-level configuration is in the repository:

- [`CLAUDE.md`](../CLAUDE.md): the project rules and constraints. Claude Code loads it automatically.
- [`.claude/settings.json`](../.claude/settings.json): shared permissions. The agent may build, test, start Docker and read git state without asking each time; `git push` is denied, so publishing stays a human step.
  Personal overrides go in `.claude/settings.local.json`, which is git-ignored.

### Reproduce

```bash
git clone git@github.com:OrestKlymko/time-deposit-take-home-kata.git
cd time-deposit-take-home-kata
claude        # start Claude Code from the repo root so CLAUDE.md is picked up
```

Docker must be running, because the tests use Testcontainers.

## Workflow

1. **Requirements first.** The kata README was given to the agent. It produced a first working version: contract, persistence, two endpoints, tests.
2. **File-by-file review.** I went through the code one file at a time. For each file the agent explained the design, pointed out problems, and offered options with trade-offs. **I decided**; the agent implemented.
3. **Tests as the guardrail.** After every change the agent ran `mvn clean test`. The characterization tests for `TimeDepositCalculator` were written before any refactoring and pinned the original behaviour throughout.
4. **Real run.** The agent also started the app against PostgreSQL in Docker and called both endpoints and Swagger UI with curl, to check the running app and not just the tests.

## What was AI-assisted and why

**Written by the agent, reviewed by me:** most of the code, the OpenAPI contract, the Flyway migrations, the tests, the README section and this document. The agent writes this boilerplate fast and keeps it consistent, and the test suite gives a quick check on it.

**Decided by me** (the agent proposed alternatives, I chose):
- the Strategy pattern for interest rules (one class per plan type) instead of a `when` in the calculator;
- contract-first OpenAPI: the controller implements an interface generated from `openapi.yaml`;
- renaming the `in`/`out` packages to `inbound`/`outbound`, after `in` broke code generation as a Kotlin keyword;
- one application service per use case (SRP);
- the package layout: `domain/model`, `domain/interest`, and `entity`/`repository`/`adapter`/`mapper` inside persistence;
- moving `TimeDepositOverview` out of the domain into the application layer, since it is a use case's output, not a business concept;
- `POST` instead of `PUT` for the balance update, because applying interest is not idempotent;
- keeping `TimeDeposit` and `TimeDepositCalculator` in the root package (README constraint), with comments explaining why.

**Changed by hand:** renaming the strategies from `*InterestStrategy` to `*Interest`, and `values()` → `entries` in `PlanType`.

**Learning.** This was my first hexagonal-architecture project; before it I had only used controller → service → repository. I used the agent as a tutor as well as a code generator. It explained each concept against this codebase and kept the interactive guide up to date as the code changed.
