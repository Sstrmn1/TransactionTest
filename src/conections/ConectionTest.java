package conections;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;

public class ConectionTest {

    public static void main(String[] args) {
        String idProducto = "1";
        int stock = -1;
        String consultaInsert = "insert into producto (descripcion, stock, precio) values('Coca Cola', 100, 1.99)";
        String consultaDelete = "delete from producto where id = 5";
        int parametroConsulta = 5;
        String consultaUpdate = "UPDATE producto\n"
                + "SET stock = stock - ?\n"
                + "WHERE idproducto = ?";
        String consultaSelect = "select stock from producto where idproducto = " + idProducto;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda",
                    "root", "altocard");
            DatabaseMetaData dbmd = con.getMetaData();
            if (dbmd.supportsSavepoints()) {
                System.out.println("Savepoint supported by the driver and database");
                con.setAutoCommit(false);
                // Rollback a Savepoint
                Savepoint sp = con.setSavepoint("spoint");

                // Consulta Select antes de la actualización
                ResultSet rs = con.createStatement().executeQuery(consultaSelect);
                if (rs.next()) {
                    stock = rs.getInt("stock");
                    System.out.println("1 Stock actual: " + stock);
                }
                rs.close();

                // Consulta Update con PreparedStatement
                PreparedStatement updateStmt = con.prepareStatement(consultaUpdate);
                updateStmt.setInt(1, parametroConsulta); // Valor para restar al stock
                updateStmt.setString(2, idProducto); // Valor del idproducto
                updateStmt.executeUpdate();
                updateStmt.close();

                // Consulta Select después de la actualización
                rs = con.createStatement().executeQuery(consultaSelect);
                if (rs.next()) {
                    stock = rs.getInt("stock");
                    System.out.println("2 Stock actual: " + stock);
                }
                rs.close();

                con.rollback(sp);

                // Consulta Select después del rollback
                rs = con.createStatement().executeQuery(consultaSelect);
                if (rs.next()) {
                    stock = rs.getInt("stock");
                    System.out.println("3 Stock actual: " + stock);
                }
                rs.close();

                // Commit
                con.commit();
                System.out.println("done");

            } else {
                System.out.println("Savepoint not supported");
            }
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
