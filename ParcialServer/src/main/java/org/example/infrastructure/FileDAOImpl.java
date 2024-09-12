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

public class FileDAOImpl {

    public List<String> getFilesByUser(String userId) throws SQLException {
        Connection conn = mysqlConn.getConnection();
        List<String> fileList = new ArrayList<>();

        // Consulta para obtener el ID del grupo del usuario
        String groupQuery = "SELECT fk_id_grupo FROM grupo_compartido WHERE fk_id_usuario = ?";
        int groupId = -1; // Para almacenar el ID del grupo
        try (PreparedStatement groupStmt = conn.prepareStatement(groupQuery)) {
            groupStmt.setString(1, userId);
            ResultSet rs = groupStmt.executeQuery();

            if (rs.next()) {
                groupId = rs.getInt("fk_id_grupo");
                System.out.println(groupId);

            } else {
                throw new SQLException("No se encontró el grupo del usuario.");
            }
        }

        // Ahora obtenemos las rutas de los archivos del grupo
        String fileQuery = "SELECT dir_ruta FROM directorio WHERE fk_id_grupo = ?";

        try (PreparedStatement fileStmt = conn.prepareStatement(fileQuery)) {
            fileStmt.setInt(1, groupId);
            ResultSet fileRs = fileStmt.executeQuery();

            while (fileRs.next()) {
                fileList.add(fileRs.getString("dir_ruta"));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener las rutas de los archivos: " + e.getMessage());
        }

        return fileList;
    }


}
