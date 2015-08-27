/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lombardia2014.core.ConfigRead;
import lombardia2014.core.SaveConfig;

/**
 *
 * @author Domek
 */
public class Settings extends MenuElementsList {

    JButton save = null, cancel = null;
    GridBagConstraints[] cTab = new GridBagConstraints[2];
    JPanel[] formPanels = new JPanel[2];
    JLabel[] namedField = null;
    JTextField[] fields = null;
    int fontSize = 12;
    int heightTextL = 20;
    ConfigRead readXML = null;
    SaveConfig saveConfig = null;

    @Override
    public void generateGui() {
        formFrame.setSize(new Dimension(400, 500));
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("Ustawienia aplikacji");
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(titleBorder);
        mainPanel.setPreferredSize(new Dimension(400, 500));
        //generatePanels
        generatePanels(c);

        formFrame.add(mainPanel);
        formFrame.setVisible(true);
        
        readConfig();
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

        createFoelds();

        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 20;

        mainPanel.add(formPanels[0], c);
        
        createButtons();

        c.gridx = 0;
        c.gridy = 1;
        c.ipady = 40;
        mainPanel.add(formPanels[1], c);
    }

    // generate fields
    public void createFoelds() {
        formPanels[0] = new JPanel(new GridBagLayout());
        cTab[0] = new GridBagConstraints();
        cTab[0].insets = new Insets(10, 10, 10, 10);
        // fields 
        namedField[0] = new JLabel();
        namedField[0].setText("VAT:");
        namedField[0].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 0;
        cTab[0].gridy = 0;
        formPanels[0].add(namedField[0], cTab[0]);

        fields[0] = new JTextField();
        fields[0].setPreferredSize(new Dimension(150, heightTextL));
        fields[0].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 1;
        cTab[0].gridy = 0;
        formPanels[0].add(fields[0], cTab[0]);

        namedField[1] = new JLabel();
        namedField[1].setText("Opłata Minimalna:");
        namedField[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 0;
        cTab[0].gridy = 1;
        formPanels[0].add(namedField[1], cTab[0]);

        fields[1] = new JTextField();
        fields[1].setPreferredSize(new Dimension(150, heightTextL));
        fields[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 1;
        cTab[0].gridy = 1;
        formPanels[0].add(fields[1], cTab[0]);

        namedField[2] = new JLabel();
        namedField[2].setText("Opłata Manipulacyjna:");
        namedField[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 0;
        cTab[0].gridy = 2;
        formPanels[0].add(namedField[2], cTab[0]);

        fields[2] = new JTextField();
        fields[2].setPreferredSize(new Dimension(150, heightTextL));
        fields[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 1;
        cTab[0].gridy = 2;
        formPanels[0].add(fields[2], cTab[0]);

        namedField[3] = new JLabel();
        namedField[3].setText("Zysk na dzien:");
        namedField[3].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 0;
        cTab[0].gridy = 3;
        formPanels[0].add(namedField[3], cTab[0]);

        fields[3] = new JTextField();
        fields[3].setPreferredSize(new Dimension(150, heightTextL));
        fields[3].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 1;
        cTab[0].gridy = 3;
        formPanels[0].add(fields[3], cTab[0]);

        namedField[4] = new JLabel();
        namedField[4].setText("Stopa:");
        namedField[4].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 0;
        cTab[0].gridy = 4;
        formPanels[0].add(namedField[4], cTab[0]);

        fields[4] = new JTextField();
        fields[4].setPreferredSize(new Dimension(150, heightTextL));
        fields[4].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 1;
        cTab[0].gridy = 4;
        formPanels[0].add(fields[4], cTab[0]);

        namedField[5] = new JLabel();
        namedField[5].setText("RSO:");
        namedField[5].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 0;
        cTab[0].gridy = 5;
        formPanels[0].add(namedField[5], cTab[0]);

        fields[5] = new JTextField();
        fields[5].setPreferredSize(new Dimension(150, heightTextL));
        fields[5].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 1;
        cTab[0].gridy = 5;
        formPanels[0].add(fields[5], cTab[0]);

        namedField[6] = new JLabel();
        namedField[6].setText("Kwota Przedterminaowa:");
        namedField[6].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 0;
        cTab[0].gridy = 6;
        formPanels[0].add(namedField[6], cTab[0]);

        fields[6] = new JTextField();
        fields[6].setPreferredSize(new Dimension(150, heightTextL));
        fields[6].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 1;
        cTab[0].gridy = 6;
        formPanels[0].add(fields[6], cTab[0]);
    }

    // generate buttons
    public void createButtons() {
        formPanels[1] = new JPanel(new GridBagLayout());
        cTab[1] = new GridBagConstraints();
        cTab[1].insets = new Insets(10, 10, 10, 10);
        cTab[1].ipady = 20;
        
        save = new JButton();
        save.setText("Zapisz");
        save.setPreferredSize(new Dimension(150, heightTextL));
        save.setFont(new Font("Dialog", Font.BOLD, 18));
        save.addActionListener(new SaveButton());
        cTab[1].fill = GridBagConstraints.HORIZONTAL;
        cTab[1].gridx = 0;
        cTab[1].gridy = 0;
        formPanels[1].add(save, cTab[1]);

        cancel = new JButton();
        cancel.setText("Anuluj");
        cancel.setPreferredSize(new Dimension(150, heightTextL));
        cancel.setFont(new Font("Dialog", Font.BOLD, 18));
        cancel.addActionListener(new CancelButton());
        cTab[1].fill = GridBagConstraints.HORIZONTAL;
        cTab[1].gridx = 1;
        cTab[1].gridy = 0;
        formPanels[1].add(cancel, cTab[1]);
    }
    
    // read config to elements
    public void readConfig() {
        readXML = new ConfigRead();
        readXML.readFile();
        
        fields[0].setText(Float.toString(readXML.getVat()));
        fields[1].setText(Float.toString(readXML.getMinFee()));
        fields[2].setText(Float.toString(readXML.getManFee()));
        fields[3].setText(Float.toString(readXML.getDailyProfit()));
        fields[4].setText(Float.toString(readXML.getLombardRate()));
        fields[5].setText(Float.toString(readXML.getRSO()));
        fields[6].setText(Float.toString(readXML.getPrematureDevotion()));
    }
    
    // actions 
    
    public class SaveButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            saveConfig = new SaveConfig();
            saveConfig.readFile();
            saveConfig.changeElement("vat", fields[0].getText());
            saveConfig.changeElement("oplataMinimalna", fields[1].getText());
            saveConfig.changeElement("oplataManipulacyjna", fields[2].getText());
            saveConfig.changeElement("zyskNaDzien", fields[3].getText());
            saveConfig.changeElement("stopa", fields[4].getText());
            saveConfig.changeElement("rso", fields[5].getText());
            saveConfig.changeElement("kwotzaPrzedterminowa", fields[6].getText());
            saveConfig.closeSession();
            formFrame.dispose();
        }
        
    }
    
    public class CancelButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
             formFrame.dispose();
        }
        
    }
    
    
    
}
