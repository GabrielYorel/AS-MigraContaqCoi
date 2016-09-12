/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas.clases;

import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gabriel-Diaz
 */
public class HiloExportCtas extends Thread {

    private JTable jtblCatCtas;
    String[][] tiposCtas;
    private JProgressBar jpbProgreso;
    private DefaultTableModel modelonuevo;
    private String servidor;
    private String servidorCOI;
    private Object[] seleccionados;
    private int puerto;
    private int puertoCOI;
    private String controlador, usuario, contrasenia, baseDatos;
    private String controladorCOI, usuarioCOI, contraseniaCOI, baseDatosCOI;
    private Connection conexionContpaqi;
    private Connection conexionCOI;
    private JButton jbtnTransferir;
    private CrearConexionFBoSQL objConexion;
    private CrearConexionFBoSQL objConexion2;
    private int indice = 0;
    private final String noEmpresa;
    private boolean  seguridadIntegrada;

    public HiloExportCtas(String noEmpresa, Object[] seleccionados, JButton jbtnTransferir, String[][] tiposCtas, JTable jtblCatCtas, JProgressBar jpbProgreso,
            DefaultTableModel modelonuevo, String controlador, String servidor, int puerto, String usuario,
            String contrasenia, String baseDatos,
            String controladorCOI, String servidorCOI, int puertoCOI, String usuarioCOI, String contraseniaCOI, String baseDatosCOI, boolean seguridadIntegrada) {
        super("hiloCargarCatCtas");
        this.seguridadIntegrada = seguridadIntegrada;
        this.jbtnTransferir = jbtnTransferir;
        this.jtblCatCtas = jtblCatCtas;
        this.jpbProgreso = jpbProgreso;
        this.modelonuevo = modelonuevo;
        this.servidor = servidor;
        this.puerto = puerto;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.baseDatos = baseDatos;
        this.controlador = controlador;
        this.servidorCOI = servidorCOI;
        this.puertoCOI = puertoCOI;
        this.usuarioCOI = usuarioCOI;
        this.contraseniaCOI = contraseniaCOI;
        this.baseDatosCOI = baseDatosCOI;
        this.controladorCOI = controladorCOI;
        this.tiposCtas = tiposCtas;
        this.noEmpresa = noEmpresa;
        this.seleccionados = seleccionados;
    }

