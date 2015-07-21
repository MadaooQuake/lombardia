/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.forms;

import lombardia2014.dataBaseInterface.QueryDB;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import lombardia2014.core.CalcDays;
import lombardia2014.dataBaseInterface.MainDBQuierues;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Mateusz
 */
public class LateClientsForm extends Forms {

    //Strings
    String frameTitle = "Spóźnieni";
    String tablePanelTitle = "Dane Klienta";
    //Frames, Panels and Table
    JFrame lateClientsFrame = new JFrame();
    JScrollPane scrollPane = null;
    JTable listClients = null;
    DefaultTableModel model;
    JPanel buttonPanel, tablePanel;
    String id = null;
    int selectRow = -1;
    //Buttons
    JButton cancel, solve = null;
    MainDBQuierues getQuery = new MainDBQuierues();

    //Converting current date
    SimpleDateFormat ft = new SimpleDateFormat("dd.MM.YYYY HH:mm");
    Date now = new Date();
    String stopDate = ft.format(now);
    CalcDays count = null;
    int daysCount;

    @Override
    public void generatePanels(GridBagConstraints c) {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };
        listClients = new JTable(model);
        model.addColumn("Imię");
        model.addColumn("Nazwisko");
        model.addColumn("Nr umowy");
        model.addColumn("Pierwotna data zwrotu");
        model.addTableModelListener(listClients);
        getLateClients();

        listClients.setAutoCreateRowSorter(true);
        scrollPane = new JScrollPane(listClients);
        listClients.setFillsViewportHeight(true);
        listClients.addMouseListener(new GetSelectRow());

        c.gridwidth = 1;
        c.gridy = 2;
        c.gridx = 0;
        c.insets = new Insets(10, 10, 10, 10);
        c.ipadx = 650;
        c.ipady = 600;
        listClients.setPreferredSize(new Dimension(650, 600));

        scrollPane.setPreferredSize(new Dimension(650, 600));
        scrollPane.setVisible(true);

        tablePanel.add(scrollPane);

    }

    private void generatePanels2() {
        GridBagConstraints c2 = new GridBagConstraints();

        solve = new JButton();
        solve.setText("Rozwiąż");
        solve.addActionListener(new solveAgreeent());
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.insets = new Insets(5, 5, 5, 5);
        c2.gridwidth = 1;
        c2.gridx = 0;
        c2.gridy = 0;
        buttonPanel.add(solve, c2);

        cancel = new JButton();
        cancel.setText("Anuluj");
        cancel.addActionListener(new CloseForm());
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.insets = new Insets(5, 5, 5, 5);
        c2.gridwidth = 1;
        c2.gridx = 1;
        c2.gridy = 0;
        buttonPanel.add(cancel, c2);

        mainPanel.add(tablePanel, c);
        c2.gridx = 0;
        c2.gridy = 5;
        mainPanel.add(buttonPanel, c2);
    }

    @Override
    public void generateGui() {
        formFrame.setSize(800, 800);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle(frameTitle);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setPreferredSize(new Dimension(800, 800));
        titleBorder = BorderFactory.createTitledBorder(blackline, frameTitle);
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        titleBorder.setBorder(blackline);
        mainPanel.setBorder(titleBorder);

        buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(100, 700));

        tablePanel = new JPanel();
        tablePanel.setPreferredSize(new Dimension(600, 600));
        titleBorder = BorderFactory.createTitledBorder(blackline, tablePanelTitle);
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        titleBorder.setBorder(blackline);
        tablePanel.setBorder(titleBorder);

        generatePanels(c);
        generatePanels2();

        formFrame.add(mainPanel);
        formFrame.setVisible(true);

    }

    public class CloseForm implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            formFrame.dispose();
        }

    }

    public void getLateClients() {

        List<HashMap<String, String>> lateClients = getQuery.lateClients();

        for (HashMap<String, String> lateClient : lateClients) {
            Object[] data = {
                lateClient.get("NAME"),
                lateClient.get("SURNAME"),
                lateClient.get("SURNAME"),
                lateClient.get("END_DATE")
            };
            model.addRow(data);
        }
    }

    // actions for selected elements in table
    private class GetSelectRow implements MouseListener {

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            JTable target = (JTable) e.getSource();
            selectRow = target.getSelectedRow();
            Point p = e.getPoint();
            int row = target.rowAtPoint(p);

            id = (String) listClients.getModel().getValueAt(
                    listClients.convertRowIndexToView(selectRow), 2);

        }

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            //nothing to do, but i must create this method :(
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            //nothing to do, but i must create this method :(
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
            //nothing to do, but i must create this method :(
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            //nothing to do, but i must create this method :(
        }

    }

    /**
     * class with db query - db qery move to another interface this class solve
     * the agreeeent when clieny late to pay.
     */
    private class solveAgreeent implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectRow >= 0) {
                selectRow = -1;
                getQuery.removeItemFromAgreement(id);
                    // add new flag in aggreement 
                // next i remove agreement :)
//                    queryResult = setQuerry.dbSetQuery("DELETE FROM Items WHERE "
//                    + "ID_AGREEMENT =" + id + ";");
            }
        }

    }

}
