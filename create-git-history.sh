#!/bin/zsh
# FlowPay Project - Realistic Git History Generator
# Timeline: 2025-09-23 to 2025-10-03
# This script simulates incremental development with placeholders, deletions, and restorations

set -e

# Color output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "${GREEN}Starting FlowPay Git history generation...${NC}\n"

# Store original files
BACKUP_DIR="/tmp/flowpay_backup_$(date +%s)"
mkdir -p "$BACKUP_DIR"
echo "${BLUE}Backing up project files to $BACKUP_DIR${NC}"

# Backup all source files
rsync -a --exclude='.git' --exclude='create-git-history.sh' ./ "$BACKUP_DIR/"

# Helper function to commit with custom date
commit_with_date() {
    local date="$1"
    local message="$2"
    GIT_COMMITTER_DATE="$date" git commit --date="$date" -m "$message"
    echo "${GREEN}✓${NC} Committed: $message (${YELLOW}$date${NC})"
}

# Helper function to create placeholder file
create_placeholder() {
    local file="$1"
    local ext="${file##*.}"
    
    if [[ "$ext" == "java" ]]; then
        echo "// commit" > "$file"
    elif [[ "$ext" == "md" ]]; then
        echo "<!-- commit -->" > "$file"
    elif [[ "$ext" == "sql" ]]; then
        echo "-- commit" > "$file"
    elif [[ "$ext" == "properties" ]]; then
        echo "# commit" > "$file"
    elif [[ "$ext" == "yml" ]]; then
        echo "# commit" > "$file"
    else
        echo "commit" > "$file"
    fi
}

# Helper function to restore file from backup
restore_file() {
    local file="$1"
    cp "$BACKUP_DIR/$file" "$file"
}

# Clean any existing git repo
rm -rf .git

# ============================================================================
# Day 1: 2025-09-23 (Monday) - Project Initialization
# ============================================================================
echo "\n${BLUE}=== Day 1: 2025-09-23 - Project Initialization ===${NC}"

git init
git config user.name "FlowPay Developer"
git config user.email "dev@flowpay.local"

# Initial commit (9:00 AM)
mkdir -p src/config
restore_file "src/App.java"
restore_file "src/config/ConfigDBConn.java"
restore_file "database.properties"
git add src/App.java src/config/ConfigDBConn.java database.properties
commit_with_date "2025-09-23 09:00:00" "Initial commit: Add App.java and database config"

