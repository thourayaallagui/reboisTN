# ms-utilisateurs

Microservice **Utilisateurs** du projet **ReboisTN** : plateforme de reboisement de la Tunisie qui permet d'organiser les plantations, de mobiliser les bénévoles et de suivre la survie de chaque arbre planté.

> Mini-projet Applications Web Distribuées (MT-41) · ESPRIT · 2026-2027

---

## 1. Rôle du microservice

`ms-utilisateurs` gère :

- les **profils des utilisateurs**, liés à leur compte Keycloak par `keycloakId` ;
- les **organisations** (ONG, école, municipalité) ;
- les **rôles métier** : `ADMIN`, `ORGANISATEUR`, `BENEVOLE`.

L'authentification (login, mot de passe, jetons JWT) est assurée par **Keycloak**. Ce microservice ne stocke que le profil métier associé.

| Élément | Valeur |
|---|---|
| Nom Eureka | `UTILISATEURS` |
| Port | `8082` |
| Base de données | H2 (`utilisateurs_db`) |
| Préfixe des routes | `/api/utilisateurs` |
| Responsable | Kmar Srarfi |

## 2. Place dans l'architecture

```
Client (Angular / mobile)
        │
        ▼
API Gateway (8080) ──► ms-utilisateurs (8082) ──► H2 (utilisateurs_db)
        │                      ▲
        ▼                      │ OpenFeign (synchrone)
 Keycloak (8180)        ms-inscriptions (8084)
```

- Le client passe toujours par l'**API Gateway** avec un jeton `Bearer JWT`.
- Le service s'enregistre dans **Eureka** (`:8761`) et récupère sa configuration depuis le **Config Server** (`:8888`).
- **Synchrone** : `ms-inscriptions` appelle `GET /api/utilisateurs/{id}` (OpenFeign) pour vérifier qu'un bénévole existe.
- **Asynchrone** : aucun événement RabbitMQ publié ou consommé à ce stade.

## 3. Modèle de données

### Utilisateur

| Champ | Type | Description |
|---|---|---|
| `id` | Long | Identifiant technique |
| `keycloakId` | String | Identifiant du compte Keycloak |
| `nom` | String | Nom |
| `prenom` | String | Prénom |
| `email` | String | Adresse e-mail |
| `telephone` | String | Numéro de téléphone |
| `role` | Role | `ADMIN`, `ORGANISATEUR`, `BENEVOLE` |
| `organisationId` | Long | Référence logique vers l'organisation (`null` pour un simple bénévole) |

### Organisation

| Champ | Type | Description |
|---|---|---|
| `id` | Long | Identifiant technique |
| `nom` | String | Nom de l'organisation |
| `type` | TypeOrganisation | `ONG`, `ECOLE`, `MUNICIPALITE` |
| `gouvernorat` | String | Gouvernorat |

> `Utilisateur.organisationId` est une **référence logique** (0..1), sans clé étrangère stricte.

## 4. API exposée

Toutes les routes sont accessibles via la Gateway : `http://localhost:8080/api/utilisateurs/...`

| Méthode | Endpoint | Fonction | Codes |
|---|---|---|---|
| GET | `/api/utilisateurs` | Lister les utilisateurs (ADMIN) | 200 |
| GET | `/api/utilisateurs/{id}` | Consulter un utilisateur | 200, 404 |
| PUT | `/api/utilisateurs/{id}` | Modifier un utilisateur | 200, 400, 404 |
| DELETE | `/api/utilisateurs/{id}` | Supprimer un utilisateur | 204, 404 |
| GET | `/api/utilisateurs/me` | Profil de l'utilisateur connecté (identifié par son jeton) | 200 |
| POST | `/api/utilisateurs` | Créer un profil après inscription Keycloak | 201, 400, 409 |
| GET | `/api/utilisateurs/organisations` | Lister les organisations | 200 |
| POST | `/api/utilisateurs/organisations` | Créer une organisation | 201, 400 |
| GET | `/api/utilisateurs/organisations/{id}` | Détail d'une organisation | 200, 404 |
| GET | `/api/utilisateurs/hello` | Vérifier que le service répond | 200 |

### Règles métier

- **Création de profil** : effectuée après l'inscription Keycloak ; `409 Conflict` si le profil existe déjà.
- **Consultation** : `404 Not Found` si l'utilisateur est introuvable.
- **Profil connecté** : l'utilisateur est identifié grâce à son jeton JWT.
- **Organisations** : de type ONG, école ou municipalité.

### Codes de réponse HTTP

