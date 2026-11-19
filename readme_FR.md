# API Réseau Social Interne (R3.03)

Une API RESTful développée avec **Java 21**, **Spring Boot** et **PostgreSQL**. Ce projet implémente le backend d'un réseau social interne d'entreprise, permettant aux employés (`User`) de publier, consulter, modifier et supprimer des publications (`Post`).

## Membres de l'équipe

* **Blouin Alexis**
* **Goddefroy François**
* **Duriez Théo**

## Prérequis

* Docker Engine ou Docker Desktop
* Docker Compose (commande `docker-compose` ou `docker compose`)
* Java 21 (en cas d'exécution locale hors Docker)

## Architecture & Modèle de Données

Le projet suit une **architecture en couches (N-Tier)** standard :

* `controller` : Endpoints REST gérant les requêtes et réponses HTTP.
* `service` : Logique métier et règles de validation.
* `repository` : Couche d'accès aux données utilisant Spring Data JPA.
* `entity` : Modèles de base de données (`User` et `Post` liés par une relation `@ManyToOne`).
* `dto` : Objets de transfert de données (*Data Transfer Objects*) pour la validation des requêtes.
* `exception` : Gestion globale des erreurs (`@RestControllerAdvice`).

## Lancement du projet

### Option 1 : Docker Compose complet (Application + Base de données)

Depuis la racine du projet, exécutez :

```bash
docker compose up
```

Compose construit l'image de l'application, démarre PostgreSQL, attend que la base de données soit opérationnelle, puis lance l'application. Au premier lancement, la construction de l'image peut prendre quelques minutes.

Une fois l'application démarrée via Docker, elle est accessible sur <http://localhost:8080>. PostgreSQL est exposé sur `localhost:5433`.

Pour exécuter les conteneurs en arrière-plan, utilisez `docker compose up -d`. Pour les arrêter, exécutez `docker compose down` depuis la racine du projet.

### Option 2 : Développement local (Base de données sur Docker + Application sur IDE/Maven)

Pour démarrer uniquement PostgreSQL et Adminer dans Docker :

```bash
docker compose up -d postgres adminer
```

Lancez ensuite `BaseSpringBootApplication.java` depuis VS Code ou via la commande `./mvnw spring-boot:run`. En mode local (`application.yaml`), l'API est accessible sur <http://localhost:8084>.

### Gestion de la base de données (Adminer)

La configuration Compose utilise un volume nommé (`postgres_data`) afin de conserver les données de la base lorsque les conteneurs sont arrêtés.

Adminer est accessible sur <http://localhost:8081/?pgsql=postgres&username=postgres&db=base_db> pour gérer la base de données PostgreSQL via une interface web (Mot de passe : `postgrespassword`).

## Réinitialiser la base de données

Si la base de données est dans un état instable et que vous souhaitez repartir de zéro, exécutez :

```bash
docker compose down -v
docker compose up -d
```

L'option `-v` supprime le volume de la base de données : **toutes les données enregistrées seront définitivement effacées**. La seconde commande recrée les conteneurs en arrière-plan.

## Documentation de l'API

URL de base (en local) : `http://localhost:8084`

### 1. Utilisateurs (`/api/users`)

| Méthode | Endpoint | Description | Statut de succès |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/users` | Créer un nouveau profil employé | `201 Created` |
| `GET` | `/api/users` | Récupérer tous les utilisateurs | `200 OK` |
| `GET` | `/api/users/{id}` | Récupérer un utilisateur par son ID | `200 OK` |
| `PUT` | `/api/users/{id}` | Mettre à jour un utilisateur existant | `200 OK` |
| `DELETE` | `/api/users/{id}` | Supprimer un utilisateur | `204 No Content` |

**Exemple de requête (`POST /api/users`) :**

```json
{
  "username": "theo.duriez",
  "email": "theo.duriez@company.com",
  "department": "IT"
}
```

### 2. Publications (`/api/posts`)

| Méthode | Endpoint | Description | Statut de succès |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/posts` | Publier un nouveau message (nécessite un `userId` existant) | `201 Created` |
| `GET` | `/api/posts` | Récupérer toutes les publications | `200 OK` |
| `GET` | `/api/posts/{id}` | Récupérer une publication par son ID | `200 OK` |
| `PUT` | `/api/posts/{id}` | Modifier le contenu d'une publication | `200 OK` |
| `DELETE` | `/api/posts/{id}` | Supprimer une publication | `204 No Content` |

**Exemple de requête (`POST /api/posts`) :**

```json
{
  "content": "Bonjour l'équipe ! Bienvenue sur notre nouveau réseau social interne.",
  "userId": 1
}
```