package org.example.services;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public void uploadFileToUser(String filename, byte[] fileData, String ownerId, String directorio) throws RemoteException, IOException {
        try {


            // Formar el path completo del archivo
            String filePath = directorio + File.separator + filename;
            String filePathDB = directorio + "/" + filename;

            // Guardar el archivo en el sistema de archivos local
            File file = new File(filePath);
            String fileName = file.getName(); // Obtener el nombre del archivo con la extensión

            // Extraer la extensión del archivo
            String fileExtension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                fileExtension = fileName.substring(dotIndex + 1);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(fileData);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("Error al guardar el archivo en el sistema local", e);
            }

            DirectorioEntity directorio1 = fileDAO.getDirectoryByPath(directorio);
            // Crear un registro en la base de datos

            boolean recordCreated = fileDAO.createFile(filename, filePathDB, ownerId, fileExtension, directorio1.getFkIdPadre(),  null);
            if (!recordCreated) {
                throw new RemoteException("Error al crear el registro del archivo en la base de datos.");
            }

        } catch (RemoteException e) {
            throw new RemoteException("Error en la operación de subida de archivos: " + e.getMessage(), e);
        }
    }


    public List<DirectorioEntity> getFilesByUser(String userId) throws RemoteException {
        try {
            System.out.println(fileDAO.getFilesByUser(userId));
            return fileDAO.getFilesByUser(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            // Retornar una lista vacía en lugar de lanzar una excepción
            return new ArrayList<>();
        }
    }
    public List<DirectorioEntity> getUsersFiles(String userId) throws RemoteException {
        try {
            System.out.println(fileDAO.getUsersFiles(userId));
            return fileDAO.getUsersFiles(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            // Retornar una lista vacía en lugar de lanzar una excepción
            return new ArrayList<>();
        }
    }

    @Override
    public List<DirectorioEntity> getALLFiles() throws RemoteException, SQLException {
        try {
            System.out.println("getAllFiles");
            System.out.println(fileDAO.getAllFiles());
            return fileDAO.getAllFiles();
        } catch (SQLException e) {
            e.printStackTrace();

            return new ArrayList<>();
        }
    }
    public List<DirectorioEntity> searchDirectories(String query) throws RemoteException {
        try {
            System.out.println(fileDAO.searchDirectories(query));
            return fileDAO.searchDirectories(query);
        } catch (SQLException e) {
            e.printStackTrace();
            // Retornar una lista vacía en lugar de lanzar una excepción
            return new ArrayList<>();
        }
    }

}
