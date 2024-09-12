package org.example.services;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

import org.example.infrastructure.FileDAOImpl;
import org.example.shared.RMIInterfaces.FileService;

public class FileServiceImpl extends UnicastRemoteObject implements FileService {
    private static final long serialVersionUID = 1L;
    private final FileDAOImpl fileDAO;

    public FileServiceImpl() throws RemoteException {
        super();
        this.fileDAO = new FileDAOImpl();

    }

    @Override
    public void uploadFile(String filename, byte[] fileData) throws RemoteException, IOException {
        // Ruta específica en el escritorio
        String filePath = "C:\\Escritorio\\PruebaSubida\\" + filename;

        // Crear el archivo en la ubicación especificada
        File file = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileData);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error saving file to the specified path", e);
        }
    }
    public List<String> getFilesByUser(String userId) throws RemoteException, SQLException {
        try {
            System.out.println(fileDAO.getFilesByUser(userId));
            return fileDAO.getFilesByUser(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error retrieving files from the database", e);
        }
    }
}
