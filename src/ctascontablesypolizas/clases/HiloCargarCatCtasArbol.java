/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas.clases;

import ctascontablesypolizas.CatCtas;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Gabriel-Diaz
 */
public class HiloCargarCatCtasArbol extends Thread {

    private JTree jTree1;
    private JProgressBar jpbProgreso;
    private String servidor;
    private int puerto;
    private String controlador, usuario, contrasenia, baseDatos;
    private Connection conexionContpaqi;
    private JButton jbtnExportar;
    private JButton jbtnTransferir;
    private CrearConexionFBoSQL objConexion;
    private DefaultTreeModel modelo;
    public CatCtas padre;
    private int indice;
    private int numCtas;
    int[] dxNiveles;
    private boolean seguridadIntegrada;
    public ArrayList<CuentaEntity> grafo = new ArrayList<CuentaEntity>(); 

    public HiloCargarCatCtasArbol(int[] dxNiveles, JButton jbtnTransferir, JButton jbtnExportar, JTree jtblCatCtas, JProgressBar jpbProgreso,
            DefaultTreeModel modelonuevo, String controlador, String servidor, int puerto, String usuario, String contrasenia, String baseDatos,
            boolean seguridadIntegrada) {
        super("hiloCargarCatCtas");
        this.seguridadIntegrada = seguridadIntegrada;
        this.dxNiveles = dxNiveles;
        this.jbtnTransferir = jbtnTransferir;
        this.jbtnExportar = jbtnExportar;
        this.jTree1 = jtblCatCtas;
        this.jpbProgreso = jpbProgreso;
        this.modelo = modelonuevo;
        this.servidor = servidor;
        this.puerto = puerto;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.baseDatos = baseDatos;
        this.controlador = controlador;
    }