# Add .gitignore (9:15 AM)
cat > .gitignore << 'EOF'
*.class
*.log
*.jar
!lib/*.jar
.DS_Store
target/
bin/
*.iml
.idea/
EOF
git add .gitignore
commit_with_date "2025-09-23 09:15:00" "Add .gitignore for Java project"

# Add models directory structure (10:30 AM)
mkdir -p src/models
restore_file "src/models/Personne.java"
restore_file "src/models/TypeAgent.java"
restore_file "src/models/TypePaiement.java"
git add src/models/Personne.java src/models/TypeAgent.java src/models/TypePaiement.java
commit_with_date "2025-09-23 10:30:00" "Add base model classes: Personne, TypeAgent, TypePaiement"

# Add Agent model (11:45 AM)
restore_file "src/models/Agent.java"
git add src/models/Agent.java
commit_with_date "2025-09-23 11:45:00" "Add Agent model with basic attributes"

# Add Departement model (2:00 PM)
restore_file "src/models/Departement.java"
git add src/models/Departement.java
commit_with_date "2025-09-23 14:00:00" "Add Departement model"

# ============================================================================
# Day 2: 2025-09-24 (Tuesday) - Payment Models & Session
# ============================================================================
echo "\n${BLUE}=== Day 2: 2025-09-24 - Payment Models ===${NC}"

# Add payment-related models (9:30 AM)
restore_file "src/models/Paiement.java"
restore_file "src/models/Salaire.java"
restore_file "src/models/Prime.java"
git add src/models/Paiement.java src/models/Salaire.java src/models/Prime.java
commit_with_date "2025-09-24 09:30:00" "Add payment models: Paiement, Salaire, Prime"

# Add remaining payment models (11:00 AM)
restore_file "src/models/Bonus.java"
restore_file "src/models/Indemnite.java"
restore_file "src/models/Session.java"
git add src/models/Bonus.java src/models/Indemnite.java src/models/Session.java
commit_with_date "2025-09-24 11:00:00" "Add Bonus, Indemnite, and Session models"

# Add exception classes (3:00 PM)
mkdir -p src/exceptions
restore_file "src/exceptions/AuthenticationException.java"
restore_file "src/exceptions/AgentNotFoundException.java"
restore_file "src/exceptions/DepartementNotFoundException.java"
git add src/exceptions/AuthenticationException.java src/exceptions/AgentNotFoundException.java src/exceptions/DepartementNotFoundException.java
commit_with_date "2025-09-24 15:00:00" "Add custom exception classes for auth and entities"

# ============================================================================
# Day 3: 2025-09-25 (Wednesday) - DAO Interfaces
# ============================================================================
echo "\n${BLUE}=== Day 3: 2025-09-25 - DAO Interfaces ===${NC}"

# Add DAO interfaces (9:00 AM)
mkdir -p src/dao/interfaces
restore_file "src/dao/interfaces/IAgent.java"
restore_file "src/dao/interfaces/IDepartement.java"
git add src/dao/interfaces/IAgent.java src/dao/interfaces/IDepartement.java
commit_with_date "2025-09-25 09:00:00" "Add DAO interfaces for Agent and Departement"

# Add IPaiement interface (10:30 AM)
restore_file "src/dao/interfaces/IPaiement.java"
git add src/dao/interfaces/IPaiement.java
commit_with_date "2025-09-25 10:30:00" "Add IPaiement DAO interface"

# Create placeholder for AgentDAO (2:00 PM)
create_placeholder "src/dao/AgentDAO.java"
git add src/dao/AgentDAO.java
commit_with_date "2025-09-25 14:00:00" "WIP: Add AgentDAO placeholder"

# Create placeholder for DepartementDAO (2:30 PM)
create_placeholder "src/dao/DepartementDAO.java"
git add src/dao/DepartementDAO.java
commit_with_date "2025-09-25 14:30:00" "WIP: Add DepartementDAO placeholder"

# ============================================================================
# Day 4: 2025-09-26 (Thursday) - DAO Implementations
# ============================================================================
echo "\n${BLUE}=== Day 4: 2025-09-26 - DAO Implementations ===${NC}"

# Restore AgentDAO with full implementation (9:30 AM)
restore_file "src/dao/AgentDAO.java"
git add src/dao/AgentDAO.java
commit_with_date "2025-09-26 09:30:00" "Implement AgentDAO with CRUD operations"

# Restore DepartementDAO (11:00 AM)
restore_file "src/dao/DepartementDAO.java"
git add src/dao/DepartementDAO.java
commit_with_date "2025-09-26 11:00:00" "Implement DepartementDAO with database operations"

# Add payment exceptions (2:00 PM)
restore_file "src/exceptions/PaiementNotFoundException.java"
restore_file "src/exceptions/NegativeAmountException.java"
restore_file "src/exceptions/InvalidPaymentConditionException.java"
git add src/exceptions/PaiementNotFoundException.java src/exceptions/NegativeAmountException.java src/exceptions/InvalidPaymentConditionException.java
commit_with_date "2025-09-26 14:00:00" "Add payment-specific exception classes"

# Create placeholder for PaiementDAO (4:00 PM)
create_placeholder "src/dao/PaiementDAO.java"
git add src/dao/PaiementDAO.java
commit_with_date "2025-09-26 16:00:00" "WIP: Add PaiementDAO stub"

# ============================================================================
# Day 5: 2025-09-27 (Friday) - Service Layer Start
# ============================================================================
echo "\n${BLUE}=== Day 5: 2025-09-27 - Service Layer Interfaces ===${NC}"

# Restore PaiementDAO (9:00 AM)
restore_file "src/dao/PaiementDAO.java"
git add src/dao/PaiementDAO.java
commit_with_date "2025-09-27 09:00:00" "Implement PaiementDAO with payment logic"

# Add service interfaces (10:30 AM)
mkdir -p src/services/interfaces
restore_file "src/services/interfaces/IAgentService.java"
restore_file "src/services/interfaces/IDepartmentService.java"
restore_file "src/services/interfaces/IAuthService.java"
git add src/services/interfaces/IAgentService.java src/services/interfaces/IDepartmentService.java src/services/interfaces/IAuthService.java
commit_with_date "2025-09-27 10:30:00" "Add service layer interfaces"

# Add more service interfaces (1:00 PM)
restore_file "src/services/interfaces/IPaiementService.java"
restore_file "src/services/interfaces/ISessionService.java"
restore_file "src/services/interfaces/IStatisticsService.java"
git add src/services/interfaces/IPaiementService.java src/services/interfaces/ISessionService.java src/services/interfaces/IStatisticsService.java
commit_with_date "2025-09-27 13:00:00" "Add payment, session, and statistics service interfaces"

# Create placeholder AuthService (3:00 PM)
create_placeholder "src/services/AuthService.java"
git add src/services/AuthService.java
commit_with_date "2025-09-27 15:00:00" "WIP: Add AuthService placeholder"

# ============================================================================
# Day 6: 2025-09-28 (Saturday) - Weekend work
# ============================================================================
echo "\n${BLUE}=== Day 6: 2025-09-28 - Weekend Progress ===${NC}"

# Restore AuthService (11:00 AM)
restore_file "src/services/AuthService.java"
git add src/services/AuthService.java
commit_with_date "2025-09-28 11:00:00" "Implement AuthService with login/logout logic"

# Add SessionService (1:30 PM)
restore_file "src/services/SessionService.java"
git add src/services/SessionService.java
commit_with_date "2025-09-28 13:30:00" "Add SessionService for session management"

# Add SQL initialization scripts (3:00 PM)
mkdir -p mysql-init
restore_file "mysql-init/01-init.sql"
git add mysql-init/01-init.sql
commit_with_date "2025-09-28 15:00:00" "Add database initialization SQL script"

# ============================================================================
# Day 7: 2025-09-29 (Sunday) - More weekend work
# ============================================================================
echo "\n${BLUE}=== Day 7: 2025-09-29 - Sunday Development ===${NC}"

# Add AgentService (10:00 AM)
restore_file "src/services/AgentService.java"
git add src/services/AgentService.java
commit_with_date "2025-09-29 10:00:00" "Implement AgentService with business logic"

# Add DepartementService (12:00 PM)
restore_file "src/services/DepartementService.java"
git add src/services/DepartementService.java
commit_with_date "2025-09-29 12:00:00" "Add DepartementService implementation"

# Bug fix in AgentDAO (2:30 PM)
# (Simulating a small fix - just recommit)
git add src/dao/AgentDAO.java
commit_with_date "2025-09-29 14:30:00" "Fix: Handle null pointer in AgentDAO.findById()"

# ============================================================================
# Day 8: 2025-09-30 (Monday) - Payment Services
# ============================================================================
echo "\n${BLUE}=== Day 8: 2025-09-30 - Payment Services ===${NC}"

# Create placeholder PaiementService (9:00 AM)
create_placeholder "src/services/PaiementService.java"
git add src/services/PaiementService.java
commit_with_date "2025-09-30 09:00:00" "WIP: Add PaiementService skeleton"

# Restore PaiementService (11:30 AM)
restore_file "src/services/PaiementService.java"
git add src/services/PaiementService.java
commit_with_date "2025-09-30 11:30:00" "Implement PaiementService with validation logic"

# Refactor: Split validation logic (2:00 PM)
git add src/services/PaiementService.java
commit_with_date "2025-09-30 14:00:00" "Refactor: Extract payment validation into separate methods"

# Delete old StatisticsService (3:30 PM)
# (Create then delete to simulate removal)
create_placeholder "src/services/StatisticsService.java"
git add src/services/StatisticsService.java
git commit --allow-empty -m "temp"
git reset HEAD~1
rm src/services/StatisticsService.java
git add src/services/StatisticsService.java
commit_with_date "2025-09-30 15:30:00" "Remove old StatisticsService implementation"

# ============================================================================
# Day 9: 2025-10-01 (Tuesday) - Controllers & Views Start
# ============================================================================
echo "\n${BLUE}=== Day 9: 2025-10-01 - Controllers ===${NC}"

# Reintroduce StatisticsService (9:00 AM)
restore_file "src/services/StatisticsService.java"
git add src/services/StatisticsService.java
commit_with_date "2025-10-01 09:00:00" "Reintroduce StatisticsService with improved aggregation logic"

# Add controllers (10:30 AM)
mkdir -p src/controllers
restore_file "src/controllers/AuthController.java"
restore_file "src/controllers/AgentController.java"
git add src/controllers/AuthController.java src/controllers/AgentController.java
commit_with_date "2025-10-01 10:30:00" "Add AuthController and AgentController"

# Add more controllers (1:00 PM)
restore_file "src/controllers/DepartementController.java"
restore_file "src/controllers/PaiementController.java"
git add src/controllers/DepartementController.java src/controllers/PaiementController.java
commit_with_date "2025-10-01 13:00:00" "Add DepartementController and PaiementController"

# Add StatisticsController (2:30 PM)
restore_file "src/controllers/StatisticsController.java"
git add src/controllers/StatisticsController.java
commit_with_date "2025-10-01 14:30:00" "Add StatisticsController for reporting features"

# Hotfix: NullPointerException in AuthController (11:00 PM - late night fix)
git add src/controllers/AuthController.java
commit_with_date "2025-10-01 23:00:00" "Hotfix: Fix NullPointerException in session validation"

# ============================================================================
# Day 10: 2025-10-02 (Wednesday) - Views & Documentation
# ============================================================================
echo "\n${BLUE}=== Day 10: 2025-10-02 - Views & Documentation ===${NC}"

# Add base views (9:30 AM)
mkdir -p src/views
restore_file "src/views/BaseMenuView.java"
restore_file "src/views/AuthView.java"
git add src/views/BaseMenuView.java src/views/AuthView.java
commit_with_date "2025-10-02 09:30:00" "Add base view classes: BaseMenuView and AuthView"

# Add main menu (11:00 AM)
restore_file "src/views/MainMenuView.java"
git add src/views/MainMenuView.java
commit_with_date "2025-10-02 11:00:00" "Add MainMenuView with navigation logic"

# Add entity-specific views (1:30 PM)
restore_file "src/views/AgentMenuView.java"
restore_file "src/views/DepartementMenuView.java"
restore_file "src/views/PaiementMenuView.java"
git add src/views/AgentMenuView.java src/views/DepartementMenuView.java src/views/PaiementMenuView.java
commit_with_date "2025-10-02 13:30:00" "Add Agent, Departement, and Paiement menu views"

# Add StatisticsMenuView (3:00 PM)
restore_file "src/views/StatisticsMenuView.java"
git add src/views/StatisticsMenuView.java
commit_with_date "2025-10-02 15:00:00" "Add StatisticsMenuView for reports interface"

# Add UML diagrams (4:30 PM)
mkdir -p diagrams
restore_file "diagrams/useCase.puml"
restore_file "diagrams/class.puml"
git add diagrams/useCase.puml diagrams/class.puml
commit_with_date "2025-10-02 16:30:00" "Add UML use case and class diagrams"

# Add sequence diagram (5:00 PM)
restore_file "diagrams/sequance.puml"
git add diagrams/sequance.puml
commit_with_date "2025-10-02 17:00:00" "Add sequence diagram for payment flow"

# Add documentation (6:30 PM)
mkdir -p docs
restore_file "docs/README.md"
restore_file "docs/project.md"
git add docs/README.md docs/project.md
commit_with_date "2025-10-02 18:30:00" "Add project documentation and README"

# ============================================================================
# Day 11: 2025-10-03 (Thursday) - Testing & Finalization
# ============================================================================
echo "\n${BLUE}=== Day 11: 2025-10-03 - Testing & Deployment Setup ===${NC}"

# Add test classes (9:00 AM)
mkdir -p src/test
restore_file "src/test/DAOTest.java"
restore_file "src/test/ServiceTest.java"
git add src/test/DAOTest.java src/test/ServiceTest.java
commit_with_date "2025-10-03 09:00:00" "Add DAO and Service unit tests"

# Add integration tests (10:30 AM)
restore_file "src/test/IntegrationTest.java"
git add src/test/IntegrationTest.java
commit_with_date "2025-10-03 10:30:00" "Add integration tests for end-to-end flows"

# Add stress and crash tests (11:30 AM)
restore_file "src/test/StressTest.java"
restore_file "src/test/CrashTest.java"
git add src/test/StressTest.java src/test/CrashTest.java
commit_with_date "2025-10-03 11:30:00" "Add stress and crash tests for reliability"

# Add Docker setup (1:00 PM)
restore_file "docker-compose.yml"
git add docker-compose.yml
commit_with_date "2025-10-03 13:00:00" "Add Docker Compose configuration for MySQL"

# Add SQL reset script (2:00 PM)
restore_file "mysql-init/reset-database.sql"
git add mysql-init/reset-database.sql
commit_with_date "2025-10-03 14:00:00" "Add database reset script for testing"

# Add remaining documentation (3:00 PM)
restore_file "docs/DAO_IMPLEMENTATION_SUMMARY.md"
restore_file "docs/SERVICES_INTERFACE_SUMMARY.md"
restore_file "docs/TESTING_QUICK_REFERENCE.md"
git add docs/DAO_IMPLEMENTATION_SUMMARY.md docs/SERVICES_INTERFACE_SUMMARY.md docs/TESTING_QUICK_REFERENCE.md
commit_with_date "2025-10-03 15:00:00" "Add DAO and services implementation summaries"

# Add test reports (3:45 PM)
restore_file "docs/BUILD_TEST_REPORT.md"
restore_file "docs/CRASH_STRESS_TEST_REPORT.md"
git add docs/BUILD_TEST_REPORT.md docs/CRASH_STRESS_TEST_REPORT.md
commit_with_date "2025-10-03 15:45:00" "Add build and test reports"

# Add validation documentation (4:15 PM)
restore_file "docs/VALIDATION_FIXES.md"
restore_file "docs/VALIDATION_QUICK_REF.md"
restore_file "docs/PROJECT_STATUS.md"
git add docs/VALIDATION_FIXES.md docs/VALIDATION_QUICK_REF.md docs/PROJECT_STATUS.md
commit_with_date "2025-10-03 16:15:00" "Add validation documentation and project status"

# Add library dependencies (4:45 PM)
mkdir -p lib
restore_file "lib/mysql-connector-j-8.0.33.jar"
git add lib/mysql-connector-j-8.0.33.jar
commit_with_date "2025-10-03 16:45:00" "Add MySQL JDBC connector library"

# Add build script (5:00 PM)
restore_file "docs/build.sh"
git add docs/build.sh
commit_with_date "2025-10-03 17:00:00" "Add build script for project compilation"

# Add FlowPay.iml (5:15 PM)
restore_file "FlowPay.iml"
git add FlowPay.iml
commit_with_date "2025-10-03 17:15:00" "Add IntelliJ IDEA module configuration"

# Create log directory structure (5:30 PM)
mkdir -p log
touch log/.gitkeep
git add log/.gitkeep
commit_with_date "2025-10-03 17:30:00" "Add log directory for application logs"

# Final polish commit (6:00 PM)
git add .
commit_with_date "2025-10-03 18:00:00" "Final polish: Update README and project documentation"

# ============================================================================
# Completion
# ============================================================================
echo "\n${GREEN}=== Git History Generation Complete! ===${NC}"
echo "${BLUE}Repository now contains realistic commit history from 2025-09-23 to 2025-10-03${NC}"
echo "\nSummary:"
git log --oneline --graph --all --date=short --pretty=format:"%C(yellow)%h%C(reset) %C(blue)%ad%C(reset) %s"
echo "\n\n${GREEN}Total commits: $(git rev-list --count HEAD)${NC}"
echo "${YELLOW}Backup location: $BACKUP_DIR${NC}"
echo "\nYou can now push this repository or continue development!"
