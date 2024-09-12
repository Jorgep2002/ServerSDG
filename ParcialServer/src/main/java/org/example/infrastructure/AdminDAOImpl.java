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
                            rs.getString("id_usuario"),
                            rs.getString("usu_contrasena")
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
    public boolean createGroup(String grp_nombre, String grp_descripcion) throws RemoteException {
        String sql = "INSERT INTO grupo (grp_nombre, grp_descripcion) VALUES (?, ?)";

        try (Connection conn = mysqlConn.getConnection()) {
            // Crear el grupo
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, grp_nombre);
                stmt.setString(2, grp_descripcion);

                int rowsAffected = stmt.executeUpdate();

                return  rowsAffected > 0;
            }

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


    public List<GroupEntity> getGroupsByUserId(String userId) throws RemoteException {
        List<GroupEntity> groupList = new ArrayList<>();
        String sql = "SELECT g.id_grupo, g.grp_nombre, g.grp_descripcion " +
                "FROM grupo g " +
                "JOIN grupo_compartido gc ON g.id_grupo = gc.fk_id_grupo " +
                "WHERE gc.fk_id_usuario = ?";

        try (Connection conn = mysqlConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId); // Establece el ID del usuario

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    GroupEntity group = new GroupEntity(
                            rs.getInt("id_grupo"),
                            rs.getString("grp_nombre"),
                            rs.getString("grp_descripcion")
                    );
                    groupList.add(group);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error al obtener los grupos del usuario", e);
        }

        return groupList;
    }

    public GroupEntity getGroupByName(String nombre) throws RemoteException{
        try (Connection conn = mysqlConn.getConnection()) {
            String query = "SELECT * FROM grupo WHERE grp_nombre = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nombre);
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
            throw new RemoteException("Error al obtener el grupo por nombre", e);
        }
    }



    // Método para crear un nuevo directorio de tipo carpeta
    public boolean createFolder(String folderName, String ownerId, String folderPath, Integer parentFolderId, String groupName) throws RemoteException {
        // Verificar si el propietario existe
        UserEntity owner = getUserById(ownerId);
        if (owner == null) {
            throw new RemoteException("El propietario con ID " + ownerId + " no existe.");
        }

        GroupEntity group = null;
        if (groupName != null) {
            // Si el nombre del grupo no es nulo, buscar el grupo
            group = getGroupByName(groupName);

            // Verificar si el grupo existe
            if (group == null) {
                throw new RemoteException("El grupo con el nombre " + groupName + " no existe.");
            }
        }

        String sql = "INSERT INTO directorio (dir_nombre, fk_id_propietario, dir_tipo, dir_ruta, fk_id_padre, fk_id_grupo) VALUES (?, ?, 'carpeta', ?, ?, ?)";

        try (Connection conn = mysqlConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Asignar los valores a la consulta
            stmt.setString(1, folderName);
            stmt.setString(2, ownerId);
            stmt.setString(3, folderPath);

            if (parentFolderId != null) {
                stmt.setInt(4, parentFolderId);
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }

            if (group != null && group.getId() != null) {
                stmt.setInt(5, group.getId());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER); // Si no hay grupo, establece fk_id_grupo a NULL
            }

            // Ejecutar la consulta
            int rowsAffected = stmt.executeUpdate();

            // Retorna true si la carpeta se ha creado exitosamente
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error al crear la carpeta");
            e.printStackTrace();
            throw new RemoteException("Error al crear la carpeta: " + e.getMessage());
        }
    }




    public boolean login(UserEntity user) throws RemoteException {
        try (Connection conn = mysqlConn.getConnection()) {
            String query = "SELECT * FROM Usuario WHERE id_usuario = ? AND usu_contrasena = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, user.getId());
                stmt.setString(2, user.getPassword());

                // Ejecutar la consulta
                try (ResultSet rs = stmt.executeQuery()) {
                    // Verificar si el ResultSet contiene algún registro
                    if (rs.next()) {
                        // Si el resultado tiene una fila, el usuario ha sido autenticado correctamente
                        return true;
                    } else {
                        // Si no hay filas, las credenciales son incorrectas
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error en el login del usuario", e);
        }
    }

    public String getUserDirectoryPath(String userId, String folderName) throws RemoteException {
        String sql;

        if (folderName != null && !folderName.isEmpty()) {
            // Consulta para buscar el directorio específico que el usuario ha solicitado
            sql = "SELECT dir_ruta FROM directorio WHERE fk_id_propietario = ? AND dir_nombre = ? AND dir_tipo = 'carpeta'";
        } else {
            // Consulta para buscar el directorio raíz (carpeta con el nombre del usuario)
            sql = "SELECT dir_ruta FROM directorio WHERE fk_id_propietario = ? AND dir_nombre = ? AND dir_tipo = 'carpeta'";
             folderName = getUserById(userId).getId(); // Obtener el nombre del usuario si no se proporciona una carpeta específica
        }

        try (Connection conn = mysqlConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.setString(2, folderName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Si encuentra el directorio solicitado o la carpeta raíz del usuario, devuelve el path
                return rs.getString("dir_ruta");
            } else if (folderName.equals(getUserById(userId).getId())) {
                // Si no encuentra el directorio solicitado, y está verificando la carpeta raíz, lanza una excepción
                throw new RemoteException("No se encontró el directorio raíz del usuario.");
            } else {
                // Si no se encontró la carpeta especificada, vuelve a verificar por la carpeta raíz del usuario
                return getUserDirectoryPath(userId, null);
            }

        } catch (SQLException e) {
            throw new RemoteException("Error al obtener el directorio: " + e.getMessage(), e);
        }
    }




}
