/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas.clases;

import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gabriel-Diaz
 */
public class HiloCargarCatCtas extends Thread {

    private JTable jtblCatCtas;
    String[][] tiposCtas;
    private JTextField jtxtNumRegistros;
    private JProgressBar jpbProgreso;
    private DefaultTableModel modelonuevo;
    private String servidor;
    private int puerto;
    private String controlador, usuario, contrasenia, baseDatos;
    private Connection conexionContpaqi;
    private JButton jbtnExportar;
    private JButton jbtnTransferir;
    private CrearConexionFBoSQL objConexion;
    private int[] dxNiveles;
    private boolean seguridadIntegrada;

    public HiloCargarCatCtas( int[] dxNiveles, JButton jbtnTransferir, String[][] tiposCtas, JButton jbtnExportar, JTable jtblCatCtas, JTextField jtxtNumRegistros, JProgressBar jpbProgreso,
            DefaultTableModel modelonuevo, String controlador, String servidor, int puerto, String usuario, String contrasenia, String baseDatos,boolean seguridadIntegrada) {
        super("hiloCargarCatCtas");
        this.seguridadIntegrada = seguridadIntegrada;
        this.dxNiveles = dxNiveles;
        this.jbtnTransferir = jbtnTransferir;
        this.jbtnExportar = jbtnExportar;
        this.jtblCatCtas = jtblCatCtas;
        this.jtxtNumRegistros = jtxtNumRegistros;
        this.jpbProgreso = jpbProgreso;
        this.modelonuevo = modelonuevo;
        this.servidor = servidor;
        this.puerto = puerto;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.baseDatos = baseDatos;
        this.tiposCtas = tiposCtas;
        this.controlador = controlador;
    }

    @Override
    public void run() {
        try {
            objConexion = new CrearConexionFBoSQL(controlador, servidor, puerto, baseDatos, usuario, contrasenia,seguridadIntegrada);
            conexionContpaqi = objConexion.getConexion();
            if (conexionContpaqi == null) {
                JOptionPane.showMessageDialog(null, "No se pudo realizar la conexión a la base de datos", "Erro en la configuracón", JOptionPane.ERROR_MESSAGE);
            } else {
                Statement stmt = conexionContpaqi.createStatement();
                ResultSet rsCountRows = stmt.executeQuery("SELECT COUNT(Id) FROM dbo.Cuentas");
                int numRows = 0;
                if (rsCountRows.next()) {
                    numRows = rsCountRows.getInt(1);
                }
                rsCountRows.close();
                ResultSet rs = stmt.executeQuery("SELECT Id,Codigo,Nombre,Tipo FROM dbo.Cuentas");
                int indice = 0;
                int logCta = 0;
                for (int i = 0; i < dxNiveles.length; i++) {
                    logCta += dxNiveles[i];
                }
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String codigo = rs.getString(2);
                    String descr = rs.getString(3);
                    String tipoContaqAbr = rs.getString(4);
                    String tipoYdescrContpaq = "";
                    String tipoyDescrCOI = "";
                    for (int i = 0; i < tiposCtas.length; i++) {
                        String tipoContpaqArr = tiposCtas[i][0];
                        if (tipoContpaqArr.equals(tipoContaqAbr)) {
                            tipoYdescrContpaq = tipoContaqAbr + " (" + tiposCtas[i][1] + ")";
                            tipoyDescrCOI = tiposCtas[i][2] + " (" + tiposCtas[i][3] + ")";
                        }
                    }
                    if (!codigo.startsWith("_") && !codigo.startsWith("0")) {
                        String ctaTemp = ponerGuines(codigo, logCta);
                        modelonuevo.addRow(new Object[]{id, ctaTemp.substring(0, ctaTemp.length() - 1), descr, "N", tipoYdescrContpaq, tipoyDescrCOI});
                    }
                    BigDecimal cont = new BigDecimal(indice + 1);
                    BigDecimal prog = (cont.divide(new BigDecimal(numRows), 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
                    String p_ent = new DecimalFormat("###,###").format(prog);
                    jpbProgreso.setValue(Integer.parseInt(p_ent));
                    jpbProgreso.setStringPainted(true);
                    jpbProgreso.setString("Progreso " + Integer.parseInt(p_ent) + " %");
                    indice++;
                }
                rs.close();
                jtblCatCtas.setModel(modelonuevo);
                jtblCatCtas.getRowSorter().toggleSortOrder(1);
                jpbProgreso.setValue(100);
                jpbProgreso.setString(" ** Lectura finalizada**");
                jtxtNumRegistros.setText(jtblCatCtas.getRowCount() + "");
                if (jtblCatCtas.getRowCount() > 0) {
                    jbtnExportar.setEnabled(true);
                        jbtnTransferir.setEnabled(true);
                }
            }
            this.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "error " + ex);
        }
    }

    public static String completaCeros(String folio, int longi) {
        int longFolio = longi;
        String folioReal = (folio + "");
        while (folioReal.length() < longFolio) {
            folioReal = folioReal + "0";
        }
        return folioReal;
    }

    public String ponerGuines(String cuenta, int longi) {
        String nvaCuenta = "";
        try {            //           
            String nvaCuentatTemp = completaCeros(cuenta, longi);
            int cont = 0;
            for (int i = 0; i < dxNiveles.length; i++) {
                if (dxNiveles[i] != 0) {
                    nvaCuenta += nvaCuentatTemp.substring(cont, (dxNiveles[i] + cont)) + "-";
                    cont += dxNiveles[i];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nvaCuenta.substring(0, nvaCuenta.length());
    }
    public Connection getConexionContpaq(){
        
        return conexionContpaqi;
    }
}
