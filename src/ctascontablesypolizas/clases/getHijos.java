/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas.clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Gabriel
 */
public class getHijos {
    private final Connection con;
    
    getHijos(Connection conec){
        this.con=conec;
    }
      public String[][] getHijosxNivel(String col1, String col2, String id, String cod) {
        String[][] infoSubctas = null;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("Select COUNT (*) FROM dbo.Asociaciones asoc "
                    + "inner join dbo.Cuentas cta on (asoc.IdSubCtade=cta.Id)"
                    + " WHERE (asoc.IdCtaSup=? AND asoc.CtaSup=?) AND asoc.IdSubCtade>10");
            stmtQuery.setString(1, id);
            stmtQuery.setString(2, cod);
            ResultSet rsCountRows = stmtQuery.executeQuery();
            int numRows = 0;
            if (rsCountRows.next()) {
                numRows = rsCountRows.getInt(1);
            }
            rsCountRows.close();
            stmtQuery.close();
            infoSubctas = new String[numRows][4];
            PreparedStatement stmtQueryInfo = con.prepareStatement("select asoc.IdSubCtaDe"
                    + ",asoc.SubCtade,cta.Nombre,cta.Tipo "
                    + "FROM dbo.Asociaciones asoc "
                    + "inner join dbo.Cuentas cta on (asoc.IdSubCtade=cta.Id)"
                    + "WHERE (asoc.IdCtaSup=? AND asoc.CtaSup=?) AND asoc.IdSubCtade>10");
            stmtQueryInfo.setString(1, id);
            stmtQueryInfo.setString(2, cod);
            ResultSet rsRowsInfo = stmtQueryInfo.executeQuery();
            int indice = 0;
            while (rsRowsInfo.next()) {
                infoSubctas[indice][0] = rsRowsInfo.getString(1);//id sub ctadet
                infoSubctas[indice][1] = rsRowsInfo.getString(2);//subctade
                infoSubctas[indice][2] = rsRowsInfo.getString(3);//nombre
                infoSubctas[indice][3] = rsRowsInfo.getString(4);//tipo
                indice++;
            }
            rsRowsInfo.close();
            stmtQueryInfo.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return infoSubctas;
    }
       public static String completaCeros(String folio) {
        int longFolio = 20;
        String folioReal = (folio + "");
        while (folioReal.length() < longFolio) {
            folioReal = folioReal + "0";
        }
        return folioReal;
    }
       
}
