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
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import static lombardia2014.Interface.MainMMenu.money;
import lombardia2014.SelfCalc;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Mateusz
 */
public class ProlongationForm extends Forms {

    String frameTitle = "Przedłużenie pożyczki";
    String tablePanelTitle = "Lista umów";
    JFrame prolongateFrame = new JFrame();
    JPanel tablePanel;
    JPanel buttonPanel;
    JScrollPane scrollPane = null;
    JTable listAgreements = null;
    DefaultTableModel model;

    //Buttons
    JButton cancel = null;
    JButton prolong = null;

    //Text in panel
    JLabel title = null;

    //Additional variables
    String agreementNumber;
    int id;
    int agreementIdent;
    String ID;

    int selectRow = -1;

    //Database variables
    QueryDB setQuerry = null;
    private ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    int iClose = 0;
    //creditForm object
    CreditForm newCredit = null;
    double adRemValue = 0.0;

    //maps
    Map<String, String> paymentPorperies = new HashMap<>();
    Map<Integer, HashMap> itemsList = new HashMap<>();
    Map<String, String> userInfo = new HashMap<>();
    Map<Integer, String> categories = new HashMap<>();

    public ProlongationForm() {
        //first i do select to db 

        // next i create payment form with parameters with rights to edit date
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

    @Override
    public void generatePanels(GridBagConstraints c) {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };

        listAgreements = new JTable(model);
        model.addColumn("Imię");
        model.addColumn("Nazwisko");
        model.addColumn("Nr umowy");
        model.addColumn("Pierwotna data zwrotu");
        model.addTableModelListener(listAgreements);
        listAgreements.addMouseListener(new GetSelectRow());

        getAgreements();

        listAgreements.setAutoCreateRowSorter(true);
        scrollPane = new JScrollPane(listAgreements);
        listAgreements.setFillsViewportHeight(true);

        c.gridwidth = 1;
        c.gridy = 2;
        c.gridx = 0;
        c.insets = new Insets(10, 10, 10, 10);
        c.ipadx = 650;
        c.ipady = 600;
        listAgreements.setPreferredSize(new Dimension(650, 600));

        scrollPane.setPreferredSize(new Dimension(650, 600));
        scrollPane.setVisible(true);

        tablePanel.add(scrollPane);

    }

    private void generatePanels2() {
        GridBagConstraints c2 = new GridBagConstraints();
        cancel = new JButton();
        cancel.setText("Anuluj");
        cancel.addActionListener(new CloseForm());
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.insets = new Insets(5, 5, 5, 5);
        c2.gridwidth = 1;
        c2.gridx = 2;
        c2.gridy = 5;

        prolong = new JButton();
        prolong.setText("Przedłuż");
        prolong.addActionListener(new ProlongAgreement());
        c2.insets = new Insets(5, 5, 5, 5);
        c2.gridwidth = 1;
        c2.gridx = 0;
        c2.gridy = 5;

        buttonPanel.add(cancel);
        buttonPanel.add(prolong);
        mainPanel.add(tablePanel, c);
        mainPanel.add(buttonPanel, c2);
    }

    public boolean isClose() {
        return iClose == 1;
    }

