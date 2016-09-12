/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas;

import ctascontablesypolizas.clases.CrearConexionFBoSQL;
import ctascontablesypolizas.clases.HiloExportCtas;
import ctascontablesypolizas.clases.Banda;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

/**
 *
 * @author Gabriel
 */
public class ElegirPeriodo extends javax.swing.JDialog {

    CatCtas padre;
    String noEmpresa;
    DefaultListModel lm = new DefaultListModel();
    String controlador;
    String servidor;
    String puerto;
    String contrasenia;
    String usuario;
    String RutaonombreBD;
    private CrearConexionFBoSQL objConexion;
    private Connection conexion;
    private String[] Ejercicios;

    /**
     * Creates new form ExportPeriodoEjercicio
     */
    public ElegirPeriodo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        AsignarIconos();
        jLabel4.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        jbtnAceptar = new javax.swing.JButton();
        jBtnSalir = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLstMovtos = new javax.swing.JList();
        jlblmovselect = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        chkVentas = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Selección de Periodo(s)");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jToolBar1.setBackground(new java.awt.Color(178, 233, 187));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jbtnAceptar.setBackground(new java.awt.Color(178, 233, 187));
        jbtnAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/aceptar2.png"))); // NOI18N
        jbtnAceptar.setToolTipText("Aceptar");
        jbtnAceptar.setFocusable(false);
        jbtnAceptar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnAceptar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAceptarActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtnAceptar);

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

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/calendario.png"))); // NOI18N

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Ejercicios"));

        jLstMovtos.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jLstMovtosValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jLstMovtos);

        jlblmovselect.setText("jLabel8");

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        chkVentas.setText("Todos");
        chkVentas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkVentasItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chkVentas)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chkVentas)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlblmovselect, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jlblmovselect)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnSalirActionPerformed
        // TODO add your handling code here:
        try {
            this.dispose();
            padre.setVisible(true);
            padre.setSelected(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jBtnSalirActionPerformed

    private void jbtnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAceptarActionPerformed
        // TODO add your handling code here:
        try {
            Object[] seleccionados = jLstMovtos.getSelectedValues();
            
            this.dispose();
            padre.setVisible(true);
            padre.setSelected(true);
            padre.jbtnTransferir.setEnabled(false);            
            try {
                HiloExportCtas miHilo = new HiloExportCtas(noEmpresa,seleccionados, padre.jbtnTransferir,
                        padre.tiposCtas, padre.jtblCatCtas,
                        padre.jpbProgreso, padre.modelonuevo,
                        padre.controlador, padre.servidor, padre.puerto, padre.usuario, padre.contrasenia, padre.RutaonombreBDCont,
                        padre.controladorCOI, padre.servidorCOI, Integer.parseInt(padre.puertoCOI), padre.usuarioCOI, padre.contraseniaCOI, padre.RutaonombreBDCOI,padre.seguridadIntegrada);
                miHilo.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jbtnAceptarActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        cargarTiposMovtos();
    }//GEN-LAST:event_formWindowOpened

    private void jLstMovtosValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jLstMovtosValueChanged
        // TODO add your handling code here:
        seleccionManual();
    }//GEN-LAST:event_jLstMovtosValueChanged

    private void chkVentasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkVentasItemStateChanged
        //         TODO add your handling code here:
        seleccionar();
    }//GEN-LAST:event_chkVentasItemStateChanged
    public void seleccionar() {
        jLstMovtos.clearSelection();
        if (chkVentas.isSelected()) {
            int indicesVentas[] = new int[Ejercicios.length];
            for (int i = 0; i < Ejercicios.length; i++) {
                indicesVentas[i] = i;
            }
            jLstMovtos.setSelectedIndices(indicesVentas);
        } else {
            jLstMovtos.setSelectedIndex(0);
        }
        jlblmovselect.setText("");
        seleccionManual();
    }

    public void seleccionManual() {
        Object[] seleccionados = jLstMovtos.getSelectedValues();
        int lim = seleccionados.length;
        String completo = "";
        if (lim != 0) {
            for (int i = 0; i < lim; i++) {
                Banda banda = (Banda) seleccionados[i];
                String id = banda.getId();
                completo += id + ",";
            }
            jlblmovselect.setText(completo.substring(0, completo.length() - 1));
        }
    }

    public void cargarTiposMovtos() {
        try {
            objConexion = new CrearConexionFBoSQL(controlador, servidor, Integer.parseInt(puerto), RutaonombreBD, usuario, contrasenia,false);
            conexion = objConexion.getConexion();
            java.util.List listEjercicios = new ArrayList();
            if (controlador.equals("DevartSQLServer")) {
                Statement stmt = conexion.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES");
                while (rs.next()) {
                    String nombreTabla = rs.getString(3);
                    if (nombreTabla.startsWith("CUENTAS") && nombreTabla.endsWith(noEmpresa)) {
                        listEjercicios.add(nombreTabla.substring(7, 9));
                    }
                }
                rs.close();
            } else {
                DatabaseMetaData metaDatos;
                String tipos[] = new String[]{"TABLE", "VIEW"};
                ResultSet rsTablas;
                metaDatos = conexion.getMetaData();
                rsTablas = metaDatos.getTables(null, "ESQUEMA", "%", tipos);
                while (rsTablas.next()) {
                    String nombreTabla = rsTablas.getString(rsTablas.findColumn("TABLE_NAME"));
                    if (nombreTabla.startsWith("CUENTAS") && nombreTabla.endsWith(noEmpresa)) {
                        listEjercicios.add(nombreTabla.substring(7, 9));
                    }
                }
                rsTablas.close();
            }
            int indice = 0;
            Ejercicios = new String[listEjercicios.size()];
            for (Iterator it = listEjercicios.iterator(); it.hasNext();) {
                String object = it.next().toString();
                Ejercicios[indice] = object;
                lm.addElement(new Banda("20" + Ejercicios[indice], Ejercicios[indice]));
                indice++;
            }


            jLstMovtos.setModel(lm);
            jLstMovtos.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            chkVentas.setSelected(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void AsignarIconos() {
        ImageIcon icono2 = (ImageIcon) jLabel4.getIcon();
        ImageIcon iconoFrame = new ImageIcon(icono2.getImage().getScaledInstance(45, 45, 20));
        this.setIconImage(iconoFrame.getImage());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ElegirPeriodo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ElegirPeriodo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ElegirPeriodo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ElegirPeriodo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ElegirPeriodo dialog = new ElegirPeriodo(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkVentas;
    private javax.swing.JButton jBtnSalir;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jLstMovtos;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton jbtnAceptar;
    private javax.swing.JLabel jlblmovselect;
    // End of variables declaration//GEN-END:variables
}
