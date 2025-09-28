-- FlowPay Database Initialization Script
-- This script creates the basic tables for the FlowPay application

-- Create database if not exists (this might not be needed since docker-compose creates it)
CREATE DATABASE IF NOT EXISTS flowpay;
USE flowpay;

-- Create Departement table
CREATE TABLE IF NOT EXISTS departement (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create TypeAgent table
CREATE TABLE IF NOT EXISTS type_agent (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create TypePaiement table
CREATE TABLE IF NOT EXISTS type_paiement (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Personne table (base table for Agent)
CREATE TABLE IF NOT EXISTS personne (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE,
    telephone VARCHAR(20),
    date_naissance DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Agent table
CREATE TABLE IF NOT EXISTS agent (
    id INT PRIMARY KEY AUTO_INCREMENT,
    personne_id INT NOT NULL,
    matricule VARCHAR(50) UNIQUE NOT NULL,
    departement_id INT NOT NULL,
    type_agent_id INT NOT NULL,
    salaire_base DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    date_embauche DATE NOT NULL,
    actif BOOLEAN DEFAULT TRUE,
    password_hash VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (personne_id) REFERENCES personne(id) ON DELETE CASCADE,
    FOREIGN KEY (departement_id) REFERENCES departement(id),
    FOREIGN KEY (type_agent_id) REFERENCES type_agent(id)
);

-- Create Paiement table
CREATE TABLE IF NOT EXISTS paiement (
    id INT PRIMARY KEY AUTO_INCREMENT,
    agent_id INT NOT NULL,
    type_paiement_id INT NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    description TEXT,
    date_paiement DATE NOT NULL,
    mois_paiement INT NOT NULL,
    annee_paiement INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (agent_id) REFERENCES agent(id),
    FOREIGN KEY (type_paiement_id) REFERENCES type_paiement(id)
);

-- Create Bonus table
CREATE TABLE IF NOT EXISTS bonus (
    id INT PRIMARY KEY AUTO_INCREMENT,
    agent_id INT NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    motif VARCHAR(255),
    date_attribution DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (agent_id) REFERENCES agent(id)
);

-- Create Indemnite table
CREATE TABLE IF NOT EXISTS indemnite (
    id INT PRIMARY KEY AUTO_INCREMENT,
    agent_id INT NOT NULL,
    type VARCHAR(100) NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    description TEXT,
    date_attribution DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (agent_id) REFERENCES agent(id)
);

-- Insert default data
INSERT INTO departement (nom, description) VALUES
('Ressources Humaines', 'Gestion du personnel et des ressources humaines'),
('Informatique', 'Développement et maintenance des systèmes informatiques'),
('Comptabilité', 'Gestion financière et comptable'),
('Marketing', 'Promotion et marketing des produits/services');

INSERT INTO type_agent (nom, description) VALUES
('Employé', 'Employé standard'),
('Manager', 'Responsable d\'équipe'),
('Directeur', 'Directeur de département'),
('Admin', 'Administrateur système');

INSERT INTO type_paiement (nom, description) VALUES
('Salaire', 'Salaire mensuel de base'),
('Prime', 'Prime exceptionnelle'),
('Bonus', 'Bonus de performance'),
('Indemnité', 'Indemnité diverses');

-- Create a default admin user
INSERT INTO personne (nom, prenom, email, telephone) VALUES
('Admin', 'System', 'admin@flowpay.com', '0000000000');

INSERT INTO agent (personne_id, matricule, departement_id, type_agent_id, salaire_base, date_embauche, password_hash) VALUES
(1, 'ADM001', 1, 4, 5000.00, '2024-01-01', 'admin123');

COMMIT;