/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lombardia2014.Interface.menu;

import java.awt.Color;
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Domek
 */
public abstract class MenuElementsList extends javax.swing.JFrame {
    JPanel mainPanel;
    Border blackline = BorderFactory.createLineBorder(Color.black);
    GridBagConstraints c = new GridBagConstraints();
    JFrame formFrame = new JFrame();
    TitledBorder titleBorder;
    
    public abstract void generateGui();

    public abstract void generatePanels(GridBagConstraints c);
}
