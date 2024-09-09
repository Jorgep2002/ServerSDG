package org.example.shared.RMIInterfaces;

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileService extends Remote {
    // Método para subir un archivo al servidor
    void uploadFile(String filename, byte[] fileData) throws RemoteException, IOException;
}
