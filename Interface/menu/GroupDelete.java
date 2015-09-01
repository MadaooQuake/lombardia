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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Domek
 */
public class GroupDelete extends MenuElementsList {

    JButton delete = null, cancel = null, search = null;
    GridBagConstraints[] cTab = new GridBagConstraints[3];
    JPanel[] formPanels = new JPanel[3];
    JTextField searchField = new JTextField();
    JTable objectList = null;
    DefaultTableModel model;
    int fontSize = 12;
    int heightTextL = 20;
    // we can change to items ot agreements list
    JList<Object> rangeOption = null;
    Object[] elementList = {"Umowy", "Przedmioty"};

    @Override
    public void generateGui() {
        formFrame.setSize(new Dimension(600, 600));
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

        generateSearch();

        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 400;

        mainPanel.add(formPanels[0], c);

        createTable();
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 400;
        c.ipady = 400;
        mainPanel.add(formPanels[1], c);

        formFrame.add(mainPanel);
        formFrame.setVisible(true);
    }

    public void generateSearch() {
        formPanels[0] = new JPanel(new GridBagLayout());
        cTab[0] = new GridBagConstraints();
        cTab[0].insets = new Insets(10, 10, 10, 10);

        rangeOption = new JList<>(elementList);
        rangeOption.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        rangeOption.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        rangeOption.setVisibleRowCount(1);
        rangeOption.setSelectedIndex(0);
        rangeOption.setPreferredSize(new Dimension(80, 30));
        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 0;
        cTab[0].gridy = 0;
        formPanels[0].add(rangeOption, cTab[0]);

        searchField.setToolTipText("Wyszukaj przedtmiot");
        searchField.setPreferredSize(new Dimension(250, 40));
        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 1;
        cTab[0].gridy = 0;
        cTab[0].ipadx = 150;
        cTab[0].ipady = 20;
        formPanels[0].add(searchField, cTab[0]);

        search = new JButton();
        search.setText("Szukaj");
        search.setFont(new Font("Dialog", Font.BOLD, 20));
        search.setPreferredSize(new Dimension(150, 30));
        search.addActionListener(new SearchButton());
        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 2;
        cTab[0].gridy = 0;
        cTab[0].ipadx = 0;
        cTab[0].ipady = 0;
        formPanels[0].add(search, cTab[0]);

    }

    public void createTable() {
        formPanels[1] = new JPanel(new GridBagLayout());
        cTab[1] = new GridBagConstraints();
        cTab[1].insets = new Insets(10, 10, 10, 10);
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  //This causes all cells to be not editable
            }
        };

        model.addColumn("ID");
        model.addColumn("Numer Umowy");
        model.addColumn("Przedmiot");

        objectList = new JTable(model);
        objectList.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(objectList);
        objectList.setFillsViewportHeight(true);
        scrollPane.setPreferredSize(new Dimension(400, 400));
        cTab[1].gridx = 0;
        cTab[1].gridy = 0;
        cTab[0].ipadx = 400;
        cTab[0].ipady = 400;
        formPanels[1].add(scrollPane, cTab[1]);
    }

    public void createButtns() {
        formPanels[2] = new JPanel(new GridBagLayout());
        cTab[2] = new GridBagConstraints();
        cTab[2].insets = new Insets(10, 10, 10, 10);

        delete = new JButton();
        delete.setText("Usuń");
        delete.setPreferredSize(new Dimension(150, heightTextL));
        delete.setFont(new Font("Dialog", Font.BOLD, 18));

        cTab[2].fill = GridBagConstraints.HORIZONTAL;
        cTab[2].gridx = 0;
        cTab[2].gridy = 0;
        formPanels[2].add(delete, cTab[2]);

        cancel = new JButton();
        cancel.setText("Anuluj");
        cancel.setPreferredSize(new Dimension(150, heightTextL));
        cancel.setFont(new Font("Dialog", Font.BOLD, 18));
        cancel.addActionListener(new CancelButton());
        cTab[2].fill = GridBagConstraints.HORIZONTAL;
        cTab[2].gridx = 1;
        cTab[2].gridy = 0;
        formPanels[2].add(cancel, cTab[2]);
    }

    // actions
    public class SearchButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(rangeOption.getSelectedValue().equals("Umowy")) {
                // to do...
            } else {
                // to do...
            }
        }
        
    }
    
    public class CancelButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // search now
            formFrame.dispose();
        }

    }

    public class DeleteButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            formFrame.dispose();
        }

    }

    private class GetSelectRow implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            // do nothing
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // do nothing
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // do nothing
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // do nothing
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // do nothing
        }

    }

}
