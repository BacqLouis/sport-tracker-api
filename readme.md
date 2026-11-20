[Lire la documentation en français](readme_FR.md)
# Internal Social Network API (R3.03)

A Spring Boot RESTful API using Java 21, Maven, and PostgreSQL. This project implements the backend of an internal company social network, allowing employees (`User`) to publish, read, update, and delete posts (`Post`).

## Team Members

- **Blouin Alexis**
- **Goddefroy François**
- **Duriez Théo**

## Requirements

- Docker Engine or Docker Desktop
- Docker Compose (`docker-compose` command)
- Java 21 (if running locally outside Docker)

## Architecture & Data Model

The project follows a standard **N-Tier layered architecture**:
- `controller`: REST endpoints handling HTTP requests and responses.
- `service`: Business logic and validation rules.
- `repository`: Data access layer using Spring Data JPA.
- `entity`: Database models (`User` and `Post` linked by a `@ManyToOne` relationship).
- `dto`: Data Transfer Objects for request payload validation.
- `exception`: Global error handling (`@RestControllerAdvice`).

## Launch

### Option 1: Full Docker Compose (App + Database)
From the project root, run:

```bash
docker compose up
```

Compose builds the application image, starts PostgreSQL, waits for the database to become healthy, and then starts the application. On the first launch, building the image may take a few minutes.

Once the application has started via Docker, it is available at <http://localhost:8080>. PostgreSQL is exposed on `localhost:5433`.

To run the containers in the background instead, use `docker compose up -d`. To stop them, run `docker compose down` from the project root.

### Option 2: Local Development (Database in Docker + App in IDE/Maven)
To run only PostgreSQL and Adminer in Docker:

```bash
docker compose up -d postgres adminer
```

Then run `BaseSpringBootApplication.java` from VS Code or with `./mvnw spring-boot:run`. In local mode (`application.yaml`), the API is available at <http://localhost:8084>.

### Database Management (Adminer)
The Compose configuration uses a named volume (`postgres_data`) to retain database data when the containers are stopped.

Adminer is available at <http://localhost:8081/?pgsql=postgres&username=postgres&db=base_db> for managing the PostgreSQL database through a web interface (Password: `postgrespassword`).

## Reset the database

If the database is in a bad state and you want to start over, run:

```bash
docker compose down -v
docker compose up -d
```

The `-v` option deletes the database volume, so **all stored database data will be permanently removed**. The second command recreates the containers in the background.

## API Documentation (Swagger UI)

Once the application is running, interactive API documentation is available via Swagger UI:

- **Swagger UI:** http://localhost:8084/swagger-ui/index.html
- **OpenAPI JSON:** http://localhost:8084/v3/api-docs

## API Documentation

Base URL (local): `http://localhost:8084`

### 1. Users (`/api/users`)

| Method | Endpoint | Description | Success Status |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/users` | Create a new employee profile | `201 Created` |
| `GET` | `/api/users` | Retrieve all users | `200 OK` |
| `GET` | `/api/users/{id}` | Retrieve a user by ID | `200 OK` |
| `PUT` | `/api/users/{id}` | Update an existing user | `200 OK` |
| `DELETE` | `/api/users/{id}` | Delete a user | `204 No Content` |

**Example Request (`POST /api/users`):**
```json
{
  "username": "theo.duriez",
  "email": "theo.duriez@company.com",
  "department": "IT"
}
```

### 2. Posts (`/api/posts`)

| Method | Endpoint | Description | Success Status |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/posts` | Publish a new post (requires an existing `userId`) | `201 Created` |
| `GET` | `/api/posts` | Retrieve all posts | `200 OK` |
| `GET` | `/api/posts/{id}` | Retrieve a post by ID | `200 OK` |
| `PUT` | `/api/posts/{id}` | Update post content | `200 OK` |
| `DELETE` | `/api/posts/{id}` | Delete a post | `204 No Content` |

**Example Request (`POST /api/posts`):**
```json
{
  "content": "Hello team! Welcome to our new internal social network.",
  "userId": 1
}
```