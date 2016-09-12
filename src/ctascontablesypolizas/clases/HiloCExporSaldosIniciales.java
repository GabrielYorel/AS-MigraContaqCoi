/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas.clases;

import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import javax.swing.*;

/**
 *
 * @author Gabriel-Diaz
 */
public class HiloCExporSaldosIniciales extends Thread {

    String[][] tiposCtas;
    private JProgressBar jpbProgreso;
    private String servidor;
    private int puerto;
    private String controlador, usuario, contrasenia, baseDatos;
    private Connection conexionCOI;
    private JButton jbtnTransferir, btnverregistros;
    private CrearConexionFBoSQL objConexion;
    private JTable tableSaldos;
    private EjercicioEntity ejercicio;
    String noEmp;

    public HiloCExporSaldosIniciales(EjercicioEntity ejercicio, JTable tablaSaldos, String noEmp, JButton btnverregistros, JButton jbtnTransferir, JProgressBar jpbProgreso,
            String controlador, String servidor, int puerto, String usuario, String contrasenia, String baseDatos) {
        super("hiloCargarCatCtas");
        this.jbtnTransferir = jbtnTransferir;
        this.jpbProgreso = jpbProgreso;
        this.servidor = servidor;
        this.puerto = puerto;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.baseDatos = baseDatos;
        this.controlador = controlador;
        this.noEmp = noEmp;
        this.tableSaldos = tablaSaldos;
        this.btnverregistros = btnverregistros;
        this.ejercicio = ejercicio;
    }

    @Override
    public void run() {
        try {
            btnverregistros.setEnabled(false);
            jbtnTransferir.setEnabled(false);
            objConexion = new CrearConexionFBoSQL(controlador, servidor, puerto, baseDatos, usuario, contrasenia,false);
            conexionCOI = objConexion.getConexion();
            try {
                int indice = 0;
                int cuentasAfectadas = 0;
                String cuentasNoAfectadas = "";
                for (int i = 0; i < tableSaldos.getRowCount(); i++) {//se recorren las polizas
                    SaldosCuentasEntity saldoCuenta = (SaldosCuentasEntity) tableSaldos.getValueAt(i, 0);
                    String ejer = String.valueOf(ejercicio.getNombre()).substring(2, String.valueOf(ejercicio.getNombre()).length());
                    String sql = "";
                    if (controlador.equals("DevartSQLServer")) {
                        sql = "select NUM_CTA "
                                + "from CUENTAS" + ejer + noEmp + " WHERE substring(NUM_CTA,1," + saldoCuenta.getCodigo().length() + ") =" + saldoCuenta.getCodigo();
                    } else {
                        sql = "select NUM_CTA "
                                + "from CUENTAS" + ejer + noEmp + " WHERE substring(NUM_CTA from 1 for " + saldoCuenta.getCodigo().length() + ") =" + saldoCuenta.getCodigo();
                    }
                    PreparedStatement stmtCta = conexionCOI.prepareStatement(sql);
                    ResultSet rsCta = stmtCta.executeQuery();
                    String ctaConCeros = "";
                    if (rsCta.next()) {
                        ctaConCeros = rsCta.getString(1);
                    } else {
                        System.out.println(saldoCuenta.getCodigo() + " " + saldoCuenta.getNombre());
                    }
                    rsCta.close();
                    stmtCta.close();
                    if (!ctaConCeros.equalsIgnoreCase("")) {
                        String sqlPartidasPoliza = "UPDATE SALDOS" + ejer + noEmp + " SET INICIAL=? WHERE NUM_CTA=? AND EJERCICIO=?";
                        PreparedStatement queryPartPoliza = conexionCOI.prepareStatement(sqlPartidasPoliza);
                        queryPartPoliza.setBigDecimal(1, saldoCuenta.getSaldoIni());
                        queryPartPoliza.setString(2, ctaConCeros);
                        queryPartPoliza.setInt(3, Integer.parseInt(ejercicio.getNombre()));
                        queryPartPoliza.execute();
                        queryPartPoliza.close();//cerramos el statement
                        cuentasAfectadas++;
                    } else {
                        cuentasNoAfectadas += "\n" + saldoCuenta.getCodigo() + " " + saldoCuenta.getNombre();
                    }
                    BigDecimal cont = new BigDecimal(indice + 1);
                    BigDecimal prog = (cont.divide(new BigDecimal(tableSaldos.getRowCount()), 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
                    String p_ent = new DecimalFormat("###,###").format(prog);
                    jpbProgreso.setValue(Integer.parseInt(p_ent));
                    jpbProgreso.setStringPainted(true);
                    jpbProgreso.setString("Actualizando saldo " + Integer.parseInt(p_ent) + " %");
                    indice++;
                }

                jbtnTransferir.setEnabled(true);
                btnverregistros.setEnabled(true);
                jpbProgreso.setValue(100);
                jpbProgreso.setString(" ** Exportación finalizada**");
                conexionCOI.close();
                JOptionPane.showMessageDialog(null, "Cuentas no afectadas\n" + cuentasNoAfectadas, "Cuentas sin afectar", JOptionPane.WARNING_MESSAGE);
                JOptionPane.showMessageDialog(null, "Los datos de actualizarón correctamente\n" + cuentasAfectadas + " Cuentas afectadas de " + tableSaldos.getRowCount(), "Exito al guardar", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e);
            }
            this.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "error " + ex);
        }
    }

    public String completaCeros(String folio, int longFolio) {
        String folioReal = (folio + "");
        while (folioReal.length() < longFolio) {
            folioReal = " " + folioReal;
        }
        return folioReal;
    }
}
