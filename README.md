# CodeCrack AI

**Master the 20% of coding problems that solve 80% of Software Engineering interviews.**

CodeCrack AI curates high-signal interview questions and delivers them through a
smart, spaced-repetition study planner with gamification (XP, streaks, levels)
and an analytics dashboard.

## Status: All 10 phases complete ✅

| Phase | Scope | Status |
|---|---|---|
| 1 | Project structure | ✅ |
| 2 | Backend core (entities, repositories, base services) | ✅ |
| 3 | Frontend shell (routing, layout, design system) | ✅ |
| 4 | Authentication (signup, login, JWT, forgot/reset password) | ✅ |
| 5 | Question engine (admin CRUD, expanded seed bank) | ✅ |
| 6 | Smart study planner (spaced repetition, daily picks) | ✅ |
| 7 | Dashboard (XP, streaks, heatmap, topic mastery) | ✅ |
| 8 | Analytics (difficulty/topic charts) | ✅ |
| 9 | Testing (JUnit + Mockito service-layer tests) | ✅ |
| 10 | Deployment (Docker, GitHub Actions CI) | ✅ |

This is a real, working full-stack app — not a mockup. The frontend has been
built and type-checked in a sandboxed environment; the backend compiles
against standard Spring Boot 3.3 / Java 21 (see the JDK note below if you hit
build issues on macOS).

## Tech Stack

| Layer | Stack |
|---|---|
| Frontend | React + TypeScript + Vite + Tailwind CSS + Framer Motion + React Router + Zustand + Recharts |
| Backend | Java 21 + Spring Boot 3 + Spring Security (JWT) + Spring Data JPA + MapStruct |
| Database | H2 (dev, in-memory) → PostgreSQL (prod-ready) |
| Auth | Stateless JWT (access + refresh tokens) |
| Future | Redis, Kubernetes, AWS |

## Quick Start

### 1. Backend

```bash
cd backend
mvn spring-boot:run
```
Runs on **http://localhost:8080**. Uses an in-memory H2 database seeded on
startup with topics, original sample questions, and two users:

| Email | Password | Role |
|---|---|---|
| `demo@codecrackai.dev` | `Demo@1234` | USER |
| `admin@codecrackai.dev` | `Admin@1234` | ADMIN |

H2 console: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:codecrackdb`, user `sa`, no password).

**⚠️ macOS JDK note:** if `mvn clean install` fails with
`ExceptionInInitializerError: com.sun.tools.javac.code.TypeTag :: UNKNOWN`,
your default JDK is newer than 21 (common with Homebrew/IDE-bundled JDKs).
Fix:
```bash
/usr/libexec/java_home -V                       # list installed JDKs
export JAVA_HOME=$(/usr/libexec/java_home -v 21) # point Maven at 21
mvn -version                                     # confirm it now shows 21
```

### 2. Frontend

```bash
cd frontend
npm install
npm run dev
```
Runs on **http://localhost:5173**, proxies `/api/*` to the backend.

### 3. Try it

1. Open http://localhost:5173 → redirected to `/login`.
2. Log in with `demo@codecrackai.dev` / `Demo@1234`, or sign up fresh.
3. Browse **Topics** and **Questions** (real data from the backend).
4. Open a question, reveal hints/editorial, click **Mark Solved** — this
   awards XP, updates your streak, and schedules a spaced-repetition
   revision date.
5. Check **Dashboard** (XP/streak/heatmap/topic mastery), **Study Plan**
   (today's picks + revision-due list), and **Analytics** (charts) — all
   backed by real endpoints.

## Project Structure

```
codecrack-ai/
├── backend/
│   └── src/main/java/com/codecrackai/backend/
│       ├── config/       # CORS, Security, JPA auditing, WebMvc
│       ├── security/     # JWT service/filter, UserPrincipal, @CurrentUser
│       ├── controller/   # REST endpoints (incl. admin/**)
│       ├── service/      # Business logic (+ impl/)
│       ├── repository/   # Spring Data JPA + Specifications
│       ├── entity/       # JPA entities (+ enums/)
│       ├── dto/          # request/ and response/ DTOs
│       ├── mapper/       # MapStruct mappers
│       ├── exception/    # Custom exceptions + GlobalExceptionHandler
│       └── seed/         # Dev-only sample data loader
│   └── src/test/java/... # JUnit + Mockito unit tests
├── frontend/
│   └── src/
│       ├── pages/         # Dashboard, Topics, Questions, StudyPlan, Analytics, auth/
│       ├── components/    # ui/ (design system) and layout/ (shell, guards)
│       ├── store/         # Zustand: auth, ui
│       ├── lib/           # apiClient (with 401 refresh), api.ts, auth.ts
│       └── types/
├── .github/               # CI workflow, issue templates, PR template
├── docker-compose.yml
└── .env.example
```

## API Reference

All endpoints are prefixed `/api/v1`. Public endpoints: `/auth/**`, `/health`,
and `GET` on `/topics/**` and `/questions/**`. Everything else requires
`Authorization: Bearer <accessToken>`. `/admin/**` requires the `ADMIN` role.

| Method | Path | Description |
|---|---|---|
| POST | `/auth/signup` | Create account, returns tokens |
| POST | `/auth/login` | Log in, returns tokens, updates streak |
| POST | `/auth/refresh` | Exchange refresh token for a new access token |
| POST | `/auth/forgot-password` | Request reset link (logged to console in dev) |
| POST | `/auth/reset-password` | Reset password with token |
| GET | `/auth/me` | Current user profile |
| GET | `/topics` | List root topics with question counts |
| GET | `/topics/{slug}` | Get one topic |
| GET | `/questions?difficulty=&topicId=&company=&pattern=&search=&page=&size=` | Paginated, filterable question list |
| GET | `/questions/{slug}` | Full question detail |
| GET/PUT | `/me/progress/{questionId}` | Get/update your progress on a question |
| GET | `/study-plan` | Today's recommended questions + revision-due list |
| GET | `/dashboard/summary` | XP, streak, accuracy, 30-day heatmap, topic mastery |
| GET | `/analytics` | Difficulty breakdown + topic mastery charts |
| POST/PUT/DELETE | `/admin/topics`, `/admin/questions` | Admin CRUD (ADMIN role) |

## Testing

```bash
cd backend
mvn test
```
Covers JWT issuing/validation, XP-awarding and spaced-repetition logic, and
auth signup/login flows — all with Mockito, no database required.

## Deployment

```bash
docker compose up --build
```
Builds and runs Postgres + backend (prod profile) + frontend (nginx) together.
See `.env.example` for the environment variables to set (JWT secret, DB
credentials, SMTP for real password-reset emails).

GitHub Actions (`.github/workflows/ci.yml`) builds and tests both backend and
frontend on every push/PR to `main`.

## What's intentionally out of scope for v1

To ship something real rather than an infinite backlog, a few things from the
original vision are deliberately deferred:
- **Full curated question bank** — the seed data is a handful of original
  sample questions proving the pipeline works end-to-end, not the "20% that
  matters" bank itself. The admin CRUD API is ready for that content to be
  added.
- **Admin UI** — admin endpoints exist and are ROLE_ADMIN-protected, but there's
  no dedicated admin frontend yet; use the API directly or a tool like
  Postman/curl with the seeded admin account.
- **OAuth, Redis caching, Kubernetes manifests** — the architecture doesn't
  block these (stateless JWT + clean service layer), they're just not built.

## License

MIT — see `LICENSE`.
