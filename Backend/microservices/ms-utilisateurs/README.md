# ms-utilisateurs

Microservice ReboisTN responsable des profils utilisateurs (référence Keycloak via `keycloakId`) et des organisations (ONG, écoles, municipalités).

## Stack

* Java 17, Spring Boot 3.2.5
* Spring Web, Spring Data JPA, Jakarta Validation
* H2 en mémoire (`utilisateurs\_db`)
* Eureka Client
* OpenAPI / Swagger UI

## Lancer localement

Prérequis : JDK 17 et Maven.

```bash
mvn spring-boot:run
```

Le service écoute sur `http://localhost:8082`.

## Vérification de disponibilité

```bash
curl http://localhost:8082/api/utilisateurs/hello
```

Réponse attendue (HTTP 200) :

```json
{
  "service": "ms-utilisateurs",
  "status": "UP",
  "message": "Microservice utilisateurs opérationnel"
}
```

## Documentation et outils

* Swagger UI : http://localhost:8082/swagger-ui.html
* OpenAPI JSON : http://localhost:8082/v3/api-docs
* Console H2 : http://localhost:8082/h2-console
* JDBC URL : `jdbc:h2:mem:utilisateurs\_db`; user `sa`; password vide.

## API REST

|Méthode|URI|Codes HTTP attendus|
|-|-|-|
|GET|`/api/utilisateurs/hello`|200|
|GET|`/api/utilisateurs`|200|
|GET|`/api/utilisateurs/{id}`|200, 404|
|GET|`/api/utilisateurs/me`|200, 401, 404|
|POST|`/api/utilisateurs`|201, 400, 409|
|PUT|`/api/utilisateurs/{id}`|200, 400, 404, 409|
|DELETE|`/api/utilisateurs/{id}`|204, 404|
|GET|`/api/utilisateurs/organisations`|200|
|POST|`/api/utilisateurs/organisations`|201, 400|

`/me` utilise temporairement l'en-tête `X-Keycloak-Id`. La validation JWT par Keycloak est prévue dans une étape ultérieure du projet.

## Docker

Construire l'image depuis ce dossier :

```bash
docker build -t ms-utilisateurs .
docker run --rm -p 8082:8082 ms-utilisateurs
```

## Notes

H2 est une base en mémoire adaptée au prototype de validation ; les données sont réinitialisées au redémarrage. La configuration de sécurité des rôles sera intégrée avec Keycloak ultérieurement.

