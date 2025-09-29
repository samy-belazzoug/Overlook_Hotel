-- Overlook Hotel - Test Data
-- Insertion des données de test pour le développement

-- Insertion des rôles
INSERT INTO roles (name) VALUES ('CLIENT'), ('EMPLOYE'), ('MANAGER'), ('ADMIN') ON CONFLICT (name) DO NOTHING;

-- Insertion des utilisateurs de test
-- Mot de passe pour tous : "password123" (encodé avec BCrypt)
INSERT INTO users (username, email, password, telephone, points_fidelite) VALUES 
('client1', 'client1@mail.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '0123456789', 100),
('client2', 'client2@mail.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '0123456790', 50),
('employe1', 'employe1@mail.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '0123456791', 0),
('manager1', 'manager1@mail.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '0123456792', 0),
('admin1', 'admin1@mail.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '0123456793', 0)
ON CONFLICT (email) DO NOTHING;

-- Attribution des rôles aux utilisateurs
INSERT INTO user_roles (user_id, role_id) VALUES 
(1, 1), -- client1 -> CLIENT
(2, 1), -- client2 -> CLIENT
(3, 2), -- employe1 -> EMPLOYE
(4, 3), -- manager1 -> MANAGER
(5, 4)  -- admin1 -> ADMIN
ON CONFLICT (user_id, role_id) DO NOTHING;

-- Insertion des chambres de test
INSERT INTO chambres (numero, type, prix, etat) VALUES 
('101', 'simple', 80.00, 'disponible'),
('102', 'simple', 80.00, 'disponible'),
('201', 'double', 120.00, 'disponible'),
('202', 'double', 120.00, 'occupee'),
('301', 'suite', 200.00, 'disponible'),
('302', 'suite', 200.00, 'nettoyage'),
('103', 'simple', 80.00, 'disponible'),
('203', 'double', 120.00, 'disponible'),
('303', 'suite', 200.00, 'disponible'),
('104', 'simple', 80.00, 'maintenance')
ON CONFLICT (numero) DO NOTHING;

-- Insertion des réservations de test
INSERT INTO reservations (date_debut, date_fin, statut, client_id, chambre_id) VALUES 
('2025-01-15', '2025-01-20', 'active', 1, 2),
('2025-01-10', '2025-01-12', 'terminee', 2, 1),
('2025-02-01', '2025-02-05', 'active', 1, 5)
ON CONFLICT DO NOTHING;

-- Insertion des plannings employés
INSERT INTO horaires_employes (employe_id, date, shift) VALUES 
(3, '2025-01-15', 'matin'),
(3, '2025-01-16', 'soir'),
(3, '2025-01-17', 'nuit'),
(3, '2025-01-18', 'matin'),
(3, '2025-01-19', 'soir')
ON CONFLICT DO NOTHING;

-- Insertion des événements
INSERT INTO evenements (titre, description, date_debut, date_fin, capacite_max, prix, statut) VALUES 
('Soirée Jazz', 'Concert de jazz dans le hall principal', '2025-02-14', '2025-02-14', 50, 25.00, 'actif'),
('Atelier Cuisine', 'Cours de cuisine française', '2025-02-20', '2025-02-20', 20, 45.00, 'actif'),
('Dégustation Vins', 'Découverte des vins de la région', '2025-03-01', '2025-03-01', 30, 35.00, 'actif')
ON CONFLICT DO NOTHING;

-- Insertion des notifications
INSERT INTO notifications (titre, message, type, date_creation, statut) VALUES 
('Bienvenue', 'Bienvenue à l''Overlook Hotel ! Profitez de votre séjour.', 'info', CURRENT_TIMESTAMP, 'active'),
('Maintenance', 'Maintenance prévue le 25 janvier de 14h à 16h.', 'warning', CURRENT_TIMESTAMP, 'active'),
('Promotion', 'Réduction de 20% sur les suites ce week-end !', 'promo', CURRENT_TIMESTAMP, 'active')
ON CONFLICT DO NOTHING;
