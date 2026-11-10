# Exemples Postman — ms-inscriptions

URL de base : `http://localhost:8080`

Les corps de requête utilisent `Content-Type: application/json`. Les identifiants de bénévole et d'événement sont des identifiants externes ; ils ne sont pas vérifiés par ce microservice.

## Créer une inscription

- **Méthode :** `POST`
- **URL :** `{{baseUrl}}/api/inscriptions`
- **Body (JSON) :**

```json
{
  "identifiantBenevole": 42,
  "identifiantEvenement": 15
}
```

- **Réponse attendue :** `201 Created`

```json
{
  "id": 1,
  "dateInscription": "2026-11-10T10:30:00",
  "statut": "EN_ATTENTE",
  "identifiantBenevole": 42,
  "identifiantEvenement": 15,
  "presence": false
}
```

## Consulter toutes les inscriptions

- **Méthode :** `GET`
- **URL :** `{{baseUrl}}/api/inscriptions`
- **Body :** aucun
- **Réponse attendue :** `200 OK`, tableau d'objets `InscriptionResponse`.

## Consulter une inscription par son identifiant

- **Méthode :** `GET`
- **URL :** `{{baseUrl}}/api/inscriptions/1`
- **Body :** aucun
- **Réponse attendue :** `200 OK`, un objet `InscriptionResponse` (ou `404 Not Found` si l'identifiant n'existe pas).

## Consulter les inscriptions d'un bénévole

- **Méthode :** `GET`
- **URL :** `{{baseUrl}}/api/inscriptions/benevole/42`
- **Body :** aucun
- **Réponse attendue :** `200 OK`, tableau d'inscriptions (vide si aucune inscription ne correspond).

## Consulter les inscriptions d'un événement

- **Méthode :** `GET`
- **URL :** `{{baseUrl}}/api/inscriptions/evenement/15`
- **Body :** aucun
- **Réponse attendue :** `200 OK`, tableau d'inscriptions (vide si aucune inscription ne correspond).

## Modifier une inscription

- **Méthode :** `PUT`
- **URL :** `{{baseUrl}}/api/inscriptions/1`
- **Body (JSON) :**

```json
{
  "identifiantBenevole": 42,
  "identifiantEvenement": 16
}
```

- **Réponse attendue :** `200 OK`, inscription modifiée (ou `404 Not Found`, `409 Conflict` selon le cas).

## Confirmer une inscription

- **Méthode :** `PATCH`
- **URL :** `{{baseUrl}}/api/inscriptions/1/confirmer`
- **Body :** aucun
- **Réponse attendue :** `200 OK`, statut `CONFIRMEE` (ou `409 Conflict` si la transition est interdite).

## Annuler une inscription

- **Méthode :** `PATCH`
- **URL :** `{{baseUrl}}/api/inscriptions/1/annuler`
- **Body :** aucun
- **Réponse attendue :** `200 OK`, statut `ANNULEE` (ou `409 Conflict` si la transition est interdite).

## Enregistrer la présence

- **Méthode :** `PATCH`
- **URL :** `{{baseUrl}}/api/inscriptions/1/presence`
- **Body :** aucun
- **Réponse attendue :** `200 OK`, champ `presence` à `true` si l'inscription est `CONFIRMEE` (sinon `409 Conflict`).

## Format des erreurs

Les erreurs sont renvoyées en JSON avec `timestamp`, `status` et `message`. Une erreur de validation renvoie `400 Bad Request`, un identifiant inconnu `404 Not Found` et un doublon ou une règle métier invalide `409 Conflict`.
