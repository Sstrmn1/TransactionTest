package conections;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ConectionTest {

    public static void main(String[] args) {
        String idProducto = "1";
        int stock = -1;
        String consultaInsert = "insert into producto (descripcion, stock, precio)values('Coca Cola', 100, 1.99)";
        String consultaDelete = "delete from producto where id = 5";
        String consultaUpdate = "UPDATE producto\n"
                + "SET stock = stock - 5\n"
                + "WHERE idproducto = 1";
        String consultaSelect = "select stock from producto where idproducto = " + idProducto;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda",
                    "root", "altocard");
            DatabaseMetaData dbmd = con.getMetaData();
            if (dbmd.supportsSavepoints()) {
                System.out.println("Savepoint supported by the driver and database");
                con.setAutoCommit(false);
                Statement stmt = con.createStatement();
//                stmt.executeUpdate(consultaInsert);
                stmt.executeUpdate(consultaSelect);
                Savepoint sp = con.setSavepoint("spoint");
                stmt.executeUpdate(consultaUpdate);
                System.out.println(stock);
                con.rollback(sp);
                System.out.println(stock);
                con.commit();
                System.out.println("done");
                stmt.close();
            } else {
                System.out.println("Savepoint not supported");
            }
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
