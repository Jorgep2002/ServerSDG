package org.example.services;

import org.example.infrastructure.AdminDAOImpl;
import org.example.shared.RMIInterfaces.AuthService;
import org.example.shared.entities.GroupEntity;
import org.example.shared.entities.UserEntity;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class AuthServiceImpl extends UnicastRemoteObject implements AuthService {

    private final AdminDAOImpl adminDAO;
    private final String baseDirectory;

    public AuthServiceImpl(String baseDirectory) throws RemoteException {
        super();
        this.adminDAO = new AdminDAOImpl();
        this.baseDirectory = baseDirectory;
    }


    @Override
    public boolean login(UserEntity user) throws RemoteException {

        try{
            return adminDAO.login(user);
        }catch (RemoteException e){
            System.out.println("Error al loguearse: " + e.getMessage());
            throw new RemoteException("Error al iniciar sesion", e);
        }
    }





    @Override
    public boolean createGroup(String nombre, String descripcion, UserEntity user) throws RemoteException {
        // Llamar al método createGroup del DAO para crear el grupo en la base de datos
        boolean isGroupCreated = adminDAO.createGroup(nombre, descripcion);


        if (isGroupCreated) {
            // Crear la carpeta correspondiente en el sistema de archivos
            File groupDirectory = new File(baseDirectory, nombre);
            if (!groupDirectory.exists()) {
                boolean isDirectoryCreated = groupDirectory.mkdirs(); // Crear la carpeta

                if (isDirectoryCreated) {
                    System.out.println("Carpeta del grupo creada: " + groupDirectory.getAbsolutePath());

                    // Guardar el directorio en la base de datos usando el método createFolder
                    boolean isFolderSaved = adminDAO.createFolder(
                            nombre,
                            user.getId(),
                            groupDirectory.getAbsolutePath(),
                            null,
                            nombre
                    );

                    if (isFolderSaved) {
                        return true; // El grupo y la carpeta fueron creados y guardados exitosamente
                    } else {
                        System.out.println("Error al guardar el directorio del grupo en la base de datos.");
                        return false; // Error al guardar el directorio
                    }
                } else {
                    System.out.println("Error al crear la carpeta para el grupo.");
                    return false; // Error al crear la carpeta
                }
            } else {
                System.out.println("La carpeta del grupo ya existe: " + groupDirectory.getAbsolutePath());
                return true; // La carpeta ya existía, pero el grupo fue creado
            }
        } else {
            System.out.println("Error al crear el grupo en la base de datos.");
            return false;
        }
    }


    @Override
    public boolean addUserToGroup(int groudId, String userId) throws RemoteException {
        try{

            return adminDAO.addUserToGroup(groudId, userId);

        }catch (RemoteException e){
            System.out.println("Error al agregar un usuario al grupo: " + e.getMessage());
            throw new RemoteException("Error al agregar un usuario el grupo", e);
        }
    }

    @Override
    public List<GroupEntity> getAllGroups() throws RemoteException {

        try{

            List<GroupEntity> groups = adminDAO.getAllGroups();

            return groups;

        }catch (RemoteException e){
            System.out.println("Error al obtener todos los grupos: " + e.getMessage());
            throw new RemoteException("Error al obtener todos los grupos", e);
        }

    }



    @Override
    public List<UserEntity> getUsersByGroupId(int groupId) throws RemoteException {
        try{

            List<UserEntity> usersByGroup = adminDAO.getUsersByGroupId(groupId);

            return usersByGroup;

        }catch (RemoteException e){
            System.out.println("Error al obtener todos los grupos: " + e.getMessage());
            throw new RemoteException("Error al obtener todos los grupos", e);
        }
    }

    public List<UserEntity> getAllUsers() throws RemoteException {

        try{

            List<UserEntity> users = adminDAO.getAllUsers();

            return users;

        }catch (RemoteException e){
            System.out.println("Error al obtener todos los usuarios: " + e.getMessage());
            throw new RemoteException("Error al obtener todos los usuarios", e);
        }

    }
    public List<GroupEntity> getGroupsByUserId(String userId) throws RemoteException {
        try {
            System.out.println(adminDAO.getGroupsByUserId(userId));
            return adminDAO.getGroupsByUserId(userId);
        } catch (RemoteException e) {
            System.out.println("Error al obtener los grupos del usuario: " + e.getMessage());
            throw new RemoteException("Error al obtener los grupos del usuario", e);
        }
    }
    public boolean createUser(UserEntity user) throws RemoteException{
        try {;
            boolean userCreated =  adminDAO.createUser(user);

            if (userCreated) {
                // Crear la carpeta para el usuario en el sistema de archivos local
                File userDirectory = new File(baseDirectory, user.getId());
                if (!userDirectory.exists()) {
                    boolean isDirectoryCreated = userDirectory.mkdirs(); // Crear la carpeta

                    if (isDirectoryCreated) {
                        System.out.println("Carpeta del usuario creada: " + userDirectory.getAbsolutePath());

                        // Guardar el directorio en la base de datos
                        boolean isFolderSaved = adminDAO.createFolder(
                                user.getId(),
                                user.getId(),
                                userDirectory.getAbsolutePath(),
                                null,
                                null
                        );

                        if (isFolderSaved) {
                            return true; // El usuario y la carpeta fueron creados y guardados exitosamente
                        } else {
                            System.out.println("Error al guardar el directorio del usuario en la base de datos.");
                            return false; // Error al guardar el directorio
                        }
                    } else {
                        System.out.println("Error al crear la carpeta para el usuario.");
                        return false; // Error al crear la carpeta
                    }
                } else {
                    System.out.println("La carpeta del usuario ya existe: " + userDirectory.getAbsolutePath());
                    return true; // La carpeta ya existía, pero el usuario fue creado
                }
            } else {
                return false; // No se pudo crear el usuario
            }

        } catch (RemoteException e) {
            System.out.println("Error al crear usuario: " + e.getMessage());
            throw new RemoteException("Error al crear usuario", e);
        }
    }

}
