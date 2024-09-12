package org.example.infrastructure;

import org.example.data.mysqlConn;
import org.example.shared.entities.DirectorioEntity;
import org.example.shared.entities.GroupEntity;
import org.example.shared.entities.UserEntity;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileDAOImpl {

    public List<DirectorioEntity> getFilesByUser(String userId) throws SQLException {
        Connection conn = mysqlConn.getConnection();
        List<DirectorioEntity> directorioList = new ArrayList<>();

        // Consulta para obtener el ID del grupo del usuario
        String groupQuery = "SELECT fk_id_grupo FROM grupo_compartido WHERE fk_id_usuario = ?";
        Integer groupId = null; // Para almacenar el ID del grupo
        try (PreparedStatement groupStmt = conn.prepareStatement(groupQuery)) {
            groupStmt.setString(1, userId);
            ResultSet rs = groupStmt.executeQuery();

            if (rs.next()) {
                groupId = rs.getInt("fk_id_grupo");
                System.out.println("ID del grupo: " + groupId);
            } else {
                throw new SQLException("No se encontró el grupo del usuario.");
            }
        }

        // Ahora obtenemos los registros del directorio del grupo
        String fileQuery = "SELECT * FROM directorio WHERE fk_id_grupo = ?";

        try (PreparedStatement fileStmt = conn.prepareStatement(fileQuery)) {
            fileStmt.setInt(1, groupId);
            ResultSet fileRs = fileStmt.executeQuery();

            while (fileRs.next()) {
                DirectorioEntity directorio = new DirectorioEntity(
                        fileRs.getObject("id_directorio") != null ? fileRs.getInt("id_directorio") : null,
                        fileRs.getString("dir_nombre"),
                        fileRs.getString("fk_id_propietario"),
                        fileRs.getString("dir_tipo"),
                        fileRs.getString("dir_ruta"),
                        fileRs.getString("dir_extension"),
                        fileRs.getTimestamp("dir_fecha_creacion"),
                        fileRs.getTimestamp("dir_fecha_modificacion"),
                        fileRs.getObject("fk_id_grupo") != null ? fileRs.getInt("fk_id_grupo") : null,
                        fileRs.getObject("fk_id_padre") != null ? fileRs.getInt("fk_id_padre") : null
                );
                directorioList.add(directorio);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener los datos del directorio: " + e.getMessage());
        }

        return directorioList;
    }
    public List<DirectorioEntity> getUsersFiles(String userId) throws SQLException {
        Connection conn = mysqlConn.getConnection();
        List<DirectorioEntity> directorioList = new ArrayList<>();

        // Consulta para obtener los registros del directorio del usuario
        String fileQuery = "SELECT * FROM directorio WHERE fk_id_propietario = ?";

        try (PreparedStatement fileStmt = conn.prepareStatement(fileQuery)) {
            fileStmt.setString(1, userId);
            ResultSet fileRs = fileStmt.executeQuery();

            while (fileRs.next()) {
                DirectorioEntity directorio = new DirectorioEntity(
                        fileRs.getObject("id_directorio") != null ? fileRs.getInt("id_directorio") : null,
                        fileRs.getString("dir_nombre"),
                        fileRs.getString("fk_id_propietario"),
                        fileRs.getString("dir_tipo"),
                        fileRs.getString("dir_ruta"),
                        fileRs.getString("dir_extension"),
                        fileRs.getTimestamp("dir_fecha_creacion"),
                        fileRs.getTimestamp("dir_fecha_modificacion"),
                        fileRs.getObject("fk_id_grupo") != null ? fileRs.getInt("fk_id_grupo") : null,
                        fileRs.getObject("fk_id_padre") != null ? fileRs.getInt("fk_id_padre") : null
                );
                directorioList.add(directorio);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener los archivos del usuario: " + e.getMessage());
        }

        System.out.println(directorioList);
        return directorioList;
    }

    public boolean createFile(String fileName, String filePath, String ownerId, String fileExtension, Integer parentFolderId, Integer groupId) throws RemoteException {
        String sql = "INSERT INTO directorio (dir_nombre, dir_ruta, fk_id_propietario, dir_tipo, dir_extension, fk_id_padre, fk_id_grupo) " +
                "VALUES (?, ?, ?, 'archivo', ?, ?, ?)";

        try (Connection conn = mysqlConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Asignar valores a los parámetros de la consulta
            stmt.setString(1, fileName);          // Nombre del archivo
            stmt.setString(2, filePath);          // Ruta del archivo
            stmt.setString(3, ownerId);           // ID del propietario
            stmt.setString(4, fileExtension);     // Extensión del archivo

            // Manejar el ID de la carpeta padre (si no hay, insertar NULL)
            if (parentFolderId != null) {
                stmt.setInt(5, parentFolderId);   // ID de la carpeta padre
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);  // Insertar NULL si no hay carpeta padre
            }

            // Manejar el ID del grupo (si no hay, insertar NULL)
            if (groupId != null) {
                stmt.setInt(6, groupId);          // ID del grupo
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);  // Insertar NULL si no hay grupo
            }

            // Ejecutar la consulta de inserción
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Devolver true si se afectó al menos una fila

        } catch (SQLException e) {
            throw new RemoteException("Error al crear el registro del archivo: " + e.getMessage(), e);
        }
    }


    public DirectorioEntity getDirectoryByPath(String path) throws RemoteException {
        String sql = "SELECT * FROM directorio WHERE dir_ruta = ? AND dir_tipo = 'carpeta'";

        try (Connection conn = mysqlConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Asignar el valor del path a la consulta
            stmt.setString(1, path);

            // Ejecutar la consulta
            ResultSet rs = stmt.executeQuery();

            // Verificar si hay un resultado
            if (rs.next()) {
                // Crear y devolver el objeto DirectorioEntity con la información obtenida
                DirectorioEntity directorio = new DirectorioEntity(
                        rs.getInt("id_directorio"),                         // ID del directorio
                        rs.getString("dir_nombre"),                  // Nombre del directorio
                        rs.getString("fk_id_propietario"),           // ID del propietario
                        rs.getString("dir_tipo"),                    // Tipo de directorio ("archivo" o "carpeta")
                        rs.getString("dir_ruta"),                    // Ruta del directorio
                        rs.getString("dir_extension"),               // Extensión (si es archivo)
                        rs.getTimestamp("dir_fecha_creacion"),       // Fecha de creación
                        rs.getTimestamp("dir_fecha_modificacion"),   // Fecha de modificación
                        rs.getObject("fk_id_grupo") != null ? rs.getInt("fk_id_grupo") : null, // Grupo (puede ser nulo)
                        rs.getObject("fk_id_padre") != null ? rs.getInt("fk_id_padre") : null // Directorio padre (puede ser nulo)
                );

                return directorio;
            } else {
                // Si no se encuentra, lanzar una excepción
                throw new RemoteException("No se encontró un directorio con la ruta especificada: " + path);
            }

        } catch (SQLException e) {
            throw new RemoteException("Error al consultar el directorio: " + e.getMessage(), e);
        }
    }

    public List<DirectorioEntity> getAllFiles() throws SQLException {
        Connection conn = mysqlConn.getConnection();
        List<DirectorioEntity> directorioList = new ArrayList<>();

        // Consulta para obtener todos los registros de la tabla directorio
        String sql = "SELECT * FROM directorio";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DirectorioEntity directorio = new DirectorioEntity(
                        rs.getObject("id_directorio") != null ? rs.getInt("id_directorio") : null,
                        rs.getString("dir_nombre"),
                        rs.getString("fk_id_propietario"),
                        rs.getString("dir_tipo"),
                        rs.getString("dir_ruta"),
                        rs.getString("dir_extension"),
                        rs.getTimestamp("dir_fecha_creacion"),
                        rs.getTimestamp("dir_fecha_modificacion"),
                        rs.getObject("fk_id_grupo") != null ? rs.getInt("fk_id_grupo") : null,
                        rs.getObject("fk_id_padre") != null ? rs.getInt("fk_id_padre") : null
                );
                directorioList.add(directorio);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener todos los archivos y directorios: " + e.getMessage());
        }
        System.out.println(directorioList);
        return directorioList;
    }
    public List<DirectorioEntity> searchDirectories(String query) throws SQLException {
        Connection conn = mysqlConn.getConnection();
        List<DirectorioEntity> directorioList = new ArrayList<>();

        // Consulta para buscar directorios en cualquier campo basado en el criterio de búsqueda
        String sql = "SELECT * FROM directorio WHERE dir_nombre LIKE ? " +
                "OR dir_ruta LIKE ? " +
                "OR dir_extension LIKE ? " +
                "OR fk_id_propietario LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + query + "%";

            // Asignar el patrón de búsqueda a cada parámetro
            stmt.setString(1, searchPattern);  // Buscar en dir_nombre
            stmt.setString(2, searchPattern);  // Buscar en dir_ruta
            stmt.setString(3, searchPattern);  // Buscar en dir_extension
            stmt.setString(4, searchPattern);  // Buscar en fk_id_propietario

            // Ejecutar la consulta
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DirectorioEntity directorio = new DirectorioEntity(
                        rs.getObject("id_directorio") != null ? rs.getInt("id_directorio") : null,
                        rs.getString("dir_nombre"),
                        rs.getString("fk_id_propietario"),
                        rs.getString("dir_tipo"),
                        rs.getString("dir_ruta"),
                        rs.getString("dir_extension"),
                        rs.getTimestamp("dir_fecha_creacion"),
                        rs.getTimestamp("dir_fecha_modificacion"),
                        rs.getObject("fk_id_grupo") != null ? rs.getInt("fk_id_grupo") : null,
                        rs.getObject("fk_id_padre") != null ? rs.getInt("fk_id_padre") : null
                );
                directorioList.add(directorio);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar directorios: " + e.getMessage());
        }

        return directorioList;
    }
}
