package org.example.infrastructure;

import org.example.data.mysqlConn;
import org.example.shared.entities.GroupEntity;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GroupDAOImpl {

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
}
