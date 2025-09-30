```markdown
# 💼 Java Console App – Agent Payment Management

This project is a **Java console application** that simulates the management of **agents**, their **departments**, and their **payments**.
It applies **Object-Oriented Programming (OOP)** principles, **Collections**, **Streams**, **Lambdas**, **Java Time API**, and **JDBC** persistence, all wrapped in a clean **MVC architecture**.

---

## 📌 Project Context
The organization wants to **digitalize** the process of managing agent payments and department tracking.
This application provides agents and department managers with a **simple**, **reliable**, and **secure** tool to:
- Manage agents and departments.
- Record and track all types of payments (**Salary, Prime, Bonus, Indemnity**).
- Apply business rules for eligibility.
- Generate detailed statistics and identify anomalies.

---

## 🎯 Objectives & Key Features

### For Agents
- View personal information and assigned department.
- Display full payment history (salary, prime, bonus, indemnity).
- Filter and sort payments by **type**, **amount**, or **date** using Streams.
- Calculate the **total** of all received payments.

### For Department Managers
- **Department Management**: Create, update, or delete departments and assign a responsible agent.
- **Agent Management**: Add, update, or remove agents and assign them to a department.
- **Payment Management**:
  - **Salary & Prime**: Addable for all agents.
  - **Bonus & Indemnity**: Addable only if the agent is **eligible** (Department Manager or Director) **and** the required event/condition is validated.
- View and filter payments of:
  - A specific agent.
  - All agents within the department.
- Automatic calculation of:
  - Total and average payments per agent and per department.
- Detect **unusual or incorrect** payments.

---

## ⚖️ Business Rules
- Each **Agent** belongs to a single **Department**.
- Each **Department** has exactly **one responsible agent**.
- A **Department** can contain multiple agents.
- An **Agent** can have multiple **Payments**.
- Payments must follow eligibility rules:
  - **Bonus & Indemnity** require:
    - Eligible agent type (**Department Manager** or **Director**).
    - Validation of the event/condition.
- Exception handling:
  - Negative amounts.
  - Missing or invalid agent/department.
  - Non-existent payment.

---

## 🏛️ Data Model (UML-Ready)

### Entities
- **Personne** *(abstract)*: `nom`, `prenom`, `email`, `motDePasse`.
- **Agent**: `idAgent`, `typeAgent` (Enum), `departement`, `paiements`.
- **Departement**: `idDepartement`, `nom`, `responsable`, `agents`.
- **Paiement**: `idPaiement`, `type` (Enum), `montant`, `date`, `motif`, `agent`.
  - `conditionValidee` (boolean) for **Bonus** and **Indemnity**.
- **Enums**:
  - `TypeAgent`: `OUVRIER`, `RESPONSABLE_DEPARTEMENT`, `DIRECTEUR`, `STAGIAIRE`.
  - `TypePaiement`: `SALAIRE`, `PRIME`, `BONUS`, `INDEMNITE`.

---

## 🧩 Architecture
The application follows an **MVC structure**:

```

src/
├─ model       # Entities (Agent, Departement, Paiement, Enums)
├─ service     # Business logic (Payment, Department, Agent services)
├─ controller  # User interaction & input validation
└─ view        # Console menus for Agents & Managers

````

### Technologies & Concepts
- **Java Collections** (`ArrayList`) to store agents, departments, and payments.
- **Streams & Lambdas** for filtering, sorting, and aggregation.
- **Functional Interfaces** (`Predicate`, `Function`, `Consumer`, `Supplier`).
- **Method References** (`::`) to simplify operations.
- **Optional** for safe handling of absent values.
- **Java Time API** for date management.
- **JDBC** for data persistence (MySQL/PostgreSQL-ready).

---

## 📊 Statistics & Reports

### Per Agent
- Annual total salary.
- Count of received **Primes**, **Bonus**, and **Indemnities**.
- Highest and lowest payment.

### Per Department
- Total payments of all agents.
- Average agent salary.
- Ranking of agents by total received amount.

### Global Report
- Total number of **Agents** and **Departments**.
- Distribution of payments by type (**% Salary/Prime/Bonus/Indemnity**).
- Agent with the highest total payment.

---

## 🧪 User Stories

### Agent
- Consult personal details and department.
- View and filter payment history.
- Calculate total payments received.

### Department Manager
- Create, update, and delete departments.
- Add, modify, or remove agents.
- Add payments according to business rules.
- View and filter agent or department payments.
- Identify anomalies in payment records.

---

## 🛠️ Learning Goals
- Apply **OOP principles** (Encapsulation, Inheritance, Polymorphism).
- Work with **Collections** and **Streams** effectively.
- Implement **error handling** and **validation**.
- Persist data using **JDBC**.

---

## 📅 Project Timeline
- **Start Date:** 22/09/2025
- **Deadline:** 03/10/2025 (9 days)

---

## ✅ Evaluation Criteria

| Category          | Weight | Details                                                                 |
|-------------------|-------:|-------------------------------------------------------------------------|
| **Design**        | 30%   | UML class diagram, MVC structure, business rules modeling               |
| **Implementation**| 40%   | OOP quality, use of collections & streams, bonus/indemnity validation   |
| **Functionality** | 20%   | Correct menu execution, scenarios, and robustness                        |
| **Presentation**  | 10%   | Technical explanations, UML walkthrough, scenario demo                   |

---

## 🗂️ Deliverables
1. **GitHub Repository** containing:
   - Complete Java source code, well-structured and documented.
   - `README.md` (this file).
   - UML diagrams (`.puml` with PlantUML):
     - Class Diagram
     - Sequence Diagram
     - Use Case Diagram
2. **Executable JAR** for easy application launch.

---

## ▶️ How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/<username>/java-agent-payments.git
   cd java-agent-payments
````

2. Compile and run:

   ```bash
   javac -d out src/**/*.java
   java -cp out Main
   ```
3. (Optional) Build JAR:

   ```bash
   jar cfe AgentPayments.jar Main -C out .
   java -jar AgentPayments.jar
   ```

---

## 🧭 Future Improvements

* Migrate to a **JavaFX** or **Spring Boot** GUI/web interface.
* Add **authentication & role-based access control**.
* Implement **REST API** for multi-platform integration.

---

## 👨‍💻 Author

Developed individually as part of the **[2023] Application Designer & Developer** training program.
This project demonstrates mastery of **Java OOP**, **Collections**, and **modern Java features**.

---


write for me models for this project without complexety and comments ok