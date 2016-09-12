package ctascontablesypolizas.clases;

import java.io.*;
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
    private ArrayList<CuentaEntity> grafo = new ArrayList<CuentaEntity>(); 

    public String getMensaje() {
        return mensaje;
    }

    public exportar_excel(boolean exportarConFormato, List<JTable> tab, File ar, List<String> nom, ArrayList<CuentaEntity> grafo) throws Exception {
        this.archi = ar;
        this.tabla = tab;
        this.nom_hoja = nom;
        this.exportarConFormato = exportarConFormato;
        this.grafo = grafo;
        if (nom.size() != tab.size()) {
            throw new Exception("ERROR");
        }
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

        for (int i=1; i< grafo.size();i++){
            cuenta = grafo.get(i);

            HSSFRow row = sheet.createRow(i);
            HSSFCell cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(cuenta.getCodigo());
            cell = row.createCell(2);
            cell.setCellValue(cuenta.getNombre());
            if(cuenta.getIdPadre()!= -1){//si es nodo raiz no tiene codigo de padre
                cell = row.createCell(3);
                cell.setCellValue(cuenta.getPadre().getCodigo());
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
    
    
    
    
}
