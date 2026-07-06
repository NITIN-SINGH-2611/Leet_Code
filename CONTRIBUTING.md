# Contributing to CodeCrack AI

Thanks for considering a contribution. This project curates high-signal interview
questions and builds the tooling around them — contributions to either side are welcome.

## Getting set up

```bash
git clone <this-repo>
cd codecrack-ai

# Backend
cd backend && mvn spring-boot:run

# Frontend (separate terminal)
cd frontend && npm install && npm run dev
```

You need **Java 21** and **Node 20+**. See the README for the full local setup,
including the JDK-version pitfall that trips people up on newer macOS installs.

## Ground rules

- **No copied problem statements.** If you're adding a question, write the
  problem statement, hints, and editorial yourself. Metadata (patterns,
  company tags, links to public judges) is fine; verbatim text from LeetCode,
  HackerRank, etc. is not.
- **Keep the architecture clean.** Controller → Service → Repository. No
  business logic in controllers; no direct repository calls from controllers.
- **Write tests for service-layer logic.** Especially anything touching XP,
  streaks, or spaced repetition — those are easy to silently break.
- **Match the existing code style.** Constructor injection via
  `@RequiredArgsConstructor`, DTOs for every API boundary, MapStruct for
  entity → DTO mapping.

## Submitting a change

1. Fork and branch from `main`.
2. Make your change with tests where it's logic-bearing.
3. Run the full local build (`mvn clean verify` in `backend/`, `npm run build`
   in `frontend/`) before opening a PR — CI runs the same checks.
4. Open a PR using the template; describe what changed and why.

## Reporting bugs / requesting features

Use the issue templates under `.github/ISSUE_TEMPLATE/`.
