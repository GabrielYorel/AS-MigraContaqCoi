/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas.Datos;

import ctascontablesypolizas.clases.*;
import ctascontablesypolizas.frmImportarSaldosIniciales;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gabriel-Diaz
 */
public class HiloCargarSaldosCuentas extends Thread {

    private JTable jtblSaldosInictas;
    String[][] tiposCtas;
    private JTextField jtxtNumRegistros;
    private JProgressBar jpbProgreso;
    private DefaultTableModel modelo;
    private String servidor;
    private int puerto;
    private String controlador, usuario, contrasenia, baseDatos;
    private Connection conexionContpaqi;
    private CrearConexionFBoSQL objConexion;
    public frmImportarSaldosIniciales padre;
    private JButton btnverRegistros;
    private JButton jbtnTransferir;
    private Boolean primerEjercicio;
    private EjercicioEntity ejercicio;
    private boolean seguridadIntegrada;

    public HiloCargarSaldosCuentas(JButton jbtnTransferir, Boolean primerEjercicio, JButton btnverRegistros, EjercicioEntity ejercicio, JTable jtblSaldosInictas, JProgressBar jpbProgreso, DefaultTableModel modelonuevo, String controlador,
            String servidor, int puerto, String usuario, String contrasenia, String baseDatos,boolean seguridadIntegrada) {
        super("hilo cargar saldos cuentas");
        this.seguridadIntegrada = seguridadIntegrada;
        this.jtblSaldosInictas = jtblSaldosInictas;
        this.jpbProgreso = jpbProgreso;
        this.modelo = modelonuevo;
        this.servidor = servidor;
        this.puerto = puerto;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.baseDatos = baseDatos;
        this.controlador = controlador;
        this.btnverRegistros = btnverRegistros;
        this.primerEjercicio = primerEjercicio;
        this.ejercicio = ejercicio;
        this.jbtnTransferir = jbtnTransferir;
    }

    @Override
    public void run() {
        try {
            btnverRegistros.setEnabled(false);
            btnverRegistros.setEnabled(false);
            jbtnTransferir.setEnabled(false);
            objConexion = new CrearConexionFBoSQL(controlador, servidor, puerto, baseDatos, usuario, contrasenia,seguridadIntegrada);
            conexionContpaqi = objConexion.getConexion();
            Statement stmt = conexionContpaqi.createStatement();
            String addWhere = "";
            if (primerEjercicio) {
                addWhere = " AND e.Id = (Select MIN(ej.Id) from Ejercicios ej)";
            } else {
                addWhere = " AND e.Id = " + ejercicio.getId();
            }
            String sql = "Select e.Ejercicio,c.Id,c.Codigo,c.Nombre,sc.SaldoIni from SaldosCuentas sc\n"
                    + "INNER JOIN Cuentas c on(c.Id = sc.IdCuenta)\n "
                    + "INNER JOIN Ejercicios e on (e.Id = sc.Ejercicio)\n "
                    + "Where sc.SaldoIni <> 0 AND sc.Tipo=1 AND c.Id > 12\n "
                    + addWhere
                    + " Order by c.Id; ";
            ResultSet RstRegPolizas = stmt.executeQuery(sql);
            jpbProgreso.setIndeterminate(true);
            int cuentas = 0;
            while (RstRegPolizas.next()) {
                SaldosCuentasEntity saldoCta = new SaldosCuentasEntity(RstRegPolizas.getString(3), RstRegPolizas.getString(4), RstRegPolizas.getBigDecimal(5));
                modelo.addRow(new Object[]{saldoCta, saldoCta.getNombre(), saldoCta.getSaldoIni()});
                jpbProgreso.setStringPainted(true);
                jpbProgreso.setString("Cargando cuentas y saldos iniciales... ");
                cuentas++;
            }
            RstRegPolizas.close();
            jpbProgreso.setIndeterminate(false);
            jpbProgreso.setValue(100);
            jpbProgreso.setString("Proceso terminado...");
            btnverRegistros.setEnabled(true);
            JOptionPane.showMessageDialog(null, "Los datos de cargaron correctamente\n" + cuentas + " Cuentas cargadas", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            if (jtblSaldosInictas.getRowCount() > 0) {
                jbtnTransferir.setEnabled(true);
            } else {
                btnverRegistros.setEnabled(false);
                jbtnTransferir.setEnabled(false);
            }
            if (conexionContpaqi != null) {
                conexionContpaqi.close();
            }
            this.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "error " + ex);
        }
    }
}
