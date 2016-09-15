package ctascontablesypolizas.clases;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import jxl.write.*;
import jxl.*;
import jxl.FormulaCell;
import jxl.format.CellFormat;
import jxl.write.biff.CellValue;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class exportar_excel {
    private File archi;
    private List<JTable> tabla;
    private List<String> nom_hoja;
    private boolean exportarConFormato = false;
    private String mensaje;
    public int[] dxNiveles;
    private ArrayList<CuentaEntity> grafo = new ArrayList<CuentaEntity>(); 
    private Connection conexionContpaqi;

    public String getMensaje() {
        return mensaje;
    }

    public exportar_excel(boolean exportarConFormato, List<JTable> tab, File ar, List<String> nom, ArrayList<CuentaEntity> grafo,Connection conexionContpaqi) throws Exception {
        this.archi = ar;
        this.tabla = tab;
        this.dxNiveles = dxNiveles;
        this.nom_hoja = nom;
        this.conexionContpaqi = conexionContpaqi;
        this.exportarConFormato = exportarConFormato;
        this.grafo = grafo;
        if (nom.size() != tab.size()) {
            throw new Exception("ERROR");
        }
        setContpaqNiveles();
    }
    
    public boolean exportarExcel() throws IOException {
        DataOutputStream out = new DataOutputStream(new FileOutputStream(archi));
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Hoja 1");
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 8000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 6000);

        HSSFFont fuente = workbook.createFont();
        fuente.setFontHeightInPoints((short) 10);
        fuente.setFontName(fuente.FONT_ARIAL);
        //fuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style.setFont(fuente);
        CuentaEntity cuenta = new CuentaEntity();
        //dando nombres iniciales a las columnas
        HSSFRow row1 = sheet.createRow(0);
        HSSFCell cell2 = row1.createCell(1);
        cell2.setCellStyle(style);
        cell2.setCellValue("Cuenta");
        cell2 = row1.createCell(2);
        cell2.setCellValue("Nombre");
        cell2 = row1.createCell(3);
        cell2.setCellValue("Cuenta Padre");
        cell2 = row1.createCell(4);
        cell2.setCellValue("Cuenta Nueva");
        cell2 = row1.createCell(5);
        cell2.setCellValue("Tipo Contpaq");
        cell2 = row1.createCell(6);
        cell2.setCellValue("Tipo COI");
        
        
        for (int i=1; i< grafo.size();i++){
            cuenta = grafo.get(i);

            HSSFRow row = sheet.createRow(i);
            HSSFCell cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(ponerGuiones(cuenta.getCodigo()));
            cell = row.createCell(2);
            cell.setCellValue(cuenta.getNombre());
            if(cuenta.getIdPadre()!= -1){//si es nodo raiz no tiene codigo de padre
                cell = row.createCell(3);
                cell.setCellValue(cuenta.getPadre().getCodigo());
            }
            
            char tempTipo = cuenta.getTipo();
            cell = row.createCell(5);
            if(tempTipo == 'A'){
                cell.setCellValue("A (Activo Deudor)");
                cell = row.createCell(6);
                cell.setCellValue("D (Deudor)");
            }else if(tempTipo == 'D'){
                cell.setCellValue("D (Pasivo Acreedor)");
                cell = row.createCell(6);
                cell.setCellValue("A (Acreedor)");
            }else if(tempTipo == 'G'){
                cell.setCellValue("G (Resultados Deudor)");
                cell = row.createCell(6);
                cell.setCellValue("D (Deudor)");
            }else if(tempTipo == 'H'){
                cell.setCellValue("H (Resultados Acreedor)");
                cell = row.createCell(6);
                cell.setCellValue("A (Acreedor)");
            }
            
            


        }
        workbook.write(out);

        return true;

    }

    
    
    public boolean export() {
        try {
            int[] colsSelect;
            CellFormat cf = null;
            WritableFont bold = new WritableFont(WritableFont.ARIAL,
                    WritableFont.DEFAULT_POINT_SIZE,
                    WritableFont.BOLD);
            bold.setPointSize(11);
            bold.setColour(jxl.format.Colour.WHITE);
            cf = new WritableCellFormat(bold);
            JTable table = tabla.get(0);
            int contColsSelect = 0;
            for (int i = 0; i < table.getColumnCount(); i++) {
                boolean resizable = table.getColumnModel().getColumn(i).getResizable();
                if (resizable) {
                    contColsSelect++;
                }
            }
            colsSelect = new int[contColsSelect];
            int indiceColsSelect = 0;
            for (int i = 0; i < table.getColumnCount(); i++) {
                boolean resizable = table.getColumnModel().getColumn(i).getResizable();
                if (resizable) {
                    colsSelect[indiceColsSelect] = i;
                    indiceColsSelect++;
                }
            }

                DataOutputStream out = new DataOutputStream(new FileOutputStream(archi));
                WritableWorkbook w = Workbook.createWorkbook(out);

                for (int index = 0; index < tabla.size(); index++) {
                    table = tabla.get(index);
                    WritableSheet s = w.createSheet(nom_hoja.get(index), 0);

                    if (exportarConFormato == false) {
                        for (int i = 0; i < colsSelect.length; i++) {
                            boolean resizable = table.getColumnModel().getColumn(colsSelect[i]).getResizable();
                            if (resizable) {
                                Object objeto = table.getColumnName(colsSelect[i]);
                                s.addCell(new Label(i, 0, String.valueOf(objeto)));
                                s.getWritableCell(i, 0).setCellFormat(cf);
                                WritableCell c = s.getWritableCell(i, 0);
                                WritableCellFormat newFormat = new WritableCellFormat(c.getCellFormat());
                                newFormat.setBackground(jxl.format.Colour.BLUE_GREY);
                                c.setCellFormat(newFormat);
                            }
                        }
                        for (int i = 0; i < colsSelect.length; i++) {
                            boolean resizable = table.getColumnModel().getColumn(colsSelect[i]).getResizable();
                            if (resizable) {
                                for (int j = 0; j < table.getRowCount(); j++) {
                                    Object objeto = table.getValueAt(j, colsSelect[i]);
                                    s.addCell(new Label(i, j + 1, String.valueOf(objeto)));
                                }
                            }
                        }
                    } else {//con formato
                        Object objeto = table.getColumnName(1);
                        s.addCell(new Label(0, 0, String.valueOf(objeto)));

                        Object objeto2 = table.getColumnName(2);
                        s.addCell(new Label(1, 0, String.valueOf(objeto2)));
                        
                        Object objeto3 = table.getColumnName(3);
                        s.addCell(new Label(2, 0, String.valueOf(objeto3)));

                        Object objeto4 = table.getColumnName(5);
                        s.addCell(new Label(3, 0, String.valueOf(objeto4)));

                        for (int j = 0; j < table.getRowCount(); j++) {
                            Object codigo = table.getValueAt(j, 1);
                            s.addCell(new Label(0, j + 1, String.valueOf(codigo)));

                            Object descr = table.getValueAt(j, 2);
                            s.addCell(new Label(1, j + 1, String.valueOf(descr)));
                            Object dpt = table.getValueAt(j, 3);
                            s.addCell(new Label(2, j + 1, String.valueOf(dpt)));

                            Object tipo = null;
                            String valor = table.getValueAt(j, 5) + "";
                            tipo = valor.substring(0, 1);
                            s.addCell(new Label(3, j + 1, String.valueOf(tipo)));
                        }
                    }
                }
                w.write();
                w.close();
                out.close();
                return true;

        } catch (IOException ex) {
            mensaje=ex.toString();
            ex.printStackTrace();
            return false;
        } catch (WriteException ex) {
            mensaje=ex.toString();
            ex.printStackTrace();
            return false;
        }
//        return false;
    }

    private String ponerGuiones(String codigo) {
        String temp2="";
        int cuenta=0;
        
        for(int i = 0; i<dxNiveles.length ; i++,cuenta++){
            System.out.println("lalalalala    "+ dxNiveles[i]);
            //temp2+= codigo.substring(cuenta, cuenta+dxNiveles[i]-1);
        }
        System.out.println(temp2);
        
        
        return " ";
    }
    
    private void setContpaqNiveles() {
        String temp ="";
        try {
            PreparedStatement stmtCountCtas = conexionContpaqi.prepareStatement("SELECT EstructCta FROM Parametros");
            ResultSet rsCount = stmtCountCtas.executeQuery();
            if (rsCount.next()) {
                temp = rsCount.getNString(1);
            }
            System.out.println("temp es: "+temp);
            rsCount.close();
        } catch (Exception e) {}

        if(temp=="")
            System.out.println("Error al obtener la EstrucCta desde contpaq para imprimir con guiones");

        String[] temp2 = temp.split("-");
        dxNiveles = new int[temp2.length];
        for(int i = 0; i < temp2.length; i++) {
            try {
                dxNiveles[i] = Integer.parseInt(temp2[i]);
                
            } catch (Exception e) {
            }

        }
        
    }

}
