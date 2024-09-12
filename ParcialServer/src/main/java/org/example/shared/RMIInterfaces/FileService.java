package org.example.shared.RMIInterfaces;

import org.example.shared.entities.DirectorioEntity;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface FileService extends Remote {
    // Método para subir un archivo al servidor
    void uploadFileToUser(String filename, byte[] fileData, String ownerId, String directorio) throws RemoteException, IOException;
    List<DirectorioEntity> getFilesByUser(String userId) throws RemoteException, SQLException;
    List<DirectorioEntity> getUsersFiles(String userId) throws RemoteException, SQLException;
    List<DirectorioEntity> getALLFiles() throws RemoteException, SQLException;
    List<DirectorioEntity> searchDirectories(String query) throws RemoteException, SQLException;

}
