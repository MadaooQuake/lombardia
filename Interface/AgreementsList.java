/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import lombardia2014.Interface.forms.ItemForm;
import lombardia2014.core.ConfigRead;
import lombardia2014.dataBaseInterface.MainDBQuierues;

/**
 *
 * @author Domek
 */
public class AgreementsList extends javax.swing.JPanel {

    JPanel mainPanel;
    TitledBorder title;
    Border blackline;
    JPanel[] buttonPanels;
    JTextField searchText = null;
    JButton buttonSearch = null;
    JTable agreementsList = null;
    DefaultTableModel model;
    Object[] data = null;
    int id = 0;
    int selectRow = -1;
    SwingWorker worker = null;
    ItemForm sellForm = null;
    ConfigRead readVat = new ConfigRead();
    float vat = 0, value = 0;
    MainDBQuierues getQuery = new MainDBQuierues();

    public AgreementsList() {
        blackline = BorderFactory.createLineBorder(Color.black);
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setSize(860, 700);
        GridBagConstraints c = new GridBagConstraints();

        buttonPanels = new JPanel[2];

        buttonPanels[0] = new JPanel(new GridBagLayout());
        title = BorderFactory.createTitledBorder(blackline, "Szukaj umowy");
        title.setTitleJustification(TitledBorder.RIGHT);
        title.setBorder(blackline);
        buttonPanels[0].setBorder(title);
        
        createSearch(c);

        c.gridheight = 1;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 0;
        c.ipady = 0;
        mainPanel.add(buttonPanels[0], c);
        
        buttonPanels[1] = new JPanel(new GridBagLayout());
        title = BorderFactory.createTitledBorder(blackline, "Lista umów");
        title.setTitleJustification(TitledBorder.RIGHT);
        title.setBorder(blackline);
        buttonPanels[1].setBorder(title);
        
        //createTable(c);
        
        c.gridheight = 1;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 0;
        c.ipady = 0;
        mainPanel.add(buttonPanels[1], c);

        this.add(mainPanel);
        setVisible(true);

    }
    
     public void createSearch(GridBagConstraints c) {
        searchText = new JTextField();
        searchText.setToolTipText("Wyszukaj depozyt");
        searchText.setPreferredSize(new Dimension(250, 40));
        searchText.setFont(new Font("Dialog", Font.BOLD, 20));
        //searchText.addActionListener();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 230, 10, 0);
        c.gridx = 0;
        c.gridy = 0;
        buttonPanels[0].add(searchText, c);

        buttonSearch = new JButton();
        buttonSearch.setText("Szukaj");
        buttonSearch.setPreferredSize(new Dimension(150, 40));
        //buttonSearch.addActionListener();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 230);
        c.gridx = 1;
        c.gridy = 0;
        buttonPanels[0].add(buttonSearch, c);
        
        

    }
     
     public void createTable(GridBagConstraints c) {

        // create table
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  //This causes all cells to be not editable
            }
        };
        model.addColumn("ID");
        model.addColumn("Kategoria");
        model.addColumn("Model");
        model.addColumn("Marka");
        model.addColumn("Typ");
        model.addColumn("Waga");
        model.addColumn("Wartość (Netto)");
        model.addColumn("Wartość (Brutto)");
        model.addColumn("IMEI");
        model.addColumn("Uwagi");
        model.addColumn("Umowa");

        //itemsTable();

        agreementsList = new JTable(model);
        agreementsList.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(agreementsList);
        agreementsList.setFillsViewportHeight(true);
        
        scrollPane.setPreferredSize(new Dimension(780, 450));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 1;
        c.gridy = 0;
        buttonPanels[1].add(scrollPane, c);

    }

}
