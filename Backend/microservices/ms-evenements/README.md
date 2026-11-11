# MS1 – Event Service (Microservice Événements - ReboisTN)

## 1. Présentation de ReboisTN
**ReboisTN** est une plateforme web et mobile collaborative dédiée à la protection de l'environnement et au reboisement en Tunisie.
Elle encourage les citoyens et bénévoles à participer activement à des actions écologiques :
- Plantation d'arbres
- Nettoyage des plages et des littoraux
- Nettoyage des rues et oueds
- Sensibilisation environnementale
- Tri et recyclage des déchets

---

## 2. Rôle et responsabilité de MS1 – Événements
Le microservice **MS1 – Événements (`ms-evenements`)** est responsable de la gestion intégrale du cycle de vie des événements écologiques :
- Création, mise à jour, suppression et consultation des événements.
- Gestion du statut de l'événement (`BROUILLON`, `PUBLIE`, `ANNULE`, `TERMINE`).
- Suivi du nombre de places et du nombre d'inscrits en temps réel.
- **Principe Microservices** : Ce microservice est autonome. Il ne contient aucune table pour les utilisateurs ni pour les inscriptions.
  - `organisateurId` est un identifiant externe géré par **MS2 – Utilisateurs**.
  - Les inscriptions sont gérées par **MS4 – Inscriptions**.

---

## 3. Technologies utilisées
- **Java** : 17 (OpenJDK LTS)
- **Framework** : Spring Boot 3.2.5
- **Persistance** : Spring Data JPA / Hibernate 6.4.4
- **Base de données** : H2 en mémoire (`evenementsdb`)
- **Validation** : Jakarta Bean Validation
- **Documentation API** : Springdoc OpenAPI 2.5.0 (Swagger UI v3)
- **Outil de build** : Maven avec **Maven Wrapper (`mvnw` / `mvnw.cmd`)** intégré

---

## 4. Configuration de la base de données H2
Le microservice utilise sa propre base de données H2 en mémoire isolée.

Fichier [`src/main/resources/application.properties`](file:///c:/Users/thour/ReboiTN/reboisTN/Backend/microservices/ms-evenements/src/main/resources/application.properties) :
```properties
server.port=8081
spring.application.name=ms-evenements

# Base de données H2 en mémoire
spring.datasource.url=jdbc:h2:mem:evenementsdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Spring Data JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false

# Console H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

- **Accès à la console H2** : [http://localhost:8081/h2-console](http://localhost:8081/h2-console)
  - **JDBC URL** : `jdbc:h2:mem:evenementsdb`
  - **User Name** : `sa`
  - **Password** : *(laisser vide)*

---

## 5. Modèle de données & Règles métier

### A. Entité `Evenement`
- `id` : Long, clé primaire auto-incrémentée
- `titre` : String, obligatoire (max 200)
- `description` : TEXT, obligatoire
- `typeEvenement` : Enum (`PLANTATION`, `NETTOYAGE`, `SENSIBILISATION`, `RECYCLAGE`, `AUTRE`)
- `dateDebut` : LocalDateTime, obligatoire
- `dateFin` : LocalDateTime, obligatoire
- `lieu` : String, obligatoire
- `ville` : String, obligatoire
- `latitude` : Double, optionnel
- `longitude` : Double, optionnel
- `nombrePlaces` : Integer, obligatoire (> 0)
- `nombreInscrits` : Integer, obligatoire (défaut : 0)
- `imageUrl` : String, optionnel
- `statut` : Enum (`BROUILLON`, `PUBLIE`, `ANNULE`, `TERMINE`, défaut : `BROUILLON`)
- `organisateurId` : Long, identifiant externe MS2
- `dateCreation` : LocalDateTime (automatique `@PrePersist`)
- `dateModification` : LocalDateTime (automatique `@PrePersist` / `@PreUpdate`)

### B. Règles métier implémentées
1. **Champs obligatoires** : `titre`, `description`, `lieu`, `ville`, `typeEvenement`, `dateDebut`, `dateFin`, `nombrePlaces` et `organisateurId`.
2. **Cohérence temporelle** : `dateFin` doit être strictement postérieure à `dateDebut`.
3. **Capacité strictement positive** : `nombrePlaces > 0`.
4. **Non-négativité des inscrits** : `nombreInscrits >= 0`.
5. **Capacité maximale respectée** : `nombreInscrits <= nombrePlaces`.
6. **Irréversibilité d'annulation** : Un événement avec statut `ANNULE` ne peut plus être repassé à `PUBLIE`.
7. **Clôture des inscriptions** : Un événement `TERMINE`, `ANNULE` ou `BROUILLON` refuse toute nouvelle inscription.
8. **Statut initial** : Tout événement créé démarre avec le statut `BROUILLON`.
9. **Inscrits initiaux** : Tout événement créé débute avec `0` inscrits.

---

## 6. Données de démonstration (Tunisie)
Au démarrage, si la table est vide, un `CommandLineRunner` insère automatiquement 4 événements réels en Tunisie :
1. **Plantation à Tunis** : *"Grande plantation citoyenne au Belvédère"* (100 places, Tunis, statut `PUBLIE`)
2. **Nettoyage à Bizerte** : *"Nettoyage écocitoyen de la plage Sidi Salem"* (60 places, Bizerte, statut `PUBLIE`)
3. **Sensibilisation à Sousse** : *"Atelier citoyen : Climat et biodiversité du Sahel"* (40 places, Sousse, statut `PUBLIE`)
4. **Recyclage à Nabeul** : *"Opération éco-tri et recyclage du littoral de Nabeul"* (50 places, Nabeul, statut `BROUILLON`)

---

## 7. Liste des endpoints REST

| Méthode | URI | Description | Codes HTTP |
|---|---|---|---|
| `GET` | `/api/events` | Liste des événements (filtres optionnels : `?type=`, `?statut=`, `?ville=`) | 200 |
| `GET` | `/api/events/{id}` | Détail d'un événement par ID | 200, 404 |
| `POST` | `/api/events` | Créer un événement (statut `BROUILLON`, 0 inscrits) | 201, 400 |
| `PUT` | `/api/events/{id}` | Modifier un événement existant | 200, 400, 404 |
| `DELETE` | `/api/events/{id}` | Supprimer un événement | 204, 404 |
| `PATCH` | `/api/events/{id}/statut?nouveauStatut=...` | Mettre à jour le statut (`PUBLIE`, `ANNULE`, `TERMINE`) | 200, 400, 404 |
| `POST` | `/api/events/{id}/inscriptions` | Incrémenter les inscriptions (respect des règles) | 200, 400, 404 |
| `DELETE` | `/api/events/{id}/inscriptions` | Décrémenter les inscriptions | 200, 400, 404 |
| `GET` | `/hello` | Endpoint de salutation | 200 |
| `GET` | `/api/events/db-check` | Vérification de la connexion H2 (`SELECT 1`) | 200 |
| `GET` | `/actuator/health` | Indicateur de santé Spring Boot Actuator | 200 |
| `GET` | `/swagger-ui/index.html` | Interface Swagger UI interactive | 200 |

---

## 8. Commandes d'exécution

### Compilation et validation des tests
```powershell
cd reboisTN\Backend\microservices\ms-evenements
.\mvnw.cmd clean test
```

### Lancement du microservice
```powershell
.\mvnw.cmd spring-boot:run
```
L'application démarre sur le port **8081**.
Documentation Swagger : [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
