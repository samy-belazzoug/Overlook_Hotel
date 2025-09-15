CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE -- CLIENT, EMPLOYEE, MANAGER, ADMIN
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

CREATE TABLE rooms (
    id SERIAL PRIMARY KEY,
    number VARCHAR(20) NOT NULL,
    type VARCHAR(50) NOT NULL, -- simple, double, suite
    price DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) DEFAULT 'available' -- available, occupied, cleaning, maintenance
);

CREATE TABLE reservations (
    id SERIAL PRIMARY KEY,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    statut VARCHAR(50) DEFAULT 'active', -- active, annulée, terminée
    client_id INT NOT NULL,
    chambre_id INT NOT NULL,
    CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES users(id),
    CONSTRAINT fk_chambre FOREIGN KEY (chambre_id) REFERENCES rooms(id)
);

CREATE TABLE feedbacks (
    id SERIAL PRIMARY KEY,
    commentaire TEXT,
    note INT CHECK (note >= 1 AND note <= 5),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    client_id INT NOT NULL,
    CONSTRAINT fk_feedback_client FOREIGN KEY (client_id) REFERENCES users(id)
);

CREATE TABLE events (
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

CREATE TABLE worker_shifts (
    id SERIAL PRIMARY KEY,
    worker_id INT NOT NULL,
    date DATE NOT NULL,
    shift VARCHAR(50), -- matin, soir, nuit
    CONSTRAINT fk_worker FOREIGN KEY (worker_id) REFERENCES users(id)
);
