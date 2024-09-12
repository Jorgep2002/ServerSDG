package org.example.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class mysqlConn {

    // Datos de conexión a la base de datos
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ds_app";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Método para obtener una nueva conexión
    public static Connection getConnection() {
        try {

            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connected successfully.");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to connect to the database.");
            return null;
        }
    }

    // Método para cerrar la conexión
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Failed to close the database connection.");
            }
        }
    }
}
