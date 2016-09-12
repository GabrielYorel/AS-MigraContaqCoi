/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas.clases;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import javax.swing.JProgressBar;

/**
 *
 * @author Gabriel
 */
public class ActualizarTipoCta {

    private static String controlador = "";
    private static String Servidor = "";
    private static String BaseBatos = "";
    private static String usuario = "";
    private static String contrasenia = "";
    private static int puerto = 0;
    private static String noEmpresa = "";
    private CrearConexionFBoSQL objConexion;
    private Connection conexion;
    private static Object[] seleccionados;
    private static JProgressBar jpbProgreso;

    public void ActualizarTipoCta(JProgressBar jpbProgreso, String controlador, String Servidor, int puerto, String BaseBatos,
            String usuario, String contrasenia,
            Object[] seleccionados, String noEmpresa) throws ClassNotFoundException {
        ActualizarTipoCta.controlador = controlador;
        ActualizarTipoCta.Servidor = Servidor;
        ActualizarTipoCta.puerto = puerto;
        ActualizarTipoCta.usuario = usuario;
        ActualizarTipoCta.BaseBatos = BaseBatos;
        ActualizarTipoCta.usuario = usuario;
        ActualizarTipoCta.contrasenia = contrasenia;
        ActualizarTipoCta.noEmpresa = noEmpresa;
        ActualizarTipoCta.seleccionados = seleccionados;
        ActualizarTipoCta.jpbProgreso = jpbProgreso;
    }

    public String[] getCtas(int nivel, String nomTabla) throws SQLException {
        String[] Ctas = null;
        Connection conexion2=null;
        try {
            objConexion = new CrearConexionFBoSQL(controlador, Servidor, puerto, BaseBatos, usuario, contrasenia,false);
             conexion2 = objConexion.getConexion();
            PreparedStatement stmNum = conexion.prepareStatement("Select COUNT(cta.NUM_CTA) "
                    + "from CUENTAS" + nomTabla + " cta where cta.NIVEL=?");
            stmNum.setInt(1, nivel);
            ResultSet rsNumCtas = stmNum.executeQuery();
            int numRows = 0;
            if (rsNumCtas.next()) {
                numRows = rsNumCtas.getInt(1);
            }
            rsNumCtas.close();
            stmNum.close();
            Ctas = new String[numRows];
            PreparedStatement query = conexion.prepareStatement(
                    "select cta.NUM_CTA from CUENTAS" + nomTabla + " cta where cta.NIVEL=?");
            query.setInt(1, nivel);
            ResultSet rsCtas = query.executeQuery();
            int indice = 0;
            while (rsCtas.next()) {
                Ctas[indice] = rsCtas.getString(1);
                indice++;
            }
            rsCtas.close();
            query.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        conexion2.close();
        return Ctas;
    }

    public void actualizar() throws ClassNotFoundException {
        try {
            jpbProgreso.setValue(0);
            jpbProgreso.setStringPainted(true);
            jpbProgreso.setString("Espere por favor calculando tiempo..");
            String nomTabla = "";
            for (int ind = 0; ind < seleccionados.length; ind++) {
                Banda banda = (Banda) seleccionados[ind];
                String id = banda.getId();
                nomTabla = id + noEmpresa;
                objConexion = new CrearConexionFBoSQL(controlador, Servidor, puerto, BaseBatos, usuario, contrasenia,false);
                conexion = objConexion.getConexion();
                String SQL = "Select max(cta.nivel) from CUENTAS" + nomTabla + " cta";
                Statement st = conexion.createStatement();
                ResultSet rsMaxNivel = st.executeQuery(SQL);
                int maxNivel = 0;
                if (rsMaxNivel.next()) {
                    maxNivel = rsMaxNivel.getInt(1);
                }
                rsMaxNivel.close();
                for (int i = maxNivel; i > 0; i--) {
                    String[] ctas = getCtas(i, nomTabla);
                    for (int indx = 0; indx < ctas.length; indx++) {
                        String cta = ctas[indx];

                        PreparedStatement stmt = conexion.prepareStatement("Select count(*) "
                                + "from CUENTAS" + nomTabla + " cta Where cta.CTA_PAPA=?");
                        stmt.setString(1, cta);
                        int numHijos = 0;
                        ResultSet rsNumHijos = stmt.executeQuery();
                        if (rsNumHijos.next()) {
                            numHijos = rsNumHijos.getInt(1);
                        }
                        rsNumHijos.close();
                        String sql = "";
                        if (numHijos <= 0) {
                            sql = "UPDATE CUENTAS" + nomTabla + " SET TIPO='D' WHERE NUM_CTA=?";
                        } else {
                            sql = "UPDATE CUENTAS" + nomTabla + " SET TIPO='A' WHERE NUM_CTA=?";
                        }
                        PreparedStatement query = conexion.prepareStatement(sql);
                        query.setString(1, cta);
                        query.executeUpdate();
                    }
                }
                conexion.close();
                BigDecimal cont = new BigDecimal(ind + 1);
                BigDecimal prog = (cont.divide(new BigDecimal(seleccionados.length), 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
                String p_ent = new DecimalFormat("###,###").format(prog);
                jpbProgreso.setValue(Integer.parseInt(p_ent));
                jpbProgreso.setStringPainted(true);
                jpbProgreso.setString("Re-indexando Niveles.. " + Integer.parseInt(p_ent) + " %");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
