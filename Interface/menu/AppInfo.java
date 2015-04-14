/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.menu;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Domek
 */
public class AppInfo extends MenuElementsList {

    JLabel aboutApp = null;

    @Override
    public void generateGui() {
        formFrame.setSize(300, 300);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("O Programie");
        mainPanel = new JPanel(new GridBagLayout());
        titleBorder = BorderFactory.createTitledBorder(blackline, "O Programie");
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        mainPanel.setBorder(titleBorder);

        generatePanels(c);

        formFrame.add(mainPanel);
        formFrame.setVisible(true);
    }

    @Override
    public void generatePanels(GridBagConstraints c) {
        c.insets = new Insets(10, 10, 10, 10);

        aboutApp = new JLabel("Lombardia");
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(aboutApp, c);
        c.insets = new Insets(0, 10, 0, 10);
        aboutApp = new JLabel("Wersja 1.0.0 Kandydat 1");
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(aboutApp, c);
        
        c.insets = new Insets(40, 10, 0, 10);
        aboutApp = new JLabel("Programista: Marcin Kiełkowski ");
        c.gridx = 0;
        c.gridy = 2;
        mainPanel.add(aboutApp, c);
        c.insets = new Insets(0, 10, 0, 10);
        aboutApp = new JLabel("Kontakt: kielkowskimarcin@prokonto.pl");
        c.gridx = 0;
        c.gridy = 3;
        mainPanel.add(aboutApp, c);
    }

}
