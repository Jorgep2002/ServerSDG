package org.example.services;

import org.example.infrastructure.GroupDAOImpl;
import org.example.shared.RMIInterfaces.AuthService;
import org.example.shared.entities.GroupEntity;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class AuthServiceImpl extends UnicastRemoteObject implements AuthService {

    private final GroupDAOImpl groupDAO;

    public AuthServiceImpl() throws RemoteException {
        super();
        this.groupDAO = new GroupDAOImpl();
    }


    @Override
    public boolean login(String username, String password) throws RemoteException {
        System.out.println("username = " + username);
        System.out.println("password = " + password);
        return true;
    }

    @Override
    public boolean createGroup(GroupEntity group) throws RemoteException {
        try {
            // Llamar al método createGroup del DAO para crear el grupo
            boolean isGroupCreated = groupDAO.createGroup(group);

            // Retornar el resultado de la creación del grupo
            return isGroupCreated;

        } catch (RemoteException e) {
            System.out.println("Error al crear el grupo: " + e.getMessage());
            throw new RemoteException("Error al crear el grupo", e);
        }
    }
}
