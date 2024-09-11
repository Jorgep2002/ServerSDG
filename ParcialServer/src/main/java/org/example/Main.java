package org.example;

import org.example.data.mysqlConn;

public class Main {
    public static void main(String[] args) {
        mysqlConn.getConnection();

        Server server = new Server(
                "localhost",
                "6803",
                "6802",
                "AuthService",
                "fileService");

        if (server.deploy()) {
            System.out.println("Servicios desplegados correctamente.");
        } else {
            System.out.println("Error al desplegar los servicios.");
        }
    }
}