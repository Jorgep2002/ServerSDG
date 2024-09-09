package org.example.shared.RMIInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthService extends Remote {
    boolean login(String username, String password) throws RemoteException;
}
