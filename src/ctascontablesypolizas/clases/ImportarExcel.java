/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas.clases;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 *
 * @author Alejandro Vargas
 */
public class ImportarExcel {
    
    private ArrayList<CuentaEntity> grafo;
    
    
    public ImportarExcel(File archivo, ArrayList<CuentaEntity> grafo) {
        this.grafo = grafo;
        int cols = 0; // No of columns
        int tmp = 0;
        
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(archivo));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row;
            HSSFCell cell;
            int rows = sheet.getPhysicalNumberOfRows();
                // This trick ensures that we get the data properly even if it doesn't start from first few rows
            
            for(int i = 0; i < 10 || i < rows; i++) {
                row = sheet.getRow(i);
                if(row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if(tmp > cols) cols = tmp;
                }
            }
            System.out.println(rows+" numero de ros+ws");
            for(int r = 1; r < rows; r++) {
                row = sheet.getRow(r);
                if(row != null) {
                    cell = row.getCell(4);
                    if(cell != null) {
                        String nuevoCodigo;
                        

                        nuevoCodigo=cell.getStringCellValue();
                        cell=row.getCell(1);
                        asignarNuevoCodigo(cell.getStringCellValue());
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private void asignarNuevoCodigo(String codigo) {
        System.out.println(codigo);
        CuentaEntity cuenta= new CuentaEntity();
        
        for(int i = 0 ;i< grafo.size(); i++){
            cuenta=grafo.get(i);
            if(cuenta.getCodigo().equals(codigo)){
                cuenta.setNuevocodigo(codigo);
                return;
            }
        }
        System.out.println("Error, no se encontró código");
    }


    
    
    
}
