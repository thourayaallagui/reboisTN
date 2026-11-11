# 🌳 ReboisTN – Reboisement et suivi des arbres en Tunisie

Plateforme web distribuée pour organiser des événements de reboisement, inscrire des participants, planter des arbres et suivre leur survie dans le temps.

> Projet **MT-41 – Applications Web Distribuées** · ESPRIT · 2026-2027

---

## 📌 Sommaire

1. [Présentation](#-présentation)
2. [Architecture](#-architecture)
3. [Microservices](#-microservices)
4. [Communication entre services](#-communication-entre-services)
5. [Technologies](#-technologies)
6. [Structure du dépôt](#-structure-du-dépôt)
7. [Prérequis](#-prérequis)
8. [Lancement](#-lancement)
9. [API](#-api)
10. [Organisation Git](#-organisation-git)
11. [Équipe](#-équipe)

---

## 🎯 Présentation

ReboisTN permet de :

- gérer les **utilisateurs** (bénévoles, organisateurs, administrateurs) ;
- publier des **événements de reboisement** ;
- **inscrire** les participants et gérer les places disponibles ;
- enregistrer les **arbres plantés** ;
- assurer le **suivi** de chaque arbre (contrôles, taux de survie) ;
- envoyer des **notifications** (confirmation, annulation…) ;
- consulter des **statistiques** globales.

## 🏗️ Architecture

Architecture **microservices** : chaque service possède sa propre base de données et expose une API REST. Les échanges asynchrones passent par **RabbitMQ**.

```
                    ┌──────────────┐
   Frontend  ─────► │ API Gateway  │
                    └──────┬───────┘
                           │  (routage + sécurité Keycloak)
   ┌───────────┬───────────┼────────────┬───────────┬──────────────┐
   ▼           ▼           ▼            ▼           ▼              ▼
 ms-utili-  ms-even-   ms-inscrip-   ms-arbres   ms-suivi   ms-statistiques
 sateurs    ements      tions                                  (MongoDB)
                           │                        │
                           └──────► RabbitMQ ◄──────┘
                                       │
                                ms-notifications

   Eureka (découverte)  ·  Config Server (configuration centralisée)
```

> Le schéma détaillé se trouve dans `Documentation/Diagrams`.

## 🧩 Microservices

| Microservice       | Rôle                                         | Base de données | Port |
|--------------------|----------------------------------------------|-----------------|------|
| `ms-utilisateurs`  | Gestion des comptes et profils               | H2              | 8081 |
| `ms-evenements`    | Création et gestion des événements           | H2              | _à compléter_ |
| `ms-inscriptions`  | Inscriptions des participants aux événements | H2              | _à compléter_ |
| `ms-arbres`        | Arbres plantés                               | MySQL           | _à compléter_ |
| `ms-suivi`         | Suivi et contrôles des arbres                | MySQL           | _à compléter_ |
| `ms-notifications` | Envoi des notifications (email)              | H2              | _à compléter_ |
| `ms-statistiques`  | Indicateurs et statistiques (travail de groupe) | MongoDB      | _à compléter_ |

> ⚠️ Remplissez la colonne **Port** avec les valeurs exactes de votre diagramme : deux services ne doivent jamais partager le même port.

## 🔄 Communication entre services

**Synchrone (REST)**

- `ms-notifications` → `GET /api/utilisateurs/{id}` sur `ms-utilisateurs` pour récupérer l'email du destinataire.

**Asynchrone (RabbitMQ)**

| Événement          | Producteur        | Effet |
|--------------------|-------------------|-------|
| `InscriptionCreee` | `ms-inscriptions` | Déclenche une notification |
| `ControleEnregistre` | `ms-suivi`      | Recalcul du taux de survie (statistiques) |

_(à compléter avec la liste exacte des événements et des consommateurs)_

## 🛠️ Technologies

| Couche            | Outils |
|-------------------|--------|
| Backend           | Java, Spring Boot, Spring Cloud Gateway, Eureka, Config Server |
| Sécurité          | Keycloak |
| Messagerie        | RabbitMQ |
| Bases de données  | H2, MySQL, MongoDB |
| Conteneurisation  | Docker, Docker Compose |
| Frontend          | _à compléter_ |

## 📁 Structure du dépôt

```
reboisTN/
├── Backend/          # Microservices (un dossier par service)
├── Frontend/         # Application web
├── Documentation/    # Diagrammes, présentation, document technique
└── README.md
```

## ✅ Prérequis

- Java 17+ et Maven
- Docker et Docker Compose
- Git
- Node.js (pour le frontend)

## 🚀 Lancement

```bash
# 1. Cloner le dépôt
git clone https://github.com/thourayaallagui/reboisTN.git
cd reboisTN

# 2. Lancer l'infrastructure et les services
docker compose up --build
```

> _À compléter : ordre de démarrage (Config Server → Eureka → Gateway → microservices), URL de Keycloak, d'Eureka et de RabbitMQ._

**Lancer un microservice seul (exemple : `ms-utilisateurs`)**

```bash
cd Backend/ms-utilisateurs
mvn spring-boot:run
```

Test rapide :

```bash
curl http://localhost:8081/api/utilisateurs/hello
```

## 📡 API

Chaque service expose une API REST sous `/api/<ressource>`.

| Service           | Exemples d'endpoints |
|-------------------|----------------------|
| `ms-utilisateurs` | `GET /api/utilisateurs/hello`, `GET /api/utilisateurs/{id}` |
| `ms-evenements`   | _à compléter_ |
| `ms-inscriptions` | _à compléter_ |
| `ms-arbres`       | _à compléter_ |
| `ms-suivi`        | _à compléter_ |
| `ms-notifications`| _à compléter_ |
| `ms-statistiques` | _à compléter_ |

Le contrat d'API détaillé de chaque service est dans `Documentation/`.

## 🌿 Organisation Git

- `main` : version stable, validée par l'équipe
- `feature/<nom-du-service>` : une branche par fonctionnalité ou microservice
- Commits courts et explicites, par exemple `feat(ms-utilisateurs): ajout du endpoint hello`
- Intégration via **Pull Request** vers `main`

## 👥 Équipe

| Membre | Microservice |
|--------|--------------|
| _Nom 1_ | `ms-utilisateurs` |
| _Nom 2_ | `ms-evenements` |
| _Nom 3_ | `ms-inscriptions` |
| _Nom 4_ | `ms-arbres` |
| _Nom 5_ | `ms-suivi` |
| _Nom 6_ | `ms-notifications` |
| Groupe  | `ms-statistiques` |

Encadrante : **Badia Bouhdid** – ESPRIT
