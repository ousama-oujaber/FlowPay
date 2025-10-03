# 💼 FlowPay - Agent Payment Management System

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED.svg)](https://www.docker.com/)
[![Status](https://img.shields.io/badge/Status-Active-success.svg)](https://github.com)

A comprehensive **Java console application** for managing agents, departments, and payment processing with robust authentication, role-based access control, and detailed statistics reporting.

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Architecture](#-architecture)
- [Technology Stack](#-technology-stack)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Usage](#-usage)
- [Project Structure](#-project-structure)
- [Business Rules](#-business-rules)
- [Database Schema](#-database-schema)
- [Testing](#-testing)
- [Documentation](#-documentation)
- [Contributing](#-contributing)

---

## 🎯 Overview

FlowPay is a **Java-based payment management system** designed to digitalize and streamline the process of managing employee payments within an organization. The application provides secure, role-based access for different user types and implements comprehensive business rules for payment eligibility and processing.

### Key Objectives

- **Digitalize** agent and department management
- **Automate** payment processing with validation rules
- **Provide** detailed statistics and reporting
- **Ensure** data integrity and security
- **Support** role-based access control

---

## ✨ Features

### 🔐 Authentication & Security
- Secure login system with session management
- Role-based access control (Agent, Department Manager, Director)
- Password encryption and validation

### 👥 Agent Management
- Create, read, update, and delete agents
- Assign agents to departments
- Define agent types (Worker, Manager, Director, Intern)
- View personal information and payment history

### 🏢 Department Management
- Complete CRUD operations for departments
- Assign responsible agents to departments
- Track department statistics and payments
- View all agents within a department

### 💰 Payment Processing
- **Four payment types**:
  - **Salary** (SALAIRE) - Available for all agents
  - **Prime** - Additional compensation for all agents
  - **Bonus** - Restricted to managers and directors
  - **Indemnity** (INDEMNITE) - Requires eligibility and condition validation

- **Smart Validation**:
  - Negative amount prevention
  - Eligibility checks for bonus and indemnity
  - Condition validation for special payments
  - Automatic date tracking

### 📊 Statistics & Reporting
- Individual agent payment summaries
- Department-wide payment analysis
- Payment type distribution
- Total and average calculations
- Anomaly detection
- Date-based filtering and sorting

---

## 🏗️ Architecture

FlowPay follows the **Model-View-Controller (MVC)** architectural pattern with a clean separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                         View Layer                          │
│  (Console UI - Menus, Input Handling, Display)             │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│                      Controller Layer                       │
│  (Request Handling, Input Validation, Flow Control)        │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│                       Service Layer                         │
│  (Business Logic, Validation, Orchestration)               │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│                         DAO Layer                           │
│  (Data Access, JDBC Operations, SQL Queries)               │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│                     Database (MySQL)                        │
└─────────────────────────────────────────────────────────────┘
```

### Design Patterns

- **MVC Pattern** - Separation of presentation, business logic, and data
- **DAO Pattern** - Abstraction of data persistence
- **Service Layer Pattern** - Business logic encapsulation
- **Interface Segregation** - Clean contracts for all services
- **Dependency Injection** - Manual DI for loose coupling

---

## 🛠️ Technology Stack

### Core Technologies
- **Java 17+** - Primary programming language
- **JDBC** - Database connectivity
- **MySQL 8.0** - Relational database management

### Java Features Utilized
- ☕ **OOP Principles** - Inheritance, Polymorphism, Encapsulation, Abstraction
- 📦 **Collections Framework** - ArrayList, List interfaces
- 🌊 **Stream API** - Filtering, mapping, reduction operations
- λ **Lambda Expressions** - Functional programming constructs
- 🔗 **Method References** - Cleaner functional code
- 📅 **Java Time API** - Modern date/time handling (LocalDate, LocalDateTime)
- 🎯 **Optional** - Null-safe value handling
- 🔢 **Enums** - Type-safe constants (TypeAgent, TypePaiement)
- ⚠️ **Custom Exceptions** - Domain-specific error handling

### Infrastructure
- **Docker & Docker Compose** - Containerized database environment
- **phpMyAdmin** - Database administration interface
- **Git** - Version control

---

## 📦 Prerequisites

Before running FlowPay, ensure you have the following installed:

- **Java Development Kit (JDK) 17 or higher**
  ```bash
  java -version
  ```

- **Docker & Docker Compose**
  ```bash
  docker --version
  docker-compose --version
  ```

- **Git** (for cloning the repository)
  ```bash
  git --version
  ```

---

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/ousama-oujaber/FlowPay
cd FlowPay
```

### 2. Start the Database

The project uses Docker Compose to manage the MySQL database and phpMyAdmin:

```bash
docker-compose up -d
```

This will:
- Create and start a MySQL 8.0 container on port `3306`
- Create and start phpMyAdmin on port `8080`
- Initialize the database with the schema from `mysql-init/01-init.sql`
- Set up the `flowpay` database with user credentials

**Access phpMyAdmin**: http://localhost:8080
- **Username**: `root`
- **Password**: `rootpassword`

### 3. Verify Database Connection

```bash
docker ps
```

You should see `flowpay_mysql` and `flowpay_phpmyadmin` containers running.

### 4. Compile the Application

```bash
javac -cp ".:lib/mysql-connector-j-8.0.33.jar" $(find src -name "*.java")
```

**Expected Output**: No errors, silent compilation success

---

## 💻 Usage

### Running the Application

```bash
java -cp ".:lib/mysql-connector-j-8.0.33.jar" src.App
```

### Login

The application will prompt for login credentials:

```
=== FlowPay - Agent Payment Management System ===
1. Login
2. Exit

Enter your email: admin@flowpay.com
Enter your password: ********
```

### Navigation

Based on your role, you'll see different menu options:

#### Agent View
- View personal information
- View payment history
- Filter and sort payments
- Calculate total payments

#### Department Manager View
- All agent features +
- Manage agents in the department
- Manage department information
- Process payments for agents
- View department statistics

#### Director View
- All features (full access)
- Manage all departments
- Manage all agents
- View organization-wide statistics

---

## 📁 Project Structure

```
FlowPay/
├── src/
│   ├── App.java                    # Main application entry point
│   ├── config/
│   │   └── ConfigDBConn.java       # Database configuration
│   ├── models/                     # Domain entities
│   │   ├── Agent.java
│   │   ├── Departement.java
│   │   ├── Paiement.java
│   │   ├── Personne.java           # Abstract base class
│   │   ├── Salaire.java
│   │   ├── Prime.java
│   │   ├── Bonus.java
│   │   ├── Indemnite.java
│   │   ├── Session.java
│   │   ├── TypeAgent.java          # Enum
│   │   └── TypePaiement.java       # Enum
│   ├── dao/                        # Data Access Objects
│   │   ├── interfaces/
│   │   │   ├── IAgent.java
│   │   │   ├── IDepartement.java
│   │   │   └── IPaiement.java
│   │   ├── AgentDAO.java
│   │   ├── DepartementDAO.java
│   │   └── PaiementDAO.java
│   ├── services/                   # Business logic layer
│   │   ├── interfaces/
│   │   │   ├── IAgentService.java
│   │   │   ├── IAuthService.java
│   │   │   ├── IDepartmentService.java
│   │   │   ├── IPaiementService.java
│   │   │   ├── ISessionService.java
│   │   │   └── IStatisticsService.java
│   │   ├── AgentService.java
│   │   ├── AuthService.java
│   │   ├── DepartementService.java
│   │   ├── PaiementService.java
│   │   ├── SessionService.java
│   │   └── StatisticsService.java
│   ├── controllers/                # Request handlers
│   │   ├── AgentController.java
│   │   ├── AuthController.java
│   │   ├── DepartementController.java
│   │   ├── PaiementController.java
│   │   └── StatisticsController.java
│   ├── views/                      # Console UI
│   │   ├── BaseMenuView.java
│   │   ├── AuthView.java
│   │   ├── MainMenuView.java
│   │   ├── AgentMenuView.java
│   │   ├── DepartementMenuView.java
│   │   ├── PaiementMenuView.java
│   │   └── StatisticsMenuView.java
│   └── exceptions/                 # Custom exceptions
│       ├── AgentNotFoundException.java
│       ├── AuthenticationException.java
│       ├── DepartementNotFoundException.java
│       ├── InvalidPaymentConditionException.java
│       ├── NegativeAmountException.java
│       └── PaiementNotFoundException.java
│
├── diagrams/                       # UML diagrams
│   ├── class.puml
│   ├── sequance.puml
│   └── useCase.puml
├── mysql-init/                     # Database initialization
│   ├── 01-init.sql
│   └── reset-database.sql
├── lib/                            # External libraries
│   └── mysql-connector-j-8.0.33.jar
├── docker-compose.yml              # Docker configuration
├── database.properties             # Database credentials
└── README.md                       # This file
```

---

## ⚖️ Business Rules

### Agent Types (TypeAgent)
- **OUVRIER** (Worker) - Basic employee
- **RESPONSABLE_DEPARTEMENT** (Department Manager) - Department head
- **DIRECTEUR** (Director) - Organization director
- **STAGIAIRE** (Intern) - Temporary trainee

### Payment Types (TypePaiement)

| Type | Available For | Requires Validation |
|------|---------------|---------------------|
| **SALAIRE** | All agents | No |
| **PRIME** | All agents | No |
| **BONUS** | Managers & Directors only | Yes (condition must be validated) |
| **INDEMNITE** | Managers & Directors only | Yes (condition must be validated) |

### Entity Relationships
- Each **Agent** belongs to exactly **one Department**
- Each **Department** has exactly **one responsible Agent**
- A **Department** can contain **multiple Agents**
- An **Agent** can have **multiple Payments**

### Validation Rules
- ❌ Negative payment amounts are prohibited
- ❌ Bonus/Indemnity requires eligible agent type (Manager or Director)
- ❌ Bonus/Indemnity requires condition validation
- ✅ All payments are timestamped automatically
- ✅ Agent must exist before payment processing
- ✅ Department must have a responsible agent

---

## 🗄️ Database Schema

### Core Tables

#### `agent`
- `id` (PK) - Unique identifier
- `personne_id` (FK) - Link to person information
- `matricule` - Employee number (unique)
- `departement_id` (FK) - Department assignment
- `type_agent_id` (FK) - Agent type
- `mot_de_passe` - Hashed password
- `date_embauche` - Hire date
- `actif` - Active status

#### `departement`
- `id` (PK) - Unique identifier
- `nom` - Department name
- `description` - Department description
- `responsable_id` (FK) - Responsible agent
- Timestamps (created_at, updated_at)

#### `paiement`
- `id` (PK) - Unique identifier
- `agent_id` (FK) - Receiving agent
- `type_paiement_id` (FK) - Payment type
- `montant` - Amount (decimal)
- `motif` - Reason/description
- `date_paiement` - Payment date
- `condition_validee` - Condition validation flag
- Timestamps (created_at, updated_at)

#### `personne`
- `id` (PK) - Unique identifier
- `nom` - Last name
- `prenom` - First name
- `email` - Email (unique)
- `telephone` - Phone number
- `date_naissance` - Birth date
- Timestamps (created_at, updated_at)

---

## 🐳 Docker Management

### Start Services
```bash
docker-compose up -d
```

### Stop Services
```bash
docker-compose down
```

### View Logs
```bash
docker-compose logs -f mysql
```

### Reset Database
```bash
# Stop containers
docker-compose down -v

# Start fresh
docker-compose up -d
```

### Access MySQL CLI
```bash
docker exec -it flowpay_mysql mysql -u flowpay_user -pflowpay_password flowpay
```

---

## 🔧 Configuration

### Database Configuration

Edit `database.properties` to customize database connection:

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/flowpay
db.username=flowpay_user
db.password=flowpay_password
```

### Docker Configuration

Edit `docker-compose.yml` to customize:
- MySQL port mapping
- phpMyAdmin port
- Root password
- Database name and credentials

---

## 📊 Statistics & Analytics

The StatisticsService provides comprehensive analytics:

### Available Statistics
- 📈 **Total payments by agent**
- 📊 **Average payment by type**
- 🏢 **Department-wide payment summaries**
- 📅 **Time-based payment analysis**
- 🔍 **Anomaly detection**
- 💰 **Payment distribution by type**
- 👥 **Agent comparison reports**

### Using Stream API

Statistics leverage Java Streams for efficient data processing:
```java
// Example: Calculate total salary for an agent
double totalSalary = agent.getPaiements().stream()
    .filter(p -> p.getType() == TypePaiement.SALAIRE)
    .mapToDouble(Paiement::getMontant)
    .sum();
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request


---

## 📝 License

This project is developed as an educational application for learning Java, OOP, JDBC, and software architecture principles.

---

## 👥 Authors

`Ousama Oujaber`

---

## 🎓 Learning Objectives Achieved

This project demonstrates mastery of:

- ✅ **Object-Oriented Programming** - Inheritance, polymorphism, encapsulation
- ✅ **Design Patterns** - MVC, DAO, Service Layer, Dependency Injection
- ✅ **Java Collections** - Lists, ArrayList, generics
- ✅ **Stream API** - Filtering, mapping, reducing, collecting
- ✅ **Lambda Expressions** - Functional interfaces and implementations
- ✅ **Method References** - Static and instance method references
- ✅ **Java Time API** - LocalDate, LocalDateTime manipulation
- ✅ **Optional** - Null-safe programming
- ✅ **JDBC** - Database connectivity and operations
- ✅ **Exception Handling** - Custom exceptions and error management
- ✅ **Enums** - Type-safe constants
- ✅ **Software Architecture** - Layered architecture, separation of concerns
- ✅ **Testing** - Unit tests, integration tests, stress tests
- ✅ **Docker** - Containerization and orchestration
