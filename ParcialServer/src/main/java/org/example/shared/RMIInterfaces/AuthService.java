package org.example.shared.RMIInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import org.example.shared.entities.GroupEntity;

public interface AuthService extends Remote {
    boolean login(String username, String password) throws RemoteException;
    boolean createGroup(GroupEntity group) throws RemoteException;
}