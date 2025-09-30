# Test de connexion pour Overlook Hotel
Write-Host "=== Test de connexion Overlook Hotel ===" -ForegroundColor Green

# Test 1: Vérifier que l'API répond
Write-Host "`n1. Test de l'API des chambres..." -ForegroundColor Yellow
try {
    $rooms = Invoke-RestMethod -Uri "http://localhost:8080/api/rooms" -Method GET
    Write-Host "✅ API des chambres fonctionne" -ForegroundColor Green
    Write-Host "Nombre de chambres: $($rooms.Count)"
} catch {
    Write-Host "❌ Erreur API des chambres: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Créer un utilisateur de test
Write-Host "`n2. Création d'un utilisateur de test..." -ForegroundColor Yellow
try {
    $createResult = Invoke-RestMethod -Uri "http://localhost:8080/api/test/create-user" -Method POST
    Write-Host "✅ Utilisateur créé: $createResult" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur création utilisateur: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Lister les utilisateurs
Write-Host "`n3. Liste des utilisateurs..." -ForegroundColor Yellow
try {
    $users = Invoke-RestMethod -Uri "http://localhost:8080/api/test/users" -Method GET
    Write-Host "✅ Utilisateurs trouvés: $($users.Count)" -ForegroundColor Green
    foreach ($user in $users) {
        Write-Host "  - $($user.email) (ID: $($user.id))"
    }
} catch {
    Write-Host "❌ Erreur liste utilisateurs: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Test de connexion
Write-Host "`n4. Test de connexion..." -ForegroundColor Yellow
try {
    $loginBody = @{
        email = "coucou@mail.com"
        password = "password123"
    } | ConvertTo-Json

    $loginResult = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
    Write-Host "✅ Connexion réussie!" -ForegroundColor Green
    Write-Host "Token: $($loginResult.token)"
    Write-Host "Rôle: $($loginResult.role)"
} catch {
    Write-Host "❌ Erreur de connexion: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Détails: $($_.Exception.Response.StatusCode)"
}

Write-Host "`n=== Fin des tests ===" -ForegroundColor Green