    @Override
    public void run() {
        try {
            objConexion = new CrearConexionFBoSQL(controlador, servidor, puerto, baseDatos, usuario, contrasenia,seguridadIntegrada);
            conexionContpaqi = objConexion.getConexion();
            PreparedStatement stmtCountCtas = conexionContpaqi.prepareStatement("select count(*) FROM dbo.Cuentas WHERE Codigo LIKE '\\_%' {escape '\\'}");
            ResultSet rsCount = stmtCountCtas.executeQuery();
            int numCtas2 = 0;
            if (rsCount.next()) {
                numCtas2 = rsCount.getInt(1);
            }
            rsCount.close();
            PreparedStatement stmtCountCtas2 = conexionContpaqi.prepareStatement(
                    "select count(*) FROM dbo.Cuentas");
            ResultSet rsCount2 = stmtCountCtas2.executeQuery();
            numCtas = 0;
            if (rsCount2.next()) {
                numCtas = rsCount2.getInt(1);
            }
            rsCount2.close();

            PreparedStatement stmtQueryCta = conexionContpaqi.prepareStatement("select Id,Codigo FROM dbo.Cuentas WHERE Codigo LIKE '\\_%' {escape '\\'}");
            ResultSet rsCtasPapaCta = stmtQueryCta.executeQuery();
            String[] ctasPapas = new String[numCtas2];
            int indice = 0;
            while (rsCtasPapaCta.next()) {
                ctasPapas[indice] = rsCtasPapaCta.getString(2);
                indice++;
            }
            rsCtasPapaCta.close();
            stmtQueryCta.close();
            int indiceExtra = 0;
            for (int i = 0; i < ctasPapas.length; i++) {//nivel 0
                String codCtaNivel1 = ctasPapas[i];
                if (getHijosxNivel(codCtaNivel1).length >= 0) {
                    String[][] ctasRaiz = getHijosxNivel(codCtaNivel1);
                    for (int indx = 0; indx < ctasRaiz.length; indx++) {
                        indiceExtra++;
                    }
                }
            }
            numCtas = numCtas - (numCtas2 + indiceExtra);
            DefaultMutableTreeNode raiz = new DefaultMutableTreeNode(new Cuentas("Cuentas contables", "Cuentas contables", ""));
            for (int i = 0; i < ctasPapas.length; i++) {//nivel 0
                String codCtaNivel1 = ctasPapas[i];
                if (getHijosxNivel(codCtaNivel1).length >= 0) {
                    String[][] ctasRaiz = getHijosxNivel(codCtaNivel1);
                    for (int indx = 0; indx < ctasRaiz.length; indx++) {
                        String SubCtade = ctasRaiz[indx][1];//raiz cta
                        DefaultMutableTreeNode papa = new DefaultMutableTreeNode(new Cuentas(ctasRaiz[indx][1], SubCtade, ctasRaiz[indx][2]));
                        populateNode(papa, SubCtade);
                        raiz.add(papa);
                    }
                }
            }
            modelo = new DefaultTreeModel(raiz);
            jTree1.setModel(modelo);

            DefaultMutableTreeNode root = (DefaultMutableTreeNode) (jTree1.getModel().getRoot());
            Enumeration<?> enumeration = root.depthFirstEnumeration();
            while (enumeration.hasMoreElements()) {
                DefaultMutableTreeNode hijo = (DefaultMutableTreeNode) enumeration.nextElement();
                Cuentas obj = (Cuentas) hijo.getUserObject();

                if (obj.getId().startsWith("0")) {
                    for (int i = 0; i < hijo.getChildCount(); i++) {
                        DefaultMutableTreeNode childAt = (DefaultMutableTreeNode) hijo.getChildAt(i);
                        modelo.insertNodeInto(childAt, (DefaultMutableTreeNode) hijo.getParent(), i);
                    }
                    if (hijo.getChildCount() <= 0) {
                        modelo.removeNodeFromParent(hijo);
                    }
                }
            }
            jTree1.setModel(modelo);

            DefaultMutableTreeNode rootOrden = (DefaultMutableTreeNode) (jTree1.getModel().getRoot());
            int hijosRoot = rootOrden.getChildCount();
            for (int i = 0; i < hijosRoot; i++) {
                int min = i;
                //buscamos el menor
                for (int indx = i + 1; indx < hijosRoot; indx++) {
                    BigDecimal ctaIndx = new BigDecimal(((Cuentas) ((DefaultMutableTreeNode) rootOrden.getChildAt(indx)).getUserObject()).getId().toString());
                    BigDecimal ctaMin = new BigDecimal(((Cuentas) ((DefaultMutableTreeNode) rootOrden.getChildAt(min)).getUserObject()).getId());
                    if (ctaIndx.compareTo(ctaMin) == -1) {
                        min = indx;    //encontramos el menor número
                    }
                }
                if (i != min) {
                    //permutamos los valores
                    DefaultMutableTreeNode auxN = (DefaultMutableTreeNode) rootOrden.getChildAt(i);
                    DefaultMutableTreeNode auxMin = (DefaultMutableTreeNode) rootOrden.getChildAt(min);
                    modelo.insertNodeInto(auxMin, rootOrden, i);
                    modelo.insertNodeInto(auxN, rootOrden, min);
                }
            }
            modelo.reload();
            jTree1.setModel(modelo);
            int logCta = 0;
            for (int i = 0; i < dxNiveles.length; i++) {
                logCta += dxNiveles[i];
            }
            DefaultMutableTreeNode root2 = (DefaultMutableTreeNode) (jTree1.getModel().getRoot());
            Enumeration<?> enumeration2 = root2.depthFirstEnumeration();
            while (enumeration2.hasMoreElements()) {
                DefaultMutableTreeNode hijo = (DefaultMutableTreeNode) enumeration2.nextElement();
                Cuentas obj = (Cuentas) hijo.getUserObject();
                hijo.setUserObject(ponerGuines(obj.getId(), logCta) + " " + obj.getNombre());
            }
            jTree1.setModel(modelo);
            jTree1.setCellRenderer(new RenderArbol());
            padre.modelo = modelo;
            padre.modelo.reload();
            padre.jTree1 = this.jTree1;
            jpbProgreso.setValue(100);
            jpbProgreso.setStringPainted(true);
            jpbProgreso.setString("**Lectura finaliada 100%**");
            crearArbol();
            this.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "error " + ex);
        }
    }

    public static String completaCeros(String folio, int longi) {
        int longFolio = longi;
        String folioReal = (folio + "");
        while (folioReal.length() < longFolio) {
            folioReal = folioReal + "0";
        }
        return folioReal;
    }

    public String ponerGuines(String cuenta, int longi) {
        String nvaCuenta = "";
        try {            //           
            String nvaCuentatTemp = completaCeros(cuenta, longi);
            int cont = 0;
            for (int i = 0; i < dxNiveles.length; i++) {
                if (dxNiveles[i] != 0) {
                    nvaCuenta+=nvaCuentatTemp.substring(cont, (dxNiveles[i] + cont)) + "-";
                    cont += dxNiveles[i];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nvaCuenta.substring(0, nvaCuenta.length()-1);
    }

    public String[][] getHijosxNivel(String cod) {
        String[][] infoSubctas = null;
        try {
            PreparedStatement stmtQuery = conexionContpaqi.prepareStatement("Select COUNT (*) FROM dbo.Asociaciones asoc "
                    + "inner join dbo.Cuentas cta on (asoc.IdSubCtade=cta.Id)"
                    + " WHERE asoc.CtaSup=? AND asoc.IdSubCtade>10");
            stmtQuery.setString(1, cod);
            ResultSet rsCountRows = stmtQuery.executeQuery();
            int numRows = 0;
            if (rsCountRows.next()) {
                numRows = rsCountRows.getInt(1);
            }
            rsCountRows.close();
            stmtQuery.close();
            infoSubctas = new String[numRows][4];
            PreparedStatement stmtQueryInfo = conexionContpaqi.prepareStatement("select asoc.IdSubCtaDe"
                    + ",asoc.SubCtade,cta.Nombre,cta.Tipo "
                    + "FROM dbo.Asociaciones asoc "
                    + "inner join dbo.Cuentas cta on (asoc.IdSubCtade=cta.Id)"
                    + "WHERE asoc.CtaSup=? AND asoc.IdSubCtade>10");
            stmtQueryInfo.setString(1, cod);
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

    private boolean populateNode(DefaultMutableTreeNode node, String cta) {
        node.removeAllChildren();
        return populateNode(node, cta, 6);
    }

    private boolean populateNode(DefaultMutableTreeNode node, String cta, int depth) {
        String[][] hijosxNivel = getHijosxNivel(cta);
        if (hijosxNivel.length > 0) {
            for (int i = 0; i < hijosxNivel.length; i++) {
                DefaultMutableTreeNode curr =
                        new DefaultMutableTreeNode(new Cuentas(hijosxNivel[i][1], cta, hijosxNivel[i][2]));
                populateNode(curr, hijosxNivel[i][1], depth - 1);
                node.add(curr);
                BigDecimal cont = new BigDecimal(indice + 1);
                BigDecimal prog = (cont.divide(new BigDecimal(numCtas), 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
                String p_ent = new DecimalFormat("###,###").format(prog);
                jpbProgreso.setValue(Integer.parseInt(p_ent));
                jpbProgreso.setStringPainted(true);
                jpbProgreso.setString("Progreso de lectura " + Integer.parseInt(p_ent) + " %");
                indice++;
            }
        }
        return true;
    }
    
    
    public void crearArbol() throws SQLException{
        int numCtasPadre=0;
        PreparedStatement stmtCountCtas = conexionContpaqi.prepareStatement("SELECT count(*) FROM dbo.Cuentas WHERE Codigo LIKE '\\_%' {escape '\\'}");
        ResultSet rsCount = stmtCountCtas.executeQuery();
        if (rsCount.next()) {
            numCtasPadre = rsCount.getInt(1);
        }
        rsCount.close();
        
        int numCtas=0;
        PreparedStatement stmtCountCtas2 = conexionContpaqi.prepareStatement("select count(*) FROM dbo.Cuentas");
        ResultSet rsCount2 = stmtCountCtas2.executeQuery();
        if (rsCount2.next()) {
            numCtas = rsCount2.getInt(1);
        }
        rsCount2.close();
        int numCtasTotal = numCtas - numCtasPadre ;
        
        //sacar los datos de las cuentas
        PreparedStatement stmtCountCtas3 = conexionContpaqi.prepareStatement(""
          + "SELECT C.Codigo, C.Nombre, C.Tipo, C.EsBaja, C.ctaMayor, A.Id, A.IdCtaSup, A.IdSubCtade, A.CtaSup,A.SubCtade\n" +
            "FROM Cuentas C, Asociaciones A \n" +
            "WHERE C.Codigo  NOT  LIKE '[_]%' AND\n" +
            "C.Id = A.IdSubCtade;");
        ResultSet rsCount3 = stmtCountCtas3.executeQuery();
        while (rsCount3.next()) {
            CuentaEntity cuenta = new CuentaEntity();
            cuenta.setCodigo(rsCount3.getString(1));
            cuenta.setNombre(rsCount3.getString(2));
            cuenta.setTipo(rsCount3.getString(3).charAt(0));
            cuenta.setEsBaja(rsCount3.getBoolean(4));
            cuenta.setCtaMayor(rsCount3.getInt(5));
            cuenta.setIdAsociacion(rsCount3.getInt(6));
            cuenta.setIdPadre(rsCount3.getInt(7));
            cuenta.setId(rsCount3.getInt(8));

            //validar si son padres del arbol
            if(rsCount3.getString(9).startsWith("_")){
                cuenta.setIdPadre(-1);//-1 significa que es raiz del arbol
            }
            grafo.add(cuenta);
        }
        rsCount3.close();
        //todos los nodos fueron creados, proximo paso: crear las relaciones
        for(int i=0 ; i<grafo.size();i++){
            CuentaEntity cuenta = new CuentaEntity();
            CuentaEntity cuentaBuscada = new CuentaEntity();
            int idCuentaBuscada;
            cuenta = grafo.get(i);
            int idPadre = cuenta.getIdPadre();
            if(idPadre==-1){//si el nodo es padre se salta
            }
            else{
                //es un nodo hijo, se buscará al nodo padre para agregar al nodo en el arreglo de los nodos hijos
                idCuentaBuscada = getIdNodoPadre(idPadre);
                cuentaBuscada = grafo.get(idCuentaBuscada);
                cuentaBuscada.addHijo(cuenta);//se agrega el hijo en el arreglo de hijos del padre
                cuenta.setPadre(cuentaBuscada);//se agrega al padre
            }
        }//arbol contenido en arreglo llamado: vaciado
        //con todas las relaciones hechas solo con los hijos directos
    }

    private int getIdNodoPadre(int idPadre) {
        for (int i=0; i< grafo.size();i++){
            CuentaEntity cuenta = new CuentaEntity();
            cuenta = grafo.get(i);
            if(cuenta.getId()==idPadre){
                //padre encontrado, se regresa el objeto
                return i;
            }
            else{
                
            }
        }
        return 0;
    }

    

    public ArrayList<CuentaEntity> getGrafo() {
        return grafo;
    }

    public void setGrafo(ArrayList<CuentaEntity> grafo) {
        this.grafo = grafo;
    }

    public Connection getConexionContpaqi() {
        return conexionContpaqi;
    }


}
