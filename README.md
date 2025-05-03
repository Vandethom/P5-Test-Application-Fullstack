# Yoga App - Full Stack Application

![Yoga App Banner](https://img.shields.io/badge/Yoga%20App-Full%20Stack-green)
![Angular](https://img.shields.io/badge/Frontend-Angular-red)
![Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot-blue)
![Coverage](https://img.shields.io/badge/Coverage-90%25-success)

Application complète pour la gestion de sessions de yoga avec une interface utilisateur Angular et une API REST Spring Boot.

## Table des matières

- [Prérequis](#prérequis)
- [Installation du projet](#installation-du-projet)
  - [Backend (Java/Spring Boot)](#backend-javaspring-boot)
  - [Frontend (Angular)](#frontend-angular)
- [Lancement de l'application](#lancement-de-lapplication)
  - [Backend](#backend)
  - [Frontend](#frontend)
- [Exécution des tests](#exécution-des-tests)
  - [Tests Backend](#tests-backend)
  - [Tests Frontend](#tests-frontend)
    - [Tests unitaires](#tests-unitaires)
    - [Tests e2e](#tests-e2e)
- [Rapport de couverture](#rapport-de-couverture)
  - [Couverture Backend](#couverture-backend)
  - [Couverture Frontend](#couverture-frontend)

## Prérequis

- Java 8+
- Maven 3.6+
- Node.js 14+
- npm 6+
- MySQL 8+

## Installation du projet

Clonez ce dépôt sur votre machine locale :

```bash
git clone https://github.com/your-username/yoga-app.git
cd yoga-app
```

### Backend (Java/Spring Boot)

1. Assurez-vous que MySQL est installé et en cours d'exécution
2. Créez une base de données pour l'application :

```bash
mysql -u root -p
CREATE DATABASE yoga;
exit;
```

3. Importez le script SQL de démarrage :

```bash
mysql -u root -p yoga < ressources/sql/script.sql
```

4. Configurez les informations de connexion à la base de données dans `back/src/main/resources/application.properties` si nécessaire

5. Installez les dépendances et compilez le projet :

```bash
cd back
mvn clean install
```

### Frontend (Angular)

1. Installez les dépendances :

```bash
cd front
npm install
```

## Lancement de l'application

### Backend

1. Démarrez le serveur Spring Boot :

```bash
cd back
mvn spring-boot:run
```

Le serveur démarre sur http://localhost:8080

### Frontend

1. Lancez l'application Angular :

```bash
cd front
npm start
```

L'application est accessible à l'adresse http://localhost:4200

## Exécution des tests

### Tests Backend

Pour lancer les tests unitaires et d'intégration du backend :

```bash
cd back
mvn test
```

### Tests Frontend

#### Tests unitaires

Les tests unitaires sont exécutés avec Jest :

```bash
cd front
npm test
```

Pour lancer les tests en mode watch :

```bash
npm run test:watch
```

#### Tests e2e

Les tests end-to-end sont exécutés avec Cypress :

```bash
cd front
npm run e2e
```

Pour lancer les tests e2e avec l'interface graphique de Cypress :

```bash
npm run e2e:open
```

## Rapport de couverture

### Couverture Backend

Pour générer le rapport de couverture du backend :

```bash
cd back
mvn test
```

Le rapport JaCoCo est disponible à l'emplacement : `back/target/site/jacoco/index.html`

### Couverture Frontend

Pour générer les rapports de couverture frontend :

```bash
cd front
npm run test:coverage    # Couverture des tests unitaires
npm run e2e:coverage     # Couverture des tests e2e
```

Les rapports sont disponibles aux emplacements suivants :
- Tests unitaires : `front/coverage/lcov-report/index.html`
- Tests e2e : `front/coverage/lcov-report/index.html`

---

## Structure du projet

- `back/` : Code source du backend Spring Boot
- `front/` : Code source du frontend Angular
- `ressources/` : Ressources additionnelles (scripts SQL, collections Postman)

## Licence

Ce projet est sous licence MIT. Voir le fichier LICENSE pour plus de détails.