/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface;

import lombardia2014.dataBaseInterface.QueryDB;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
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
import lombardia2014.Interface.forms.CustomerForm;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author marcin
 */
public final class CustomersList extends javax.swing.JPanel {

    JPanel mainPanel;
    TitledBorder title = null;
    Border blackline;
    JPanel[] buttonPanels;
    JTextField searchText = null;
    JButton buttonSearch = null;
    JTable litsCustomers = null;
    DefaultTableModel model;
    Object[] data = null;
    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    Boolean update;
    CustomerForm CustomerInfo = null;
    int id = 0;
    int selectRow = -1;
    SwingWorker worker = null;

    public CustomersList(Boolean update_) {
        update = update_;
    }

    public CustomersList() {
        blackline = BorderFactory.createLineBorder(Color.black);
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setSize(860, 700);
        GridBagConstraints c = new GridBagConstraints();
        /// Create two panels who has inside some elements
        buttonPanels = new JPanel[2];

        // first panel 
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
        searchText.setText("");
        searchText.addActionListener(new CustomerFilter());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 230, 10, 0);
        c.gridx = 0;
        c.gridy = 0;
        buttonPanels[0].add(searchText, c);

        buttonSearch = new JButton();
        buttonSearch.setText("Szukaj");
        buttonSearch.setPreferredSize(new Dimension(150, 40));
        buttonSearch.addActionListener(new CustomerFilter());

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
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };
        model.addColumn("Imię");
        model.addColumn("Nazwisko");
        model.addColumn("Adres");
        model.addColumn("Pesel");
        model.addColumn("Zaufany klient");
        model.addTableModelListener(litsCustomers);

        addCustomerstoTable();

        litsCustomers = new JTable(model);
        litsCustomers.addMouseListener(new GetSelectRow());
        litsCustomers.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(litsCustomers);
        litsCustomers.setFillsViewportHeight(true);

        scrollPane.setPreferredSize(new Dimension(780, 450));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 1;
        c.gridy = 0;
        buttonPanels[1].add(scrollPane, c);

    }

    /**
     * @see load from db customers and add to object :D
     */
    public void addCustomerstoTable() {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();

            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT NAME, SURNAME, ADDRESS, "
                    + "PESEL, TRUST FROM Customers;");

            while (queryResult.next()) {
                String trustString = "Nie zaufany";
                int trustVal = queryResult.getInt("TRUST");
                if (trustVal == 1) {
                    trustString = "Zaufany";
                }
                Object[] data = {
                    queryResult.getString("NAME"),
                    queryResult.getString("SURNAME"),
                    queryResult.getString("ADDRESS"),
                    queryResult.getString("PESEL"),
                    trustString
                };
                model.addRow(data);
            }

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    /**
     * @ update table
     */
    public void updateCustomerstoTable() {
        model.setRowCount(0);

        addCustomerstoTable();

        repaint();
    }

    // action time :)
    // actions for selected elements in table
    private class GetSelectRow implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            JTable table = (JTable) e.getSource();
            Point p = e.getPoint();

            if (e.getClickCount() == 2) {
                selectRow = table.getSelectedRow();
                int row = table.rowAtPoint(p);
                //get id user - method
                id = getCusId();
                CustomerInfo = new CustomerForm(id);
                CustomerInfo.generateGui();
                id = -1;
                // run swingworker
                worker = new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        while (true) {
                            if (CustomerInfo.isClose() == true) {
                                updateCustomerstoTable();
                                break;
                            }
                            Thread.sleep(100);
                        }
                        return null;
                    }

                };
                worker.execute();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

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

        private int getCusId() {
            int id = 0;
            try {
                setQuerry = new QueryDB();
                conDB = setQuerry.getConnDB();

                stmt = conDB.createStatement();

                queryResult = setQuerry.dbSetQuery("SELECT ID FROM Customers"
                        + " WHERE NAME LIKE '" + litsCustomers.getModel().getValueAt(
                                litsCustomers.convertRowIndexToView(selectRow), 0)
                        + "' AND SURNAME LIKE '" + litsCustomers.getModel().getValueAt(
                                litsCustomers.convertRowIndexToView(selectRow), 1)
                        + "';");

                while (queryResult.next()) {
                    id = queryResult.getInt("ID");
                }
                selectRow = -1;
                setQuerry.closeDB();
            } catch (SQLException ex) {
                LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", ex.getMessage()
                        + "\n" + Arrays.toString(ex.getStackTrace()));
                startLogging.logToFile(text);
            }
            return id;
        }

    }

    /**
     * @see this class implement filter option
     */
    private class CustomerFilter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (searchText.getText().length() > 2) {
                // method
                searchCustomer();
            } else {
                updateCustomerstoTable();
            }
        }

        // search customer
        private void searchCustomer() {
            try {
                model.setRowCount(0);
                setQuerry = new QueryDB();
                conDB = setQuerry.getConnDB();

                stmt = conDB.createStatement();

                queryResult = setQuerry.dbSetQuery("SELECT * FROM Customers"
                        + " WHERE NAME LIKE '%" + searchText.getText()
                        + "%' OR SURNAME LIKE '%" + searchText.getText()
                        + "%';");

                while (queryResult.next()) {
                    String trustString = "Nie zaufany";
                    int trustVal = queryResult.getInt("TRUST");
                    if (trustVal == 1) {
                        trustString = "Zaufany";
                    }
                    Object[] data = {
                        queryResult.getString("NAME"),
                        queryResult.getString("SURNAME"),
                        queryResult.getString("ADDRESS"),
                        queryResult.getString("PESEL"),
                        trustString
                    };
                    model.addRow(data);
                    repaint();
                }

            } catch (SQLException ex) {
                LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", ex.getMessage()
                        + "\n" + Arrays.toString(ex.getStackTrace()));
                startLogging.logToFile(text);
            }
        }
    }

}
