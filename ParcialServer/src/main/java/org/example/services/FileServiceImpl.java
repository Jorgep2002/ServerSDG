package org.example.services;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

import org.example.infrastructure.AdminDAOImpl;
import org.example.infrastructure.FileDAOImpl;
import org.example.shared.RMIInterfaces.FileService;
import org.example.shared.entities.DirectorioEntity;

public class FileServiceImpl extends UnicastRemoteObject implements FileService {
    private static final long serialVersionUID = 1L;
    private final FileDAOImpl fileDAO;
    private final AdminDAOImpl adminDAO;

    public FileServiceImpl() throws RemoteException {
        super();
        this.fileDAO = new FileDAOImpl();
        this.adminDAO = new AdminDAOImpl();

    }

    @Override
    public void uploadFileToUser(String filename, byte[] fileData, String ownerId, DirectorioEntity directorio) throws RemoteException, IOException {
        try {
            // Obtener el directorio donde se encuentra el usuario
            String userDir = adminDAO.getUserDirectoryPath(ownerId, directorio.getDirNombre());

            // Formar el path completo del archivo
            String filePath = userDir + File.separator + filename;

            // Guardar el archivo en el sistema de archivos local
            File file = new File(filePath);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(fileData);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("Error al guardar el archivo en el sistema local", e);
            }

            // Crear un registro en la base de datos
            boolean recordCreated = fileDAO.createFile(filename, filePath, ownerId, directorio.getFkIdPadre());
            if (!recordCreated) {
                throw new RemoteException("Error al crear el registro del archivo en la base de datos.");
            }

        } catch (RemoteException e) {
            throw new RemoteException("Error en la operación de subida de archivos: " + e.getMessage(), e);
        }
    }


    public List<DirectorioEntity> getFilesByUser(String userId) throws RemoteException, SQLException {
        try {
            System.out.println(fileDAO.getFilesByUser(userId));
            return fileDAO.getFilesByUser(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error al traer archvios de grupo", e);
        }
    }

    public List<DirectorioEntity> getUsersFiles(String userId) throws RemoteException, SQLException {
        try {
            System.out.println(fileDAO.getUsersFiles(userId));
            return fileDAO.getUsersFiles(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error al traer los archivos del usuario", e);
        }
    }
}
