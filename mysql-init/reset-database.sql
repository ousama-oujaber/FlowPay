-- FlowPay Database Reset and Recreation Script
-- This script drops existing tables and creates new ones for CRUD operations

USE flowpay;

-- Disable foreign key checks to avoid constraint issues during drop
SET FOREIGN_KEY_CHECKS = 0;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS paiement;
DROP TABLE IF EXISTS bonus;
DROP TABLE IF EXISTS indemnite;
DROP TABLE IF EXISTS salaire;
DROP TABLE IF EXISTS prime;
DROP TABLE IF EXISTS agent;
DROP TABLE IF EXISTS departement;
DROP TABLE IF EXISTS personne;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Create Personne table (base class)
CREATE TABLE personne (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Departement table
CREATE TABLE departement (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL UNIQUE,
    responsable_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Agent table
CREATE TABLE agent (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    type_agent ENUM('OUVRIER', 'RESPONSABLE_DEPARTEMENT', 'DIRECTEUR', 'STAGIAIRE') NOT NULL,
    departement_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (departement_id) REFERENCES departement(id) ON DELETE SET NULL
);

-- Add foreign key constraint for departement responsable after agent table is created
ALTER TABLE departement ADD FOREIGN KEY (responsable_id) REFERENCES agent(id) ON DELETE SET NULL;

-- Create Paiement table (base payment table)
CREATE TABLE paiement (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type ENUM('SALAIRE', 'PRIME', 'BONUS', 'INDEMNITE') NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    date_paiement DATE NOT NULL DEFAULT (CURRENT_DATE),
    motif VARCHAR(255),
    agent_id INT NOT NULL,
    condition_validee BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (agent_id) REFERENCES agent(id) ON DELETE CASCADE,
    CHECK (montant >= 0)
);

-- Insert default departments
INSERT INTO departement (nom) VALUES
('Ressources Humaines'),
('Informatique'),
('Comptabilité'),
('Marketing'),
('Production');

-- Insert default admin agent
INSERT INTO agent (nom, prenom, email, mot_de_passe, type_agent, departement_id) VALUES
('Admin', 'System', 'admin@flowpay.com', 'admin123', 'DIRECTEUR', 1);

-- Update first department to have admin as responsable
UPDATE departement SET responsable_id = 1 WHERE id = 1;

-- Insert some sample agents
INSERT INTO agent (nom, prenom, email, mot_de_passe, type_agent, departement_id) VALUES
('Dupont', 'Jean', 'jean.dupont@flowpay.com', 'password123', 'RESPONSABLE_DEPARTEMENT', 2),
('Martin', 'Marie', 'marie.martin@flowpay.com', 'password123', 'OUVRIER', 2),
('Bernard', 'Pierre', 'pierre.bernard@flowpay.com', 'password123', 'OUVRIER', 3),
('Durand', 'Sophie', 'sophie.durand@flowpay.com', 'password123', 'STAGIAIRE', 1);

-- Insert some sample payments
INSERT INTO paiement (type, montant, motif, agent_id, condition_validee) VALUES
('SALAIRE', 3000.00, 'Salaire mensuel', 1, TRUE),
('SALAIRE', 2500.00, 'Salaire mensuel', 2, TRUE),
('PRIME', 500.00, 'Prime de performance', 2, TRUE),
('BONUS', 1000.00, 'Bonus annuel', 1, TRUE),
('INDEMNITE', 200.00, 'Indemnité transport', 1, TRUE);

COMMIT;