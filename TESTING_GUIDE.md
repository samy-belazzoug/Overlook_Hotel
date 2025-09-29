# 🧪 Guide de Test - Overlook Hotel API

## Problème Identifié
L'erreur 400 lors de la connexion indique un problème avec l'authentification. Voici comment le résoudre :

## 🔧 Solutions à Tester

### 1. Vérifier que l'application est démarrée
```bash
# Vérifier que l'API répond
curl http://localhost:8080/api/rooms
```

### 2. Tester la connexion avec des utilisateurs de test

#### Option A: Utiliser les données de test existantes
Les utilisateurs suivants ont été créés dans `data.sql` :
- **Client** : `client1@mail.com` / `password123`
- **Admin** : `admin1@mail.com` / `password123`
- **Employé** : `employe1@mail.com` / `password123`

#### Option B: Créer un nouvel utilisateur via l'API
```bash
# Créer un utilisateur de test
curl -X POST http://localhost:8080/api/test/create-user

# Vérifier les utilisateurs
curl http://localhost:8080/api/test/users
```

### 3. Tester la connexion
```bash
# Test avec PowerShell
$body = @{email="test@mail.com"; password="password123"} | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Body $body -ContentType "application/json"

# Test avec curl
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@mail.com","password":"password123"}'
```

### 4. Tester avec le frontend
1. Ouvrez `test-api.html` dans votre navigateur
2. Cliquez sur "Test Login" pour tester la connexion
3. Cliquez sur "Test Rooms" pour tester l'API des chambres

## 🐛 Dépannage

### Si l'erreur 400 persiste :

1. **Vérifier les logs de l'application** :
   - Regardez la console où l'application Spring Boot s'exécute
   - Cherchez les erreurs d'authentification

2. **Vérifier la base de données** :
   ```sql
   -- Se connecter à PostgreSQL
   psql -U hotel_user -d overlook_hotel
   
   -- Vérifier les utilisateurs
   SELECT * FROM users;
   SELECT * FROM roles;
   SELECT * FROM user_roles;
   ```

3. **Vérifier la configuration Spring Security** :
   - L'`AuthController` a été mis à jour pour gérer les deux types d'utilisateurs
   - Le `CustomUserDetailsService` a été mis à jour
   - Le `CustomUserDetails` a été mis à jour

### Si l'application ne démarre pas :

1. **Vérifier les dépendances** :
   ```bash
   ./mvnw clean compile
   ```

2. **Vérifier la configuration de la base de données** :
   - Vérifiez que PostgreSQL est démarré
   - Vérifiez les paramètres dans `application.properties`

3. **Vérifier les logs de démarrage** :
   - Regardez les messages d'erreur dans la console

## ✅ Tests de Validation

### Test 1: API des chambres (doit fonctionner)
```bash
curl http://localhost:8080/api/rooms
```
**Résultat attendu** : Liste des chambres en JSON

### Test 2: Création d'utilisateur
```bash
curl -X POST http://localhost:8080/api/test/create-user
```
**Résultat attendu** : Message de succès

### Test 3: Connexion
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@mail.com","password":"password123"}'
```
**Résultat attendu** : Token JWT et informations utilisateur

### Test 4: Frontend
1. Ouvrez `frontend/index.html`
2. Essayez de vous connecter avec `test@mail.com` / `password123`
3. Vérifiez que la recherche de chambres fonctionne

## 🚀 Prochaines Étapes

Une fois l'authentification résolue :

1. **Tester toutes les fonctionnalités** :
   - Recherche de chambres
   - Création de réservations
   - Interface admin
   - Plannings employés

2. **Ajouter des données de test** :
   - Plus d'utilisateurs
   - Plus de chambres
   - Plus de réservations

3. **Tester l'intégration complète** :
   - Frontend ↔ Backend
   - Tous les rôles utilisateurs
   - Toutes les fonctionnalités

## 📞 Support

Si le problème persiste :
1. Vérifiez les logs de l'application
2. Vérifiez la configuration de la base de données
3. Testez avec les commandes curl ci-dessus
4. Utilisez `test-api.html` pour tester l'API

---

**Note** : L'erreur 400 est généralement due à un problème de validation des données ou de configuration Spring Security. Les modifications apportées devraient résoudre le problème.
