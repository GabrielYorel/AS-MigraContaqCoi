/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ctascontablesypolizas.clases;

import java.math.BigDecimal;

/**
 *
 * @author ELI
 */
public class SaldosCuentasEntity {
    
    private String codigo;
    private String nombre;
    private BigDecimal saldoIni;

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

    public BigDecimal getSaldoIni() {
        return saldoIni;
    }

    public void setSaldoIni(BigDecimal saldoIni) {
        this.saldoIni = saldoIni;
    }

    @Override
    public String toString() {
        return codigo;
    }

    public SaldosCuentasEntity(String codigo, String nombre, BigDecimal saldoIni) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.saldoIni = saldoIni;
    }

}
