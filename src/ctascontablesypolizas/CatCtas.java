/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas;

import ctascontablesypolizas.clases.HiloCargarCatCtas;
import ctascontablesypolizas.clases.CrearConexionFBoSQL;
import ctascontablesypolizas.clases.CuentaEntity;
import ctascontablesypolizas.clases.HiloCargarCatCtasArbol;
import ctascontablesypolizas.clases.ImportarExcel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Admin
 */
public class CatCtas extends javax.swing.JInternalFrame {

    GUIPrincipal padre;
    String controlador;
    String servidor;
    int puerto;
    String RutaonombreBDCont;
    String usuario;
    String contrasenia;
    protected DefaultTableModel modelonuevo;
    protected String[][] tiposCtas;
    protected CrearConexionFBoSQL objConexion;
    protected Connection conexionContpaqi;
    protected String puertoCOI;
    protected String contraseniaCOI;
    protected String usuarioCOI;
    protected String tipoCCOI;
    protected String servidorCOI;
    protected String RutaonombreBDCOI;
    protected String controladorCOI;
    protected Connection conexionCOI;
    protected CrearConexionFBoSQL objConexionCOI;
    String nomEmpre;
    protected String nomTabla;
    public DefaultTreeModel modelo;
    int[] dxNiveles;
    boolean seguridadIntegrada;
    public ArrayList<CuentaEntity> grafo = new ArrayList<CuentaEntity>(); 
    
