package ctascontablesypolizas.clases;

import static ctascontablesypolizas.clases.HiloCargarCatCtas.completaCeros;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import jxl.write.*;
import jxl.*;

public class Exportar_excelPol {

    String mensaje = "";

    public String getMensaje() {
        return mensaje;
    }
    private File archi;
    java.util.List polizas;
    private String nom_hoja;
    String noEmpre;
    int[] dxNiveles;

    public Exportar_excelPol(int[] dxNiveles, java.util.List oList, File ar, String NombreHoja) throws Exception {
        this.dxNiveles = dxNiveles;
        this.archi = ar;
        this.polizas = oList;
        this.nom_hoja = NombreHoja;

    }
    public boolean export() throws ParseException, SQLException, ClassNotFoundException {
        try {
            int logCta = 0;
            for (int i = 0; i < dxNiveles.length; i++) {
                logCta += dxNiveles[i];
            }
            DecimalFormat decFrmat = new DecimalFormat("###,##0.00");
            DataOutputStream out = new DataOutputStream(new FileOutputStream(archi));
            WritableWorkbook w = Workbook.createWorkbook(out);
            WritableSheet s = w.createSheet(nom_hoja, 0);
            int indicePol = 2;
            for (Iterator it = polizas.iterator(); it.hasNext();) {
                Object[][] pol = (Object[][]) it.next();
                Object[][] partPolizas = (Object[][]) pol[6][0];
                if (partPolizas.length > 0) {//  Se verifica que las polizas tengan partidas
                    String tipo = "";//
                    int tipoPol = Integer.parseInt(pol[0][0] + "");
                    if (tipoPol == 1) {
                        tipo = "Ig";
                    } else if (tipoPol == 2) {
                        tipo = "Eg";
                    } else if (tipoPol == 3) {
                        tipo = "Dr";
                    }
                    String Folio = pol[1][0] + "";//                
                    String ConceptoPoliza = pol[2][0] + "";//
                    String Fecha = pol[3][0] + "";
                    SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
                    Date fecha = formatoDelTexto.parse(Fecha);
                    SimpleDateFormat formatoDia = new SimpleDateFormat("dd");//
                    s.addCell(new Label(0, indicePol, tipo));//columna--fila--->tipo poliza
                    s.addCell(new Label(1, indicePol, Folio));//columna--fila-->numero o folio
                    s.addCell(new Label(2, indicePol, ConceptoPoliza));//columna--fila-->concepto
                    s.addCell(new jxl.write.Number(3, indicePol, Integer.parseInt(formatoDia.format(fecha))));//columna--fila-->fecha con formato dd
                    indicePol++;
                    int ejercicio = Integer.parseInt(pol[8][0] + "");//ejercicio
                    String ejer = String.valueOf(ejercicio).substring(2, String.valueOf(ejercicio).length());
                    /*
                     * se obtienen la partidas de la póliza
                     */


                    for (int i = 0; i < partPolizas.length; i++) {//compras proveedores
                        boolean tipoMovto = java.lang.Boolean.parseBoolean(partPolizas[i][0] + "");
                        String conceptoMovto = partPolizas[i][2] + "";

                        String codigoCta = String.valueOf(partPolizas[i][3]);
                        String nvaCuenta=ponerGuines(codigoCta, logCta);
                        s.addCell(new Label(1, indicePol, nvaCuenta.substring(0, nvaCuenta.length()-1)));//columna--fila--->cuenta contable
                        s.addCell(new jxl.write.Number(2, indicePol, Integer.parseInt("0")));//columna--fila--->depto
                        s.addCell(new Label(3, indicePol, conceptoMovto));//columna--fila--->conceptod del movto
                        s.addCell(new jxl.write.Number(4, indicePol, Double.parseDouble("1")));//columna--fila-->tipo cambio
                        if (tipoMovto) {//Abono
                            BigDecimal bigAbono = new BigDecimal(partPolizas[i][1] + "");
                            s.addCell(new jxl.write.Number(6, indicePol, Double.parseDouble(bigAbono.toString().replace(",", ""))));//columna--fila--->Abono
                        } else {//cargo
                            BigDecimal bigCargo = new BigDecimal(partPolizas[i][1] + "");
                            s.addCell(new jxl.write.Number(5, indicePol, Double.parseDouble(bigCargo.toString().replace(",", ""))));//columna--fila--->Cargo
                        }
                        indicePol++;
                    }
                    s.addCell(new Label(1, indicePol, "FIN_PARTIDAS"));//columna--fila
                    indicePol = indicePol + 1;
                }
            }

            w.write();
            w.close();
            out.close();
            return true;

        } catch (IOException ex) {
            mensaje = ex.toString();
            ex.printStackTrace();
            return false;
        } catch (WriteException ex) {
            mensaje = ex.toString();
            ex.printStackTrace();
            return false;
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
}
