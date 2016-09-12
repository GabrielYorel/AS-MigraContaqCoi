/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas.clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class CrearConexionFBoSQL {

    private static String controlador = "";
    private static String Servidor = "";
    private static String BaseBatos = "";
    private static String usuario = "";
    private static String contrasenia = "";
    private static int puerto = 0;
    boolean integratedSecurity = false;

    /**
     * Inicia una conexión a Base de Datos Microsoft SQL Server o Bases de datos
     * de FireBird 2.5.
     *
     * @param controlador Especifica el tipo de conexión a establecer SQL Server
     * o FireBird
     * @param servidor Servidor al cual se establece la conexion (nombre o
     * dirección IP).
     * @param puerto Puerto a traves del cual se establece la conexión con la
     * base de datos
     * @param BD Nombre o ruta de la Base de Datos, De acuerdo al controlador
     * elegido
     * @param usuario Usuario que inicia la conexion.
     * @param contrasenya Contraseña del usuario que inicia la conexion.
     *
     * Base de Datos.
     *
     * @return El objeto que contiene la conexion a la Base de Datos.
     */
    public CrearConexionFBoSQL(String controlador, String Servidor, int puerto, String BaseBatos, String usuario, String contrasenia, boolean integratedSecurity) {
        CrearConexionFBoSQL.controlador = controlador;
        CrearConexionFBoSQL.Servidor = Servidor;
        CrearConexionFBoSQL.puerto = puerto;
        CrearConexionFBoSQL.usuario = usuario;
        CrearConexionFBoSQL.BaseBatos = BaseBatos;
        CrearConexionFBoSQL.usuario = usuario;
        CrearConexionFBoSQL.contrasenia = contrasenia;
        this.integratedSecurity = integratedSecurity;
    }

    public Connection getConexion() throws ClassNotFoundException {
        Connection con = null;
        if (controlador.equals("DevartSQLServer")) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                if (integratedSecurity) {
                    con = DriverManager.getConnection("jdbc:sqlserver://" + Servidor + ":" + puerto + ";databaseName=" + BaseBatos + ";integratedSecurity=true");
                } else {
                    String connectionUrl = "jdbc:sqlserver://" + Servidor + ":" + puerto + ";"
                            + "databaseName=" + BaseBatos + ";user=" + usuario + ";password=" + contrasenia;
//                 Establish the connection.
                    con = DriverManager.getConnection(connectionUrl);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            try {
                Class.forName("org.firebirdsql.jdbc.FBDriver");
                Properties props = new Properties();
                props.setProperty("user", usuario);
                props.setProperty("password", contrasenia);
                props.setProperty("encoding", "UNICODE_FSS");
                con = DriverManager.getConnection(
                        "jdbc:firebirdsql:" + Servidor + "/" + puerto + ":" + BaseBatos, props);
//                    "jdbc:firebirdsql:+"+IPServer+"/3050:" + rutaArchivo, props);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return con;
    }
}
