package org.example.infrastructure;

import org.example.data.mysqlConn;
import org.example.shared.entities.GroupEntity;
import org.example.shared.entities.UserEntity;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAOImpl {

    //Metodo para crear un usuario
    public boolean createUser(UserEntity user) throws RemoteException {
        try (Connection conn = mysqlConn.getConnection()) {
            String query = "INSERT INTO Usuario (id_usuario, usu_contrasena) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, user.getId());
                stmt.setString(2, user.getPassword());

                // Ejecutar la consulta
                int rowsAffected = stmt.executeUpdate();

                // Retorna true si se ha creado el grupo (una fila afectada)
                return rowsAffected > 0;

            }
        } catch (SQLException e) {
            System.out.println("Error al crear un uusario");
            e.printStackTrace();
            throw new RemoteException("Error al crear el usuario", e);
        }
    }

    // Método para verificar si un usuario existe por ID
    public UserEntity getUserById(String id) throws RemoteException {
        try (Connection conn = mysqlConn.getConnection()) {
            String query = "SELECT * FROM Usuario WHERE id_usuario = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new UserEntity(
                                rs.getString("id_usuario"),
                                rs.getString("usu_contrasena")
                        );
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error al obtener el usuario por ID", e);
        }
    }


    public List<UserEntity> getAllUsers() throws RemoteException {
        try (Connection conn = mysqlConn.getConnection()) {
            String query = "SELECT * FROM Usuario";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                List<UserEntity> userList = new ArrayList<>();
                while (rs.next()) {
                    UserEntity user = new UserEntity(
                            rs.getString("id"),
                            rs.getString("password")
                    );
                    userList.add(user);
                }
                return userList;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los usuarios");
            e.printStackTrace();
            throw new RemoteException("Error al obtener todos los usuarios", e);
        }
    } // Método para obtener todos los usuarios que pertenecen a un grupo específico
    public List<UserEntity> getUsersByGroupId(int groupId) throws RemoteException {
        List<UserEntity> userList = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.usu_contrasena " +
                "FROM Usuario u " +
                "JOIN grupo_compartido gc ON u.id_usuario = gc.fk_id_usuario " +
                "WHERE gc.fk_id_grupo = ?";

        try (Connection conn = mysqlConn.getConnection(); // Usa la conexión de mysqlDatabase
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, groupId); // Establece el ID del grupo

            try (ResultSet rs = stmt.executeQuery()) {
                // Procesar el ResultSet
                while (rs.next()) {
                    UserEntity user = new UserEntity(
                            rs.getString("id_usuario"),
                            rs.getString("usu_contrasena")
                    );
                    userList.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error al obtener los usuarios del grupo", e);
        }

        return userList;
    }







    // Método para crear un grupo
    public boolean createGroup(GroupEntity group) throws RemoteException {
        String sql = "INSERT INTO grupo (grp_nombre, grp_descripcion) VALUES (?, ?)";

        try (Connection conn = mysqlConn.getConnection(); // Usa la conexión de mysqlDatabase
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Asignar valores a los campos de la consulta SQL
            stmt.setString(1, group.getName());
            stmt.setString(2, group.getDescription());

            // Ejecutar la consulta
            int rowsAffected = stmt.executeUpdate();

            // Retorna true si se ha creado el grupo (una fila afectada)
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error al crear el grupo");
            e.printStackTrace();
            throw new RemoteException("Error al crear el grupo: " + e.getMessage());
        }
    }


    // Método para verificar si un grupo existe por ID
    public GroupEntity getGroupById(int id) throws RemoteException {
        try (Connection conn = mysqlConn.getConnection()) {
            String query = "SELECT * FROM grupo WHERE id_grupo = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Crear un objeto GroupEntity a partir de los datos de la base de datos
                        return new GroupEntity(
                                rs.getInt("id_grupo"),
                                rs.getString("grp_nombre"),
                                rs.getString("grp_descripcion")
                        );
                    }
                    // Retorna null si no se encuentra el grupo
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error al obtener el grupo por ID", e);
        }
    }

    //Metodo par aobtener todos los grupos existentes
    public List<GroupEntity> getAllGroups() throws RemoteException {
        List<GroupEntity> groups = new ArrayList<>();
        String sql = "SELECT * FROM grupo";

        try (Connection conn = mysqlConn.getConnection(); // Usa la conexión de mysqlDatabase
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Procesar el ResultSet
            while (rs.next()) {
                GroupEntity group = new GroupEntity(
                        rs.getInt("id_grupo"),
                        rs.getString("grp_nombre"),
                        rs.getString("grp_descripcion")
                );
                groups.add(group);
                System.out.println(groups);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error al obtener todos los grupos", e);
        }

        return groups;
    }

    // Método para agregar un usuario a un grupo
    public boolean addUserToGroup(int groupId, String userId) throws RemoteException {
        // Verificar si el usuario existe
        UserEntity user = getUserById(userId);
        if (user == null) {
            throw new RemoteException("El usuario con ID " + userId + " no existe.");
        }

        // Verificar si el grupo existe
        GroupEntity group = getGroupById(groupId);
        if (group == null) {
            throw new RemoteException("El grupo con ID " + groupId + " no existe.");
        }

        // Agregar el usuario al grupo
        String sql = "INSERT INTO grupo_compartido (fk_id_grupo, fk_id_usuario) VALUES (?, ?)";

        try (Connection conn = mysqlConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Asignar valores a los campos de la consulta SQL
            stmt.setInt(1, groupId);
            stmt.setString(2, userId);

            // Ejecutar la consulta
            int rowsAffected = stmt.executeUpdate();

            //Devuelve true si se pudo hacer la consulta
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error al agregar el usuario al grupo: " + e.getMessage());
        }
    }

}
