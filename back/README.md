# MDD (Back end)

Ce projet correspond au **back end Java/Spring Boot** sécurisé de l’application full stack **MDD (Monde de Dév)**. Il permet aux utilisateurs de consulter des articles, de commenter, de s’abonner à des thèmes, de gérer leur profil et de publier du contenu, le tout avec une authentification JWT garantissant un accès sécurisé aux sessions.

---

## Technologies utilisées

- **Java 21**
- **Maven**
- **Spring Boot**
- **Spring Security + JWT**
- **Spring Data JPA (Hibernate)**
- **MySQL**
- **Lombok**
- **Swagger/OpenAPI pour la documentation**
- **Projet développé sous IntelliJ IDEA 2025.2**

---

## Architecture du projet
```
├── src/main/java/com/openclassrooms/mddapi
│   ├── MddApiApplication.java            # Point d’entrée Spring Boot de l’application
│   ├── config
│   │   └── OpenApiConfig.java            # Configuration Swagger / OpenAPI pour documenter l’API
│   ├── controller
│   │   ├── ArticleController.java        # Endpoints liés aux articles et au fil d’actualité
│   │   ├── AuthController.java           # Endpoints d’authentification (login, register, profil)
│   │   └── SubjectController.java        # Endpoints de consultation et d’abonnement aux thèmes 
│   ├── dto
│   │   ├── ArticleDTO.java               # Représentation d’un article renvoyée au frontend
│   │   ├── ArticleListResponse.java      # Wrapper pour la réponse de liste d’articles
│   │   ├── AuthRequest.java              # Données envoyées lors de la connexion
│   │   ├── AuthResponse.java             # Réponse contenant le JWT après authentification
│   │   ├── AuthUserProfileResponse.java  # Données du profil utilisateur connecté
│   │   ├── AuthUserUpdateRequest.java    # Données envoyées pour modifier le profil utilisateur
│   │   ├── AuthUserUpdateResponse.java   # Réponse après mise à jour du profil (nouveau JWT)
│   │   ├── CommentDTO.java               # Représentation d’un commentaire
│   │   ├── CreateArticleRequest.java     # Données nécessaires à la création d’un article
│   │   ├── CreateCommentRequest.java     # Données nécessaires à l’ajout d’un commentaire
│   │   ├── RegisterRequest.java          # Données envoyées lors de l’inscription
│   │   ├── RegisterResponse.java         # Réponse retournant le JWT après inscription
│   │   ├── SubjectDTO.java               # Représentation d’un thème avec statut d’abonnement
│   │   ├── SubscriptionDTO.java          # Données d’un abonnement utilisateur ↔ thème
│   │   └── UserDTO.java                  # Représentation simplifiée d’un utilisateur
│   ├── entity
│   │   ├── Article.java                  # Entité JPA représentant un article	
│   │   ├── Comment.java                  # Entité JPA représentant un commentaire
│   │   ├── Subject.java                  # Entité JPA représentant un thème
│   │   ├── Subscription.java             # Entité JPA représentant un abonnement
│   │   └── User.java                     # Entité JPA représentant un utilisateur
│   ├── exception
│   │   ├── ApiError.java                     # Modèle standardisé des erreurs API
│   │   ├── ConflictException.java            # Exception levée en cas de conflit métier (409)
│   │   ├── ResourceNotFoundException.java    # Exception levée quand une ressource est introuvable (404)
│   │   └── GlobalExceptionHandler.java       # Gestion centralisée des exceptions REST
│   ├── repository
│   │   ├── ArticleRepository.java            # Accès base de données pour les articles
│   │   ├── CommentRepository.java            # Accès base de données pour les commentaires
│   │   ├── SubjectRepository.java            # Accès base de données pour les thèmes
│   │   ├── SubscriptionRepository.java       # Accès base de données pour les abonnements
│   │   └── UserRepository.java               # Accès base de données pour les utilisateurs
│   ├── security
│   │   ├── CustomUserDetails.java            # Implémentation Spring Security de l’utilisateur
│   │   ├── CustomUserDetailsService.java     # Chargement des utilisateurs pour l’authentification
│   │   ├── JwtFilter.java                    # Filtre interceptant et validant les JWT
│   │   ├── JwtUtils.java                     # Outils de génération et validation des JWT
│   │   ├── SecurityConfig.java               # Configuration globale de Spring Security
│   │   └── SecurityUtils.java                # Utilitaires pour accéder à l’utilisateur connecté
│   └── service
│       ├── ArticleService.java               # Logique métier liée aux articles et au feed 
│       ├── AuthService.java                  # Logique métier d’authentification et de profil
│       ├── CommentService.java               # Logique métier des commentaires
│       └── SubjectService.java               # Logique métier des thèmes et abonnements
└── src/main/resources/
    └── application.properties                # Configuration application (BDD, JWT)

```
## Modèle de données (Base de données)

