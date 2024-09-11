package org.example.services;

import org.example.infrastructure.AdminDAOImpl;
import org.example.shared.RMIInterfaces.AuthService;
import org.example.shared.entities.GroupEntity;
import org.example.shared.entities.UserEntity;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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

        //Llamar al metodo createUser del DAO

            System.out.println("username = " + user.getId());
            System.out.println("password = " + user.getPassword());
            return true;

    }





    @Override
    public boolean createGroup(GroupEntity group) throws RemoteException {
        try {
            // Llamar al método createGroup del DAO para crear el grupo
            boolean isGroupCreated = adminDAO.createGroup(group);

            if (isGroupCreated) {
                // Crear la carpeta correspondiente en el sistema de archivos
                File groupDirectory = new File(baseDirectory, group.getName());
                if (!groupDirectory.exists()) {
                    if (!groupDirectory.mkdirs()) {
                        throw new IOException("No se pudo crear el directorio para el grupo: " + group.getName());
                    }
                }
            }

            // Retornar el resultado de la creación del grupo
            return isGroupCreated;

        } catch (RemoteException e) {
            System.out.println("Error al crear el grupo: " + e.getMessage());
            throw new RemoteException("Error al crear el grupo", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
}
