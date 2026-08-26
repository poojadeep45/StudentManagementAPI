# Student Management API

A REST API for managing students, courses, and enrollments, built with **Java** and **Spring Boot**. It started as a project during a Spring Boot internship at Systems Limited and has since been extended into a full portfolio piece covering layered architecture, JPA relationships, security (JWT + OAuth 2.0/OIDC), testing, and containerized cloud deployment.

## Features

- **CRUD operations** with Jakarta Bean Validation on all endpoints
- **Layered architecture**: Controller → Service → Repository, with global exception handling
- **Six related JPA entities** — `Student`, `Department`, `Address`, `ContactInfo`, `Instructor`, `Course`, `Enrollment` — with custom JPQL/native queries and Jackson circular-reference handling
- **Authentication & authorization**
  - JWT-based auth (registration, login, role-based access, a custom `JwtAuthFilter`)
  - Email verification and password reset/change flows
  - Full OAuth 2.0 / OIDC stack — Authorization Server, Resource Server, and OAuth Client all implemented in the same application
- **Testing** across all three layers: `@DataJpaTest` (repository), Mockito (service), `@WebMvcTest` (controller)
- **Observability & config**: rolling-file logging, Spring Boot Actuator, multi-document YAML with environment-specific Spring Profiles
- **Database**: MySQL in production, migrated from an initial H2 setup

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| Framework | Spring Boot, Spring Data JPA, Spring Security |
| Database | MySQL (H2 for local/dev) |
| Auth | JWT, OAuth 2.0 / OIDC |
| Testing | JUnit, Mockito, Spring Test |
| Containerization | Docker (multi-stage build), Docker Compose |
| CI/CD | GitHub Actions → Docker Hub |
| Hosting | Railway |

## Deployment

The app is fully containerized and deployed end to end:

1. **Dockerfile** — multi-stage build (JDK build stage + lightweight JRE runtime stage)
2. **Docker Compose** — runs the app alongside a MySQL service for local integration testing
3. **Docker Hub** — images are pushed automatically on every push to `main`
4. **GitHub Actions** — CI/CD workflow builds and pushes the image, using repository secrets for Docker Hub credentials
5. **Railway** — live cloud deployment with a managed MySQL instance, a public URL, and environment variables injected at runtime (no hardcoded credentials)

## Getting Started

### Prerequisites

- Java 17+ (or the version specified in `pom.xml`)
- Maven (or use the included `mvnw` wrapper)
- Docker & Docker Compose (for containerized setup)
- MySQL (if running without Docker)

### Run locally

```bash
git clone https://github.com/<your-username>/StudentManagementAPI.git
cd StudentManagementAPI

# using the Maven wrapper
./mvnw spring-boot:run
```

Configure your database connection and JWT secret in `src/main/resources/application.yml` (or via environment variables) before running.

### Run with Docker Compose

```bash
docker compose up --build
```

This starts the API together with a MySQL container, fully networked and ready to accept requests.

## API Overview

The API exposes REST endpoints for:

- `/students` — CRUD for student records
- `/departments`, `/courses`, `/instructors`, `/enrollments` — related resources and relationships
- `/auth` — registration, login, email verification, password reset
- `/oauth2` — OAuth 2.0 / OIDC endpoints (authorization, token, JWKS)

> Full endpoint documentation (request/response shapes, status codes) can be added here or generated via springdoc-openapi / Swagger.

## Testing

```bash
./mvnw test
```

Covers repository queries (`@DataJpaTest`), service logic (Mockito), and controller behavior (`@WebMvcTest`).

## Project Structure

```
src/main/java/com/example/StudentManagementAPI/
├── controller/
├── service/
├── repository/
├── entity/
├── security/
├── exception/
└── config/
```

## About

Built as a learning project and portfolio piece to demonstrate production-style Spring Boot practices: layered design, relational data modeling, modern authentication, automated testing, and a real CI/CD-to-cloud pipeline.

## License

Specify a license (e.g. MIT) here if you intend to open-source this project.