    @Override
    public void run() {
        try {
            objConexion = new CrearConexionFBoSQL(controlador, servidor, puerto, baseDatos, usuario, contrasenia,seguridadIntegrada);
            conexionContpaqi = objConexion.getConexion();//            
            PreparedStatement stmtQuery = conexionContpaqi.prepareStatement("select COUNT (*) FROM dbo.Cuentas WHERE Codigo LIKE '\\_%' {escape '\\'}"); //LIKE '\\_%' {escape '\\'};
            ResultSet rsCtasPapa = stmtQuery.executeQuery();
            int numCtas = 0;
            if (rsCtasPapa.next()) {
                numCtas = rsCtasPapa.getInt(1);
            }
            rsCtasPapa.close();
            stmtQuery.close();
            String[][] ctasPapa = new String[numCtas][2];
            PreparedStatement stmtQueryCta = conexionContpaqi.prepareStatement("select Id,Codigo FROM dbo.Cuentas WHERE Codigo LIKE '\\_%' {escape '\\'}");
            ResultSet rsCtasPapaCta = stmtQueryCta.executeQuery();
            int indiceCtas = 0;
            while (rsCtasPapaCta.next()) {
                ctasPapa[indiceCtas][0] = rsCtasPapaCta.getString(1);
                ctasPapa[indiceCtas][1] = rsCtasPapaCta.getString(2);
                indiceCtas++;
            }
            rsCtasPapaCta.close();
            stmtQueryCta.close();
            getHijos obj = new getHijos(conexionContpaqi);
    
            for (int i = 0; i < ctasPapa.length; i++) {//nivel 0
                String idCtaNivel1 = ctasPapa[i][0];
                String codCtaNivel1 = ctasPapa[i][1];
                if (obj.getHijosxNivel("", "", idCtaNivel1, codCtaNivel1).length >= 0) {
                    String cuentaRaiz = "";
                    String[][] ctasRaiz = obj.getHijosxNivel("", "", idCtaNivel1, codCtaNivel1);
                    for (int j = 0; j < ctasRaiz.length; j++) {//nivel 1 cuentas raiz

                        String IdSubCtaDe = ctasRaiz[j][0];
                        String SubCtade = ctasRaiz[j][1];//raiz cta
                        String nombre = ctasRaiz[j][2];//nombre
                        String tipoContaq = ctasRaiz[j][3];//tipo
                        int naturaleza = getNaturaleza(tipoContaq);
                        if (obj.getHijosxNivel("", "", IdSubCtaDe, SubCtade).length >= 0) {
                            if (!SubCtade.startsWith("0")){
                                cuentaRaiz = SubCtade;
                                registrarCuenta(getHijos.completaCeros(SubCtade) + "1", "A", nombre, "-1", (getHijos.completaCeros(SubCtade) + "1"), "1", naturaleza);
                            }                           
                            String[][] ctasNivel1 = obj.getHijosxNivel("", "", IdSubCtaDe, SubCtade);
                            for (int k = 0; k < ctasNivel1.length; k++) {//nivel 2
                                String IdSubCtaDeN2 = ctasNivel1[k][0];
                                String SubCtadeN2 = ctasNivel1[k][1];//papa
                                String nombreN2 = ctasNivel1[k][2];
                                String tipoContaq2 = ctasNivel1[k][3];//tipo
                                int naturaleza2 = getNaturaleza(tipoContaq2);
                                int nivel=0;
                                if (SubCtade.startsWith("0")) {
                                    cuentaRaiz = SubCtadeN2;
                                    nivel=1;  
                                }else{
                                  nivel=2;  
                                }
                                if (!SubCtadeN2.startsWith("0")) {
                                    if (SubCtade.startsWith("0")) {
                                        registrarCuenta(getHijos.completaCeros(SubCtadeN2) + "1", "A", nombreN2, "-1", getHijos.completaCeros(cuentaRaiz) + "1", "1", naturaleza2);
                                    } else {
                                        registrarCuenta(getHijos.completaCeros(SubCtadeN2) + nivel, "D", nombreN2, getHijos.completaCeros(SubCtade) + (nivel-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel+"", naturaleza2);
                                    }
                                }
                                if (obj.getHijosxNivel("", "", IdSubCtaDeN2, SubCtadeN2).length >= 0) {
                                    String[][] ctasNivel3 = obj.getHijosxNivel("", "", IdSubCtaDeN2, SubCtadeN2);
                                    for (int l = 0; l < ctasNivel3.length; l++) {//nivel 3
                                        String IdSubCtaDeN3 = ctasNivel3[l][0];
                                        String SubCtadeN3 = ctasNivel3[l][1];//papa
                                        String nombreN3 = ctasNivel3[l][2];
                                        String tipoContaq3 = ctasNivel3[l][3];//tipo
                                        int naturaleza3 = getNaturaleza(tipoContaq3);
                                        int nivel3=3;
                                        if (SubCtade.startsWith("0")) {
                                            if (SubCtadeN2.startsWith("0")) {
                                                cuentaRaiz = SubCtadeN3;
                                                 nivel3=1;
                                            } else {
                                                cuentaRaiz = SubCtadeN2;
                                                nivel3=2;
                                            }
                                        }else{
                                          nivel3=3;  
                                          }

                                        if (!SubCtadeN3.startsWith("0")) {
                                            if (SubCtadeN2.startsWith("0")) {
                                                if (SubCtade.startsWith("0")) {
                                                    registrarCuenta(getHijos.completaCeros(SubCtadeN3) + "1", "A", nombreN3, "-1", getHijos.completaCeros(cuentaRaiz) + "1", "1", naturaleza3);
                                                } else {
                                                    registrarCuenta(getHijos.completaCeros(SubCtadeN3) + nivel3, "D", nombreN3, getHijos.completaCeros(SubCtade) + (nivel3-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel3+"", naturaleza3);
                                                }
                                            } else {
                                                registrarCuenta(getHijos.completaCeros(SubCtadeN3) + nivel3, "D", nombreN3, getHijos.completaCeros(SubCtadeN2) + (nivel3-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel3+"", naturaleza3);
                                            }
                                        }

                                        if (obj.getHijosxNivel("", "", IdSubCtaDeN3, SubCtadeN3).length >= 0) {
                                            String[][] ctasNivel4 = obj.getHijosxNivel("", "", IdSubCtaDeN3, SubCtadeN3);
                                            for (int m = 0; m < ctasNivel4.length; m++) {//nivel 2
                                                String IdSubCtaDeN4 = ctasNivel4[m][0];
                                                String SubCtadeN4 = ctasNivel4[m][1];//papa
                                                String nombreN4 = ctasNivel4[m][2];
                                                String tipoContaq4 = ctasNivel4[m][3];//tipo
                                                int naturaleza4 = getNaturaleza(tipoContaq4);
                                                int nivel4=4;
                                                if (SubCtade.startsWith("0")) {
                                                    if (SubCtadeN2.startsWith("0")) {
                                                        if (SubCtadeN3.startsWith("0")) {
                                                            cuentaRaiz = SubCtadeN4;
                                                            nivel4=1;
                                                        } else {
                                                            cuentaRaiz = SubCtadeN3;
                                                            nivel4=2;
                                                        }
                                                    } else {
                                                        nivel4=3;
                                                        cuentaRaiz = SubCtadeN2;
                                                    }
                                                }
                                                if (!SubCtadeN4.startsWith("0")) {
                                                    if (SubCtadeN3.startsWith("0")) {
                                                        if (SubCtadeN2.startsWith("0")) {
                                                            if (SubCtade.startsWith("0")) {
                                                                registrarCuenta(getHijos.completaCeros(SubCtadeN4) + "1", "A", nombreN4, "-1", getHijos.completaCeros(cuentaRaiz) + "1", "1", naturaleza4);
                                                            } else {
                                                                registrarCuenta(getHijos.completaCeros(SubCtadeN4) + nivel4, "D", nombreN4, getHijos.completaCeros(SubCtade) + (nivel4-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel4+"", naturaleza4);
                                                            }
                                                        } else {
                                                            registrarCuenta(getHijos.completaCeros(SubCtadeN4) + nivel4, "D", nombreN4, getHijos.completaCeros(SubCtadeN2) + (nivel4-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel4+"", naturaleza4);
                                                        }
                                                    } else {
                                                        registrarCuenta(getHijos.completaCeros(SubCtadeN4) + nivel4, "D", nombreN4, getHijos.completaCeros(SubCtadeN3) + (nivel4-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel4+"", naturaleza4);
                                                    }
                                                }
    
                                                if (obj.getHijosxNivel("", "", IdSubCtaDeN4, SubCtadeN4).length >= 0) {
                                                    String[][] ctasNivel5 = obj.getHijosxNivel("", "", IdSubCtaDeN4, SubCtadeN4);
                                                    for (int n = 0; n < ctasNivel5.length; n++) {//nivel 2
                                                        String IdSubCtaDeN5 = ctasNivel5[n][0];
                                                        String SubCtadeN5 = ctasNivel5[n][1];//papa
                                                        String nombreN5 = ctasNivel5[n][2];
                                                        String tipoContaq5 = ctasNivel5[n][3];//tipo
                                                        int naturaleza5 = getNaturaleza(tipoContaq5);
                                                        int nivel5=5;
                                                        if (SubCtade.startsWith("0")) {
                                                            if (SubCtadeN2.startsWith("0")) {
                                                                if (SubCtadeN3.startsWith("0")) {
                                                                    if (SubCtadeN4.startsWith("0")) {
                                                                        cuentaRaiz = SubCtadeN5;
                                                                        nivel5=1;
                                                                    } else {
                                                                        cuentaRaiz = SubCtadeN4;
                                                                        nivel5=2;
                                                                    }
                                                                } else {
                                                                    cuentaRaiz = SubCtadeN3;
                                                                    nivel5=3;
                                                                }
                                                            } else {
                                                                cuentaRaiz = SubCtadeN2;
                                                                nivel5=4;
                                                            }
                                                        }
                                                        if (!SubCtadeN5.startsWith("0")) {
                                                            if (SubCtadeN4.startsWith("0")) {
                                                                if (SubCtadeN3.startsWith("0")) {
                                                                    if (SubCtadeN2.startsWith("0")) {
                                                                        if (SubCtade.startsWith("0")) {
                                                                            registrarCuenta(getHijos.completaCeros(SubCtadeN5) + "1", "A", nombreN5, "-1", getHijos.completaCeros(cuentaRaiz) + "1", "1", naturaleza5);
                                                                        } else {
                                                                            registrarCuenta(getHijos.completaCeros(SubCtadeN5) + nivel5, "D", nombreN5, getHijos.completaCeros(SubCtade) + (nivel5-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel5+"", naturaleza5);
                                                                        }
                                                                    } else {
                                                                        registrarCuenta(getHijos.completaCeros(SubCtadeN5) + nivel5, "D", nombreN5, getHijos.completaCeros(SubCtadeN2) + (nivel5-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel5+"", naturaleza5);
                                                                    }
                                                                } else {
                                                                    registrarCuenta(getHijos.completaCeros(SubCtadeN5) + nivel5, "D", nombreN5, getHijos.completaCeros(SubCtadeN3) + (nivel5-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel5+"", naturaleza5);
                                                                }
                                                            } else {
                                                                registrarCuenta(getHijos.completaCeros(SubCtadeN5) + nivel5, "D", nombreN5, getHijos.completaCeros(SubCtadeN4) + (nivel5-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel5+"", naturaleza5);
                                                            }
                                                        }

                                                        if (obj.getHijosxNivel("", "", IdSubCtaDeN5, SubCtadeN5).length >= 0) {
                                                            String[][] ctasNivel6 = obj.getHijosxNivel("", "", IdSubCtaDeN5, SubCtadeN5);
                                                            for (int b = 0; b < ctasNivel6.length; b++) {//nivel 2
                                                                String IdSubCtaDeN6 = ctasNivel6[b][0];
                                                                String SubCtadeN6 = ctasNivel6[b][1];//papa
                                                                String nombreN6 = ctasNivel6[b][2];
                                                                String tipoContaq6 = ctasNivel6[b][3];//tipo
                                                                int naturaleza6 = getNaturaleza(tipoContaq6);
                                                                int nivel6=6;
                                                                if (SubCtade.startsWith("0")) {
                                                                    if (SubCtadeN2.startsWith("0")) {
                                                                        if (SubCtadeN3.startsWith("0")) {
                                                                            if (SubCtadeN4.startsWith("0")) {
                                                                                if (SubCtadeN5.startsWith("0")) {
                                                                                    cuentaRaiz = SubCtadeN6;
                                                                            nivel6=1;
                                                                                }else{
                                                                                  cuentaRaiz = SubCtadeN5;
                                                                                  nivel6=2;                                                       
                                                                                }
                                                                            } else {
                                                                                cuentaRaiz = SubCtadeN4;
                                                                            nivel6=3;
                                                                            }
                                                                        } else {
                                                                            cuentaRaiz = SubCtadeN3;
                                                                            nivel6=4;
                                                                        }
                                                                    } else {
                                                                        cuentaRaiz = SubCtadeN2;
                                                                        nivel6=5;
                                                                    }
                                                                }
                                                                if (!SubCtadeN6.startsWith("0")) {
                                                                    if (SubCtadeN5.startsWith("0")) {
                                                                        if (SubCtadeN4.startsWith("0")) {
                                                                            if (SubCtadeN3.startsWith("0")) {
                                                                                if (SubCtadeN2.startsWith("0")) {
                                                                                    if (SubCtade.startsWith("0")) {
                                                                                        registrarCuenta(getHijos.completaCeros(SubCtadeN6) + "1", "A", nombreN6, "-1", getHijos.completaCeros(cuentaRaiz) + "1", "1", naturaleza6);
                                                                                    } else {
                                                                                        registrarCuenta(getHijos.completaCeros(SubCtadeN6) + nivel6, "D", nombreN6, getHijos.completaCeros(SubCtade) + (nivel6-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel6+"", naturaleza6);
                                                                                    }
                                                                                } else {
                                                                                    registrarCuenta(getHijos.completaCeros(SubCtadeN6) + nivel6, "D", nombreN6, getHijos.completaCeros(SubCtadeN2) + (nivel6-1), getHijos.completaCeros(cuentaRaiz) + "1",nivel6+"", naturaleza6);
                                                                                }
                                                                            } else {
                                                                                registrarCuenta(getHijos.completaCeros(SubCtadeN6) + nivel6, "D", nombreN6, getHijos.completaCeros(SubCtadeN3) + (nivel6-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel6+"", naturaleza6);
                                                                            }
                                                                        } else {
                                                                            registrarCuenta(getHijos.completaCeros(SubCtadeN6) + nivel6, "D", nombreN6, getHijos.completaCeros(SubCtadeN4) + (nivel6-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel6+"", naturaleza6);
                                                                        }
                                                                    } else {
                                                                        registrarCuenta(getHijos.completaCeros(SubCtadeN6) + nivel6, "D", nombreN6, getHijos.completaCeros(SubCtadeN5) + (nivel6-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel6+"", naturaleza6);
                                                                    }
                                                                }
                                                                if (obj.getHijosxNivel("", "", IdSubCtaDeN6, SubCtadeN6).length >= 0) {
                                                                    String[][] ctasNivel7 = obj.getHijosxNivel("", "", IdSubCtaDeN6, SubCtadeN6);
                                                                    for (int c = 0; c < ctasNivel7.length; c++) {//nivel 2
                                                                        String IdSubCtaDeN7 = ctasNivel7[c][0];
                                                                        String SubCtadeN7 = ctasNivel7[c][1];//papa
                                                                        String nombreN7 = ctasNivel7[c][2];
                                                                        String tipoContaq7 = ctasNivel7[c][3];//tipo
                                                                        int naturaleza7 = getNaturaleza(tipoContaq7);
                                                                        int nivel7=7;
                                                                        if (SubCtade.startsWith("0")) {
                                                                            if (SubCtadeN2.startsWith("0")) {
                                                                                if (SubCtadeN3.startsWith("0")) {
                                                                                    if (SubCtadeN4.startsWith("0")) {
                                                                                        if (SubCtadeN5.startsWith("0")) {
                                                                                            if (SubCtadeN6.startsWith("0")) {
                                                                                                cuentaRaiz = SubCtadeN7;
                                                                                    nivel7=1;
                                                                                            } else {
                                                                                                cuentaRaiz = SubCtadeN6;
                                                                                    nivel7=2;
                                                                                            }
                                                                                        } else {
                                                                                            cuentaRaiz = SubCtadeN5;
                                                                                    nivel7=3;
                                                                                        }
                                                                                    } else {
                                                                                        cuentaRaiz = SubCtadeN4;
                                                                                    nivel7=4;
                                                                                    }
                                                                                } else {
                                                                                    cuentaRaiz = SubCtadeN3;
                                                                                    nivel7=5;
                                                                                }
                                                                            } else {
                                                                                cuentaRaiz = SubCtadeN2;
                                                                                nivel7=6;
                                                                            }
                                                                        }
                                                                        if (!SubCtadeN7.startsWith("0")) {
                                                                            if (SubCtadeN6.startsWith("0")) {
                                                                                if (SubCtadeN5.startsWith("0")) {
                                                                                    if (SubCtadeN4.startsWith("0")) {
                                                                                        if (SubCtadeN3.startsWith("0")) {
                                                                                            if (SubCtadeN2.startsWith("0")) {
                                                                                                if (SubCtadeN2.startsWith("0")) {
                                                                                                    registrarCuenta(getHijos.completaCeros(SubCtadeN7) + nivel7, "A", nombreN7, "-1", getHijos.completaCeros(cuentaRaiz) + "1", "1", naturaleza7);
                                                                                                } else {
                                                                                                    registrarCuenta(getHijos.completaCeros(SubCtadeN7) + nivel7, "D", nombreN7, getHijos.completaCeros(SubCtade) + (nivel7-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel7+"", naturaleza7);
                                                                                                }
                                                                                            } else {
                                                                                                registrarCuenta(getHijos.completaCeros(SubCtadeN7) + nivel7, "D", nombreN7, getHijos.completaCeros(SubCtadeN2) + (nivel7-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel7+"", naturaleza7);
                                                                                            }
                                                                                        } else {
                                                                                            registrarCuenta(getHijos.completaCeros(SubCtadeN7) + nivel7, "D", nombreN7, getHijos.completaCeros(SubCtadeN3) + (nivel7-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel7+"", naturaleza7);
                                                                                        }
                                                                                    } else {
                                                                                        registrarCuenta(getHijos.completaCeros(SubCtadeN7) + nivel7, "D", nombreN7, getHijos.completaCeros(SubCtadeN4) + (nivel7-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel7+"", naturaleza7);
                                                                                    }
                                                                                } else {
                                                                                    registrarCuenta(getHijos.completaCeros(SubCtadeN7) + nivel7, "D", nombreN7, getHijos.completaCeros(SubCtadeN5) + (nivel7-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel7+"", naturaleza7);
                                                                                }
                                                                            } else {
                                                                                registrarCuenta(getHijos.completaCeros(SubCtadeN7) + nivel7, "D", nombreN7, getHijos.completaCeros(SubCtadeN6) + (nivel7-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel7+"", naturaleza7);
                                                                            }
                                                                        }

                                                                        if (obj.getHijosxNivel("", "", IdSubCtaDeN7, SubCtadeN7).length >= 0) {
                                                                            String[][] ctasNivel8 = obj.getHijosxNivel("", "", IdSubCtaDeN7, SubCtadeN7);
                                                                            for (int d = 0; d < ctasNivel8.length; d++) {//nivel 2
                                                                                String IdSubCtaDeN8 = ctasNivel8[d][0];
                                                                                String SubCtadeN8 = ctasNivel8[d][1];//papa
                                                                                String nombreN8 = ctasNivel8[d][2];
                                                                                String tipoContaq8 = ctasNivel8[d][3];//tipo
                                                                                int naturaleza8 = getNaturaleza(tipoContaq8);
                                                                                  int nivel8=8;
                                                                                if (SubCtade.startsWith("0")) {
                                                                                    if (SubCtadeN2.startsWith("0")) {
                                                                                        if (SubCtadeN3.startsWith("0")) {
                                                                                            if (SubCtadeN4.startsWith("0")) {
                                                                                                if (SubCtadeN5.startsWith("0")) {
                                                                                                    if (SubCtadeN6.startsWith("0")) {
                                                                                                        if (SubCtadeN7.startsWith("0")) {
                                                                                                            cuentaRaiz = SubCtadeN8;
                                                                                            nivel8=1;
                                                                                                        } else {
                                                                                                            cuentaRaiz = SubCtadeN7;
                                                                                            nivel8=2;
                                                                                                        }
                                                                                                    } else {
                                                                                                        cuentaRaiz = SubCtadeN6;
                                                                                            nivel8=3;
                                                                                                    }
                                                                                                } else {
                                                                                                    cuentaRaiz = SubCtadeN5;
                                                                                            nivel8=4;
                                                                                                }
                                                                                            } else {
                                                                                                cuentaRaiz = SubCtadeN4;
                                                                                            nivel8=5;
                                                                                            }
                                                                                        } else {
                                                                                            cuentaRaiz = SubCtadeN3;
                                                                                            nivel8=6;
                                                                                        }
                                                                                    } else {
                                                                                        cuentaRaiz = SubCtadeN2;
                                                                                        nivel8=7;
                                                                                    }
                                                                                }
                                                                                if (!SubCtadeN8.startsWith("0")) {
                                                                                    if (SubCtadeN7.startsWith("0")) {
                                                                                        if (SubCtadeN6.startsWith("0")) {
                                                                                            if (SubCtadeN5.startsWith("0")) {
                                                                                                if (SubCtadeN4.startsWith("0")) {
                                                                                                    if (SubCtadeN3.startsWith("0")) {
                                                                                                        if (SubCtadeN2.startsWith("0")) {
                                                                                                            if (SubCtade.startsWith("0")) {
                                                                                                                registrarCuenta(getHijos.completaCeros(SubCtadeN8) + "1", "A", nombreN8, "-1", getHijos.completaCeros(cuentaRaiz) + "1", "1", naturaleza8);
                                                                                                            } else {
                                                                                                                registrarCuenta(getHijos.completaCeros(SubCtadeN8) + nivel8, "D", nombreN8, getHijos.completaCeros(SubCtade) + (nivel8-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel8+"", naturaleza8);
                                                                                                            }
                                                                                                        } else {
                                                                                                            registrarCuenta(getHijos.completaCeros(SubCtadeN8) + nivel8, "D", nombreN8, getHijos.completaCeros(SubCtadeN2) + (nivel8-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel8+"", naturaleza8);
                                                                                                        }
                                                                                                    } else {
                                                                                                        registrarCuenta(getHijos.completaCeros(SubCtadeN8) + nivel8, "D", nombreN8, getHijos.completaCeros(SubCtadeN3) + (nivel8-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel8+"", naturaleza8);
                                                                                                    }
                                                                                                } else {
                                                                                                    registrarCuenta(getHijos.completaCeros(SubCtadeN8) + nivel8, "D", nombreN8, getHijos.completaCeros(SubCtadeN4) + (nivel8-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel8+"", naturaleza8);
                                                                                                }
                                                                                            } else {
                                                                                                registrarCuenta(getHijos.completaCeros(SubCtadeN8) + nivel8, "D", nombreN8, getHijos.completaCeros(SubCtadeN5) + (nivel8-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel8+"", naturaleza8);
                                                                                            }
                                                                                        } else {
                                                                                            registrarCuenta(getHijos.completaCeros(SubCtadeN8) + nivel8, "D", nombreN8, getHijos.completaCeros(SubCtadeN6) + (nivel8-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel8+"", naturaleza8);
                                                                                        }
                                                                                    } else {
                                                                                        registrarCuenta(getHijos.completaCeros(SubCtadeN8) + nivel8, "D", nombreN8, getHijos.completaCeros(SubCtadeN7) + (nivel8-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel8+"", naturaleza8);
                                                                                    }
                                                                                }
                                                                                if (obj.getHijosxNivel("", "", IdSubCtaDeN8, SubCtadeN8).length >= 0) {
                                                                                    String[][] ctasNivel9 = obj.getHijosxNivel("", "", IdSubCtaDeN8, SubCtadeN8);
                                                                                    for (int e = 0; e < ctasNivel9.length; e++) {//nivel 2
                                                                                        String SubCtadeN9 = ctasNivel9[e][1];//papa
                                                                                        String nombreN9 = ctasNivel9[e][2];
                                                                                        String tipoContaq9 = ctasNivel9[e][3];//tipo
                                                                                        int naturaleza9 = getNaturaleza(tipoContaq9);
                                                                                        int nivel9=9;
                                                                                        if (SubCtade.startsWith("0")) {
                                                                                            if (SubCtadeN2.startsWith("0")) {
                                                                                                if (SubCtadeN3.startsWith("0")) {
                                                                                                    if (SubCtadeN4.startsWith("0")) {
                                                                                                        if (SubCtadeN5.startsWith("0")) {
                                                                                                            if (SubCtadeN6.startsWith("0")) {
                                                                                                                if (SubCtadeN7.startsWith("0")) {
                                                                                                                    if (SubCtadeN8.startsWith("0")) {
                                                                                                                        cuentaRaiz = SubCtadeN9;
                                                                                                        nivel9=1;
                                                                                                                    } else {
                                                                                                                        cuentaRaiz = SubCtadeN8;
                                                                                                        nivel9=2;
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    cuentaRaiz = SubCtadeN7;
                                                                                                        nivel9=3;
                                                                                                                }
                                                                                                            } else {
                                                                                                                cuentaRaiz = SubCtadeN6;
                                                                                                        nivel9=4;
                                                                                                            }
                                                                                                        } else {
                                                                                                            cuentaRaiz = SubCtadeN5;
                                                                                                        nivel9=5;
                                                                                                        }
                                                                                                    } else {
                                                                                                        cuentaRaiz = SubCtadeN4;
                                                                                                        nivel9=6;
                                                                                                    }
                                                                                                } else {
                                                                                                    cuentaRaiz = SubCtadeN3;
                                                                                                    nivel9=7;
                                                                                                }
                                                                                            } else {
                                                                                                cuentaRaiz = SubCtadeN2;
                                                                                                nivel9=8;
                                                                                            }
                                                                                        }
                                                                                        if (!SubCtadeN9.startsWith("0")) {
                                                                                            if (SubCtadeN8.startsWith("0")) {
                                                                                                if (SubCtadeN7.startsWith("0")) {
                                                                                                    if (SubCtadeN6.startsWith("0")) {
                                                                                                        if (SubCtadeN5.startsWith("0")) {
                                                                                                            if (SubCtadeN4.startsWith("0")) {
                                                                                                                if (SubCtadeN3.startsWith("0")) {
                                                                                                                    if (SubCtadeN2.startsWith("0")) {
                                                                                                                        if (SubCtade.startsWith("0")) {
                                                                                                                            registrarCuenta(getHijos.completaCeros(SubCtadeN9) + "1", "A", nombreN9, "-1", getHijos.completaCeros(cuentaRaiz) + "1", "1", naturaleza9);
                                                                                                                        } else {
                                                                                                                            registrarCuenta(getHijos.completaCeros(SubCtadeN9) + nivel9, "D", nombreN9, getHijos.completaCeros(SubCtade) + (nivel9-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel9+"", naturaleza9);
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        registrarCuenta(getHijos.completaCeros(SubCtadeN9) + nivel9, "D", nombreN9, getHijos.completaCeros(SubCtadeN2) + (nivel9-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel9+"", naturaleza9);
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    registrarCuenta(getHijos.completaCeros(SubCtadeN9) + nivel9, "D", nombreN9, getHijos.completaCeros(SubCtadeN3) + (nivel9-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel9+"", naturaleza9);
                                                                                                                }
                                                                                                            } else {
                                                                                                                registrarCuenta(getHijos.completaCeros(SubCtadeN9) + nivel9, "D", nombreN9, getHijos.completaCeros(SubCtadeN4) + (nivel9-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel9+"", naturaleza9);
                                                                                                            }
                                                                                                        } else {
                                                                                                            registrarCuenta(getHijos.completaCeros(SubCtadeN9) + nivel9, "D", nombreN9, getHijos.completaCeros(SubCtadeN5) + (nivel9-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel9+"", naturaleza9);
                                                                                                        }
                                                                                                    } else {
                                                                                                        registrarCuenta(getHijos.completaCeros(SubCtadeN9) + nivel9, "D", nombreN9, getHijos.completaCeros(SubCtadeN6) + (nivel9-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel9+"", naturaleza9);
                                                                                                    }
                                                                                                } else {
                                                                                                    registrarCuenta(getHijos.completaCeros(SubCtadeN9) + nivel9, "D", nombreN9, getHijos.completaCeros(SubCtadeN7) + (nivel9-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel9+"", naturaleza9);
                                                                                                }
                                                                                            } else {
                                                                                                registrarCuenta(getHijos.completaCeros(SubCtadeN9) + nivel9, "D", nombreN9, getHijos.completaCeros(SubCtadeN8) + (nivel9-1), getHijos.completaCeros(cuentaRaiz) + "1", nivel9+"", naturaleza9);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (conexionContpaqi != null) {
                conexionContpaqi.close();
            }
            if (conexionCOI != null) {
                conexionCOI.close();
            }
            ActualizarTipoCta objAct = new ActualizarTipoCta();
            objAct.ActualizarTipoCta(jpbProgreso, controladorCOI, servidorCOI, puertoCOI, baseDatosCOI, usuarioCOI, contraseniaCOI, seleccionados, noEmpresa);
            objAct.actualizar();
            JOptionPane.showMessageDialog(null, "Los datos se guardaron correctamente");
            jpbProgreso.setValue(100);
            jpbProgreso.setString(" ** Exportacion finalizada**");
            jbtnTransferir.setEnabled(true);
            this.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "error " + ex);
        }
    }

    public Integer getNaturaleza(String tipoContaqAbr) {
        int naturaleza = 0;
        try {
            for (int i = 0; i < tiposCtas.length; i++) {
                String tipoContpaqArr = tiposCtas[i][0];
                if (tipoContpaqArr.equals(tipoContaqAbr)) {
                    if (tiposCtas[i][2].toString().equalsIgnoreCase("A")) {
                        naturaleza = 1;
                    } else {
                        naturaleza = 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return naturaleza;
    }

    public void registrarCuenta(String cta, String tipo, String nombre, String ctaPapa, String ctaRaiz, String nivel, int naturaleza) {
        try {
            objConexion2 = new CrearConexionFBoSQL(controladorCOI, servidorCOI, puertoCOI, baseDatosCOI, usuarioCOI, contraseniaCOI,false);
            conexionCOI = objConexion2.getConexion();
            String nomTabla = "";
            for (int i = 0; i < seleccionados.length; i++) {
                Banda banda = (Banda) seleccionados[i];
                String id = banda.getId();
                nomTabla = id + noEmpresa;

                PreparedStatement stmtQuery = conexionCOI.prepareStatement(""
                        + "INSERT INTO CUENTAS" + nomTabla + " (NUM_CTA,STATUS,TIPO,NOMBRE,DEPTSINO,BANDMULTI,BANDAJT,CTA_PAPA,CTA_RAIZ,NIVEL,CTA_COMP,NATURALEZA) "
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
                String SubctaNombre = "";
                if (nombre.length() > 40) {
                    SubctaNombre = nombre.substring(0, 39);
                } else {
                    SubctaNombre = nombre;
                }
                stmtQuery.setString(1, cta);//NUM CTA
                stmtQuery.setString(2, "A");//STATUS
                stmtQuery.setString(3, tipo);//TIPO acomulativa o detalle
                stmtQuery.setString(4, SubctaNombre);//NOMBRE
                stmtQuery.setString(5, "N");//DEPTSINO
                stmtQuery.setInt(6, 1);//BANDMULTI
                stmtQuery.setString(7, "N");//BANDAJT
                stmtQuery.setString(8, ctaPapa);//CTA_PAPA
                stmtQuery.setString(9, ctaRaiz);//CTA_RAIZ
                stmtQuery.setString(10, nivel);//NIVEL
                stmtQuery.setString(11, "");//CTA-COMP
                stmtQuery.setInt(12, naturaleza);//NATURALEZA deodora o acreedora
                stmtQuery.execute();
            }
            BigDecimal cont = new BigDecimal(indice + 1);
            BigDecimal prog = (cont.divide(new BigDecimal(jtblCatCtas.getRowCount()), 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
            String p_ent = new DecimalFormat("###,###").format(prog);
            jpbProgreso.setValue(Integer.parseInt(p_ent));
            jpbProgreso.setStringPainted(true);
            jpbProgreso.setString("Progreso de transferencia " + Integer.parseInt(p_ent) + " %");
            indice++;
            conexionCOI.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
