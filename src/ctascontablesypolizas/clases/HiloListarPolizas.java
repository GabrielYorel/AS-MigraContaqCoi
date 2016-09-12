/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas.clases;

import ctascontablesypolizas.ListadoPolizas;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gabriel-Diaz
 */
public class HiloListarPolizas extends Thread {

    private JTable jtblCatCtas;
    String[][] tiposCtas;
    private JTextField jtxtNumRegistros;
    private JProgressBar jpbProgreso;
    private DefaultTableModel modelo;
    private String servidor;
    private int puerto;
    private String controlador, usuario, contrasenia, baseDatos;
    private Connection conexionContpaqi;
    private JButton jbtnTransferir;
    private CrearConexionFBoSQL objConexion;
    public ListadoPolizas padre;
    java.util.List oList;
    int numRegistros;
    String sql;
    private int anio;
    private JButton jBtnGuardar;
    private JLabel jLabel1;
    private boolean cargarTodo;
    private boolean seguridadIntegrada;

    public HiloListarPolizas(boolean cargarTodo, JLabel jLabel1, JButton jBtnGuardar, int anio, String sql, java.util.List oList, JButton jbtnTransferir, JTable jtblCatCtas, int NumRegistros,
            JProgressBar jpbProgreso, DefaultTableModel modelonuevo, String controlador,
            String servidor, int puerto, String usuario, String contrasenia, String baseDatos, boolean seguridadIntegrada) {
        super("hiloCargarCatCtas");
        this.seguridadIntegrada = seguridadIntegrada;
        this.numRegistros = NumRegistros;
        this.oList = oList;
        this.jbtnTransferir = jbtnTransferir;
        this.jtblCatCtas = jtblCatCtas;
        this.jpbProgreso = jpbProgreso;
        this.modelo = modelonuevo;
        this.servidor = servidor;
        this.puerto = puerto;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.baseDatos = baseDatos;
        this.controlador = controlador;
        this.sql = sql;
        this.anio = anio;
        this.jBtnGuardar = jBtnGuardar;
        this.jLabel1 = jLabel1;
        this.cargarTodo = cargarTodo;
    }

