/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas.clases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 *
 * @author Gabriel-Diaz
 */
public class CrearAcrhivo {

    public static void copyFile(File sourceFile, File destFile) throws IOException {

        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
    public static void abrirArchivos(String pPath){
        try {
            String [] cmd=new String[5];
            cmd[0]="cmd";
            cmd[1]="/C";
            cmd[2]="start";
            cmd[3]="\"\"";
            cmd[4]="\""+pPath+"\"";
            Runtime rt =Runtime.getRuntime();
            rt.exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
