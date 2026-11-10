# ms-arbres

Registre des arbres plantés (espèce, position GPS, gouvernorat, date, événement) et catalogue des espèces.

- Port : 8084 · Base : MySQL (arbres_db, créée automatiquement) · Nom Eureka : ARBRES
- Swagger : http://localhost:8084/swagger-ui.html
- MySQL (XAMPP) : utilisateur root, mot de passe vide par défaut (sinon variable d environnement `DB_PASSWORD`)
- Sans MySQL : lancer avec le profil `h2` (base en mémoire), console sur http://localhost:8084/h2-console

| Méthode | URI | Codes |
|---|---|---|
| GET | /api/arbres/hello | 200 |
| GET | /api/arbres | 200 |
| GET | /api/arbres/{id} | 200, 404 |
| POST | /api/arbres | 201, 400, 404 |
| PUT | /api/arbres/{id} | 200, 400, 404 |
| PATCH | /api/arbres/{id}/statut | 200, 400, 404 |
| DELETE | /api/arbres/{id} | 204, 404 |
| GET / POST | /api/arbres/especes | 200 / 201, 400, 409 |
| GET | /api/arbres/especes/{id} | 200, 404 |
| PUT | /api/arbres/especes/{id} | 200, 400, 404, 409 |
| DELETE | /api/arbres/especes/{id} | 204, 404, 409 |

La vérification de l événement (OpenFeign) arrive en S6, la publication `ArbrePlante` (RabbitMQ) en S7.
