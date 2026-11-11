# 🌳 ReboisTN – Reboisement et suivi des arbres en Tunisie

> **Reboiser la Tunisie et suivre chaque arbre planté, de la plantation à la survie.**

Plateforme web distribuée (architecture microservices) pour organiser des journées de plantation, inscrire des bénévoles, enregistrer les arbres plantés et suivre leur survie dans le temps.

Projet **MT-41 – Applications Web Distribuées** · ESPRIT · 2026-2027 · Équipe **4SSA1-g3**

---

## 📌 Sommaire

1. [Présentation](#-présentation)
2. [Acteurs](#-acteurs)
3. [Architecture](#-architecture)
4. [Microservices](#-microservices)
5. [Communication entre services](#-communication-entre-services)
6. [Technologies](#-technologies)
7. [Structure du dépôt](#-structure-du-dépôt)
8. [Prérequis](#-prérequis)
9. [Lancement](#-lancement)
10. [API](#-api)
11. [Organisation Git et gestion de projet](#-organisation-git-et-gestion-de-projet)
12. [Équipe](#-équipe)

---

## 🎯 Présentation

**Problème** : en 2023, 4 800 ha de forêt ont brûlé en Tunisie (5 900 ha en 2022) et seuls 4 % des 438 incendies de l'été avaient des causes purement naturelles (source : Webdo, décembre 2023). Comment organiser les plantations, mobiliser les bénévoles et vérifier que les arbres plantés survivent ?

**ReboisTN** permet de :

- gérer les **utilisateurs** et les **organisations** (ONG, écoles, municipalités) ;
- créer et publier des **événements de plantation** (lieu, date, places) ;
- **inscrire** les bénévoles, confirmer leur inscription et marquer leur présence ;
- enregistrer chaque **arbre planté** (espèce, position GPS, date) ;
- assurer le **suivi de survie** (état de santé, mesures, interventions, photos) ;
- envoyer des **notifications** (rappels, confirmations, alertes) ;
- calculer des **statistiques** (taux de survie par zone et par espèce).

## 👥 Acteurs

| Acteur | Rôle |
|---|---|
| **Visiteur** | Consulte les événements et la carte des plantations |
| **Bénévole** | S'inscrit aux plantations, plante des arbres, reçoit des notifications |
| **Organisateur** (ONG, école, municipalité) | Crée les événements, gère les inscriptions, suit la survie des arbres (hérite des droits du bénévole) |
| **Administrateur** | Gère les comptes, les organisations et les espèces, consulte les statistiques |

L'authentification et les rôles sont gérés par **Keycloak**.

## 🏗️ Architecture

Chaque microservice possède sa propre base de données et expose une API REST. Tous les appels des clients passent par l'**API Gateway** avec un jeton JWT. Les échanges asynchrones passent par **RabbitMQ**.

```
 Web (Angular) / Mobile ──► API Gateway :8080 ◄──► Keycloak :8180
                                  │
      ┌───────────┬───────────┬───┴───────┬────────────┬────────────┬──────────────┐
      ▼           ▼           ▼           ▼            ▼            ▼              ▼
  utilisateurs evenements  arbres    inscriptions    suivi    notifications   statistiques
    :8082        :8081      :8083       :8084        :8085       :8086          :8087
                    └──────────── RabbitMQ :5672 (événements) ────────────┘

   Eureka :8761 (découverte)  ·  Config Server :8888 (configuration)
```

Le diagramme complet se trouve dans [`Documentation/Diagrams/Architecture-Technique`](Documentation/Diagrams/Architecture-Technique).

## 🧩 Microservices

| Microservice | Responsable | Rôle | Base de données | Port |
|---|---|---|---|---|
| `ms-utilisateurs` | Kmar Srarfi | Profils et organisations | H2 | 8082 |
| `ms-evenements` | Thouraya Allagui | Journées de plantation | H2 | 8081 |
| `ms-inscriptions` | Ghofrane Jridi | Inscriptions, places, présence | MySQL | 8084 |
| `ms-arbres` | Mezen Elghoul | Registre des arbres et espèces | MySQL | 8083 |
| `ms-suivi` | Souhail Mechergui | Contrôles de survie, photos | MySQL | 8085 |
| `ms-notifications` | Ayoub Abed | Alertes e-mail et in-app | H2 | 8086 |
| `ms-statistiques` | **Groupe** | Taux de survie et bilans | MongoDB | 8087 |

Infrastructure : `discovery-server` (Eureka, 8761), `config-server` (8888), `api-gateway` (8080), Keycloak (8180), RabbitMQ (5672 / UI 15672).

## 🔄 Communication entre services

**Synchrone – OpenFeign (séance 6), avec fallback Resilience4j**

| Appelant | Appelé | Objectif |
|---|---|---|
| `ms-inscriptions` | `ms-evenements` | Vérifier que l'événement est publié et qu'il reste des places |
| `ms-inscriptions` | `ms-utilisateurs` | Vérifier que le bénévole existe |
| `ms-arbres` | `ms-evenements` | Vérifier que l'événement existe |
| `ms-suivi` | `ms-arbres` | Vérifier que l'arbre existe avant d'enregistrer un contrôle |

**Asynchrone – RabbitMQ (séance 7), exchange `reboistn.events`**

| Événement | Producteur | Consommateur(s) | Effet |
|---|---|---|---|
| `EvenementPublie` | `ms-evenements` | notifications, statistiques | Préparer les notifications, mettre à jour les statistiques |
| `EvenementAnnule` | `ms-evenements` | notifications | Informer les participants |
| `EvenementTermine` | `ms-evenements` | statistiques | Actualiser les statistiques |
| `InscriptionConfirmee` | `ms-inscriptions` | notifications | Confirmer au bénévole |
| `InscriptionAnnulee` | `ms-inscriptions` | notifications | Informer bénévole et organisateur |
| `PresenceMarquee` | `ms-inscriptions` | statistiques | Statistiques de participation |
| `ArbrePlante` | `ms-arbres` | suivi, statistiques | Créer le suivi initial, mettre à jour les statistiques |
| `ControleEnregistre` | `ms-suivi` | notifications, statistiques | Prévenir l'organisateur si l'état est MAUVAIS ou MORT, recalculer le taux de survie |

**Principe** : synchrone quand une réponse immédiate est nécessaire (vérification), asynchrone pour les notifications et statistiques (le producteur n'attend pas).

## 🛠️ Technologies

| Couche | Outils |
|---|---|
| Microservices | Java 17, Spring Boot 3.2 |
| Infrastructure | Spring Cloud Gateway, Eureka, Config Server |
| Sécurité | Keycloak (rôles, jetons JWT) |
| Communication | OpenFeign, Resilience4j, RabbitMQ |
| Bases de données | H2, MySQL, MongoDB |
| Documentation API | Swagger / OpenAPI |
| Conteneurisation | Docker, Docker Compose |
| Frontend | Angular, Leaflet (carte) ; application mobile |

## 📁 Structure du dépôt

```
reboisTN/
├── Backend/
│   ├── api-gateway/
│   ├── config-server/ · config-repo/
│   ├── discovery-server/
│   ├── microservices/
│   │   ├── ms-utilisateurs/   ├── ms-evenements/
│   │   ├── ms-inscriptions/   ├── ms-arbres/
│   │   ├── ms-suivi/          ├── ms-notifications/
│   │   └── ms-statistiques/
│   └── docker-compose.yml
├── Frontend/
│   ├── WebApp/
│   └── MobileApp/
├── Documentation/
│   ├── Presentation/          # présentation de la séance 4
│   ├── Diagrams/              # architecture, classes d'analyse et de domaine
│   └── ReboisTN_Dossier_Technique.docx
└── README.md
```

## ✅ Prérequis

- Java 17 et Maven
- Docker et Docker Compose
- Git
- Node.js (frontend)
- MySQL et MongoDB en local pour les services concernés (ou via Docker)

## 🚀 Lancement

```bash
git clone https://github.com/thourayaallagui/reboisTN.git
cd reboisTN/Backend

# Infrastructure : Eureka, Config Server, API Gateway, RabbitMQ, Keycloak
docker compose up -d --build
```

Les microservices métier seront ajoutés au `docker-compose` de la séance 5 à la séance 9. En attendant, chaque service se lance seul :

```bash
cd Backend/microservices/ms-utilisateurs     # ou un autre service
mvn spring-boot:run
```

Ordre recommandé : **Eureka → Config Server → microservices → API Gateway**.

| Outil | URL |
|---|---|
| API Gateway | http://localhost:8080 |
| Eureka | http://localhost:8761 |
| Config Server | http://localhost:8888 |
| Keycloak | http://localhost:8180 |
| RabbitMQ (UI) | http://localhost:15672 |

Test rapide d'un service :

```bash
curl http://localhost:8082/api/utilisateurs/hello
```

## 📡 API

Chaque service expose une API REST sous `/api/<ressource>`, routée par la Gateway.

| Service | Route Gateway | Exemples d'endpoints |
|---|---|---|
| `ms-utilisateurs` | `/api/utilisateurs/**` | `POST /api/utilisateurs`, `GET /api/utilisateurs/me`, `GET /api/utilisateurs/organisations` |
| `ms-evenements` | `/api/evenements/**` | `POST /api/evenements`, `PATCH /api/evenements/{id}/publier`, `GET /api/evenements/{id}/disponibilite` |
| `ms-inscriptions` | `/api/inscriptions/**` | `POST /api/inscriptions`, `PATCH /api/inscriptions/{id}/confirmer`, `PATCH /api/inscriptions/{id}/presence` |
| `ms-arbres` | `/api/arbres/**` | `POST /api/arbres`, `PATCH /api/arbres/{id}/statut`, `GET /api/arbres/especes` |
| `ms-suivi` | `/api/suivis/**` | `POST /api/suivis`, `GET /api/suivis/arbre/{arbreId}` |
| `ms-notifications` | `/api/notifications/**` | voir le README du service |
| `ms-statistiques` | `/api/statistiques/**` | `GET /api/statistiques/hello` (squelette) |

Le contrat d'API détaillé de chaque service est dans son README (`Backend/microservices/<service>/README.md`) et dans le dossier technique (`Documentation/`).

## 🌿 Organisation Git et gestion de projet

- `main` : version stable, validée par l'équipe.
- `feature/<service>` : une branche par fonctionnalité ou microservice.
- Commits explicites, par exemple `feat(ms-utilisateurs): ajout de GET /organisations/{id}`.
- Intégration via **Pull Request** vers `main`.
- **Gestion de projet** : GitHub Projects (tableau Kanban : Backlog → À faire → En cours → En revue → Terminé), une carte par tâche avec responsable, label par microservice et jalon (S5 à S9). Réunion hebdomadaire de 15 minutes.

## 👥 Équipe

| Membre | Microservice |
|---|---|
| Kmar Srarfi | `ms-utilisateurs` |
| Thouraya Allagui | `ms-evenements` |
| Ghofrane Jridi | `ms-inscriptions` |
| Mezen Elghoul | `ms-arbres` |
| Souhail Mechergui | `ms-suivi` |
| Ayoub Abed | `ms-notifications` |
| Groupe | `ms-statistiques`, API Gateway, Eureka, Config Server, Keycloak, Docker, Frontend |

Encadrante : **Badia Bouhdid** – ESPRIT
