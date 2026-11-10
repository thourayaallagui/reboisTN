# ms-suivi

Tree tracking service for ReboisTN. It uses Spring Boot 3.2.5, Spring Cloud 2023.0.1, Java 17, JPA, and MySQL/MariaDB.

## Configuration

Copy `.env.example` values into the environment used to start the service. Spring Boot does not read `.env` files automatically. Do not commit local environment files.

The default database is `suivi_db`. The schema must already exist: Hibernate uses `ddl-auto=update` for tables and does not create or drop the database. The XAMPP database currently used by some local setups is `ms_suivi`; set `MYSQL_DATABASE=ms_suivi` to continue using that existing schema without copying or deleting data.

Set `SUIVI_SECURITY_PASSWORD` before startup; the Basic-auth password has no source-code default. `MYSQL_DATABASE` defaults to `suivi_db`, `MYSQL_USER` to `root`, and `MYSQL_PASSWORD` to blank for local XAMPP. `SUIVI_SECURITY_MODE` defaults to `basic`. Set it to `keycloak` and provide the team's actual `KEYCLOAK_ISSUER_URI` only after the realm/issuer is agreed. The service requires authentication in both modes.

Run locally from PowerShell after exporting the environment variables:

```powershell
.\mvnw.cmd spring-boot:run
```

Eureka registration uses service ID `SUIVI` and `EUREKA_URL` (default `http://localhost:8761/eureka`).

## API routes

Existing direct routes remain available: `/api/interventions/**` and `/api/photos/**`. Gateway-compatible aliases are `/api/suivis/interventions/**` and `/api/suivis/photos/**`. The Gateway's existing `/api/suivis/**` route sends those aliases to `lb://SUIVI`.

`GET /hello` is a protected service health greeting. The current Gateway configuration only routes `/api/suivis/**`, so `/hello` is direct-service only unless the team adds a Gateway route.

`test.json` is a manually maintained request body for local curl smoke tests; it is not an automated test fixture.
