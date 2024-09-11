package org.example.shared.RMIInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import org.example.shared.entities.GroupEntity;
import org.example.shared.entities.UserEntity;

public interface AuthService extends Remote {
    boolean login(UserEntity user) throws RemoteException;
    boolean createGroup(GroupEntity group) throws RemoteException;
    boolean addUserToGroup(int groudId,String userId) throws RemoteException;
    List<GroupEntity> getAllGroups() throws RemoteException;
    List<UserEntity> getUsersByGroupId(int groupId) throws RemoteException;
}