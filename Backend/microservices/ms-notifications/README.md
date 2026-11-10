# ms-notifications — ReboisTN

## Rôle
Microservice de **notifications** de la plateforme ReboisTN.  
Il gère la création et l'exposition des notifications (`EMAIL` ou `IN_APP`) générées à partir d'événements métier asynchrones (via RabbitMQ). Aucun appel synchrone sortant.

## Informations techniques

| Propriété              | Valeur                                  |
|------------------------|-----------------------------------------|
| Port                   | `8086`                                  |
| spring.application.name| `NOTIFICATIONS`                         |
| Base de données        | H2 en mémoire (`notifications_db`)      |
| Console H2             | http://localhost:8086/h2-console         |
| Swagger UI             | http://localhost:8086/swagger-ui.html    |
| Profil actif           | `dev` (data seeder avec 3 notifications)|

## Endpoints REST

| Méthode | URI                            | Description                              | Codes |
|---------|--------------------------------|------------------------------------------|-------|
| GET     | `/api/notifications`           | Notifications de l'utilisateur connecté  | 200   |
| GET     | `/api/notifications/non-lues`  | Nombre de notifications non lues         | 200   |
| PATCH   | `/api/notifications/{id}/lue`  | Marquer une notification comme lue       | 200, 404 |
| DELETE  | `/api/notifications/{id}`      | Supprimer une notification               | 204, 404 |

> **Note** : L'utilisateur connecté est identifié par l'en-tête temporaire `X-User-Id`.  
> En S8, cet en-tête sera remplacé par le `sub` du JWT Keycloak.

## Événements consommés (RabbitMQ — à activer en S7)

| Événement                | Routing Key            | Source           |
|--------------------------|------------------------|------------------|
| `EvenementCreeEvent`     | `evenement.cree`       | ms-evenements    |
| `InscriptionConfirmeeEvent` | `inscription.confirmee` | ms-inscriptions |
| `ArbrePlanteEvent`       | `arbre.plante`         | ms-arbres        |
| `ControleEnregistreEvent`| `controle.enregistre`  | ms-suivi         |

Exchange : `reboistn.events` (topic)

## Lancement

```bash
# Depuis le dossier ms-notifications
mvn spring-boot:run

# Ou avec Docker
docker build -t ms-notifications .
docker run -p 8086:8086 ms-notifications
```

## Tests

```bash
mvn clean test
```

## Séances suivantes

| Séance | Tâche                                                    |
|--------|----------------------------------------------------------|
| S5     | Activer Eureka (`eureka.client.enabled=true`)            |
| S7     | Implémenter les `@RabbitListener` + envoi d'e-mails      |
| S8     | Sécuriser avec JWT Keycloak (remplacer `X-User-Id`)      |
| S9     | Ajouter Resilience4j (circuit breaker, retry)            |

