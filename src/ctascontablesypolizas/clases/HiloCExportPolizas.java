/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas.clases;

import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;

/**
 *
 * @author Gabriel-Diaz
 */
public class HiloCExportPolizas extends Thread {

    String[][] tiposCtas;
    private JProgressBar jpbProgreso;
    private String servidor;
    private int puerto;
    private String controlador, usuario, contrasenia, baseDatos;
    private Connection conexionCOI;
    private JButton jbtnTransferir;
    private CrearConexionFBoSQL objConexion;
    java.util.List oList = new ArrayList();
    String noEmp;
    private int indice;

    public HiloCExportPolizas(String noEmp, java.util.List oList, JButton jbtnTransferir, JProgressBar jpbProgreso,
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
        this.oList = oList;
    }

    @Override
    public void run() {
        try {
            objConexion = new CrearConexionFBoSQL(controlador, servidor, puerto, baseDatos, usuario, contrasenia,false);
            conexionCOI = objConexion.getConexion();
            try {
                for (Iterator it = oList.iterator(); it.hasNext();) {//se recorren las polizas
                    Object[][] pol = (Object[][]) it.next();
                    Object[][] partPolizas = (Object[][]) pol[6][0];
                    if (partPolizas.length > 0) {//  Se verifica que las polizas tengan partidas
                        String tipoPoliza = "";//tipo del apolicza
                        int tipoPol = Integer.parseInt(pol[0][0] + "");//tipo poliza
                        if (tipoPol == 1) {
                            tipoPoliza = "Ig";
                        } else if (tipoPol == 2) {
                            tipoPoliza = "Eg";
                        } else if (tipoPol == 3) {
                            tipoPoliza = "Dr";
                        } else if (tipoPol == 6) {
                            tipoPoliza = "Ch";
                        }
                        int periodo = Integer.parseInt(pol[7][0] + "");//periodo
                        String per = "";
                        if (periodo < 10) {
                            per = "0" + periodo;
                        } else {
                            if (periodo == 14) {
                                per = String.valueOf(13);
                            } else {
                                per = String.valueOf(periodo);
                            }
                        }
                        if (periodo == 14) {
                            periodo = periodo - 1;
                        }
                        
                        int ejercicio = Integer.parseInt(pol[8][0] + "");//ejercicio
                        
                        String ejer = String.valueOf(ejercicio).substring(2, String.valueOf(ejercicio).length());
                        String folioPolContpaq = String.valueOf(pol[1][0]);//folio de la poliza en contpaq

                        String SQL = "SELECT FOLIO" + per + " FROM FOLIOS" + noEmp + " WHERE TIPPOL=? AND EJERCICIO=?";
                        PreparedStatement pstmt = conexionCOI.prepareStatement(SQL);
                        pstmt.setString(1, tipoPoliza);
                        pstmt.setInt(2, ejercicio);
                        ResultSet rsFolioP = pstmt.executeQuery();
                        String folioPoliza = "";//ultimo folio                        
                        if (rsFolioP.next()) {
                            folioPoliza = rsFolioP.getString(1).trim();
                        }
                        rsFolioP.close();
                        pstmt.close();
                        if (!folioPoliza.equalsIgnoreCase("")) {
                            String nvoFolioTemp = String.valueOf((Integer.parseInt(folioPoliza) + 1));//nuevo folio
                            String nvoFolio = completaCeros(nvoFolioTemp, 5);//nuevo folio                    
                            String fechaPol = String.valueOf(pol[3][0]);
                            Timestamp tmf=Timestamp.valueOf(fechaPol);                            
                            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                            String fechaSQL=formato.format(tmf);
                            String conceptoPol = String.valueOf(pol[2][0]);
                            String campoFecha = "";
                            if (controlador.equalsIgnoreCase("DevartSQLServer")) {
                                campoFecha = fechaSQL;
                            }else{
                                campoFecha=fechaPol;
                            }
                            int numPartidasPol = partPolizas.length;//numero de partidas de la poliza
                            String logaudita = "N";
                            String contabiliz = "N";
                            int numParCua = 0;
                            int tieneDocs = 0;
                            int proContab = 0;
                            String origen = "COI";
                            String sqlPoliza = "INSERT INTO POLIZAS" + ejer + noEmp + " (TIPO_POLI,NUM_POLIZ,PERIODO,EJERCICIO,FECHA_POL,CONCEP_PO,"
                                    + "NUM_PART,LOGAUDITA,CONTABILIZ,NUMPARCUA,TIENEDOCUMENTOS,PROCCONTAB,ORIGEN) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                            PreparedStatement query = conexionCOI.prepareStatement(sqlPoliza);
                            String cptoPol = "";
                            if (conceptoPol.length() >= 120) {
                                cptoPol = conceptoPol.substring(0, 118);
                            } else {
                                cptoPol = conceptoPol;
                            }


                            query.setString(1, tipoPoliza);
                            query.setString(2, nvoFolio);
                            query.setInt(3, periodo);
                            query.setInt(4, ejercicio);
                            query.setString(5, campoFecha);
                            query.setString(6, cptoPol);
                            query.setInt(7, numPartidasPol);
                            query.setString(8, logaudita);
                            query.setString(9, contabiliz);
                            query.setInt(10, numParCua);
                            query.setInt(11, tieneDocs);
                            query.setInt(12, proContab);
                            query.setString(13, origen);
                            query.execute();
                            query.close();//cerramos el statement

                            for (int i = 0; i < partPolizas.length; i++) {
                                double numPartida = Double.valueOf(i) + 1;//numero de partida
                                String noCta = String.valueOf(partPolizas[i][3]);//numero de cuenta
                                String conceptoMovto = String.valueOf(partPolizas[i][2]);//concepto de movto
                                boolean tipoMovtoCONT = Boolean.parseBoolean(String.valueOf(partPolizas[i][0]));
                                String tipoMovtoCOI = "";
                                if (tipoMovtoCONT) {//tipo de movto Abono
                                    tipoMovtoCOI = "H";
                                } else {//tipo de movto cargo
                                    tipoMovtoCOI = "D";
                                }
                                Double montoMovto = Double.parseDouble(String.valueOf(partPolizas[i][1]));
                                int numDepto = 0;
                                Double tipoCambio = 1.0;
                                int contrapar = 0;
                                int orden = Integer.valueOf(i) + 1;
                                int cCostos = 0;
                                int cGrupos = 0;
                                String sql = "";
                                if (controlador.equals("DevartSQLServer")) {
                                    sql = "select NUM_CTA "
                                            + "from CUENTAS" + ejer + noEmp + " WHERE substring(NUM_CTA,1," + noCta.length() + ") =" + noCta;
                                } else {
                                    sql = "select NUM_CTA "
                                            + "from CUENTAS" + ejer + noEmp + " WHERE substring(NUM_CTA from 1 for " + noCta.length() + ") =" + noCta;
                                }
                                PreparedStatement stmtCta = conexionCOI.prepareStatement(sql);

                                ResultSet rsCta = stmtCta.executeQuery();
                                String ctaConCeros = "";
                                if (rsCta.next()) {
                                    ctaConCeros = rsCta.getString(1);
                                }
                                rsCta.close();
                                stmtCta.close();
                                String sqlPartidasPoliza = "INSERT INTO AUXILIAR" + ejer + noEmp + " (TIPO_POLI,NUM_POLIZ,NUM_PART,PERIODO,EJERCICIO,NUM_CTA,"
                                        + "FECHA_POL,CONCEP_PO,DEBE_HABER,MONTOMOV,NUMDEPTO,TIPCAMBIO,CONTRAPAR,ORDEN,CCOSTOS,CGRUPOS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                PreparedStatement queryPartPoliza = conexionCOI.prepareStatement(sqlPartidasPoliza);
                                String cptoMovto = "";
                                if (conceptoMovto.length() >= 120) {
                                    cptoMovto = conceptoMovto.substring(0, 118);
                                } else {
                                    cptoMovto = conceptoMovto;
                                }
                                queryPartPoliza.setString(1, tipoPoliza);
                                queryPartPoliza.setString(2, nvoFolio);
                                queryPartPoliza.setDouble(3, numPartida);
                                queryPartPoliza.setInt(4, periodo);
                                queryPartPoliza.setInt(5, ejercicio);
                                queryPartPoliza.setString(6, ctaConCeros);
                                queryPartPoliza.setString(7, campoFecha);
                                queryPartPoliza.setString(8, cptoMovto);
                                queryPartPoliza.setString(9, tipoMovtoCOI);
                                queryPartPoliza.setDouble(10, montoMovto);
                                queryPartPoliza.setInt(11, numDepto);
                                queryPartPoliza.setDouble(12, tipoCambio);
                                queryPartPoliza.setInt(13, contrapar);
                                queryPartPoliza.setInt(14, orden);
                                queryPartPoliza.setInt(15, cCostos);
                                queryPartPoliza.setInt(16, cGrupos);
                                queryPartPoliza.execute();
                                queryPartPoliza.close();//cerramos el statement
                            }

                            PreparedStatement queryFolio = conexionCOI.prepareStatement(
                                    "UPDATE FOLIOS" + noEmp + " SET FOLIO" + per + "=? WHERE TIPPOL=? AND EJERCICIO=?");
                            queryFolio.setString(1, nvoFolioTemp);
                            queryFolio.setString(2, tipoPoliza);
                            queryFolio.setDouble(3, ejercicio);
                            queryFolio.execute();
                            queryFolio.close();

                        }
                        BigDecimal cont = new BigDecimal(indice + 1);
                        BigDecimal prog = (cont.divide(new BigDecimal(oList.size()), 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
                        String p_ent = new DecimalFormat("###,###").format(prog);
                        jpbProgreso.setValue(Integer.parseInt(p_ent));
                        jpbProgreso.setStringPainted(true);
                        jpbProgreso.setString("Progreso de exportación a COI " + Integer.parseInt(p_ent) + " %");
                        indice++;
                    }
                }
                jbtnTransferir.setEnabled(true);
                jpbProgreso.setValue(100);
                jpbProgreso.setString(" ** Exportación finalizada**");
                conexionCOI.close();
                JOptionPane.showMessageDialog(null, "Los datos de transfirieron correctamente", "Exito al guardar", JOptionPane.INFORMATION_MESSAGE);
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
