package org.example;

import org.example.services.AuthServiceImpl;
import org.example.services.FileServiceImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {
    private String ip;
    private String userPort;
    private String filePort;
    private String authServiceName;
    private String fileServiceName;
    private String userUri;
    private String fileUri;

    public Server(String ip, String userPort, String filePort, String authServiceName, String fileServiceName) {
        this.ip = ip;
        this.userPort = userPort;
        this.filePort = filePort;
        this.authServiceName = authServiceName;
        this.fileServiceName = fileServiceName;
        this.userUri = "//" + this.ip + ":" + this.userPort + "/" + this.authServiceName;
        this.fileUri = "//" + this.ip + ":" + this.filePort + "/" + this.fileServiceName;
    }

    public boolean deploy() {
        try {
            System.setProperty("java.rmi.server.hostname", ip);

            // Crear e implementar servicios
            AuthServiceImpl authService = new AuthServiceImpl("");
            FileServiceImpl fileService = new FileServiceImpl();

            // Iniciar el registro RMI
            LocateRegistry.createRegistry(Integer.parseInt(userPort));
            LocateRegistry.createRegistry(Integer.parseInt(filePort));

            // Registrar servicios en el registro RMI
            Naming.rebind(userUri, authService);
            Naming.rebind(fileUri, fileService);

            System.out.println("Servicios desplegados con éxito.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
