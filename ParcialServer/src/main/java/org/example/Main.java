package org.example;

public class Main {
    public static void main(String[] args) {

        Server server = new Server(
                "localhost",
                "6803",
                "6802",
                "authService",
                "fileService");

        if (server.deploy()) {
            System.out.println("Servicios desplegados correctamente.");
        } else {
            System.out.println("Error al desplegar los servicios.");
        }
    }
}