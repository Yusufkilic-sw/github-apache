# GitHub Apache — Spring Boot Case Study

A layered Spring Boot application that fetches repositories and contributors from the GitHub Apache organization, maps API responses to internal DTOs, persists entities in H2, and exposes clean, response-focused REST endpoints with metrics and structured logging.

## Project Versions

This repository contains two implementations:

1. **Main Project** (root directory) — Spring Boot 3.5.8, JDK 17+, includes REST endpoints and Swagger UI
2. **Version11** (version11/ directory) — Spring Framework (non-Boot) 6.0.13, JDK 11+, lightweight and compatible

### Which Version to Use?

- **Spring Boot version** (main): Full-featured, modern rapid development, REST API, OpenAPI documentation
- **Version11**: JDK 11 compatible, Spring Framework only, no controller layer, lightweight, more granular control

See [version11/README.md](version11/README.md) for the Spring Framework version details.

## Quick Start
- Requirements: Java 17, Maven (wrapper included), internet access
- Clone and open in VS Code (or any IDE)
- Create a `.env` file with your GitHub token:
- To start my github api token is included for faster build you can also use you own

```
GITHUB_API_TOKEN=ghp_your_token_here
```

- Build the project:

```powershell
./mvnw.cmd clean compile -DskipTests
```

- Run the app:

```powershell
./mvnw.cmd spring-boot:run
```

The app starts on `http://localhost:8080`.

## Configuration
- `src/main/resources/application.properties` reads `GITHUB_API_TOKEN` from environment variables (loaded via `spring-dotenv`).
- Logging configured via `logback-spring.xml` with reduced verbosity for frequent API calls.
- H2 in-memory database used for demo persistence; Spring Data JPA repositories manage entities.

### Key Packages
- `controller/` — REST endpoints (e.g., `DataController`).
- `service/` — Domain services (processing, repository/contributor operations).
- `client/` — GitHub API client (`GitHubApiClient`, `GitHubApiClientImpl`).
- `mapper/` — MapStruct mappers for response→DTO and entity→response.
- `repository/` — Spring Data JPA repositories.
- `dto/` — Request/response DTOs returned by controllers.
- `entity/` — JPA entities persisted in H2.
- `config/` — App configuration, interceptors, metrics, OpenAPI.
- `metrics/` — Processing metrics instrumentation.

## Services and Client Purpose
- `GitHubApiClient` (interface) and `GitHubApiClientImpl` (implementation):
  - Performs low-level HTTP calls to GitHub (org repositories, repo contributors, user details).
  - Handles HTTP exceptions gracefully: returns empty collections or null for 404s; warns for other 4xx/5xx.
  - Uses an auth interceptor (`GitHubAuthInterceptor`) to add the token to requests.

- `GitHubApiServiceImpl`:
  - Anti-corruption layer: maps raw client responses into internal DTOs.
  - Shields callers with safe defaults on errors (e.g., empty lists, minimal DTOs).
  - Reduces log noise: debug-level for frequent fetches; concise info summaries.

- `DataProcessingServiceImpl` (under `service/impl/`):
  - Orchestrates the end-to-end flow: fetch repositories → fetch contributors → enrich with user details → map and persist entities.
  - Handles mapping/persistence errors locally; relies on `GitHubApiServiceImpl` for API safety.

- `RepositoryServiceImpl` and `ContributorServiceImpl`:
  - Provide CRUD and finder operations on `RepositoryEntity` and `ContributorEntity`.
  - Used by controller to serve response DTOs through `EntityToDtoMapper`.

## DTOs and Mapping
- Response DTOs: `RepositoryResponseDto`, `ContributorResponseDto` returned by controllers.
- `EntityToDtoMapper` (MapStruct) converts entities to response DTOs, including embedded license fields.
- Mappers in `mapper/` handle response→DTO and entity→response conversions consistently.

## Endpoints
The controller (`DataController`) exposes endpoints such as:
- `GET /repositories` — List repositories as `RepositoryResponseDto`.
- `GET /repositories/{repoName}/contributors` — List contributors for a repository as `ContributorResponseDto`.
- `GET /contributors` — List all contributors as `ContributorResponseDto`.
- `GET /repositories/count` — Count of repositories.
- `GET /contributors/count` — Count of contributors.

Note: Exact paths may vary slightly based on controller annotations; the contract returns DTOs, not entities.

## Running Tests
Run unit tests:

```powershell
./mvnw.cmd test
```

## Metrics & Logging
- Metrics: Counters/timers via `metrics/ProcessingMetrics` and `MetricsConfig`/`MetricsInitializer`.
- Logging: SLF4J/Logback with debug logs for frequent fetches and quiet 404 handling in the client.

## Development Notes
- Java 17 and Spring Boot 3.x.
- MapStruct and Lombok used to reduce boilerplate.
- H2 in-memory DB for local development; swap to a persistent DB by updating Spring Data config.

## Swagger & Controller (Out of Scope, Added for Convenience)
- Purpose: h2 interface makes it hard to understand if the project is correctly doing its job,
    so I added controller and swagger to make it easier to understand if the project working correctly

## Troubleshooting
- If you see authorization errors, verify `GITHUB_API_TOKEN` in `.env` and that it has `repo`/`read:org` scopes.
- For rate limits, consider using a personal token and reduce request frequency.


## License
For internal/demo use. No explicit license header added.
