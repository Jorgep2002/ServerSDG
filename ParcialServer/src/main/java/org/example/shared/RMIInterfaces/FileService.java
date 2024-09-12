package org.example.shared.RMIInterfaces;

import org.example.shared.entities.DirectorioEntity;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface FileService extends Remote {
    // Método para subir un archivo al servidor
    void uploadFile(String filename, byte[] fileData) throws RemoteException, IOException;
    List<DirectorioEntity> getFilesByUser(String userId) throws RemoteException, SQLException;
    List<DirectorioEntity> getUsersFiles(String userId) throws RemoteException, SQLException;
}