    @Override
    public void run() {
        try {
            if (cargarTodo) {
                objConexion = new CrearConexionFBoSQL(controlador, servidor, puerto, baseDatos, usuario, contrasenia, seguridadIntegrada);
                conexionContpaqi = objConexion.getConexion();
                Statement stmt = conexionContpaqi.createStatement();
                ResultSet RstRegPolizas = stmt.executeQuery(sql);
                Object[][] encPolizas = new Object[numRegistros][8];
                int indice2 = 0;
                while (RstRegPolizas.next()) {
                    encPolizas[indice2][0] = RstRegPolizas.getInt(1);//id poliza
                    encPolizas[indice2][1] = RstRegPolizas.getInt(2);//período poliza
                    encPolizas[indice2][2] = RstRegPolizas.getInt(3);//tipo poliza
                    encPolizas[indice2][3] = RstRegPolizas.getInt(4);//folio poliza
                    encPolizas[indice2][4] = RstRegPolizas.getString(5);//concepto
                    encPolizas[indice2][5] = RstRegPolizas.getTimestamp(6);//fecha
                    encPolizas[indice2][6] = RstRegPolizas.getBigDecimal(7);//cargos
                    encPolizas[indice2][7] = RstRegPolizas.getBigDecimal(8);//abonos
                    BigDecimal cont = new BigDecimal(indice2 + 1);
                    BigDecimal prog = (cont.divide(new BigDecimal(numRegistros), 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
                    String p_ent = new DecimalFormat("###,###").format(prog);
                    jpbProgreso.setValue(Integer.parseInt(p_ent));
                    jpbProgreso.setStringPainted(true);
                    jpbProgreso.setString("Cargando pólizas... " + Integer.parseInt(p_ent) + " %");
                    indice2++;
                }
                RstRegPolizas.close();
                jpbProgreso.setValue(0);
                jpbProgreso.setString("");
                /*
                 * se recorren todas las polizas encontradas en la tabla dbo.polizas
                 */
                for (int i = 0; i < encPolizas.length; i++) {
                    String idPol = encPolizas[i][0] + "";
                    String periodoPol = encPolizas[i][1] + "";
                    String tipoPol = encPolizas[i][2] + "";
                    String folioPol = encPolizas[i][3] + "";
                    String conceptoPol = encPolizas[i][4] + "";
                    String fechaPol = encPolizas[i][5] + "";
                    String cargosPol = encPolizas[i][6] + "";
                    String abonosPol = encPolizas[i][7] + "";
                    /*
                     * Se obtiene el numero de partidas de la poliza almacenada en el arreglo
                     */
                    ResultSet RsCountPartidasPol = stmt.executeQuery("Select count(part.Id) "
                            + "from dbo.MovimientosPoliza part inner join dbo.Cuentas ctas "
                            + "on(part.IdCuenta=ctas.Id) where part.Folio=" + folioPol + " AND (part.Ejercicio=" + anio
                            + " AND part.Periodo=" + periodoPol + ") and part.idPoliza=" + idPol + "");
                    int numPartidasPol = 0;
                    if (RsCountPartidasPol.next()) {
                        numPartidasPol = RsCountPartidasPol.getInt(1);
                    }
                    RsCountPartidasPol.close();
                    /*
                     * Se obtiene la informacion de las partidas de la poliza
                     */
                    Object[][] partPolizas = new Object[numPartidasPol][5];
                    ResultSet RsPartidasPol = stmt.executeQuery("Select "
                            + "part.tipoMovto,part.Importe,part.Concepto,ctas.Codigo,ctas.Nombre "
                            + "from dbo.MovimientosPoliza part inner join dbo.Cuentas ctas "
                            + "on(part.IdCuenta=ctas.Id) where part.Folio=" + folioPol + " AND (part.Ejercicio=" + anio
                            + " AND part.Periodo=" + periodoPol + ") and part.idPoliza=" + idPol + "");
                    int indicePart = 0;
                    while (RsPartidasPol.next()) {
                        partPolizas[indicePart][0] = RsPartidasPol.getBoolean(1);//tipo movto
                        partPolizas[indicePart][1] = RsPartidasPol.getBigDecimal(2);//importe
                        partPolizas[indicePart][2] = RsPartidasPol.getString(3);//Concepto movto
                        partPolizas[indicePart][3] = RsPartidasPol.getString(4);//cta contable
                        partPolizas[indicePart][4] = RsPartidasPol.getString(5);//nombre cta
                        indicePart++;
                    }
                    RsPartidasPol.close();

                    /*
                     * se crea una nueva poliza
                     */
                    Object[][] poliza = new Object[9][1];//antepenultimo-->impuesto-ultimo-->arreglocvdDocs

                    poliza[0][0] = tipoPol;
                    poliza[1][0] = folioPol;
                    poliza[2][0] = conceptoPol;
                    poliza[3][0] = fechaPol;
                    poliza[4][0] = cargosPol;
                    poliza[5][0] = abonosPol;
                    poliza[6][0] = partPolizas;
                    poliza[7][0] = periodoPol;//periodo
                    poliza[8][0] = anio;//ejercicio
                        /*
                     * Se agrega a la lista de polizas
                     */
                    oList.add(poliza);
                    BigDecimal cont = new BigDecimal(i + 1);
                    BigDecimal prog = (cont.divide(new BigDecimal(numRegistros), 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
                    String p_ent = new DecimalFormat("###,###").format(prog);
                    jpbProgreso.setValue(Integer.parseInt(p_ent));
                    jpbProgreso.setStringPainted(true);
                    jpbProgreso.setString("Cargando partidas de pólizas... " + Integer.parseInt(p_ent) + " %");
                }//fin for-- recorrido para polizas del arreglo.
            }
            jpbProgreso.setValue(0);
            jpbProgreso.setString("");
            DecimalFormat decFrmat = new DecimalFormat("###,##0.00");
//            for (int i = jtblCatCtas.getRowCount() - 1; i >= 0; i--) {
//                modelo.removeRow(i);
//            }
            int indice = 0;
            for (Iterator it = oList.iterator(); it.hasNext();) {
                Object[][] pol = (Object[][]) it.next();
                String tipo = "";
                int tipoPol = Integer.parseInt(pol[0][0] + "");
                String folioPol = pol[1][0] + "";
                String conceptoPol = pol[2][0] + "";
                String fechaPol = pol[3][0] + "";
                String cargosPol = pol[4][0] + "";
                String abonosPol = pol[5][0] + "";
                if (tipoPol == 1) {
                    tipo = "Ig (Ingreso)";
                } else if (tipoPol == 2) {
                    tipo = "Eg (Egreso)";
                } else if (tipoPol == 3) {
                    tipo = "Dr (Diario)";
                } else if (tipoPol == 6) {
                    tipo = "Ch (Cheque)";
                }
                modelo.addRow(new Object[]{indice + 1, tipo, folioPol, conceptoPol,
                    fechaPol, decFrmat.format(new BigDecimal(cargosPol)), decFrmat.format(new BigDecimal(abonosPol))});
                BigDecimal cont = new BigDecimal(indice + 1);
                BigDecimal prog = (cont.divide(new BigDecimal(numRegistros), 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
                String p_ent = new DecimalFormat("###,###").format(prog);
                jpbProgreso.setValue(Integer.parseInt(p_ent));
                jpbProgreso.setStringPainted(true);
                jpbProgreso.setString("Agregando a la tabla... " + Integer.parseInt(p_ent) + " %");
                indice++;
            }
            if (jtblCatCtas.getRowCount() > 0) {
                jBtnGuardar.setEnabled(true);
                jbtnTransferir.setEnabled(true);
                jLabel1.setText("Total de pólizas " + oList.size() + "");
            } else {
                jBtnGuardar.setEnabled(false);
                jbtnTransferir.setEnabled(false);
            }
            if (conexionContpaqi != null) {
                conexionContpaqi.close();
            }
            jpbProgreso.setValue(100);
            jpbProgreso.setStringPainted(true);
            jpbProgreso.setString("Pólizas cargadas con exito... 100%");
            padre.oList = oList;
            this.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "error " + ex);
        }
    }
}
