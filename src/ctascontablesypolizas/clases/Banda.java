/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas.clases;

/**
 *
 * @author Gabriel
 */
public class Banda {

    private String id; //1
    private String nombre;

    public Banda(String nombre, String id) { //2
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() { //3
        return id;
    }

    @Override
    public String toString() { //4
        return nombre;
    }
}
