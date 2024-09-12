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


}
