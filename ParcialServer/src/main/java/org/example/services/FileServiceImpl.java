package org.example.services;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import org.example.shared.RMIInterfaces.FileService;
import org.example.shared.entities.GroupEntity;

public class FileServiceImpl extends UnicastRemoteObject implements FileService {
    private static final long serialVersionUID = 1L;

    public FileServiceImpl() throws RemoteException {
        super();
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

}
