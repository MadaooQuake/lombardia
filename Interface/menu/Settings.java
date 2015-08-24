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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Domek
 */
public class Settings extends MenuElementsList {

    JPanel[] buttonPanels;
    JLabel[] namedField = null;
    JTextField[] fields = null;

    @Override
    public void generateGui() {
        formFrame.setSize(new Dimension(400, 500));
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("Ustawienia aplikacji");

        //generatePanels
        generatePanels(c);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(titleBorder);

    }

    @Override
    public void generatePanels(GridBagConstraints c) {
        // read config elements
        // 8 eements
        namedField = new JLabel[8];
        fields = new JTextField[8];

        blackline = BorderFactory.createLineBorder(Color.black);
        mainPanel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();

        buttonPanels = new JPanel[2];

    }
    
    // generate fields
    
    // generate buttons

    // read config to elements
}
