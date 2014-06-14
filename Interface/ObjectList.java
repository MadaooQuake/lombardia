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
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author marcin
 */
public class ObjectList extends javax.swing.JPanel {
    JPanel mainPanel;
    TitledBorder title;
    Border blackline;
    JPanel[] buttonPanels;
    JTextField searchText = null;
    JButton buttonSearch = null;
    JTable litsCustomers = null;
    String[] columnName = {"Identyfikator", "Nazwa", "Kategoria", "Wartość"};
    DefaultTableModel model;
    Object[][] data = {{"test"}};

    public ObjectList() {
        blackline = BorderFactory.createLineBorder(Color.black);
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setSize(860, 700);
        GridBagConstraints c = new GridBagConstraints();
        
        buttonPanels = new JPanel[2];
        
        buttonPanels[0] = new JPanel(new GridBagLayout());
        title = BorderFactory.createTitledBorder(blackline, "Wyszukiwarka");
        title.setTitleJustification(TitledBorder.RIGHT);
        title.setBorder(blackline);
        buttonPanels[0].setBorder(title);

        // to do... method to create elements of defined panel above
        createSearch(c);
        
        c.gridheight = 1;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 0;
        c.ipady = 0;
        mainPanel.add(buttonPanels[0], c);

        // secound panel
        buttonPanels[1] = new JPanel(new GridBagLayout());
        title = BorderFactory.createTitledBorder(blackline, "Lista Klientów");
        title.setTitleJustification(TitledBorder.RIGHT);
        title.setBorder(blackline);
        buttonPanels[1].setBorder(title);

        // to do... method to create elements of defined panel above
        createTable(c);
        
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
    
        /**
     * @see create fields and button to search customers from database
     * @param c
     */
    public void createSearch(GridBagConstraints c) {
        searchText = new JTextField();
        searchText.setToolTipText("Wyszukaj klienta");
        searchText.setPreferredSize(new Dimension(250, 40));
        searchText.setFont(new Font("Dialog", Font.BOLD, 20));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 230, 10, 0);
        c.gridx = 0;
        c.gridy = 0;
        buttonPanels[0].add(searchText, c);

        buttonSearch = new JButton();
        buttonSearch.setText("Szukaj");
        buttonSearch.setPreferredSize(new Dimension(150, 40));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 230);
        c.gridx = 1;
        c.gridy = 0;
        buttonPanels[0].add(buttonSearch, c);

    }

    /**
     * @see create fields and button to search customers from database
     * @param c
     */
    public void createTable(GridBagConstraints c) {

        // create table
        model = new DefaultTableModel(data, columnName);

        litsCustomers = new JTable(model);
        litsCustomers.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(litsCustomers);
        litsCustomers.setFillsViewportHeight(true);
        
        scrollPane.setPreferredSize(new Dimension(780,450));
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 1;
        c.gridy = 0;
        buttonPanels[1].add(scrollPane, c);


    }
    
}