    public class ProlongAgreement implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    public class CloseForm implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            formFrame.dispose();
        }
    }

    private Map<String, Integer> prolongAgreement() {
        Map<String, Integer> ids = new HashMap<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT * FROM Agreements "
                    + "WHERE ID_AGREEMENTS LIKE '" + ID + "';");

            while (queryResult.next()) {
                paymentPorperies.put("NR Umowy", queryResult.getString("ID_AGREEMENTS"));
                paymentPorperies.put("Data rozpoczecia", queryResult.getString("START_DATE"));
                paymentPorperies.put("Data zwrotu", queryResult.getString("STOP_DATE"));
                paymentPorperies.put("Kwota", queryResult.getString("VALUE"));
                paymentPorperies.put("Rabat", queryResult.getString("DISCOUNT"));
                paymentPorperies.put("Opłata magayznowania", queryResult.getString("SAVEPRICE"));
                paymentPorperies.put("Opłata manipulacyjna", queryResult.getString("COMMISSION"));
                paymentPorperies.put("Razem do zapłaty", queryResult.getString("VALUE_REST"));
                paymentPorperies.put("Łączna waga", queryResult.getString("ITEM_WEIGHT"));
                paymentPorperies.put("Łączna wartosc", queryResult.getString("ITEM_VALUE"));
                // get ids
                ids.put("CustID", queryResult.getInt("ID_CUSTOMER"));
                ids.put("AgrID", queryResult.getInt("ID"));
            }

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
        setQuerry.closeDB();
        return ids;
    }

    /**
     * @see this class get user info and save to hashmap
     *
     */
    private void getUserInfo(int id) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT * FROM Customers WHERE "
                    + "ID = " + id + ";");

            while (queryResult.next()) {
                userInfo.put("Imie", queryResult.getString("NAME"));
                userInfo.put("Nazwisko", queryResult.getString("SURNAME"));
                userInfo.put("Adres", queryResult.getString("ADDRESS"));
                userInfo.put("Pesel", queryResult.getString("PESEL"));
                userInfo.put("Zaufany klient", queryResult.getString("TRUST"));
            }

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
        setQuerry.closeDB();
    }

    /**
     * @see this method get items from
     */
    private void getItemsFromAgreement(int id) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();
            int i = 0; //inrement this value to save item to list

            // first i select categories and save to db :)
            queryResult = setQuerry.dbSetQuery("SELECT * FROM Category;");

            while (queryResult.next()) {
                categories.put(queryResult.getInt("ID"),
                        queryResult.getString("NAME"));
            }

            // now action to items
            queryResult = setQuerry.dbSetQuery("SELECT * FROM Items WHERE "
                    + "ID_AGREEMENT = " + id + ";");
            Map<String, String> items = new HashMap<>();

            while (queryResult.next()) {
                items.put("Kategoria", categories.get(
                        queryResult.getInt("ID_CATEGORY")));
                items.put("Model", queryResult.getString("MODEL"));
                items.put("Marka", queryResult.getString("BAND"));
                items.put("Typ", queryResult.getString("TYPE"));
                items.put("Waga", queryResult.getString("WEIGHT"));
                items.put("Wartość", queryResult.getString("VALUE"));
                items.put("IMEI", queryResult.getString("IMEI"));
                items.put("Uwagi", queryResult.getString("ATENCION"));
                itemsList.put(i, (HashMap) items);
                i++;
            }
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
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

    public void deleteAgreement(String ID) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();

            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("DELETE FROM Agreements WHERE "
                    + "ID_AGREEMENTS LIKE '" + ID + "';");

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

    }

    // actions for selected elements in table
    private class GetSelectRow implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            JTable table = (JTable) e.getSource();
            Point p = e.getPoint();

            if (e.getClickCount() == 2) {
                selectRow = table.getSelectedRow();
                int row = table.rowAtPoint(p);

                ID = (String) listAgreements.getModel().getValueAt(
                        listAgreements.convertRowIndexToView(selectRow), 2);

                Map<String, Integer> Ids = new HashMap<>();
                Ids.putAll(prolongAgreement());
                if (Ids.get("CustID") != null) {
                    getUserInfo(Ids.get("CustID"));
                    getItemsFromAgreement(Ids.get("AgrID"));
                    // save money
                    SelfCalc actualCalc = new SelfCalc();
                    actualCalc.chackValue(formFrame);
                    actualCalc.addToSelf(Float.parseFloat(
                            paymentPorperies.get("Łączna wartosc")));
                    adRemValue = Double.parseDouble(
                            paymentPorperies.get("Łączna wartosc"));
                    money.setText(actualCalc.getValue() + " zł");
                    // next i create new payment with constructor :)
                    newCredit = new CreditForm(itemsList, paymentPorperies, userInfo, false);
                    deleteAgreement(ID);
                    iClose = 1;
                    formFrame.dispose();
                }

            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //// do nothing
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
