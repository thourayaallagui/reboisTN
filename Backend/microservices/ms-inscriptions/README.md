# ms-inscriptions

Microservice REST de gestion des inscriptions de bénévoles aux événements ReboisTN. Il stocke les identifiants des bénévoles et des événements, ainsi que la date, le statut et la présence. Il ne gère pas les données détaillées de ces autres domaines.

## Prérequis

- Java 17 ou version ultérieure
- XAMPP avec MySQL démarré (port 3306)
- Le compte MySQL configuré pour autoriser la création de la base, ou la base créée manuellement

Par défaut, l'application se connecte à `jdbc:mysql://localhost:3306/inscriptionsdb` avec l'utilisateur `root` et un mot de passe vide, selon la configuration XAMPP courante. L'URL inclut `createDatabaseIfNotExist=true`. Vous pouvez remplacer ces valeurs avec `DB_URL`, `DB_USERNAME` et `DB_PASSWORD`.

Pour créer la base manuellement dans phpMyAdmin, ouvrez l'onglet **SQL** et exécutez :

```sql
CREATE DATABASE IF NOT EXISTS inscriptionsdb
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

## Démarrage et tests

Depuis la racine de ce microservice, démarrez l'application avec :

```powershell
.\mvnw.cmd spring-boot:run
```

L'API écoute sur `http://localhost:8080`. Les paramètres JDBC peuvent être définis dans l'environnement avant le démarrage, par exemple `$env:DB_PASSWORD = 'votre-mot-de-passe'`.

Swagger UI est disponible sur `http://localhost:8080/swagger-ui/index.html` et le document OpenAPI JSON sur `http://localhost:8080/v3/api-docs`.

Exécutez les tests avec H2 en mémoire (aucun serveur MySQL requis) :

```powershell
.\mvnw.cmd test
```

## Endpoints REST

Toutes les routes ont le préfixe `/api/inscriptions`.

| Méthode | Chemin | Description | Réponse nominale |
|---|---|---|---|
| POST | `/api/inscriptions` | Créer une inscription | 201 |
| GET | `/api/inscriptions` | Lister les inscriptions | 200 |
| GET | `/api/inscriptions/{id}` | Consulter une inscription | 200 |
| GET | `/api/inscriptions/benevole/{identifiantBenevole}` | Lister par bénévole | 200 |
| GET | `/api/inscriptions/evenement/{identifiantEvenement}` | Lister par événement | 200 |
| PUT | `/api/inscriptions/{id}` | Modifier les identifiants associés | 200 |
| PATCH | `/api/inscriptions/{id}/confirmer` | Confirmer | 200 |
| PATCH | `/api/inscriptions/{id}/annuler` | Annuler | 200 |
| PATCH | `/api/inscriptions/{id}/presence` | Enregistrer la présence | 200 |
| DELETE | `/api/inscriptions/{id}` | Supprimer une inscription | 204 |

Les requêtes de création et de modification attendent un JSON contenant `identifiantBenevole` et `identifiantEvenement` (nombres non nuls). Les exemples détaillés figurent dans [docs/postman-exemples.md](docs/postman-exemples.md), et la collection importable est [docs/ms-inscriptions.postman_collection.json](docs/ms-inscriptions.postman_collection.json).

## Statuts et règles métier

| Statut actuel | Action | Nouveau statut | Autorisé |
|---|---|---|---|
| — | Création | EN_ATTENTE | Oui |
| EN_ATTENTE | Confirmer | CONFIRMEE | Oui |
| EN_ATTENTE | Annuler | ANNULEE | Oui |
| CONFIRMEE | Annuler | ANNULEE | Oui |
| ANNULEE | Confirmer ou réactiver | — | Non |

- Une seule inscription est permise pour une même paire bénévole/événement. Un doublon renvoie `409 Conflict`.
- La présence ne peut être enregistrée que si le statut est `CONFIRMEE`; elle passe alors à `true`.
- Une inscription annulée ne peut pas être réactivée.
- La date de l'événement n'est pas vérifiée par ce microservice.

## Format des erreurs

Les erreurs sont des objets JSON contenant `timestamp`, `status` et `message`, par exemple :

```json
{
  "timestamp": "2026-11-10T12:00:00Z",
  "status": 404,
  "message": "Inscription introuvable avec l'identifiant 42"
}
```

Les erreurs de validation, les corps JSON illisibles et les paramètres d'URL mal formatés renvoient `400`. Une ressource inexistante renvoie `404`, une règle métier non respectée renvoie `409`, et une erreur inattendue renvoie `500` avec un message générique sans détail technique.

## Intégration future

Les bénévoles et les événements sont référencés uniquement par leurs identifiants (`identifiantBenevole` et `identifiantEvenement`). Le microservice ne crée pas leurs entités et ne vérifie pas actuellement leur existence auprès des services correspondants. Une intégration avec ces microservices pourra être ajoutée ultérieurement.
