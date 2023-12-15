package conections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.Savepoint;
import java.sql.DatabaseMetaData;

public class Conexion {
    //atributos

    private final String DRIVER = "org.gjt.mm.mysql.Driver";
    private final String URL = "jdbc:mysql://localhost:3306/";
    private final String BD = "tienda";
    private final String USUARIO = "root";
    private final String PASSWORD = "altocard";

    public Connection cadena;
    public static Conexion instancia;
    private Savepoint savepoint;
    private DatabaseMetaData dbmd;

    //metodos
    public Conexion() {
        this.cadena = null;
    }

    public Connection conectar() {
        try {
            Class.forName(DRIVER);
            this.cadena = DriverManager.getConnection(URL + BD, USUARIO, PASSWORD);
            // Desactivar autocommit
//            this.cadena.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(0);
        }
        return this.cadena;

    }

    public void desconectar() {
        try {
            this.cadena.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void setSavepoint() {
        try {
            savepoint = this.cadena.setSavepoint();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public Savepoint getSavepoint() {
        return savepoint;
    }

    public DatabaseMetaData getMetadata(Connection connection) {
        try {
            this.dbmd = connection.getMetaData();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return this.dbmd;
    }

    public void setAutoCommit(boolean estado) {
        try {
            this.cadena.setAutoCommit(estado);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void releaseSavepoint() {
        try {
            cadena.releaseSavepoint(savepoint);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void rollbackToSavepoint(Savepoint savepoint) {
        try {
            if (savepoint != null) {
                cadena.rollback(savepoint);
            } else {
                System.out.println("Savepoint is null. No rollback performed.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void commit() {
        try {
            if (savepoint != null) {
                cadena.commit();
            } else {
                System.out.println("Savepoint is null. No commit performed.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public synchronized static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

}