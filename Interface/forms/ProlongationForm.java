package lombardia2014.Interface.forms;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import static lombardia2014.Interface.MainMMenu.money;
import lombardia2014.core.SelfCalc;
import lombardia2014.dataBaseInterface.MainDBQuierues;
import lombardia2014.dataBaseInterface.UserOperations;
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
    int iClose = 0;
    //creditForm object
    CreditForm newCredit = null;
    double adRemValue = 0.0;
    MainDBQuierues getQuery = new MainDBQuierues();


    //maps
    Map<String, String> paymentPorperies = new HashMap<>();
    Map<Integer, HashMap> itemsList = new HashMap<>();
    Map<String, String> userInfo = new HashMap<>();
    Map<Integer, String> categories = new HashMap<>();
    SwingWorker<Void, Void> worker = null;
    UserOperations sniffOperations = new UserOperations();

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

        buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setPreferredSize(new Dimension(100, 600));

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
        listAgreements.setPreferredSize(new Dimension(600, 600));

        scrollPane.setPreferredSize(new Dimension(600, 600));
        scrollPane.setVisible(true);

        tablePanel.add(scrollPane);

    }

    private void generatePanels2() {
        GridBagConstraints c2 = new GridBagConstraints();
        prolong = new JButton();
        prolong.setText("Przedłuż");
        prolong.addActionListener(new ProlongAgreement());
        c2.insets = new Insets(5, 5, 5, 5);
        c2.gridwidth = 1;
        c2.gridx = 0;
        c2.gridy = 5;
        buttonPanel.add(prolong, c2);

        cancel = new JButton();
        cancel.setText("Anuluj");
        cancel.addActionListener(new CloseForm());
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.gridwidth = 1;
        c2.gridx = 1;
        c2.gridy = 5;
        buttonPanel.add(cancel, c2);

        c.insets = new Insets(5, 5, 5, 5);
        c.gridy = 0;
        c.gridx = 0;
        c.ipadx = 600;
        c.ipady = 600;

        mainPanel.add(tablePanel, c);
        c.gridy = 1;
        c.gridx = 0;
        c.ipadx = 0;
        c.ipady = 0;
        mainPanel.add(buttonPanel, c);
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
        paymentPorperies = getQuery.getAgreementByID(ID);
        Map<String, Integer> ids = new HashMap<>();

        // get ids
        ids.put("CustID", Integer.parseInt(paymentPorperies.get("CustID")));
        ids.put("AgrID", Integer.parseInt(paymentPorperies.get("AgrID")));
        
        return ids;
    }

    /**
     * @see this class get user info and save to hashmap
     *
     */
    private void getUserInfo(int id) {
          userInfo = getQuery.getUserByID(id);
    }

    /**
     * @see this method get items from
     */
    private void getItemsFromAgreement(int id) {
        categories = getQuery.getCategoriesWithID();
        itemsList = getQuery.itemsElement(categories, id);
    }

    public void getAgreements() {
        List<HashMap<String, String>> Settlements = getQuery.getAgreementsAndCustomers();

        for(HashMap<String, String> agreement : Settlements) {
            Object[] data = {
                agreement.get("Imie"),
                agreement.get("Nazwisko"),
                agreement.get("NR Umowy"),
                agreement.get("Data zwrotu"),
            };
            model.addRow(data);
            agreementIdent = Integer.parseInt(agreement.get("AgrID"));
        }
    }

    public void deleteAgreement(String ID) {
        getQuery.deleteAgreement(ID);
    }

    // actions for selected elements in table
    private class GetSelectRow implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            JTable table = (JTable) e.getSource();

            if (e.getClickCount() == 2) {
                selectRow = table.getSelectedRow();

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
                    //deleteAgreement(ID);
                    worker = new SwingWorker<Void, Void>() {

                        @Override
                        protected Void doInBackground() {
                            try {
                                while (true) {
                                    if (newCredit.isClose() == true) {
                                        sniffOperations.saveOperations("Wystawiono umowe kredytu na:"
                                                + newCredit.getAddRemoValue());
                                        deleteAgreement(ID);
                                        break;
                                    }
                                    Thread.sleep(100);
                                }
                            } catch (Exception ex) {
                                LombardiaLogger startLogging = new LombardiaLogger();
                                String text = startLogging.preparePattern("Error", ex.getMessage()
                                        + "\n" + Arrays.toString(ex.getStackTrace()));
                                startLogging.logToFile(text);
                            }
                            return null;
                        }

                    };
                    worker.execute();
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
