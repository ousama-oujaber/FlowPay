# FlowPay - Java Database Application

## Project Structure

```
FlowPay/
├── lib/                          # External JAR dependencies
│   └── mysql-connector-j-8.0.33.jar  # MySQL JDBC Driver
├── src/                          # Java source code
│   ├── config/                   # Database configuration
│   │   ├── ConfigDBConn.java     # JDBC connection manager
│   │   └── DatabaseConnectionTest.java  # Connection test utility
│   ├── models/                   # Data models
│   ├── services/                 # Business logic
│   └── views/                    # User interface
├── mysql-init/                   # Database initialization
│   └── 01-init.sql              # Database schema and data
├── database.properties          # Database configuration
├── docker-compose.yml           # MySQL container setup
└── build.sh                     # Build and run script
```

## Database Setup

### 1. Start MySQL Database
```bash
docker-compose up -d
```

### 2. Verify Database is Running
```bash
docker-compose ps
```

## JDBC Connection Setup

### ✅ Already Configured!
- **MySQL JDBC Driver**: `lib/mysql-connector-j-8.0.33.jar` ✓
- **Database Configuration**: `database.properties` ✓
- **Connection Manager**: `src/config/ConfigDBConn.java` ✓

### Database Connection Details
- **Host**: localhost:3306
- **Database**: flowpay
- **User**: flowpay_user
- **Password**: flowpay_password
- **Root User**: root / rootpassword

## How to Use

### Build Script Commands
```bash
# Compile all Java files
./build.sh compile

# Test database connection
./build.sh test-db

# Run a specific class
./build.sh run src.config.DatabaseConnectionTest

# Clean compiled files
./build.sh clean
```

### Manual Compilation (if needed)
```bash
# Compile with MySQL driver in classpath
javac -cp ".:lib/mysql-connector-j-8.0.33.jar" src/config/*.java

# Run with MySQL driver in classpath
java -cp ".:lib/mysql-connector-j-8.0.33.jar" src.config.DatabaseConnectionTest
```

## Using Database Connection in Your Code

```java
import src.config.ConfigDBConn;
import java.sql.Connection;
import java.sql.SQLException;

// Get a database connection
try (Connection conn = ConfigDBConn.getConnection()) {
    // Your database operations here
    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM agent");
    ResultSet rs = stmt.executeQuery();
    // Process results...
} catch (SQLException e) {
    logger.severe("Database error: " + e.getMessage());
}

// Test connection
if (ConfigDBConn.testConnection()) {
    System.out.println("Database connection OK!");
}
```

## Database Schema

The database includes these tables:
- `personne` - Personal information
- `agent` - Employee records
- `departement` - Departments
- `type_agent` - Employee types
- `type_paiement` - Payment types
- `paiement` - Payment records
- `bonus` - Bonus payments
- `indemnite` - Allowances

### Default Admin User
- **Matricule**: ADM001
- **Login**: admin@flowpay.com
- **Password**: admin123

## Next Steps

1. **Implement Model Classes**: Complete your model classes (`Agent.java`, `Paiement.java`, etc.)
2. **Develop Services**: Use the connection pattern shown in `AgentService.java`
3. **Build Views**: Connect your UI to the services
4. **Add Features**: Implement your business logic

## Troubleshooting

### Connection Issues
```bash
# Test database connection
./build.sh test-db

# Check if MySQL container is running
docker-compose ps

# View MySQL logs
docker-compose logs mysql
```

### Compilation Issues
- Ensure MySQL JAR is in `lib/` directory
- Use the build script or include JAR in classpath
- Check Java syntax and imports

## Development Tips

- Always use try-with-resources for database connections
- Use PreparedStatement to prevent SQL injection
- Handle SQLException appropriately
- Use the logging framework instead of System.out/err