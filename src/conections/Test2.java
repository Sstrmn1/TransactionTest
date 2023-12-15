package conections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test2 {

    public static void main(String[] args) {
        String idProducto = "1";
        int stock = -1;
        int parametroConsulta = 5;

        // Crear instancia de la clase Conexion
        Conexion conexion = Conexion.getInstancia();

        try {
            // Conectar a la base de datos
            conexion.conectar();

            // Verificar si Savepoints son compatibles
            if (conexion.getMetadata(conexion.cadena).supportsSavepoints()) {
                System.out.println("Savepoint supported by the driver and database");

                // Desactivar el modo autocommit
                conexion.setAutoCommit(false);

                // Crear un Savepoint
                conexion.setSavepoint();

                // Consulta Select antes de la actualización
                ResultSet rs = conexion.cadena.createStatement().executeQuery(
                        "select stock from producto where idproducto = " + idProducto);

                if (rs.next()) {
                    stock = rs.getInt("stock");
                    System.out.println("1 Stock actual: " + stock);
                }
                rs.close();

                // Consulta Update con PreparedStatement
                PreparedStatement updateStmt = conexion.cadena.prepareStatement(
                        "UPDATE producto SET stock = stock - ? WHERE idproducto = ?");
                updateStmt.setInt(1, parametroConsulta);
                updateStmt.setString(2, idProducto);
                updateStmt.executeUpdate();
                updateStmt.close();

                // Consulta Select después de la actualización
                rs = conexion.cadena.createStatement().executeQuery(
                        "select stock from producto where idproducto = " + idProducto);

                if (rs.next()) {
                    stock = rs.getInt("stock");
                    System.out.println("2 Stock actual: " + stock);
                }
                rs.close();

                // Rollback a Savepoint
                conexion.rollbackToSavepoint(conexion.getSavepoint());

                // Consulta Select después del rollback
                rs = conexion.cadena.createStatement().executeQuery(
                        "select stock from producto where idproducto = " + idProducto);

                if (rs.next()) {
                    stock = rs.getInt("stock");
                    System.out.println("3 Stock actual: " + stock);
                }
                rs.close();

                // Confirmar los cambios
                conexion.commit();
                System.out.println("Done");

            } else {
                System.out.println("Savepoint not supported");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // Desconectar siempre, ya sea que ocurra una excepción o no
            conexion.desconectar();
        }
    }
}
