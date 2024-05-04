package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    private static final String URL = "jdbc:mysql://localhost:3306/lyn";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final long HEALTH_CHECK_INTERVAL_MS = 5 * 60 * 1000; // 5 minutes
    private Connection connection;
    private static MyDataBase instance;
    private Connection cnx ;
    private void MyDatabase() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection established");
            // Start the connection health check task
            startConnectionHealthCheckTask();
        } catch (SQLException e) {
            System.err.println("Failed to establish connection: " + e.getMessage());
        }
    }

    public static MyDataBase getInstance() {
        if (instance == null) {
            instance = new MyDataBase();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            // Check if the connection is closed
            if (connection == null || connection.isClosed()) {
                // Reconnect to the database
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connection reestablished.");
            }
        } catch (SQLException e) {
            System.err.println("Error reconnecting to the database: " + e.getMessage());
        }
        return connection;
    }


    private void startConnectionHealthCheckTask() {
        Thread healthCheckThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(HEALTH_CHECK_INTERVAL_MS);
                    checkConnectionHealth();
                } catch (InterruptedException e) {
                    System.err.println("Connection health check thread interrupted: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        });
        healthCheckThread.setDaemon(true);
        healthCheckThread.start();
    }

    private void checkConnectionHealth() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connection reestablished");
            }
        } catch (SQLException e) {
            System.err.println("Failed to check connection health: " + e.getMessage());
        }
    }
}
