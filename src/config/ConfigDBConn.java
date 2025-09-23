package src.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigDBConn {
    private static final String PROPERTIES_FILE = "database.properties";
    private static final Properties properties = new Properties();
    private static final Logger logger = Logger.getLogger(ConfigDBConn.class.getName());
    
    static {
        loadProperties();
    }
    
    
    private ConfigDBConn() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    private static void loadProperties() {
        try (FileInputStream input = new FileInputStream(PROPERTIES_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            logger.severe("Error loading database properties: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        try {
            
            Class.forName(properties.getProperty("db.driver"));
            
            
            return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
            );
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
    
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && conn.isValid(2);
        } catch (SQLException e) {
            logger.severe("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}