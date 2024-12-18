# Projet Fournisseur d'Identité

## **Description**

Ce projet implémente un fournisseur d'identité sous forme d'API REST, développé avec **Spring Boot** et conteneurisé avec **Docker**. Il permet :

- L'inscription des utilisateurs avec validation par email.
- L'authentification multifacteur avec PIN envoyé par email.
- La gestion des comptes utilisateurs (modification des informations sauf email).
- Une gestion sécurisée des mots de passe et des sessions.
- Une documentation API générée automatiquement via **Swagger**.

---

## **Fonctionnalités**

### 1. Inscription

- Création d'un compte utilisateur.
- Validation de l'email via un lien.

### 2. Authentification multifacteur

- Connexion avec email et mot de passe.
- Vérification d'un PIN temporaire (valide 90 secondes) envoyé par email.

### 3. Gestion des comptes

- Modification des informations personnelles de l'utilisateur (sauf l'email).

### 4. Sécurité

- Hashage sécurisé des mots de passe avec **BCrypt**.
- Limitation des tentatives de connexion (paramétrable, par défaut 3).
- Réinitialisation des tentatives via email.
- Réinitialisation automatique après une connexion réussie.

### 5. Sessions

- Gestion des sessions avec durée de vie paramétrable.
- Utilisation de **JSON Web Tokens (JWT)** pour l'authentification.

### 6. Documentation API

- Documentation générée automatiquement via **Swagger/OpenAPI**.

---

## **Prérequis**

- **Java 17+**
- **Maven 3.8+**
- **Docker** et **Docker Compose**

---

## **Installation et Lancement**

### 1. Cloner le projet

```bash
git clone <https://github.com/rarianaAina/cloudProject.git>
cd <cloudProject>
```

### 2. Construire et lancer avec Docker

```bash
docker-compose up --build
```

### 3. Accéder à l'application

- API Swagger : `http://localhost:8080/swagger-ui.html`

---

## **Structure du Projet**

### **Arborescence principale**

```plaintext
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.identityprovider
│   │   │       ├── controller  # Gestion des endpoints REST
│   │   │       ├── model       # Entités et DTO
│   │   │       ├── repository  # Accès à la base de données
│   │   │       ├── service     # Logique métier
│   │   │       └── config      # Configuration (sécurité, JWT, etc.)
│   │   └── resources
│   │       ├── application.yml # Configuration de l'application
│   │       └── templates       # Templates email
├── docker-compose.yml           # Configuration Docker Compose
├── README.md                    # Documentation du projet
└── postman_collection.json      # Collection Postman pour tester l'API
```

---

## **Endpoints Principaux**

### **Authentification**

- `POST /api/auth/register` : Inscription d'un utilisateur.
- `POST /api/auth/login` : Connexion avec vérification du PIN.

### **Utilisateur**

- `GET /api/users/me` : Récupérer les informations de l'utilisateur connecté.
- `PUT /api/users/me` : Modifier les informations personnelles.

### **Administration**

- `POST /api/auth/reset-attempts` : Réinitialisation des tentatives de connexion.

---

## **Tests API**

- Utiliser le fichier **Postman Collection** fourni : `postman_collection.json`.
- Importer la collection dans Postman pour tester les différents endpoints.

---

## **Contributeurs**

### **Nom et Prénom :**

- **Rariana Miadana** : **ETU00001**
- **Harena Andraina Ramarosandratana** : **ETU00002**
- **Larry Joann** : **ETU00003**
- **Mampionona Rinasoa Ramarosandratana** : **ETU001015**

---

## **Documentation Technique**

### 1. Modèle Conceptuel des Données (MCD)

Inclure un diagramme du modèle conceptuel des données pour présenter les relations entre les entités principales.

### 2. Scénarios d'utilisation

- Inscription et validation par email.
- Authentification multifacteur avec PIN.
- Gestion des comptes utilisateurs.

### 3. Choix Techniques

- **Spring Boot** : Framework rapide et robuste pour les API REST.
- **BCrypt** : Sécurité des mots de passe.
- **JWT** : Gestion des sessions sécurisée.
- **Docker** : Déploiement simple et portable.

---
