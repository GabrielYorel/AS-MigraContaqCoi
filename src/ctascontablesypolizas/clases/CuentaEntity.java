/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas.clases;

import java.util.ArrayList;

/**
 *
 * @author Alejandro Vargas
 */
public class CuentaEntity {
    private int id;
    private String codigo;
    private String nombre;
    private String tipo;
    private boolean esBaja;
    private int ctaMayor;
    private int idAsociacion;
    private int idPadre;
    private CuentaEntity padre;
    private ArrayList <CuentaEntity> hijos= new ArrayList<CuentaEntity>();
    private boolean importarCOI;
    private String nuevocodigo;

    

    public String getNuevocodigo() {
        return nuevocodigo;
    }

    public void setNuevocodigo(String nuevocodigo) {
        this.nuevocodigo = nuevocodigo;
    }
    
    public CuentaEntity getPadre() {
        return padre;
    }

    public void setPadre(CuentaEntity padre) {
        this.padre = padre;
    }

    public boolean isImportarCOI() {
        return importarCOI;
    }

    public void setImportarCOI(boolean importarCOI) {
        this.importarCOI = importarCOI;
    }
    

    public CuentaEntity(){
        
    }
    
    public CuentaEntity(int id, String codigo, String nombre, String tipo, boolean esBaja, int ctaMayor, int idAsociacion, int idPadre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.esBaja = esBaja;
        this.ctaMayor = ctaMayor;
        this.idAsociacion = idAsociacion;
        this.idPadre = idPadre;
    }

    public ArrayList<CuentaEntity> getHijos() {
        return hijos;
    }
    
    public int getHijosCount(){
        return hijos.size();
    }

    public void setHijos(ArrayList<CuentaEntity> hijos) {
        this.hijos = hijos;
    }
    public void addHijo(CuentaEntity hijo)
    {
        hijos.add(hijo);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isEsBaja() {
        return esBaja;
    }

    public void setEsBaja(boolean esBaja) {
        this.esBaja = esBaja;
    }

    public int getCtaMayor() {
        return ctaMayor;
    }

    public void setCtaMayor(int ctaMayor) {
        this.ctaMayor = ctaMayor;
    }

    public int getIdAsociacion() {
        return idAsociacion;
    }

    public void setIdAsociacion(int idAsociacion) {
        this.idAsociacion = idAsociacion;
    }

    public int getIdPadre() {
        return idPadre;
    }

    public void setIdPadre(int idPadre) {
        this.idPadre = idPadre;
    }
    
    
    
    
}
