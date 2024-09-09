package org.example.services;

import org.example.shared.RMIInterfaces.AuthService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class AuthServiceImpl extends UnicastRemoteObject implements AuthService {


    public AuthServiceImpl() throws RemoteException {
        super();
    }


    @Override
    public boolean login(String username, String password) throws RemoteException {
        System.out.println("username = " + username);
        System.out.println("password = " + password);
        return true;
    }
}
