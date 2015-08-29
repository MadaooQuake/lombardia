/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Domek
 */
public class GroupDelete extends MenuElementsList {

    JButton delete = null, cancel = null;
    GridBagConstraints[] cTab = new GridBagConstraints[3];

    @Override
    public void generateGui() {
        formFrame.setSize(new Dimension(400, 600));
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("Lista użytkowników");

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(titleBorder);

        generatePanels(c);

        formFrame.add(mainPanel);
        formFrame.setVisible(true);
    }

    @Override
    public void generatePanels(GridBagConstraints c) {
        blackline = BorderFactory.createLineBorder(Color.black);
        mainPanel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(titleBorder);

        formFrame.add(mainPanel);
        formFrame.setVisible(true);
    }
    
    public void generateSearch() {
        
    }

}
