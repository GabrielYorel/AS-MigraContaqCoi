/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas;

import ctascontablesypolizas.clases.CrearConexionFBoSQL;
import ctascontablesypolizas.clases.HiloCExportPolizas;
import ctascontablesypolizas.clases.HiloListarPolizas;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Gabriel
 */
public class ListadoPolizas extends javax.swing.JInternalFrame {

    GUIPrincipal padre;
    boolean oculto;
    private javax.swing.table.DefaultTableModel modelo;
    private File fFilePROD;
    private String DevolucionesCompras;
    private Connection conexionContpaqi;
    private CrearConexionFBoSQL objConexion;
    private String controladorCont;
    private String RutaonombreBDCont;
    private String servidorCont;
    private String tipoCCont;
    private String usuarioCont;
    private String contraseniaCont;
    private int puertoCont;
    private String noEmp;
    private Connection conexionCOI;
    private String controladorCOI;
    private String RutaonombreBDCOI;
    private String servidorCOI;
    private String tipoCCOI;
    private String usuarioCOI;
    private String contraseniaCOI;
    private String puertoCOI;
    private String sql;
    private int numPolizas;
    private int anio;
    String noEmpre;
    int[] dxNiveles;
    public List oList;
    private boolean seguridadIntegrada;

    /**
     * Creates new form ListadoPolizas
     */
    public ListadoPolizas() {
        initComponents();        
//        this.setSize(741, 550);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                if (ke.getID() == java.awt.event.KeyEvent.KEY_RELEASED && ke.getKeyCode() == java.awt.event.KeyEvent.VK_F5) {
                    getVentanaAgregarPoliza();
                }
                return false;
            }
        });
        oList=new ArrayList();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnGuardar = new javax.swing.JButton();
        jBtnAgregar = new javax.swing.JButton();
        jBtnEliminar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jbtnExportarAContpaq = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jBtnVerModelo = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jBtnSalir = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTxtDirectorio = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jBtnAbrirDirectorio = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Listado de polizas CONTPAQ i®");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/polcontcoi.png"))); // NOI18N
        setMinimumSize(new java.awt.Dimension(741, 501));
        setName("ListPolizas"); // NOI18N
        setPreferredSize(new java.awt.Dimension(741, 501));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameActivated(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameDeactivated(evt);
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameDeiconified(evt);
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameIconified(evt);
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N.P", "Tipo", "Concepto", "Fecha", "Cargos", "Abonos"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setToolTipText("Pólizas ");
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jToolBar1.setBackground(new java.awt.Color(178, 233, 187));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jBtnGuardar.setBackground(new java.awt.Color(178, 233, 187));
        jBtnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/exportar.png"))); // NOI18N
        jBtnGuardar.setEnabled(false);
        jBtnGuardar.setFocusable(false);
        jBtnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGuardarActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnGuardar);

        jBtnAgregar.setBackground(new java.awt.Color(178, 233, 187));
        jBtnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        jBtnAgregar.setToolTipText("Agregar");
        jBtnAgregar.setEnabled(false);
        jBtnAgregar.setFocusable(false);
        jBtnAgregar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnAgregar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnAgregarActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnAgregar);

        jBtnEliminar.setBackground(new java.awt.Color(178, 233, 187));
        jBtnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jBtnEliminar.setToolTipText("Eliminar");
        jBtnEliminar.setEnabled(false);
        jBtnEliminar.setFocusable(false);
        jBtnEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnEliminar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnEliminarActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnEliminar);
        jToolBar1.add(jSeparator2);

        jbtnExportarAContpaq.setBackground(new java.awt.Color(178, 233, 187));
        jbtnExportarAContpaq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Transferir.png"))); // NOI18N
        jbtnExportarAContpaq.setEnabled(false);
        jbtnExportarAContpaq.setFocusable(false);
        jbtnExportarAContpaq.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnExportarAContpaq.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnExportarAContpaq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnExportarAContpaqActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtnExportarAContpaq);
        jToolBar1.add(jSeparator3);

        jBtnVerModelo.setBackground(new java.awt.Color(178, 233, 187));
        jBtnVerModelo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/verModelo.png"))); // NOI18N
        jBtnVerModelo.setToolTipText("Ver modelo");
        jBtnVerModelo.setEnabled(false);
        jBtnVerModelo.setFocusable(false);
        jBtnVerModelo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnVerModelo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnVerModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnVerModeloActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnVerModelo);
        jToolBar1.add(jSeparator1);

        jBtnSalir.setBackground(new java.awt.Color(178, 233, 187));
        jBtnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salir.png"))); // NOI18N
        jBtnSalir.setToolTipText("Salir");
        jBtnSalir.setFocusable(false);
        jBtnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnSalirActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnSalir);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Ruta para guardar el archivo de excel"));

        jTxtDirectorio.setEditable(false);

        jLabel2.setText("Directorio del archivo:");

        jBtnAbrirDirectorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/examinar.png"))); // NOI18N
        jBtnAbrirDirectorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnAbrirDirectorioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTxtDirectorio, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jBtnAbrirDirectorio, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jBtnAbrirDirectorio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jTxtDirectorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel2))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void configurarTabla() {
        try {
            modelo = new javax.swing.table.DefaultTableModel() {
                public boolean isCellEditable(int fila, int col) {
                    return false;
                }
                Class[] tipos = new Class[]{Object.class, String.class, String.class, String.class, String.class, String.class, String.class};

                public Class getColumnClass(int col) {
                    return tipos[col];
                }
            };
            modelo.addColumn("N.P");
            modelo.addColumn("Tipo");
            modelo.addColumn("Folio");
            modelo.addColumn("Concepto");
            modelo.addColumn("Fecha");
            modelo.addColumn("Cargos");
            modelo.addColumn("Abonos");
            jTable1.setModel(modelo);

            //Reordenador por columna
            jTable1.setRowSorter(new javax.swing.table.TableRowSorter(modelo));

            //Asignar tamaños de columna
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(10);//np
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(30);//tipo
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(30);//folio
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(120);//concepto
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(20);//fecha
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(20);//cargos
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(20);//abonos
            //Selección sencilla
            jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

            //No permitir mover las columnas
            jTable1.getTableHeader().setReorderingAllowed(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void llenarTabla(java.util.List olistR, String sql, int numPolizas, int anio, boolean cargarTodo) {
        try {
            limpiarTabla();
            this.sql = sql;
            this.numPolizas = numPolizas;
            this.anio = anio;
            oList = olistR;
            jbtnExportarAContpaq.setEnabled(false);
            HiloListarPolizas miHilo = new HiloListarPolizas(cargarTodo, jLabel1, jBtnGuardar, anio, sql, olistR, jbtnExportarAContpaq, jTable1, numPolizas,
                    jProgressBar1, modelo, controladorCont, servidorCont, puertoCont, usuarioCont, contraseniaCont, RutaonombreBDCont,seguridadIntegrada);
            miHilo.padre = this;
            miHilo.start();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Error ", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int noClic = evt.getClickCount();
        if (noClic == 2) {
            getDetallePoliza();
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jBtnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnAgregarActionPerformed
        // TODO add your handling code here:
        getVentanaAgregarPoliza();
    }//GEN-LAST:event_jBtnAgregarActionPerformed
    public void getVentanaAgregarPoliza() {
        try {
            if (conexionContpaqi != null) {
                conexionContpaqi.close();
                this.setVisible(false);
                InterfaseContpaqi objPoliza = new InterfaseContpaqi();
                getDesktopPane().add(objPoliza);
                objPoliza.olist = this.oList;
                objPoliza.padre = this;
                objPoliza.controlador = this.controladorCont;
                objPoliza.servidor = this.servidorCont;
                objPoliza.puerto = this.puertoCont;
                objPoliza.seguridadIntegrada = this.seguridadIntegrada;
                objPoliza.RutaonombreBDCont = this.RutaonombreBDCont;
                objPoliza.usuario = this.usuarioCont;
                objPoliza.contrasenia = this.contraseniaCont;
                objPoliza.noEmpre=noEmpre;
                objPoliza.setTitle("Filtrado para pólizas de Contpaq i");
                objPoliza.setVisible(true);
                objPoliza.setSelected(true);
            } else {
                if (conexionContpaqi != null) {
                    conexionContpaqi.close();
                }
                JOptionPane.showMessageDialog(this, "Seleccione una base de datos para trabajar", "No ha seleccionado una base de datos", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jBtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnEliminarActionPerformed
        // TODO add your handling code here:
        try {
            int fila = (Integer) jTable1.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                int opc = JOptionPane.showConfirmDialog(this, "¿Desea eliminar la pólizas?", "Eliminar Pólizas", JOptionPane.YES_NO_OPTION);
                if (opc == JOptionPane.YES_OPTION) {
                    Object[][] pol = (Object[][]) oList.get(fila);
                    modelo.removeRow(fila);
                    oList.remove(fila);
                    limpiarTabla();
                    llenarTabla(oList, sql, numPolizas, anio, false);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jBtnEliminarActionPerformed
    public void limpiarTabla() {
        for (int i = jTable1.getRowCount() - 1; i >= 0; i--) {
            modelo.removeRow(i);
        }
    }
    private void jBtnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnSalirActionPerformed
        // TODO add your handling code here:
        salirFin();
    }//GEN-LAST:event_jBtnSalirActionPerformed

    public void salirFin() {
        try {
            padre.jMIConfParams.setEnabled(true);
            padre.jbtnImportarCtaCuentas.setEnabled(true);
            padre.jbtnImportarPoliza.setEnabled(true);
            padre.jMIReporte.setEnabled(true);
            padre.jMIPolizas.setEnabled(true);
            padre.banderaReportes = false;
            this.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
this.setLocation(this.getParent().getWidth() / 2 - this.getWidth() / 2, this.getParent().getHeight() / 2 - this.getHeight() / 2 - 20);
        leerConfiguracion();
        leerConfiguracionNieles();
        configurarTabla();
    }//GEN-LAST:event_formInternalFrameOpened

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        salirFin();
    }//GEN-LAST:event_formInternalFrameClosing
    public void leerConfiguracionNieles() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            File confsys = new File("ConfSistema.xml");
            if (confsys.exists()) {
                Document doc = dBuilder.parse(confsys);
                doc.getDocumentElement().normalize();
                /*
                 * lectura de los digitos por nivel de COI digitosPorNivel
                 */
                NodeList NLDirComunDPN = doc.getElementsByTagName("digitosPorNivel");
                Node NodeNLDPN = NLDirComunDPN.item(0);
                Element elementoNodeNLDPN = (Element) NodeNLDPN;
                int nivel1 = Integer.parseInt(getTagValue("nivel1", elementoNodeNLDPN));
                int nivel2 = Integer.parseInt(getTagValue("nivel2", elementoNodeNLDPN));
                int nivel3 = Integer.parseInt(getTagValue("nivel3", elementoNodeNLDPN));
                int nivel4 = Integer.parseInt(getTagValue("nivel4", elementoNodeNLDPN));
                int nivel5 = Integer.parseInt(getTagValue("nivel5", elementoNodeNLDPN));
                int nivel6 = Integer.parseInt(getTagValue("nivel6", elementoNodeNLDPN));
                int nivel7 = Integer.parseInt(getTagValue("nivel7", elementoNodeNLDPN));
                int nivel8 = Integer.parseInt(getTagValue("nivel8", elementoNodeNLDPN));
                int nivel9 = Integer.parseInt(getTagValue("nivel9", elementoNodeNLDPN));
            } else {
                JOptionPane.showMessageDialog(this, "No se encontro un archivo de confguracion para la aplicación", "Falta un archivo", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void jBtnVerModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnVerModeloActionPerformed
        // TODO add your handling code here:
        getDetallePoliza();
    }//GEN-LAST:event_jBtnVerModeloActionPerformed

    public void getDetallePoliza() {
        try {
            int fila = (Integer) jTable1.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                Object[][] pol = (Object[][]) oList.get(fila);
                String tipo = "";
                int tipoPol = Integer.parseInt(pol[0][0] + "");
                String folioPol = pol[1][0] + "";
                String fechaPol = pol[3][0] + "";
                if (tipoPol == 1) {
                    tipo = "Ig (Ingreso)";
                } else if (tipoPol == 2) {
                    tipo = "Eg (Egreso)";
                } else if (tipoPol == 3) {
                    tipo = "Dr (Diario)";
                }
                ModeloPoliza objDoctosPol = new ModeloPoliza(null, true);
                objDoctosPol.padre = this;
                objDoctosPol.setTitle("Poliza " + tipo + " Folio: " + folioPol + " Fecha: " + fechaPol);
                objDoctosPol.poliza = pol;
                objDoctosPol.setVisible(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void jBtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGuardarActionPerformed
        // TODO add your handling code here:
        if (!jTxtDirectorio.getText().trim().equalsIgnoreCase("")) {
            try {
                File archivoExcel = new File(jTxtDirectorio.getText().replace("\\", "/"));
                archivoExcel.createNewFile();
                ctascontablesypolizas.clases.Exportar_excelPol excelExporter = new ctascontablesypolizas.clases.Exportar_excelPol(dxNiveles,oList, archivoExcel, "Póliza");
                if (excelExporter.export()) {
                    JOptionPane.showMessageDialog(null, "DATOS EXPORTADOS CON EXITO!");
                    llama();
                } else {
                    JOptionPane.showMessageDialog(this, excelExporter.getMensaje());
                }
            } catch (IOException ex) {
                Logger.getLogger(ListadoPolizas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ListadoPolizas.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Indique el directorio en dode se guardara el archivo", "Sin directorio", JOptionPane.ERROR_MESSAGE);
            jBtnAbrirDirectorio.requestFocus();
        }
    }//GEN-LAST:event_jBtnGuardarActionPerformed

    public void llama() {
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + jTxtDirectorio.getText().replace("\\", "/"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cierre el archivo para continuar " + e);
        }
    }
    private void jBtnAbrirDirectorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnAbrirDirectorioActionPerformed
        // TODO add your handling code here:
        try {
            JFileChooser fileChooser = new JFileChooser();            
            if (fFilePROD != null) {
                if (!jTxtDirectorio.getText().trim().equalsIgnoreCase("")) {
                    fileChooser.setCurrentDirectory(new File(jTxtDirectorio.getText()));
                }
            }
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Todos los archivos *.xls", "xls", "Excel"));//filtro para ver solo archivos .edu
            int seleccion = fileChooser.showSaveDialog(null);
            try {
                if (seleccion == JFileChooser.APPROVE_OPTION) {//comprueba si ha presionado el boton de aceptar
                    File JFC = fileChooser.getSelectedFile();
                    String PATH = JFC.getAbsolutePath();//obtenemos el path del archivo a guardar
                    File temp = null;
                    String nomb = "";
                    //comprobamos si a la hora de guardar obtuvo la extension y si no se la asignamos
                    if (!(PATH.endsWith(".xls"))) {
                        temp = new File(PATH + ".xls");
                        nomb = PATH + ".xls";
                        JFC.renameTo(temp);//renombramos el archivo
                    } else {
                        nomb = PATH;
                        temp = new File(PATH);
                    }
                    if (temp.exists()) {                        
                        int opc2 = JOptionPane.showConfirmDialog(this, "\n"
                                + "¿El archivo ya existe desea reemplazarlo?", "Archivo duplicado", JOptionPane.YES_NO_OPTION);
                        if (opc2 == JOptionPane.YES_OPTION) {
                           jTxtDirectorio.setText(nomb);
                        }                        
                    } else {
                        jTxtDirectorio.setText(nomb);
                    }
                }
            } catch (Exception e) {//por alguna excepcion salta un mensaje de error
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al guardar el archivo!", "Oops! Error", JOptionPane.ERROR_MESSAGE);
            }
            
            
            
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }//GEN-LAST:event_jBtnAbrirDirectorioActionPerformed

    private void jbtnExportarAContpaqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExportarAContpaqActionPerformed
        // TODO add your handling code here:
        try {
            exportarPolizas("Adjuntar");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jbtnExportarAContpaqActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        // TODO add your handling code here:-513
        jScrollPane1.setSize(new Dimension(this.getWidth(), this.getHeight()-169));
    }//GEN-LAST:event_formComponentResized

    private void formInternalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeiconified
        // TODO add your handling code here:
    }//GEN-LAST:event_formInternalFrameDeiconified

    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
        // TODO add your handling code here:
    }//GEN-LAST:event_formInternalFrameIconified

    private void formInternalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeactivated
        // TODO add your handling code here:
    }//GEN-LAST:event_formInternalFrameDeactivated

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_formInternalFrameActivated

    public void leerConfiguracion() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File("ConfSistema.xml"));
            doc.getDocumentElement().normalize();
            /*
             * obtener el numero de la empresa
             */
            NodeList NLEmpresa = doc.getElementsByTagName("Empresa");
            Node NodeNLEmpresa = NLEmpresa.item(0);
            Element elementoNodeNLEmpresa = (Element) NodeNLEmpresa;
            noEmp = getTagValue("NoEmp", elementoNodeNLEmpresa);
            String cadena = "";
            //obtener configuracion de conexion a COntpaq
            NodeList NLRutaonombreBDCont = doc.getElementsByTagName("ConexionCONT");
            Node NodeNLRutaonombreBDCont = NLRutaonombreBDCont.item(0);
            Element elementoNodeNLRutaonombreBDCont = (Element) NodeNLRutaonombreBDCont;

            controladorCont = getTagValue("controladorBD", elementoNodeNLRutaonombreBDCont);
            RutaonombreBDCont = getTagValue("rutaonombreBD", elementoNodeNLRutaonombreBDCont);
            servidorCont = getTagValue("ip", elementoNodeNLRutaonombreBDCont);
            tipoCCont = getTagValue("tipoC", elementoNodeNLRutaonombreBDCont);
            usuarioCont = getTagValue("usuario", elementoNodeNLRutaonombreBDCont);
            contraseniaCont = getTagValue("contrasenia", elementoNodeNLRutaonombreBDCont);
            puertoCont = Integer.parseInt(getTagValue("puerto", elementoNodeNLRutaonombreBDCont));
            seguridadIntegrada = Boolean.parseBoolean(getTagValue("IntegratedSecurity", elementoNodeNLRutaonombreBDCont));
            objConexion = new CrearConexionFBoSQL(controladorCont, servidorCont, puertoCont,
                    RutaonombreBDCont, usuarioCont, contraseniaCont,seguridadIntegrada);
            conexionContpaqi = objConexion.getConexion();
            if (conexionContpaqi == null) {
                cadena += " CONTPAQ i\n";
            }
            //obtener configuracion de conexion a coi
            NodeList NLRutaonombreBD = doc.getElementsByTagName("ConexionSAE");
            Node NodeNLRutaonombreBD = NLRutaonombreBD.item(0);
            Element elementoNodeNLRutaonombreBD = (Element) NodeNLRutaonombreBD;

            controladorCOI = getTagValue("controladorBD", elementoNodeNLRutaonombreBD);
            RutaonombreBDCOI = getTagValue("rutaonombreBD", elementoNodeNLRutaonombreBD);
            servidorCOI = getTagValue("ip", elementoNodeNLRutaonombreBD);
            tipoCCOI = getTagValue("tipoC", elementoNodeNLRutaonombreBD);
            usuarioCOI = getTagValue("usuario", elementoNodeNLRutaonombreBD);
            contraseniaCOI = getTagValue("contrasenia", elementoNodeNLRutaonombreBD);
            puertoCOI = getTagValue("puerto", elementoNodeNLRutaonombreBD);
            objConexion = new CrearConexionFBoSQL(controladorCOI, servidorCOI, Integer.parseInt(puertoCOI),
                    RutaonombreBDCOI, usuarioCOI, contraseniaCOI,false);
            conexionCOI = objConexion.getConexion();
            if (conexionCOI == null) {
                cadena += " APEL COI\n ";
            }

            if (conexionContpaqi == null || conexionCOI == null) {
                JOptionPane.showConfirmDialog(this, "Error al realizar la conexión de:\n " + cadena + "\n\n"
                        + "No es posible continuar con la tarea\n", "Error en la conexión", JOptionPane.ERROR_MESSAGE);
                cerrarSesion();
            } else {
                conexionContpaqi.close();
                conexionCOI.close();
                jBtnGuardar.setEnabled(true);
                jBtnAgregar.setEnabled(true);
                jBtnEliminar.setEnabled(true);
                jBtnVerModelo.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cerrarSesion() {
        padre.jMIReporte.setEnabled(true);
        padre.jMIPolizas.setEnabled(true);
        padre.jbtnImportarCtaCuentas.setEnabled(true);
        padre.jbtnImportarPoliza.setEnabled(true);
        padre.banderaReportes = false;
        padre.jMIConfParams.setEnabled(true);
        this.dispose();

    }

    public String getTagValue(String tag, Element elemento) {
        NodeList lista = elemento.getElementsByTagName(tag).item(0).getChildNodes();
        Node valor = (Node) lista.item(0);
        return valor.getNodeValue();
    }

    public void exportarPolizas(String tipoExportacion) throws ClassNotFoundException {
        try {
            jbtnExportarAContpaq.setEnabled(false);
            HiloCExportPolizas miHilo = new HiloCExportPolizas(noEmp, oList, jbtnExportarAContpaq,
                    jProgressBar1, controladorCOI, servidorCOI, Integer.parseInt(puertoCOI), usuarioCOI, contraseniaCOI, RutaonombreBDCOI);
            miHilo.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnAbrirDirectorio;
    private javax.swing.JButton jBtnAgregar;
    private javax.swing.JButton jBtnEliminar;
    private javax.swing.JButton jBtnGuardar;
    private javax.swing.JButton jBtnSalir;
    private javax.swing.JButton jBtnVerModelo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    public javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField jTxtDirectorio;
    private javax.swing.JButton jbtnExportarAContpaq;
    // End of variables declaration//GEN-END:variables
}