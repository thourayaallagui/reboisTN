# ms-utilisateurs

Profils des utilisateurs (lien avec Keycloak par keycloakId) et organisations (ONG, école, municipalité).

- Port : 8081 · Base : H2 (utilisateurs_db, en mémoire) · Nom Eureka : UTILISATEURS
- Swagger : http://localhost:8081/swagger-ui.html
- Console H2 : http://localhost:8081/h2-console (JDBC URL jdbc:h2:mem:utilisateurs_db, utilisateur sa, mot de passe vide)

| Méthode | URI | Codes |
|---|---|---|
| GET | /api/utilisateurs | 200 |
| GET | /api/utilisateurs/{id} | 200, 404 |
| GET | /api/utilisateurs/me | 200, 401, 404 |
| POST | /api/utilisateurs | 201, 400, 409 |
| PUT | /api/utilisateurs/{id} | 200, 400, 404 |
| DELETE | /api/utilisateurs/{id} | 204, 404 |
| GET / POST | /api/utilisateurs/organisations | 200 / 201, 400 |

Les rôles (401/403) seront appliqués avec Keycloak en S8.