    /**
     * Creates new form CatCtas
     */
    public CatCtas() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPMVista = new javax.swing.JPopupMenu();
        jToolBar1 = new javax.swing.JToolBar();
        jbtnExportar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jbtnExportar1 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jbtnTransferir = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jbtnSalir = new javax.swing.JButton();
        jtxtNumRegistros = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblCatCtas = new javax.swing.JTable();
        jpbProgreso = new javax.swing.JProgressBar();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jProgressBar1 = new javax.swing.JProgressBar();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Cuentas contables CONTPAQ i®");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ctascontcoi.png"))); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameIconified(evt);
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                formComponentHidden(evt);
            }
        });

        jToolBar1.setBackground(new java.awt.Color(178, 233, 187));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jbtnExportar.setBackground(new java.awt.Color(178, 233, 187));
        jbtnExportar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/exportar.png"))); // NOI18N
        jbtnExportar.setToolTipText("Exportar a MS Excel");
        jbtnExportar.setEnabled(false);
        jbtnExportar.setFocusable(false);
        jbtnExportar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnExportar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnExportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnExportarActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtnExportar);
        jToolBar1.add(jSeparator1);

        jbtnExportar1.setBackground(new java.awt.Color(178, 233, 187));
        jbtnExportar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/excel.png"))); // NOI18N
        jbtnExportar1.setToolTipText("Exportar a MS Excel");
        jbtnExportar1.setFocusable(false);
        jbtnExportar1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnExportar1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnExportar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnExportar1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtnExportar1);
        jToolBar1.add(jSeparator3);

        jbtnTransferir.setBackground(new java.awt.Color(178, 233, 187));
        jbtnTransferir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Transferir.png"))); // NOI18N
        jbtnTransferir.setToolTipText("Importar a ASPEL COI");
        jbtnTransferir.setEnabled(false);
        jbtnTransferir.setFocusable(false);
        jbtnTransferir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnTransferir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnTransferir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnTransferirActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtnTransferir);
        jToolBar1.add(jSeparator2);

        jbtnSalir.setBackground(new java.awt.Color(178, 233, 187));
        jbtnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salir.png"))); // NOI18N
        jbtnSalir.setToolTipText("Salir al menu principal");
        jbtnSalir.setFocusable(false);
        jbtnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSalirActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtnSalir);

        jtxtNumRegistros.setEnabled(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Registros:");
        jLabel1.setToolTipText("");

        jTabbedPane1.setBackground(new java.awt.Color(178, 233, 187));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Catalogo de cuentas  de CONTPAQ i®, preparadas para exportación a ASPEL® COI", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setToolTipText("Cuentas contables");
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jtblCatCtas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N.P", "Codigo", "Descripcción", "Tipo (Naturaleza) Contpaq i", "Tipo (Naturaleza) COI"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtblCatCtas.setComponentPopupMenu(jPMVista);
        jScrollPane1.setViewportView(jtblCatCtas);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jpbProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpbProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );

        jTabbedPane1.addTab("Vista en tabla", jPanel1);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.setExpandsSelectedPaths(false);
        jTree1.setRootVisible(false);
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTree1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Vista en arbol", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtNumRegistros, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(jTabbedPane1)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtNumRegistros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void configurarTabla() {
        modelonuevo = new javax.swing.table.DefaultTableModel() {
            public boolean isCellEditable(int fila, int col) {
                return false;
            }
        ;
        };

        modelonuevo.addColumn("Id");//1
        modelonuevo.addColumn("Codigo");//2
        modelonuevo.addColumn("Descripcción");//3
        modelonuevo.addColumn("Departamental");//3
        modelonuevo.addColumn("Tipo (Naturaleza) Contpaq i");//4
        modelonuevo.addColumn("Tipo (Naturaleza) COI");//4



        //Reordenador por columna
        jtblCatCtas.setRowSorter(new javax.swing.table.TableRowSorter(modelonuevo));

        //Selección sencilla
        jtblCatCtas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        //No permitir mover las columnas
        jtblCatCtas.getTableHeader().setReorderingAllowed(false);
        for (int i = 0; i < jtblCatCtas.getColumnCount(); i++) {
            jtblCatCtas.getColumnModel().getColumn(i).setResizable(true);
            jtblCatCtas.getTableHeader().getColumnModel().getColumn(i).setResizable(true);
        }
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(modelonuevo);
        jtblCatCtas.setRowSorter(sorter);
        jtblCatCtas.setModel(modelonuevo);
    }

    public void limpiarTabla() {
        for (int i = jtblCatCtas.getRowCount() - 1; i >= 0; i--) {
            modelonuevo.removeRow(i);
        }
    }

    public void llenarTabla() {
        try {
            limpiarTabla();
            HiloCargarCatCtas miHilo = new HiloCargarCatCtas(dxNiveles,jbtnTransferir, tiposCtas, jbtnExportar, jtblCatCtas,
                    jtxtNumRegistros, jpbProgreso, modelonuevo, controlador, servidor, puerto, usuario, contrasenia, RutaonombreBDCont,seguridadIntegrada);
            miHilo.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        this.setLocation(this.getParent().getWidth() / 2 - this.getWidth() / 2, this.getParent().getHeight() / 2 - this.getHeight() / 2 - 20);
        leerTiposDeCuenta();
//        leerConfiguracionNieles();
        configurarTabla();
        cargarPopupMenu();
        llenarTabla();
        cargarArbool();
    }//GEN-LAST:event_formInternalFrameOpened
    public void cargarArbool() {
        try {
            HiloCargarCatCtasArbol miHilo = new HiloCargarCatCtasArbol(dxNiveles,jbtnTransferir, jbtnExportar, jTree1,
                    jProgressBar1, modelo, controlador, servidor, puerto, usuario, contrasenia, RutaonombreBDCont,seguridadIntegrada);
            miHilo.padre = this;
            miHilo.start();
            grafo = miHilo.getGrafo();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error " + e.toString());
        }
    }

    private void leerTiposDeCuenta() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            File archvo = new File("tiposCtas.xml");
            if (archvo.exists()) {
                Document doc = dBuilder.parse(archvo);
                doc.getDocumentElement().normalize();
                //obtener directorio comun sae
                NodeList NLDirComunSAE = doc.getElementsByTagName("Cta");
                tiposCtas = new String[NLDirComunSAE.getLength()][4];
                for (int i = 0; i < NLDirComunSAE.getLength(); i++) {
                    NodeList NLTC = NLDirComunSAE.item(i).getChildNodes();
                    String tipoContpaqi = "";
                    String descrContpaqi = "";
                    String tipoCOI = "";
                    String descrCoi = "";
                    for (int j = 0; j < NLTC.getLength(); j++) {
                        String nodo = NLTC.item(j).getNodeName();
                        if (nodo.equals("tipoContpaqi")) {
                            tipoContpaqi = NLTC.item(j).getTextContent();
                        } else if (nodo.equals("descrContpaqi")) {
                            descrContpaqi = NLTC.item(j).getTextContent();
                        } else if (nodo.equals("tipoCOI")) {
                            tipoCOI = NLTC.item(j).getTextContent();
                        } else if (nodo.equals("descrCoi")) {
                            descrCoi = NLTC.item(j).getTextContent();
                        }
                    }
                    tiposCtas[i][0] = tipoContpaqi;
                    tiposCtas[i][1] = descrContpaqi;
                    tiposCtas[i][2] = tipoCOI;
                    tiposCtas[i][3] = descrCoi;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Falta un archivo de configuración", "Falta archivo", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOcultarColumnasJTable(JTable tbl, int columna) {
        try {
            tbl.getColumnModel().getColumn(columna).setMaxWidth(0);
            tbl.getColumnModel().getColumn(columna).setMinWidth(0);
            tbl.getColumnModel().getColumn(columna).setResizable(false);
            tbl.getTableHeader().getColumnModel().getColumn(columna).setMaxWidth(0);
            tbl.getTableHeader().getColumnModel().getColumn(columna).setMinWidth(0);
            tbl.getTableHeader().getColumnModel().getColumn(columna).setResizable(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setMostrarColumnasJTable(JTable tbl, int columna, int max, int min) {
        try {
            tbl.getColumnModel().getColumn(columna).setMaxWidth(max);
            tbl.getColumnModel().getColumn(columna).setMinWidth(max);
            tbl.getColumnModel().getColumn(columna).setResizable(true);
            tbl.getTableHeader().getColumnModel().getColumn(columna).setMaxWidth(max);
            tbl.getTableHeader().getColumnModel().getColumn(columna).setMinWidth(min);
            tbl.getTableHeader().getColumnModel().getColumn(columna).setResizable(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getTagValue(String tag, Element elemento) {
        NodeList lista = elemento.getElementsByTagName(tag).item(0).getChildNodes();
        Node valor = (Node) lista.item(0);
        return valor.getNodeValue();
    }
    public void cargarPopupMenu() {
        ItemListener iListener = new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                AbstractButton aButton = (AbstractButton) event.getSource();
                int state = event.getStateChange();
                JCheckBoxMenuItem item = (JCheckBoxMenuItem) event.getItem();
                String nombreItem = item.getText();
                if (state == ItemEvent.SELECTED) {
                    if (nombreItem.equalsIgnoreCase("N.P")) {
                        setMostrarColumnasJTable(jtblCatCtas, 0, 30, 30);
                    } else if (nombreItem.equalsIgnoreCase("Codigo")) {
                        setMostrarColumnasJTable(jtblCatCtas, 1, 90, 80);
                    } else if (nombreItem.equalsIgnoreCase("Descripcción")) {
                        setMostrarColumnasJTable(jtblCatCtas, 2, 250, 250);
                    } else if (nombreItem.equalsIgnoreCase("Departamental")) {
                        setMostrarColumnasJTable(jtblCatCtas, 3, 150, 150);
                    } else if (nombreItem.equalsIgnoreCase("Tipo (Naturaleza) Contpaq i")) {
                        setMostrarColumnasJTable(jtblCatCtas, 4, 100, 100);
                    } else if (nombreItem.equalsIgnoreCase("Tipo (Naturaleza) COI")) {
                        setMostrarColumnasJTable(jtblCatCtas, 5, 100, 100);
                    }
                } else {
                    if (nombreItem.equalsIgnoreCase("N.P")) {
                        setOcultarColumnasJTable(jtblCatCtas, 0);
                    } else if (nombreItem.equalsIgnoreCase("Codigo")) {
                        setOcultarColumnasJTable(jtblCatCtas, 1);
                    } else if (nombreItem.equalsIgnoreCase("Descripcción")) {
                        setOcultarColumnasJTable(jtblCatCtas, 2);
                    }  else if (nombreItem.equalsIgnoreCase("Departamental")) {
                        setOcultarColumnasJTable(jtblCatCtas, 3);
                    }else if (nombreItem.equalsIgnoreCase("Tipo (Naturaleza) Contpaq i")) {
                        setOcultarColumnasJTable(jtblCatCtas, 4);
                    } else if (nombreItem.equalsIgnoreCase("Tipo (Naturaleza) COI")) {
                        setOcultarColumnasJTable(jtblCatCtas, 5);
                    }
                }
            }
        };
        JCheckBoxMenuItem jMcveAlmacen = new JCheckBoxMenuItem("N.P", true);
        jPMVista.add(jMcveAlmacen);
        jMcveAlmacen.addItemListener(iListener);
        JCheckBoxMenuItem jMDescrAlmacen = new JCheckBoxMenuItem("Codigo", true);
        jPMVista.add(jMDescrAlmacen);
        jMDescrAlmacen.addItemListener(iListener);
        JCheckBoxMenuItem jMCveProd = new JCheckBoxMenuItem("Descripcción", true);
        jPMVista.add(jMCveProd);
        jMCveProd.addItemListener(iListener);
        JCheckBoxMenuItem jMdescr = new JCheckBoxMenuItem("Departametal", true);
        jPMVista.add(jMdescr);
        jMdescr.addItemListener(iListener);
        JCheckBoxMenuItem jMDescrprod = new JCheckBoxMenuItem("Tipo (Naturaleza) Contpaq i", true);
        jPMVista.add(jMDescrprod);
        jMDescrprod.addItemListener(iListener);
        JCheckBoxMenuItem jMTipoCOI = new JCheckBoxMenuItem("Tipo (Naturaleza) COI", true);
        jPMVista.add(jMTipoCOI);
        jMTipoCOI.addItemListener(iListener);
    }
    private void jbtnExportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExportarActionPerformed
        // TODO add your handling code here:
        try {
            boolean exportarConFormato = false;
            int opc = JOptionPane.showConfirmDialog(this, "¿Desea realizar la exportación de acuerdo al formato requerido"
                    + " para la importacion desde COI?", "Como realizar la exportación", JOptionPane.YES_NO_OPTION);
            if (opc == JOptionPane.YES_OPTION) {
                exportarConFormato = true;
            } else {
                exportarConFormato = false;
            }
            JFileChooser fileChooser = new JFileChooser();
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
                            jtblCatCtas.getRowSorter().toggleSortOrder(0);
                            jtblCatCtas.getRowSorter().toggleSortOrder(1);
                            List<JTable> tb = new ArrayList<JTable>();
                            //***************NOMBRE DE LA HOJA*************************/
                            List<String> nom = new ArrayList<String>();
                            tb.add(jtblCatCtas);
                            //-------------------------
                            nom.add("Reporte de Inventario.");
                            //-------------------
                            ctascontablesypolizas.clases.exportar_excel excelExporter = new ctascontablesypolizas.clases.exportar_excel(exportarConFormato, tb, temp, nom, grafo);
                            if (excelExporter.exportarExcel()) {
                                JOptionPane.showMessageDialog(null, "DATOS EXPORTADOS CON EXITO!");
                                llama(nomb);
                            } else {
                                JOptionPane.showMessageDialog(this, excelExporter.getMensaje());
                            }
                        }                        
                    } else {
                        jtblCatCtas.getRowSorter().toggleSortOrder(0);
                        jtblCatCtas.getRowSorter().toggleSortOrder(1);
                        List<JTable> tb = new ArrayList<JTable>();
                        //***************NOMBRE DE LA HOJA*************************/
                        List<String> nom = new ArrayList<String>();
                        tb.add(jtblCatCtas);
                        //-------------------------
                        nom.add("Reporte de Inventario.");
                        //-------------------
                        ctascontablesypolizas.clases.exportar_excel excelExporter = new ctascontablesypolizas.clases.exportar_excel(exportarConFormato, tb, temp, nom, grafo);
                        if (excelExporter.exportarExcel()) {
                            JOptionPane.showMessageDialog(null, "DATOS EXPORTADOS CON EXITO!");
                            llama(nomb);
                        } else {
                            JOptionPane.showMessageDialog(this, excelExporter.getMensaje());
                        }
                    }
                }
            } catch (Exception e) {//por alguna excepcion salta un mensaje de error
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al guardar el archivo!", "Oops! Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }//GEN-LAST:event_jbtnExportarActionPerformed

    private void jbtnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSalirActionPerformed
        // TODO add your handling code here:
        cerrarVentana();
    }//GEN-LAST:event_jbtnSalirActionPerformed

    private void jbtnTransferirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnTransferirActionPerformed
        // TODO add your handling code here:
        ElegirPeriodo ventana = new ElegirPeriodo(null, true);
        ventana.padre = this;
        ventana.controlador = controladorCOI;
        ventana.servidor = servidorCOI;
        ventana.puerto = puertoCOI;
        ventana.RutaonombreBD = RutaonombreBDCOI;
        ventana.usuario = usuarioCOI;
        ventana.contrasenia = contraseniaCOI;
        ventana.noEmpresa = nomEmpre;
        ventana.setVisible(true);
    }//GEN-LAST:event_jbtnTransferirActionPerformed

    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
        // TODO add your handling code here:
    }//GEN-LAST:event_formInternalFrameIconified

    private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
        // TODO add your handling code here:
    }//GEN-LAST:event_formComponentHidden

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
        // TODO add your handling code here:
