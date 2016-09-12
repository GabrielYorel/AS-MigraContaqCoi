/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ctascontablesypolizas.clases;

import javax.swing.tree.*;
import javax.swing.*;
import java.awt.*;

public class RenderArbol extends DefaultTreeCellRenderer {

    /**
     * Creates a new instance of RendererArbol
     */
    ImageIcon normal;
    ImageIcon cerrado;
    ImageIcon abierto;

    public RenderArbol() {
        normal = new javax.swing.ImageIcon(getClass().getResource("/imagenes/normal.png"));
        abierto = new javax.swing.ImageIcon(getClass().getResource("/imagenes/abierto.png"));
        cerrado = new javax.swing.ImageIcon(getClass().getResource("/imagenes/cerrado.png"));
    }
  

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
//        String val = value.toString();
//        ImageIcon i;
        DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) value;
        if (nodo.getChildCount() > 0) {
            if (expanded) {
                setIcon(abierto);
            } else {
                setIcon(cerrado);
            }
        } else {
            setIcon(normal);
        }
        return this;
    }
}