
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
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import static lombardia2014.Interface.MainMMenu.money;
import lombardia2014.core.SelfCalc;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Mateusz
 */
public class RepaymentCreditForm extends Forms {

    //Rest elements
    String number;
    String customerName;
    String customerSurname;
    String panelTitle = "Zwrot pozyczki";
    String tablePaneltitle = "Dane klienta";
    int agreementIdent;
    int selectRow = -1;
    JFrame repaymentFrame = new JFrame();
    JPanel tablePanel;
    JScrollPane scrollPane = null;
    JTable listItems = null;
    DefaultTableModel model;

    //Buttons
    JButton cancel = null;
    JButton search = null;
    //Text in panel
    JLabel title = null;
    JLabel agreement = null;
    JLabel clientName = null;
    // Fields for searching
    JTextField agreementNumberfield = null;
    JTextField customerNamefield = null;
    QueryDB setQuerry = null;
    private ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    int iClose = 0;
    double adRemValue = 0.0;

    @Override
    public void generatePanels(GridBagConstraints c) {

        // field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.ipadx = 5;
        agreement = new JLabel();
        agreement.setText("Podaj numer umowy:");
        mainPanel.add(agreement, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        c.ipadx = 10;
        agreementNumberfield = new JTextField();
        agreementNumberfield.setSize(10, 20);
        agreementNumberfield.addMouseListener(new BlockInputField());
        mainPanel.add(agreementNumberfield, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(0, 0, 0, 0);
        c.ipadx = 0;
        clientName = new JLabel();
        clientName.setText("Podaj imię i nazwisko klienta:");
        mainPanel.add(clientName, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        c.ipadx = 5;
        customerNamefield = new JTextField();
        customerNamefield.setSize(10, 20);
        customerNamefield.addMouseListener(new BlockAgreementField());
        mainPanel.add(customerNamefield, c);

        // butons
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 1;
        search = new JButton();
        search.addActionListener(new LoadClientInfo());
        search.setText("Wyszukaj");

        mainPanel.add(search, c);

        cancel = new JButton();
        cancel.setText("Anuluj");
        cancel.addActionListener(new CloseForm());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridwidth = 1;
        c.gridx = 4;
        c.gridy = 2;
        c.ipadx = 20;

        mainPanel.add(cancel, c);
    }

    private void generatePanels2() {
        GridBagConstraints c2 = new GridBagConstraints();
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };
        listItems = new JTable(model);
        model.addColumn("Imię");
        model.addColumn("Nazwisko");
        model.addColumn("Nr umowy");
        model.addColumn("Data zwrotu");
        model.addTableModelListener(listItems);
        getAgreements();

        listItems.setAutoCreateRowSorter(true);
        listItems.addMouseListener(new GetSelectRow());
        scrollPane = new JScrollPane(listItems);
        listItems.setFillsViewportHeight(true);

        c2.gridwidth = 3;
        c2.gridy = 4;
        c2.gridx = 0;
        c2.ipady = 400;
        c2.ipadx = 450;
        c2.insets = new Insets(10, 10, 10, 10);

        listItems.setPreferredSize(new Dimension(400, 400));
        scrollPane.setPreferredSize(new Dimension(400, 400));
        scrollPane.setVisible(true);

        tablePanel.add(scrollPane);
        mainPanel.add(tablePanel, c2);

    }

    @Override
    public void generateGui() {
        formFrame.setSize(700, 700);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle(panelTitle);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setPreferredSize(new Dimension(400, 700));
        titleBorder = BorderFactory.createTitledBorder(blackline, panelTitle);
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        titleBorder.setBorder(blackline);
        mainPanel.setBorder(titleBorder);

        tablePanel = new JPanel();
        tablePanel.setPreferredSize(new Dimension(300, 700));
        titleBorder = BorderFactory.createTitledBorder(blackline, tablePaneltitle);
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        titleBorder.setBorder(blackline);
        tablePanel.setBorder(titleBorder);

        generatePanels(c);
        generatePanels2();

        formFrame.add(mainPanel);
        formFrame.setVisible(true);

    }
    
        
    public Double getAddRemoValue() {
        return adRemValue;
    }

    class LoadClientInfo implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {

                setQuerry = new QueryDB();
                conDB = setQuerry.getConnDB();
                stmt = conDB.createStatement();

                //Data validation
                if (!agreementNumberfield.getText().isEmpty()) {
                    queryResult = setQuerry.dbSetQuery("SELECT Customers.NAME AS NAME, "
                            + "Customers.SURNAME AS SURNAME, Customers.ID AS CustomerID, "
                            + "Agreements.ID_AGREEMENTS AS AGREEMENT_ID, Agreements.ID AS ID,"
                            + " Agreements.STOP_DATE AS END_DATE"
                            + " FROM Customers, Agreements WHERE Customers.ID = Agreements.ID_CUSTOMER"
                            + " AND Agreements.ID_AGREEMENTS Like '%" + agreementNumberfield.getText() + "%';");

                    while (queryResult.next()) {

                        agreementIdent = queryResult.getInt("ID");

                        Object[] data = {
                            queryResult.getString("NAME"),
                            queryResult.getString("SURNAME"),
                            queryResult.getString("AGREEMENT_ID"),
                            queryResult.getString("END_DATE")
                        };
                        model.addRow(data);
                    }

                } else if (!customerNamefield.getText().isEmpty()) {

                    String[] names = customerNamefield.getText().split(" \\\\s+ ");

                    queryResult = setQuerry.dbSetQuery("SELECT Customers.NAME AS NAME, "
                            + "Customers.SURNAME AS SURNAME, Customers.ID AS CustomerID, "
                            + "Agreements.ID_AGREEMENTS AS AGREEMENT_ID, Agreements.ID AS ID,"
                            + "Agreements.STOP_DATE AS END_DATE"
                            + "FROM Customers, Agreements WHERE Customers.ID = Agreements.ID_CUSTOMER "
                            + "AND Customers.Name LIKE '" + names[0] + "' AND "
                            + "Customers.Surname LIKE '" + names[1] + "';");

                    while (queryResult.next()) {

                        agreementIdent = queryResult.getInt("ID");

                        Object[] data = {queryResult.getString("NAME"),
                            queryResult.getString("SURNAME"),
                            queryResult.getString("AGREEMENT_ID"),
                            queryResult.getString("END_DATE")};
                        model.addRow(data);

                    }

                } else {
                    JOptionPane.showMessageDialog(formFrame,
                            "Wypełnij pole",
                            "Błąd wyszukiwania",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException sqlE) {
                LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", sqlE.getMessage()
                        + "\n" + Arrays.toString(sqlE.getStackTrace()));
                startLogging.logToFile(text);
            }
        }
    }

    public boolean isClose() {
        return iClose == 1;
    }

    public class CloseForm implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            formFrame.dispose();
        }

    }

    public void getAgreements() {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();

            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT Customers.NAME AS NAME, "
                    + "Customers.SURNAME AS SURNAME, Customers.ID AS CustomerID, "
                    + "Agreements.ID_AGREEMENTS AS AGREEMENT_ID, Agreements.ID AS ID,"
                    + " Agreements.STOP_DATE AS END_DATE"
                    + " FROM Customers, Agreements WHERE Customers.ID = Agreements.ID_CUSTOMER ;");

            while (queryResult.next()) {

                Object[] data = {queryResult.getString("NAME"),
                    queryResult.getString("SURNAME"),
                    queryResult.getString("AGREEMENT_ID"),
                    queryResult.getString("END_DATE")};
                model.addRow(data);
                agreementIdent = queryResult.getInt("ID");

            }

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    public void deleteAgreement(int agreementIdent) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();

            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("DELETE FROM Items WHERE "
                    + "ID_AGREEMENT =" + agreementIdent + ";");

            queryResult = setQuerry.dbSetQuery("DELETE FROM Agreements WHERE"
                    + " ID =" + agreementIdent + ";");
            setQuerry.closeDB();

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

    }

    private class BlockInputField implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            agreementNumberfield.setEnabled(true);
            agreementNumberfield.setEditable(true);
            customerNamefield.setEnabled(false);
            customerNamefield.setEditable(false);
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    private class BlockAgreementField implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            customerNamefield.setEnabled(true);
            customerNamefield.setEditable(true);
            agreementNumberfield.setEnabled(false);
            agreementNumberfield.setEditable(false);

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    // actions for selected elements in table
    private class GetSelectRow implements MouseListener {

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            JTable table = (JTable) e.getSource();
            Point p = e.getPoint();

            if (e.getClickCount() == 2) {
                selectRow = table.getSelectedRow();
                int row = table.rowAtPoint(p);
                int decision = JOptionPane.showConfirmDialog(formFrame,
                        "Czy aby napewno ta pozyczka została ukonczona ?",
                        "Ostrzeżenie ",
                        JOptionPane.YES_NO_OPTION);
                if (decision == JOptionPane.YES_OPTION) {
                    try {
                        String agreementValue = null;
                        setQuerry = new QueryDB();
                        conDB = setQuerry.getConnDB();

                        stmt = conDB.createStatement();
                        queryResult = setQuerry.dbSetQuery("SELECT VALUE_REST FROM Agreements"
                                + " WHERE ID = " + agreementIdent + ";");

                        while (queryResult.next()) {
                            agreementValue = queryResult.getString("VALUE_REST");
                        }

                        SelfCalc actualCalc = new SelfCalc();
                        actualCalc.chackValue(formFrame);
                        actualCalc.addToSelf(Float.parseFloat(agreementValue));
                        adRemValue = Double.parseDouble(agreementValue);
                        money.setText(actualCalc.getValue() + " zł");
                        deleteAgreement(agreementIdent);
                        formFrame.dispose();
                        setQuerry.closeDB();
                        iClose = 1;
                    } catch (SQLException ex) {
                        LombardiaLogger startLogging = new LombardiaLogger();
                        String text = startLogging.preparePattern("Error", ex.getMessage()
                                + "\n" + Arrays.toString(ex.getStackTrace()));
                        startLogging.logToFile(text);
                    }

                }
            }

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
}