Le schéma ci-dessous représente les relations entre les principales entités de la base de données de l’application **MDD**.
```
+----------------+       +----------------+
|     users      |       |    subjects    |
+----------------+       +----------------+
| user_id (PK)   |       | subject_id (PK)|
| email          |       | name           |
| username       |       | description    |
| password       |       +----------------+
+----------------+               ^
       ^                         |
       |                         |
       |                         |
       |                         |
+----------------+    +--------------------+
|   articles     |    |    subscriptions   |
+----------------+    +--------------------+
| article_id (PK)|    | subscription_id(PK)|
| subject_id (FK)|----| user_id (FK)       |
| user_id (FK)   |----| subject_id (FK)    |
| title          |    +--------------------+
| content        |
| created_at     |
+----------------+
       ^
       |
       |
+----------------+
|    comments    |      PK = clé primaire
+----------------+      FK = clé étrangère
| comment_id (PK)|
| article_id (FK)|
| user_id (FK)   |
| content        |
| created_at     |
+----------------+
```
Ce modèle permet :
- une relation **many-to-many** entre utilisateurs et thèmes via `subscriptions`
- un rattachement clair des articles à un auteur et à un thème
- une gestion des commentaires liée à la fois à l’article et à l’utilisateur

## Configuration
Dans src/main/resources/application.properties :

MySQL (doit être installé)
La base de données **mddbd** doit exister. Le script SQL permettant de créer la base de données et de remplir la table de thèmes (Subjects) est disponible dans : `ressources/sql/script.sql`

- file **src/main/resources/application.properties** :
  spring.datasource.url=jdbc:mysql://localhost:3306/mddbd?allowPublicKeyRetrieval=true        
  spring.datasource.username={DB_USER}  
  spring.datasource.password={DB_PASSWORD}<br><br>

## Endpoints

| Méthode | URL                            | Description                                                                    | Auth |
| ------- | ------------------------------ | ------------------------------------------------------------------------------ | ---- |
| POST    | /api/auth/register             | Crée un nouvel utilisateur                                                     | Non  |
| POST    | /api/auth/login                | Connexion par email ou username (retourne JWT)                                 | Non  |
| GET     | /api/auth/me                   | Récupère le profil de l’utilisateur connecté                                   | Oui  |
| PUT     | /api/auth/me                   | Mise à jour des infos de l’utilisateur connecté (retourne JWT)                 | Oui  |
| GET     | /api/articles                  | Récupère le feed des articles abonnés (triable par `order`)                    | Oui  |
| GET     | /api/articles/{id}             | Récupère un article par ID                                                     | Oui  |
| POST    | /api/articles                  | Crée un nouvel article                                                         | Oui  |
| GET     | /api/articles/{id}/comments    | Récupère les commentaires d’un article                                         | Oui  |
| POST    | /api/articles/{id}/comments    | Ajoute un commentaire à un article                                             | Oui  |
| GET     | /api/subjects                  | Récupère tous les sujets, filtrables par abonnements (`subscribed=true/false`) | Oui  |
| POST    | /api/subjects/{id}/subscribe   | S’abonner à un sujet                                                           | Oui  |
| POST    | /api/subjects/{id}/unsubscribe | Se désabonner d’un sujet                                                       | Oui  |



## Swagger UI
- Accessible sur la route : /swagger-ui/index.html
- Permet de tester tous les endpoints et voir les modèles (DTO) utilisés.

## JavaDoc
- mvn javadoc:javadoc -Dshow=private
- à consulter dans /target/site/apidocs/index.html

## Commandes Maven
- mvn clean install
- mvn spring-boot:run

## Sécurité et JWT
- JWT utilisé pour authentification stateless.
- Header requis :
  Authorization: Bearer <JWT_TOKEN>
- Routes auth/login et auth/register sont publiques.
- Toutes les autres routes nécessitent JWT valide.

## Author

Julio Daniel GIL CANO
