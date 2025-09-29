# 🏨 Overlook Hotel - Hotel Management System

Un système de gestion d'hôtel moderne et complet avec backend Spring Boot et frontend responsive.

## 🚀 Fonctionnalités

### 👥 Clients
- 🔍 Recherche de chambres par dates, prix et type
- 📅 Réservation de chambres disponibles
- 📋 Consultation de ses réservations
- ❌ Annulation de réservations

### 👨‍💼 Administrateurs
- 🏠 Gestion complète des chambres (CRUD)
- 👤 Gestion des utilisateurs (clients & employés)
- 📊 Gestion des réservations
- 📅 Gestion des plannings employés
- 📈 Tableau de bord avec statistiques

### 👷 Employés
- 📅 Consultation de leurs plannings de travail
- 📊 Vue d'ensemble de leurs shifts

## 🛠️ Technologies

### Backend
- **Spring Boot 3.5.5** - Framework principal
- **Spring Data JPA** - Persistance des données
- **Spring Security** - Authentification et autorisation
- **JWT** - Tokens d'authentification
- **PostgreSQL** - Base de données
- **Maven** - Gestion des dépendances

### Frontend
- **HTML5** - Structure
- **CSS3** - Styles (framework personnalisé inspiré de Tailwind)
- **JavaScript ES6+** - Logique côté client
- **Responsive Design** - Compatible mobile/desktop

## 📋 Prérequis

- Java 17+
- Maven 3.6+
- PostgreSQL 12+
- Navigateur web moderne

## 🚀 Installation et Lancement

### 1. Cloner le projet
```bash
git clone <repository-url>
cd overlook_hotel/demo
```

### 2. Configuration de la base de données
```sql
-- Créer la base de données
CREATE DATABASE overlook_hotel;

-- Créer un utilisateur (optionnel)
CREATE USER hotel_user WITH PASSWORD 'hotel_password';
GRANT ALL PRIVILEGES ON DATABASE overlook_hotel TO hotel_user;
```

### 3. Configuration de l'application
Modifiez `src/main/resources/application.properties` :
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/overlook_hotel
spring.datasource.username=hotel_user
spring.datasource.password=hotel_password
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 4. Lancement du backend
```bash
# Compiler et lancer l'application
./mvnw spring-boot:run

# Ou avec Maven
mvn spring-boot:run
```

L'API sera disponible sur : `http://localhost:8080`

### 5. Lancement du frontend
```bash
# Ouvrir le fichier index.html dans un navigateur
# Ou utiliser un serveur local simple
cd frontend
python -m http.server 8000
# Puis ouvrir http://localhost:8000
```

## 📚 API Endpoints

### Authentification
- `POST /api/auth/login` - Connexion
- `POST /api/auth/register` - Inscription

### Chambres (Public)
- `GET /api/rooms` - Liste des chambres
- `GET /api/rooms/search` - Recherche avec filtres
- `GET /api/rooms/{id}` - Détails d'une chambre

### Réservations (Client)
- `POST /api/reservations` - Créer une réservation
- `GET /api/reservations/my-reservations` - Mes réservations
- `PUT /api/reservations/{id}/cancel` - Annuler une réservation

### Administration
- `GET /api/admin/rooms` - Gestion des chambres
- `GET /api/admin/users` - Gestion des utilisateurs
- `GET /api/admin/reservations` - Gestion des réservations
- `GET /api/admin/shifts` - Gestion des plannings

### Employés
- `GET /api/employee/shifts` - Mes plannings
- `GET /api/employee/shifts/date` - Plannings par date

## 🎨 Interface Utilisateur

### Pages Principales
- **Accueil** (`/index.html`) - Recherche et réservation
- **Mes Réservations** (`/my-bookings.html`) - Gestion des réservations
- **Admin Dashboard** (`/admin/dashboard.html`) - Tableau de bord admin
- **Gestion Chambres** (`/admin/rooms.html`) - CRUD chambres
- **Plannings Employés** (`/employee/shifts.html`) - Consultation des shifts

### Design System
- **Couleurs** : Palette moderne avec bleu primaire et vert secondaire
- **Typographie** : Inter font pour une lisibilité optimale
- **Composants** : Cards, boutons, formulaires cohérents
- **Animations** : Transitions fluides et micro-interactions
- **Responsive** : Mobile-first design

## 🧪 Tests

```bash
# Lancer tous les tests
./mvnw test

# Tests avec rapport de couverture
./mvnw test jacoco:report
```

## 📊 Base de Données

### Tables Principales
- `users` - Utilisateurs (clients, employés, admins)
- `roles` - Rôles utilisateurs
- `chambres` - Chambres de l'hôtel
- `reservations` - Réservations
- `horaires_employes` - Plannings des employés
- `evenements` - Événements de l'hôtel
- `notifications` - Notifications système

## 🔐 Sécurité

- **JWT Authentication** - Tokens sécurisés
- **Role-based Access Control** - Contrôle d'accès par rôles
- **Password Encryption** - Mots de passe chiffrés (BCrypt)
- **CORS Configuration** - Configuration CORS pour le frontend
- **Input Validation** - Validation des données d'entrée

## 🚀 Déploiement

### Docker (Optionnel)
```bash
# Build de l'image
docker build -t overlook-hotel .

# Lancement du conteneur
docker run -p 8080:8080 overlook-hotel
```

### Variables d'environnement
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/overlook_hotel
export SPRING_DATASOURCE_USERNAME=hotel_user
export SPRING_DATASOURCE_PASSWORD=hotel_password
export JWT_SECRET=your-secret-key
```

## 📝 Utilisation

### 1. Connexion
- **Client** : Utilisez les boutons de connexion sur la page d'accueil
- **Admin** : Connectez-vous avec un compte administrateur
- **Employé** : Connectez-vous avec un compte employé

### 2. Réservation (Client)
1. Recherchez des chambres avec les filtres
2. Sélectionnez une chambre disponible
3. Confirmez la réservation
4. Consultez vos réservations dans "Mes Réservations"

### 3. Administration
1. Accédez au tableau de bord admin
2. Gérez les chambres, utilisateurs, réservations
3. Consultez les statistiques de l'hôtel

### 4. Plannings Employés
1. Connectez-vous en tant qu'employé
2. Consultez vos plannings de travail
3. Filtrez par date ou type de shift

## 🐛 Dépannage

### Problèmes courants
1. **Erreur de connexion DB** : Vérifiez les paramètres PostgreSQL
2. **CORS errors** : Vérifiez la configuration CORS
3. **JWT errors** : Vérifiez la clé secrète JWT
4. **404 errors** : Vérifiez les URLs des endpoints

### Logs
```bash
# Voir les logs de l'application
tail -f logs/application.log

# Logs Maven
./mvnw spring-boot:run -X
```

## 🤝 Contribution

1. Fork le projet
2. Créez une branche feature (`git checkout -b feature/AmazingFeature`)
3. Commit vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## 👥 Équipe

- **Backend** : Spring Boot, JPA, Security
- **Frontend** : HTML5, CSS3, JavaScript
- **Database** : PostgreSQL
- **DevOps** : Maven, Docker

## 📞 Support

Pour toute question ou problème :
- 📧 Email : support@overlook-hotel.com
- 📱 Téléphone : +33 1 23 45 67 89
- 🌐 Site web : https://overlook-hotel.com

---

**Overlook Hotel** - Votre séjour parfait vous attend ! 🏨✨