| Code | Signification |
|---|---|
| 200 | Succès |
| 201 | Ressource créée |
| 204 | Suppression réussie |
| 400 | Requête invalide |
| 401 / 403 | Non authentifié / accès refusé (Keycloak, séance 8) |
| 404 | Ressource introuvable |
| 409 | Conflit métier (profil déjà existant) |

### Exemples

```bash
# Vérifier que le service répond (via la Gateway)
curl http://localhost:8080/api/utilisateurs/hello

# Créer une organisation
curl -X POST http://localhost:8080/api/utilisateurs/organisations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT>" \
  -d '{"nom":"Association Verte","type":"ONG","gouvernorat":"Tunis"}'

# Créer un profil
curl -X POST http://localhost:8080/api/utilisateurs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT>" \
  -d '{
        "keycloakId":"<id-keycloak>",
        "nom":"Ben Ali",
        "prenom":"Sami",
        "email":"sami@example.com",
        "telephone":"20123456",
        "role":"BENEVOLE"
      }'

# Profil de l'utilisateur connecté
curl http://localhost:8080/api/utilisateurs/me -H "Authorization: Bearer <JWT>"
```

## 5. Technologies

| Domaine | Technologie |
|---|---|
| Microservice | Spring Boot |
| Découverte de services | Spring Cloud Netflix Eureka (client) |
| Point d'entrée | Spring Cloud Gateway |
| Sécurité | Keycloak (rôles, jetons JWT) |
| Base de données | H2 |
| Documentation API | Swagger / OpenAPI |
| Conteneurisation | Docker, Docker Compose |

## 6. Prérequis

- Java 17 (ou la version utilisée par le projet)
- Maven 3.8+
- Git
- Eureka Server et API Gateway démarrés (voir le dossier `Backend`)
- Docker (optionnel, pour le lancement global)

## 7. Lancement

### En local

```bash
# 1. Cloner le dépôt
git clone https://github.com/thourayaallagui/reboisTN.git
cd reboisTN/Backend/ms-utilisateurs

# 2. Démarrer le service
mvn spring-boot:run
```

Ordre de démarrage recommandé : **Config Server → Eureka → ms-utilisateurs → API Gateway**.

### Avec Docker

```bash
docker build -t ms-utilisateurs .
docker run -p 8082:8082 ms-utilisateurs
```

Ou via le `docker-compose` global du projet :

```bash
docker compose up -d
```

### Accès utiles

| Outil | URL |
|---|---|
| Service direct | http://localhost:8082/api/utilisateurs/hello |
| Via la Gateway | http://localhost:8080/api/utilisateurs/hello |
| Swagger UI | http://localhost:8082/swagger-ui/index.html |
| Console H2 | http://localhost:8082/h2-console |
| Eureka | http://localhost:8761 |

> Console H2 : JDBC URL `jdbc:h2:mem:utilisateurs_db`. Adapter selon `application.properties`.

## 8. Configuration

Extrait type de `application.properties` (à adapter) :

```properties
spring.application.name=UTILISATEURS
server.port=8082

# H2
spring.datasource.url=jdbc:h2:mem:utilisateurs_db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true

# Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

## 9. Structure du projet

```
ms-utilisateurs/
├── src/main/java/.../
│   ├── controller/     # Contrôleurs REST
│   ├── service/        # Logique métier
│   ├── repository/     # Accès aux données (JPA)
│   ├── entity/         # Utilisateur, Organisation, Role, TypeOrganisation
│   ├── dto/            # Objets de transfert
│   └── exception/      # Gestion des erreurs (404, 409, ...)
├── src/main/resources/
│   └── application.properties
├── Dockerfile
├── pom.xml
└── README.md
```

## 10. Feuille de route

| Séance | Objectif | État |
|---|---|---|
| S4 | Validation de la conception, projet Spring Boot créé | ✅ |
| S5 | Enregistrement Eureka, routage via la Gateway | ⏳ |
| S6 | Appels OpenFeign entrants (`ms-inscriptions`) | ⏳ |
| S8 | Sécurité Keycloak : rôles, tokens (401 / 403) | ⏳ |
| S9 | Dockerisation, intégration du front | ⏳ |
| S10 | Tests finaux, démo et soutenance | ⏳ |

## 11. Contribution

- Une branche par fonctionnalité (`feature/...`), Pull Request obligatoire.
- Suivi des tâches dans **GitHub Projects** (Kanban : Backlog → À faire → En cours → En revue → Terminé).

## 12. Auteur

- **Responsable du microservice** : Kmar Srarfi
- **Projet** : ReboisTN · ESPRIT · Module MT-41 Applications Web Distribuées
