/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas.clases;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Gabriel
 */
public class Cuentas {
//    DefaultMutableTreeNode node;
    private String id; //1
    private String idParent; //1
    private String nombre;

    public Cuentas(String id,String idParent, String nombre) { //2
        this.id = id;
        this.idParent=idParent;
        this.nombre = nombre;        
    }

    public String getId() { //3
        return id;
    }
    public String getIdParent() { //3
        return idParent;
    }
    
    public String getNombre() { //3
        return nombre;
    }

    public String toString() { //4
        return id+" "+nombre;
    }
    
}
