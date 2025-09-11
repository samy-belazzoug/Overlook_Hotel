

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE -- CLIENT, EMPLOYE, MANAGER, ADMIN
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telephone VARCHAR(20),
    date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    points_fidelite INT DEFAULT 0
);

CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE chambres (
    id SERIAL PRIMARY KEY,
    numero VARCHAR(20) NOT NULL,
    type VARCHAR(50) NOT NULL, -- simple, double, suite
    prix DECIMAL(10,2) NOT NULL,
    etat VARCHAR(50) DEFAULT 'disponible' -- disponible, occupée, nettoyage
);

CREATE TABLE reservations (
    id SERIAL PRIMARY KEY,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    statut VARCHAR(50) DEFAULT 'active', -- active, annulée, terminée
    client_id INT NOT NULL,
    chambre_id INT NOT NULL,
    CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES users(id),
    CONSTRAINT fk_chambre FOREIGN KEY (chambre_id) REFERENCES chambres(id)
);

CREATE TABLE feedbacks (
    id SERIAL PRIMARY KEY,
    commentaire TEXT,
    note INT CHECK (note >= 1 AND note <= 5),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    client_id INT NOT NULL,
    CONSTRAINT fk_feedback_client FOREIGN KEY (client_id) REFERENCES users(id)
);

CREATE TABLE evenements (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(100) NOT NULL,
    description TEXT,
    date TIMESTAMP NOT NULL,
    gestionnaire_id INT NOT NULL,
    CONSTRAINT fk_event_manager FOREIGN KEY (gestionnaire_id) REFERENCES users(id)
);

CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    contenu TEXT NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    destinataire_id INT NOT NULL,
    type VARCHAR(50),
    CONSTRAINT fk_notification_user FOREIGN KEY (destinataire_id) REFERENCES users(id)
);

CREATE TABLE horaires_employes (
    id SERIAL PRIMARY KEY,
    employe_id INT NOT NULL,
    date DATE NOT NULL,
    shift VARCHAR(50), -- matin, soir, nuit
    CONSTRAINT fk_employe FOREIGN KEY (employe_id) REFERENCES users(id)
);



INSERT INTO roles (name) VALUES ('CLIENT'), ('EMPLOYE'), ('MANAGER'), ('ADMIN');

INSERT INTO users (username, email, password, telephone, points_fidelite) 
VALUES 
('client1', 'client1@mail.com', 'password123', '0600000001', 10),
('manager1', 'manager1@mail.com', 'password123', '0600000002', 0),
('employe1', 'employe1@mail.com', 'password123', '0600000003', 0);

INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- client1 est CLIENT
(2, 3), -- manager1 est MANAGER
(3, 2); -- employe1 est EMPLOYE

INSERT INTO chambres (numero, type, prix, etat) VALUES
('101', 'simple', 50.00, 'disponible'),
('102', 'double', 80.00, 'disponible'),
('201', 'suite', 150.00, 'en nettoyage');

INSERT INTO reservations (date_debut, date_fin, statut, client_id, chambre_id) VALUES
('2025-09-10', '2025-09-15', 'active', 1, 1);

INSERT INTO feedbacks (commentaire, note, client_id) VALUES
('Super séjour !', 5, 1);

INSERT INTO evenements (titre, description, date, gestionnaire_id) VALUES
('Mariage', 'Réservation salle pour mariage', '2025-09-20', 2);

INSERT INTO notifications (contenu, destinataire_id, type) VALUES
('Votre réservation est confirmée', 1, 'client');

INSERT INTO horaires_employes (employe_id, date, shift) VALUES
(3, '2025-09-12', 'matin');