//        if (evt.getClickCount() == 2) {
//            jTree1.setCellRenderer(new RenderArbol());
//        }
    }//GEN-LAST:event_jTree1MouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        cerrarVentana();
    }//GEN-LAST:event_formInternalFrameClosing

    private void jbtnExportar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExportar1ActionPerformed
      try{
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Todos los archivos *.xls", "xls", "Excel"));//filtro para ver solo archivos .edu
            int seleccion = fileChooser.showSaveDialog(null);
            try {
                if (seleccion == JFileChooser.APPROVE_OPTION) {//comprueba si ha presionado el boton de aceptar
                    File JFC = fileChooser.getSelectedFile();
                    ImportarExcel importar = new ImportarExcel(JFC);
                    
                    
                }
            } catch (Exception e) {//por alguna excepcion salta un mensaje de error
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al guardar el archivo!", "Oops! Error", JOptionPane.ERROR_MESSAGE);
            }
            
            
            
            
      }  catch (Exception e){
          
      }
        
        
        
    }//GEN-LAST:event_jbtnExportar1ActionPerformed

    public void cerrarVentana() {
        padre.jMIReporte.setEnabled(true);
        padre.jMIPolizas.setEnabled(true);
        padre.jbtnImportarCtaCuentas.setEnabled(true);
        padre.jbtnImportarPoliza.setEnabled(true);
        padre.banderaReportes = false;
        padre.jMIConfParams.setEnabled(true);
        this.dispose();
    }

    public void llama(String temp) {
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + temp);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cierre el archivo para continuar " + e);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPopupMenu jPMVista;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JTree jTree1;
    private javax.swing.JButton jbtnExportar;
    private javax.swing.JButton jbtnExportar1;
    private javax.swing.JButton jbtnSalir;
    protected javax.swing.JButton jbtnTransferir;
    protected javax.swing.JProgressBar jpbProgreso;
    protected javax.swing.JTable jtblCatCtas;
    private javax.swing.JTextField jtxtNumRegistros;
    // End of variables declaration//GEN-END:variables
}