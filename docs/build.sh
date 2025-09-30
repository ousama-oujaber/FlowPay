#!/bin/bash

# FlowPay Java Application Build Script
# This script compiles and runs Java classes with the MySQL connector

MYSQL_JAR="lib/mysql-connector-j-8.0.33.jar"
CLASSPATH=".:$MYSQL_JAR"

echo "FlowPay Build Script"
echo "==================="

case "$1" in
    "compile")
        echo "Compiling all Java files..."
        find src -name "*.java" -exec javac -cp "$CLASSPATH" {} +
        echo "✓ Compilation completed"
        ;;
    "test-db")
        echo "Testing database connection..."
        javac -cp "$CLASSPATH" src/config/*.java
        java -cp "$CLASSPATH" src.config.DatabaseConnectionTest
        ;;
    "run")
        if [ -z "$2" ]; then
            echo "Usage: ./build.sh run <ClassName>"
            echo "Example: ./build.sh run src.config.DatabaseConnectionTest"
            exit 1
        fi
        echo "Running $2..."
        java -cp "$CLASSPATH" "$2"
        ;;
    "clean")
        echo "Cleaning compiled files..."
        find src -name "*.class" -delete
        echo "✓ Clean completed"
        ;;
    *)
        echo "Usage: ./build.sh {compile|test-db|run <class>|clean}"
        echo ""
        echo "Commands:"
        echo "  compile  - Compile all Java source files"
        echo "  test-db  - Test database connection"
        echo "  run      - Run a specific Java class"
        echo "  clean    - Remove all compiled .class files"
        echo ""
        echo "Examples:"
        echo "  ./build.sh compile"
        echo "  ./build.sh test-db"
        echo "  ./build.sh run src.config.DatabaseConnectionTest"
        ;;
esac