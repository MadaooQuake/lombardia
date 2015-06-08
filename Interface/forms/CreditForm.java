/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.forms;

import lombardia2014.dataBaseInterface.QueryDB;
import lombardia2014.generators.AutoSuggestor;
import lombardia2014.generators.CreditAgreement;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import lombardia2014.core.ConfigRead;
import lombardia2014.core.SelfCalc;
import lombardia2014.core.SetLocatnion;
import lombardia2014.core.ValueCalc;
import lombardia2014.dataBaseInterface.MainDBQuierues;
import lombardia2014.generators.ItemChecker;
import lombardia2014.generators.LombardiaLogger;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author Domek
 */
public class CreditForm extends Forms {

    JButton cancel = null;
    JButton ok = null;
    JLabel fieldName = null;
    JPanel[] actionPanels = null;
    JButton newCustomer = null;
    JButton oldCustomer = null;
    JButton newItem = null;
    JButton addOldItem = null;
    JButton listItem = null;
    JLabel[] namedField = null;
    JTextField[] fields = null;
    JTextField addresCustomer = null;
    int limitItems = 50;
    int howItem = 0;
    int itemX = 10, itemY = 300;
    int howMany = 0;
    int fontSize = 12;
    int heightTextL = 20;
    int iClose = 0;
    JComboBox selectCategory = null;
    AutoSuggestor selectCustomer = null;
    JCheckBox goodClient = null;
    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    JPanel itemPanel = null;
    JPanel newItemPanel = null;
    JScrollPane scrollPane = null;
    GridLayout itemPanelLayout = null;
    JCheckBox[] jewelleryItem = null;
    JTextField[] jewelleryField = null;
    JTextField[] gameFields = null;
    CreditAgreement newDoc = null;
    ValueCalc precentCalc = null;
    ConfigRead readParam = null;
    float rrso = 0, discount = 0;
    String weight = "0";
    boolean update = false;
    SelfCalc moneySafe = null;
    SpinnerModel modelSpinner = null;
    JSpinner spinner = null;

    // only for test in next time app get customerr list from DB
    GridBagConstraints[] cTab = new GridBagConstraints[4];
    GridBagConstraints newItemGrid = new GridBagConstraints();
    // Hashmap to save items in payments  form
    Map<Integer, HashMap> itemsList = new HashMap<>();
    Map<Integer, HashMap> oldItemsList = new HashMap<>();
    Map<String, String> paymentPorperies = new HashMap<>();
    Map<String, String> userInfo = new HashMap<>();
    JLabel itemValue = null;
    JButton deleteItem = null;
    FormValidator checkItem = new FormValidator();
    JDatePickerImpl datePicker = null;
    Date selectedDate = null;
    Date curretDate = null;
    SimpleDateFormat ft = new SimpleDateFormat("dd.MM.YYYY HH:mm");
    UtilDateModel model = new UtilDateModel();
    double adRemValue = 0.0;
    ItemChecker creatItemtoDB = new ItemChecker();
    SetLocatnion polishSign = new SetLocatnion();
    MainDBQuierues getQuery = null;

    public CreditForm(Map itemsList_, Map paymentPorperies_,
            Map userInfo_, boolean update_) {
        itemsList.putAll(itemsList_);
        paymentPorperies.putAll(paymentPorperies_);
        userInfo.putAll(userInfo_);
        update = update_;
        oldItemsList.putAll(itemsList);
        generateGui();
        getValues();

    }

    public CreditForm() {

    }

    @Override
    public void generateGui() {
        readParam = new ConfigRead();
        readParam.readFile();
        formFrame.setSize(1000, 750);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("Nowa pożyczka");
        formFrame.setUndecorated(true);
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setPreferredSize(new Dimension(1000, 750));
        titleBorder = BorderFactory.createTitledBorder(blackline, "Nowa pożyczka");
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        titleBorder.setBorder(blackline);
        mainPanel.setBorder(titleBorder);

        generatePanels(c);

        formFrame.add(mainPanel);
        formFrame.setVisible(true);
    }

    /**
     * @see generate all panels for this form
     * @param c
     */
    @Override
    public void generatePanels(GridBagConstraints c) {
        namedField = new JLabel[27];
        fields = new JTextField[25];
        actionPanels = new JPanel[5];
        // select customer
        actionPanels[0] = new JPanel(new GridBagLayout());
        titleBorder = BorderFactory.createTitledBorder(blackline, "Dane Klienta");
        titleBorder.setTitleJustification(TitledBorder.LEFT);
        titleBorder.setBorder(blackline);
        actionPanels[0].setBorder(titleBorder);
        c.insets = new Insets(5, 2, 2, 2);
        // method to create fields and 
        customerForm();

        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 0;
        mainPanel.add(actionPanels[0], c);

        //next form. this form show list of item
        actionPanels[1] = new JPanel(new GridBagLayout());
        titleBorder = BorderFactory.createTitledBorder(blackline, "Dodaj Depozyt");
        titleBorder.setTitleJustification(TitledBorder.LEFT);
        titleBorder.setBorder(blackline);
        actionPanels[1].setBorder(titleBorder);

        itemForm();

        c.gridx = 1;
        c.gridy = 0;
        c.ipady = 0;
        mainPanel.add(actionPanels[1], c);

        // form about credit rules
        actionPanels[2] = new JPanel(new GridBagLayout());
        titleBorder = BorderFactory.createTitledBorder(blackline, "Warunki Kredytu");
        titleBorder.setTitleJustification(TitledBorder.LEFT);
        titleBorder.setBorder(blackline);
        actionPanels[2].setBorder(titleBorder);

        creditForm();

        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 40;
        c.ipady = 40;
        mainPanel.add(actionPanels[2], c);

        //new panel he show list of items
        actionPanels[4] = new JPanel(new GridBagLayout());
        titleBorder = BorderFactory.createTitledBorder(blackline, "Lista Depozytów");
        titleBorder.setTitleJustification(TitledBorder.LEFT);
        titleBorder.setBorder(blackline);
        actionPanels[4].setBorder(titleBorder);

        // elements in panels
        depositForm();
        //position
        c.gridx = 1;
        c.gridy = 1;
        c.ipadx = 10;
        c.ipady = 60;
        mainPanel.add(actionPanels[4], c);

        //panel without the borer :D
        actionPanels[3] = new JPanel(new GridBagLayout());
        c.insets = new Insets(5, 0, 0, 5);
        actionForm();
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        c.ipady = 0;
        c.ipady = 2;
        mainPanel.add(actionPanels[3], c);
    }

    /**
     * @see this method generate all elements on customer form :)
     *
     */
    private void customerForm() {
        cTab[0] = new GridBagConstraints();
        cTab[0].insets = new Insets(5, 5, 5, 5);
        // first add buttons
        newCustomer = new JButton();
        newCustomer.setText("Nowy");
        newCustomer.setPreferredSize(new Dimension(10, heightTextL));
        newCustomer.setFont(new Font("Dialog", Font.BOLD, fontSize));
        newCustomer.addActionListener(new NewCustomer());
        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 0;
        cTab[0].gridy = 0;
        cTab[0].ipadx = 130;
        actionPanels[0].add(newCustomer, cTab[0]);

        oldCustomer = new JButton();
        oldCustomer.setText("Stały");
        oldCustomer.setPreferredSize(new Dimension(150, heightTextL));
        oldCustomer.setFont(new Font("Dialog", Font.BOLD, fontSize));
        oldCustomer.addActionListener(new OldCustomer());
        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 1;
        cTab[0].gridy = 0;
        actionPanels[0].add(oldCustomer, cTab[0]);

        //list of old customers
        namedField[0] = new JLabel();
        namedField[0].setText("Wybierz klienta:");
        namedField[0].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 0;
        cTab[0].gridy = 1;
        actionPanels[0].add(namedField[0], cTab[0]);

        // new filds
        fields[2] = new JTextField();
        fields[2].setPreferredSize(new Dimension(150, heightTextL));
        fields[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[2].getDocument().addDocumentListener(new fillCustomer());

        selectCustomer = new AutoSuggestor(
                fields[2], formFrame, null, Color.WHITE.brighter(),
                Color.BLUE, Color.RED, 0.75f, 50, 59) {
                    @Override
                    public boolean wordTyped(String typedWord) {
                        getQuery = new MainDBQuierues();
                        List<String> words = (ArrayList) getQuery.getUsersByNameAndSurname();
                        setDictionary((ArrayList<String>) words);
                        return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
                    }
                };

        cTab[0].gridx = 1;
        cTab[0].gridy = 1;
        actionPanels[0].add(fields[2], cTab[0]);

        // now i create rest filds in this panel
        namedField[1] = new JLabel();
        namedField[1].setText("Imię:");
        namedField[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 0;
        cTab[0].gridy = 2;
        actionPanels[0].add(namedField[1], cTab[0]);

        fields[0] = new JTextField();
        fields[0].setPreferredSize(new Dimension(150, heightTextL));
        fields[0].setFont(new Font("Dialog", Font.BOLD, fontSize));
        //fields[0].addKeyListener(new CreatePaymID());
        fields[0].setText(null);
        cTab[0].gridx = 1;
        cTab[0].gridy = 2;
        actionPanels[0].add(fields[0], cTab[0]);

        // new field
        namedField[2] = new JLabel();
        namedField[2].setText("Nazwisko:");
        namedField[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 0;
        cTab[0].gridy = 3;
        actionPanels[0].add(namedField[2], cTab[0]);

        fields[1] = new JTextField();
        fields[1].setPreferredSize(new Dimension(150, heightTextL));
        fields[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
        //fields[1].addKeyListener(new CreatePaymID());
        fields[1].setText(null);
        cTab[0].gridx = 1;
        cTab[0].gridy = 3;
        actionPanels[0].add(fields[1], cTab[0]);

        namedField[3] = new JLabel();
        namedField[3].setText("Adres:");
        namedField[3].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 0;
        cTab[0].gridy = 4;
        actionPanels[0].add(namedField[3], cTab[0]);

        addresCustomer = new JTextField();
        addresCustomer.setPreferredSize(new Dimension(140, 60));
        addresCustomer.setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].insets = new Insets(5, 5, 5, 0);
        cTab[0].gridx = 1;
        cTab[0].gridy = 4;
        actionPanels[0].add(addresCustomer, cTab[0]);

        namedField[4] = new JLabel();
        namedField[4].setText("Pesel:");
        namedField[4].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 0;
        cTab[0].gridy = 5;
        actionPanels[0].add(namedField[4], cTab[0]);

        fields[3] = new JTextField();
        fields[3].setPreferredSize(new Dimension(150, heightTextL));
        fields[3].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 1;
        cTab[0].gridy = 5;
        actionPanels[0].add(fields[3], cTab[0]);

        namedField[14] = new JLabel();
        namedField[14].setText("Zaufany klient:");
        namedField[14].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 0;
        cTab[0].gridy = 6;
        actionPanels[0].add(namedField[14], cTab[0]);

        goodClient = new JCheckBox();
        goodClient.setSelected(false);
        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 1;
        cTab[0].gridy = 6;
        actionPanels[0].add(goodClient, cTab[0]);

        fields[2].setEnabled(true);
        fields[0].setEnabled(false);
        fields[1].setEnabled(false);
        addresCustomer.setEnabled(false);
        fields[3].setEnabled(false);
        oldCustomer.setEnabled(false);
        goodClient.setEnabled(false);
    }

    private void itemForm() {
        cTab[1] = new GridBagConstraints();
        cTab[1].insets = new Insets(5, 5, 5, 5);
        // first add buttons
        // create mini form to add new product in database
        newItem = new JButton();
        newItem.setText("Nowa");
        newItem.setPreferredSize(new Dimension(150, heightTextL));
        newItem.addActionListener(new newItem());
        newItem.setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[1].fill = GridBagConstraints.HORIZONTAL;
        cTab[1].gridx = 0;
        cTab[1].gridy = 0;
        cTab[1].ipady = 0;
        cTab[1].ipadx = 110;
        actionPanels[1].add(newItem, cTab[1]);

        addOldItem = new JButton();
        addOldItem.setText("Dodaj");
        addOldItem.setPreferredSize(new Dimension(150, heightTextL));
        addOldItem.setFont(new Font("Dialog", Font.BOLD, fontSize));
        addOldItem.addActionListener(new addItemToList());
        cTab[1].gridx = 1;
        cTab[1].gridy = 0;
        actionPanels[1].add(addOldItem, cTab[1]);

        // combobox with list of all items
        namedField[5] = new JLabel();
        namedField[5].setText("Kategoria:");
        namedField[5].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[1].fill = GridBagConstraints.HORIZONTAL;
        cTab[1].gridx = 0;
        cTab[1].gridy = 1;

        actionPanels[1].add(namedField[5], cTab[1]);

        // Create selector;
        JaweryForm newItemForm = new JaweryForm();
        selectCategory = new JComboBox(getCategories());
        selectCategory.setSelectedIndex(0);
        selectCategory.addActionListener(newItemForm);

        cTab[1].gridx = 1;
        cTab[1].gridy = 1;

        actionPanels[1].add(selectCategory, cTab[1]);

        newItemPanel = new JPanel(new GridBagLayout());
        scrollPane = new JScrollPane(newItemPanel);
        scrollPane.setPreferredSize(new Dimension(330, 118));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        newItemGrid.fill = GridBagConstraints.HORIZONTAL;
        newItemForm.generateJaweryForm();

        cTab[1].gridwidth = 2;
        cTab[1].gridx = 0;
        cTab[1].gridy = 2;
        cTab[1].ipadx = 330;
        cTab[1].ipady = 118;
        actionPanels[1].add(scrollPane, cTab[1]);
        // 6, 8

    }

    private void creditForm() {

        namedField[6] = new JLabel();
        namedField[6].setText("Data zwrotu:");
        namedField[6].setFont(new Font("Dialog", Font.BOLD, fontSize));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 90;
        c.ipady = 5;
        actionPanels[2].add(namedField[6], c);

        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        datePicker = new JDatePickerImpl(datePanel);
        datePicker.setPreferredSize(new Dimension(150, heightTextL));
        datePicker.setFont(new Font("Dialog", Font.BOLD, fontSize));
        datePicker.addActionListener(new calculatePaymentsOfForm());

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        actionPanels[2].add(datePicker, c);

        namedField[7] = new JLabel();
        namedField[7].setText("Kwota:");
        namedField[7].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        actionPanels[2].add(namedField[7], c);

        fields[4] = new JTextField();
        fields[4].setPreferredSize(new Dimension(150, heightTextL));
        fields[4].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[4].getDocument().addDocumentListener(new calculatePaymentsOfForm());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        actionPanels[2].add(fields[4], c);

        namedField[16] = new JLabel();
        namedField[16].setText("RSO:");
        namedField[16].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        actionPanels[2].add(namedField[16], c);

        fields[13] = new JTextField();
        fields[13].setPreferredSize(new Dimension(150, heightTextL));
        fields[13].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[13].setEditable(false);
        c.gridx = 1;
        c.gridy = 3;
        actionPanels[2].add(fields[13], c);

        namedField[17] = new JLabel();
        namedField[17].setText("Opłata magayznowania:");
        namedField[17].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        actionPanels[2].add(namedField[17], c);

        fields[14] = new JTextField();
        fields[14].setPreferredSize(new Dimension(150, heightTextL));
        fields[14].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[14].setEditable(false);
        c.gridx = 1;
        c.gridy = 4;
        actionPanels[2].add(fields[14], c);

        namedField[21] = new JLabel();
        namedField[21].setText("Rabat(%):");
        namedField[21].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        actionPanels[2].add(namedField[21], c);

        fields[18] = new JTextField();
        fields[18].setPreferredSize(new Dimension(150, heightTextL));
        fields[18].setFont(new Font("Dialog", Font.BOLD, fontSize));
        // i must add option when rabat is set 
        fields[18].getDocument().addDocumentListener(new calculatePaymentsOfForm());
        c.gridx = 1;
        c.gridy = 5;
        actionPanels[2].add(fields[18], c);

        /**
         * new elements *
         */
        // handling payment
        namedField[22] = new JLabel();
        namedField[22].setText("Opłata manipulacyjna ("
                + readParam.getMinFee() + "):");
        namedField[22].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 6;
        actionPanels[2].add(namedField[22], c);

        fields[19] = new JTextField();
        fields[19].setPreferredSize(new Dimension(150, heightTextL));
        fields[19].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[19].setEditable(false);
        c.gridx = 1;
        c.gridy = 6;
        actionPanels[2].add(fields[19], c);

        // rro
        namedField[23] = new JLabel();
        namedField[23].setText("Odsetki Terminowe ("
                + readParam.getLombardRate() + "):");
        namedField[23].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 7;
        actionPanels[2].add(namedField[23], c);

        fields[20] = new JTextField();
        fields[20].setPreferredSize(new Dimension(150, heightTextL));
        fields[20].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[20].setEditable(false);
        c.gridx = 1;
        c.gridy = 7;
        actionPanels[2].add(fields[20], c);

        // fee
        namedField[24] = new JLabel();
        namedField[24].setText("Łączne opłaty");
        namedField[24].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 8;
        actionPanels[2].add(namedField[24], c);

        fields[21] = new JTextField();
        fields[21].setPreferredSize(new Dimension(150, heightTextL));
        fields[21].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[21].setEditable(false);
        c.gridx = 1;
        c.gridy = 8;
        actionPanels[2].add(fields[21], c);

        // all 
        namedField[25] = new JLabel();
        namedField[25].setText("Razem do zapłaty:");
        namedField[25].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 9;
        actionPanels[2].add(namedField[25], c);

        fields[22] = new JTextField();
        fields[22].setPreferredSize(new Dimension(150, heightTextL));
        fields[22].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[22].setEditable(false);
        c.gridx = 1;
        c.gridy = 9;
        actionPanels[2].add(fields[22], c);

        namedField[19] = new JLabel();
        namedField[19].setText("Łączna Wartość:");
        namedField[19].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.gridx = 0;
        c.gridy = 10;
        actionPanels[2].add(namedField[19], c);

        fields[16] = new JTextField();
        fields[16].setPreferredSize(new Dimension(150, heightTextL));
        fields[16].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.gridx = 1;
        c.gridy = 10;
        actionPanels[2].add(fields[16], c);
    }

    private void depositForm() {
        cTab[2] = new GridBagConstraints();
        cTab[2].insets = new Insets(1, 1, 1, 1);
        cTab[2].ipadx = 100;

        namedField[18] = new JLabel();
        namedField[18].setText("Łączna waga (gramy):");
        namedField[18].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[2].gridx = 0;
        cTab[2].gridy = 0;
        actionPanels[4].add(namedField[18], cTab[2]);

        fields[15] = new JTextField();
        fields[15].setPreferredSize(new Dimension(150, heightTextL));
        fields[15].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[2].gridx = 1;
        cTab[2].gridy = 0;
        actionPanels[4].add(fields[15], cTab[2]);

        // internal panel :D
        itemPanelLayout = new GridLayout(0, 2);

        itemPanel = new JPanel(itemPanelLayout);
        itemPanel.setPreferredSize(new Dimension(200, 220));
        scrollPane = new JScrollPane(itemPanel);
        //scrollPane.setLayout(itemPanelLayout);
        scrollPane.setPreferredSize(new Dimension(200, 220));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        titleBorder = BorderFactory.createTitledBorder(blackline, "Lista Depozytów");
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        titleBorder.setBorder(blackline);
        scrollPane.setBorder(titleBorder);
        cTab[2].gridwidth = 2;
        cTab[2].gridheight = 2;
        cTab[2].gridx = 0;
        cTab[2].gridy = 2;
        cTab[2].ipadx = 250;
        cTab[2].ipady = 220;
        actionPanels[4].add(scrollPane, cTab[2]);
    }

    private void actionForm() {
        ok = new JButton();
        ok.setText("Generuj");
        ok.setPreferredSize(new Dimension(150, heightTextL));
        ok.setFont(new Font("Dialog", Font.BOLD, 18));
        ok.addActionListener(new CreatePayment());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 5;

        actionPanels[3].add(ok, c);

        cancel = new JButton();
        cancel.setText("Anuluj");
        cancel.setPreferredSize(new Dimension(150, heightTextL));
        cancel.setFont(new Font("Dialog", Font.BOLD, 18));
        cancel.addActionListener(new CloseForm());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        actionPanels[3].add(cancel, c);
    }

    /**
     * @return @see this method fill the Agreement ID
     */
    public int fillPayID() {
        if ((fields[0].getText().length() > 1) && (fields[1].getText().length() > 1)) {
            getQuery = new MainDBQuierues();
            howMany = getQuery.getMaxIDAgreements();
        } else {
            fields[12].setText(null);
        }

        return howMany;
    }

    /**
     * @create index
     * @return
     */
    public String createIndex() {
        String pattern;

        String IDPayment = fields[0].getText().substring(0, 1);
        IDPayment += fields[1].getText().substring(0, 1);

        pattern = IDPayment;
        //Generate id
        Random randomNum = new Random();

        int id = randomNum.nextInt(999999 - 100000) + 100000;

        pattern += id;

        return pattern;

    }

    public Double getAddRemoValue() {
        return adRemValue;
    }

    // fill records 
    private void getValues() {
        boolean trust = false;
        // user info
        // userInfo
        // name
        fields[0].setText(userInfo.get("Imie"));
        // surename
        fields[1].setText(userInfo.get("Nazwisko"));
        // address
        addresCustomer.setText(userInfo.get("Adres"));
        // pesel
        fields[3].setText(userInfo.get("Pesel"));
        // trust :D
        trust = userInfo.get("Zaufany klient").equals("1");
        goodClient.setSelected(trust);

        // agreement info paymentPorperies
        //agreement id
        // end date
        String[] fullDate = paymentPorperies.get("Data zwrotu").split(" ");
        String year = fullDate[0];
        model.setDate(Integer.parseInt(year.substring(6, year.length())),
                Integer.parseInt(year.substring(3, 5)) - 1,
                Integer.parseInt(year.substring(0, 2)));

        model.setSelected(true);
        // value
        fields[4].setText(paymentPorperies.get("Kwota"));
        fields[15].setText(paymentPorperies.get("Łączna waga"));
        fields[16].setText(paymentPorperies.get("Łączna wartosc"));

        addItemToList items = new addItemToList();
        items.generateItemtoForm();

    }

    /**
     * time to actions under is action class - class which turn on create new
     * customer - class turn on new elemeners - class who create some new
     * elements - etc
     */
    /**
     * first panel - about customers
     */
    public class NewCustomer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            fields[0].setEnabled(true);
            fields[1].setEnabled(true);
            addresCustomer.setEnabled(true);
            fields[3].setEnabled(true);
            oldCustomer.setEnabled(true);
            fields[2].setEnabled(false);
            //listOldUser.setEditable(false);
            newCustomer.setEnabled(false);
            goodClient.setEnabled(true);
        }

    }

    public class OldCustomer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            fields[0].setEnabled(false);
            fields[1].setEnabled(false);
            addresCustomer.setEnabled(false);
            fields[3].setEnabled(false);
            newCustomer.setEnabled(true);
            oldCustomer.setEnabled(false);
            fields[2].setEnabled(true);
            goodClient.setEnabled(false);
            //listOldUser.setEditable(true);
        }

    }

    /**
     * Item list actions
     */
    public class newItem implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            NewCategory addNews = new NewCategory();
            addNews.generateGui();
        }

    }

    public class addItemToList implements ActionListener, ItemFormGenerator {

        boolean checkElement = false;

        @Override
        public void actionPerformed(ActionEvent e) {

            // selec some category :D
            switch (selectCategory.getSelectedItem().toString().toLowerCase()) {
                case "wyroby jubilerskie":
                    generateJaweryForm();
                    break;
                case "laptop":
                    generateLaptopForm();
                    break;
                case "komputer":
                    generatePCForm();
                    break;
                case "monitor":
                    generateMonitorForm();
                    break;
                case "telewizor":
                    generateTVForm();
                    break;
                case "telefon":
                    generatePhoneForm();
                    break;
                case "tablet":
                    generateTabletForm();
                    break;
                case "gry":
                    generateGamesForm();
                    break;
                default:
                    generateDefault();
                    break;
            }
            generateItemtoForm();

        }

        /**
         * @param countItem
         * @param model
         * @param brand
         * @param type
         * @param weigth
         * @param value
         * @param imei
         * @param warnings
         * @param Category
         * @see add item to hashMap
         */
        public void addItemtoList(int countItem, String model, String brand,
                String type, String weigth, String value, String imei,
                String warnings, String Category) {
            if (!model.isEmpty()) {
                Map<String, String> items = new HashMap<>();
                if (itemsList.isEmpty()) {
                    for (int count = 0; count < countItem; count++) {
                        items.put("Kategoria", Category);
                        items.put("Model", model);
                        items.put("Marka", brand);
                        items.put("Typ", type);
                        items.put("Waga", weigth);
                        items.put("Wartość", value);
                        items.put("IMEI", imei);
                        items.put("Uwagi", warnings);
                        itemsList.put(count, (HashMap) items);
                    }
                } else {
                    int lastItemIndex = itemsList.size();
                    for (int count = lastItemIndex; count < countItem + lastItemIndex; count++) {
                        items.put("Kategoria", Category);
                        items.put("Model", model);
                        items.put("Marka", brand);
                        items.put("Typ", type);
                        items.put("Waga", weigth);
                        items.put("Wartość", value);
                        items.put("IMEI", imei);
                        items.put("Uwagi", warnings);
                        itemsList.put(count, (HashMap) items);
                    }

                }
            } else {
                JOptionPane.showMessageDialog(formFrame,
                        "Pole Model jest wymagane",
                        "Nieprawidłowa warotśc!",
                        JOptionPane.ERROR_MESSAGE);
            }

        }

        /**
         * @param msg
         * @see error message
         */
        public void errorMessage(String msg) {
            JOptionPane.showMessageDialog(formFrame,
                    "Podano złą warość, prosze poprawić pole z ilością " + msg,
                    "Nieprawidłowa warotśc!",
                    JOptionPane.ERROR_MESSAGE);
        }

        /**
         *
         */
        @Override
        public void generateJaweryForm() {
            int ring = 0, ringM = 0, bracelet = 0, chain = 0,
                    signet = 0, coin = 0, earring = 0, watch = 0, clip = 0,
                    brooch = 0;

            // ring
            if (jewelleryItem[0].isSelected()) {
                if (!jewelleryField[0].getText().isEmpty()) {
                    checkElement = checkItem.checkJaweryElements(jewelleryField[0].getText());
                    if (checkElement == true) {
                        ring = Integer.parseInt(jewelleryField[0].getText());
                        addItemtoList(ring, "pierscionek", "", "", "", "", "", "",
                                "Wyroby Jubilerskie");
                    } else {
                        errorMessage("pierscionki");
                    }
                } else {
                    ring = 1;
                    addItemtoList(ring, "pierscionek", "", "", "", "", "", "",
                            "Wyroby Jubilerskie");
                }
            }
            // marriage rings
            if (jewelleryItem[1].isSelected()) {
                if (!jewelleryField[1].getText().isEmpty()) {
                    checkElement = checkItem.checkJaweryElements(jewelleryField[1].getText());
                    if (checkElement == true) {
                        ringM = Integer.parseInt(jewelleryField[1].getText());
                        addItemtoList(ringM, "obrączka", "", "", "", "", "", "",
                                "Wyroby Jubilerskie");
                    } else {
                        errorMessage("obrączki");
                    }
                } else {
                    ringM = 1;
                    addItemtoList(ringM, "obrączka", "", "", "", "", "", "",
                            "Wyroby Jubilerskie");
                }
            }
            // bracelet 
            if (jewelleryItem[2].isSelected()) {
                if (!jewelleryField[2].getText().isEmpty()) {
                    checkElement = checkItem.checkJaweryElements(jewelleryField[2].getText());
                    if (checkElement == true) {
                        bracelet = Integer.parseInt(jewelleryField[2].getText());
                        addItemtoList(bracelet, "branzoletka", "", "", "", "", "", "",
                                "Wyroby Jubilerskie");
                    } else {
                        errorMessage("branzoletki");
                    }
                } else {
                    bracelet = 1;
                    addItemtoList(bracelet, "branzoletka", "", "", "", "", "", "",
                            "Wyroby Jubilerskie");
                }
            }

            // chain
            if (jewelleryItem[3].isSelected()) {
                if (!jewelleryField[3].getText().isEmpty()) {
                    checkElement = checkItem.checkJaweryElements(jewelleryField[3].getText());
                    if (checkElement == true) {
                        chain = Integer.parseInt(jewelleryField[3].getText());
                        addItemtoList(chain, "branzoletka", "", "", "", "", "", "",
                                "Wyroby Jubilerskie");
                    } else {
                        errorMessage("łancuszki");
                    }
                } else {
                    chain = 1;
                    addItemtoList(chain, "branzoletka", "", "", "", "", "", "",
                            "Wyroby Jubilerskie");
                }
            }

            // pendant
            if (jewelleryItem[4].isSelected()) {
                if (!jewelleryField[4].getText().isEmpty()) {
                    checkElement = checkItem.checkJaweryElements(jewelleryField[4].getText());
                    if (checkElement == true) {
                        chain = Integer.parseInt(jewelleryField[4].getText());
                        addItemtoList(chain, "wiśiorek", "", "", "", "", "", "",
                                "Wyroby Jubilerskie");
                    } else {
                        errorMessage("wiśiorek");
                    }
                } else {
                    chain = 1;
                    addItemtoList(chain, "wiśiorek", "", "", "", "", "", "",
                            "Wyroby Jubilerskie");
                }
            }

            // signet
            if (jewelleryItem[5].isSelected()) {
                if (!jewelleryField[5].getText().isEmpty()) {
                    checkElement = checkItem.checkJaweryElements(jewelleryField[5].getText());
                    if (checkElement == true) {
                        signet = Integer.parseInt(jewelleryField[5].getText());
                        addItemtoList(signet, "sygnet", "", "", "", "", "", "",
                                "Wyroby Jubilerskie");
                    } else {
                        errorMessage("sygnet");
                    }
                } else {
                    signet = 1;
                }
            }

            // coin
            if (jewelleryItem[6].isSelected()) {
                if (!jewelleryField[6].getText().isEmpty()) {
                    checkElement = checkItem.checkJaweryElements(jewelleryField[6].getText());
                    if (checkElement == true) {
                        coin = Integer.parseInt(jewelleryField[6].getText());
                        addItemtoList(coin, "moneta", "", "", "", "", "", "",
                                "Wyroby Jubilerskie");
                    } else {
                        errorMessage("monet");
                    }
                } else {
                    coin = 1;
                    addItemtoList(coin, "moneta", "", "", "", "", "", "",
                            "Wyroby Jubilerskie");
                }
            }

            // earring
            if (jewelleryItem[7].isSelected()) {
                if (!jewelleryField[7].getText().isEmpty()) {
                    checkElement = checkItem.checkJaweryElements(jewelleryField[7].getText());
                    if (checkElement == true) {
                        earring = Integer.parseInt(jewelleryField[7].getText());
                        addItemtoList(earring, "moneta", "", "", "", "", "", "",
                                "Wyroby Jubilerskie");
                    } else {
                        errorMessage("kolczyki");
                    }
                } else {
                    earring = 1;
                }
            }

            // watch
            if (jewelleryItem[8].isSelected()) {
                if (!jewelleryField[8].getText().isEmpty()) {
                    checkElement = checkItem.checkJaweryElements(jewelleryField[8].getText());
                    if (checkElement == true) {
                        watch = Integer.parseInt(jewelleryField[8].getText());
                        addItemtoList(watch, "moneta", "", "", "", "", "", "",
                                "Wyroby Jubilerskie");
                    } else {
                        errorMessage("zagarków");
                    }
                } else {
                    watch = 1;
                    addItemtoList(watch, "moneta", "", "", "", "", "", "",
                            "Wyroby Jubilerskie");
                }
            }

            // clip
            if (jewelleryItem[9].isSelected()) {
                if (!jewelleryField[9].getText().isEmpty()) {
                    checkElement = checkItem.checkJaweryElements(jewelleryField[9].getText());
                    if (checkElement == true) {
                        clip = Integer.parseInt(jewelleryField[9].getText());
                        addItemtoList(clip, "spinka", "", "", "", "", "", "",
                                "Wyroby Jubilerskie");
                    } else {
                        errorMessage("spinek");
                    }
                } else {
                    clip = 1;
                    addItemtoList(clip, "spinka", "", "", "", "", "", "",
                            "Wyroby Jubilerskie");
                }
            }

            // brooch
            if (jewelleryItem[10].isSelected()) {
                if (!jewelleryField[10].getText().isEmpty()) {
                    checkElement = checkItem.checkJaweryElements(jewelleryField[10].getText());
                    if (checkElement == true) {
                        brooch = Integer.parseInt(jewelleryField[10].getText());
                        addItemtoList(brooch, "broszka", "", "", "", "", "", "",
                                "Wyroby Jubilerskie");
                    } else {
                        errorMessage("broszek");
                    }
                } else {
                    brooch = 1;
                    addItemtoList(brooch, "broszka", "", "", "", "", "", "",
                            "Wyroby Jubilerskie");
                }
            }
        }

        /**
         *
         */
        @Override
        public void generatePhoneForm() {
            //fields[6]
            if (fields[6].getText().isEmpty() && fields[7].getText().isEmpty()) {
                errorMessage("Model i Marka");
            } else {
                //fields[10]
                checkElement = checkItem.checkValue(fields[10].getText().length(),
                        fields[10].getText());
                if (checkElement == false) {
                    checkElement = checkItem.checkJaweryElements(fields[17].getText());

                    if (checkElement == true) {
                        addItemtoList(1, fields[6].getText(), fields[7].getText(),
                                fields[8].getText(), fields[9].getText(),
                                fields[10].getText(), fields[17].getText(),
                                fields[11].getText(), selectCategory.getSelectedItem().toString());
                    } else {
                        addItemtoList(1, fields[6].getText(), fields[7].getText(),
                                fields[8].getText(), fields[9].getText(),
                                fields[10].getText(), fields[17].getText(),
                                fields[11].getText(), selectCategory.getSelectedItem().toString());
                    }
                } else {
                    checkElement = checkItem.checkJaweryElements(fields[17].getText());

                    if (checkElement == true) {
                        addItemtoList(1, fields[6].getText(), fields[7].getText(),
                                fields[8].getText(), fields[9].getText(),
                                fields[10].getText(), fields[17].getText(),
                                fields[11].getText(), selectCategory.getSelectedItem().toString());
                    } else {
                        addItemtoList(1, fields[6].getText(), fields[7].getText(),
                                fields[8].getText(), fields[9].getText(),
                                fields[10].getText(), fields[17].getText(),
                                fields[11].getText(), selectCategory.getSelectedItem().toString());
                    }
                }
            }
        }

        @Override
        public void generateTabletForm() {
            //fields[10]
            checkElement = checkItem.checkValue(fields[10].getText().length(),
                    fields[10].getText());
            if (checkElement == false) {
                addItemtoList(1, fields[6].getText(), fields[7].getText(),
                        fields[8].getText(), fields[9].getText(),
                        fields[10].getText(), fields[17].getText(),
                        fields[11].getText(), selectCategory.getSelectedItem().toString());
            } else {
                addItemtoList(1, fields[6].getText(), fields[7].getText(),
                        fields[8].getText(), fields[9].getText(),
                        fields[10].getText(), fields[17].getText(),
                        fields[11].getText(), selectCategory.getSelectedItem().toString());
            }
        }

        @Override
        public void generateTVForm() {
            checkElement = checkItem.checkValue(fields[10].getText().length(),
                    fields[10].getText());
            if (checkElement == false) {
                addItemtoList(1, fields[6].getText(), fields[7].getText(),
                        fields[8].getText(), fields[9].getText(),
                        fields[10].getText(), fields[17].getText(),
                        fields[11].getText(), selectCategory.getSelectedItem().toString());
            } else {
                addItemtoList(1, fields[6].getText(), fields[7].getText(),
                        fields[8].getText(), fields[9].getText(),
                        fields[10].getText(), fields[17].getText(),
                        fields[11].getText(), selectCategory.getSelectedItem().toString());
            }
        }

        @Override
        public void generateLaptopForm() {
            checkElement = checkItem.checkValue(fields[10].getText().length(),
                    fields[10].getText());
            if (checkElement == false) {
                addItemtoList(1, fields[6].getText(), fields[7].getText(),
                        fields[8].getText(), fields[9].getText(),
                        fields[10].getText(), fields[17].getText(),
                        fields[11].getText(), selectCategory.getSelectedItem().toString());
            } else {
                addItemtoList(1, fields[6].getText(), fields[7].getText(),
                        fields[8].getText(), fields[9].getText(),
                        fields[10].getText(), fields[17].getText(),
                        fields[11].getText(), selectCategory.getSelectedItem().toString());
            }
        }

        /**
         *
         */
        @Override
        public void generatePCForm() {
            checkElement = checkItem.checkValue(fields[10].getText().length(),
                    fields[10].getText());
            if (checkElement == false) {
                addItemtoList(1, fields[6].getText(), fields[7].getText(),
                        fields[8].getText(), fields[9].getText(),
                        fields[10].getText(), fields[17].getText(),
                        fields[11].getText(), selectCategory.getSelectedItem().toString());
            } else {
                addItemtoList(1, fields[6].getText(), fields[7].getText(),
                        fields[8].getText(), fields[9].getText(),
                        fields[10].getText(), fields[17].getText(),
                        fields[11].getText(), selectCategory.getSelectedItem().toString());
            }
        }

        @Override
        public void generateMonitorForm() {
            checkElement = checkItem.checkValue(fields[10].getText().length(),
                    fields[10].getText());
            if (checkElement == false) {
                addItemtoList(1, fields[6].getText(), fields[7].getText(),
                        fields[8].getText(), fields[9].getText(),
                        fields[17].getText(), fields[10].getText(),
                        fields[11].getText(), selectCategory.getSelectedItem().toString());
            } else {
                addItemtoList(1, fields[6].getText(), fields[7].getText(),
                        fields[8].getText(), fields[9].getText(),
                        fields[17].getText(), fields[10].getText(),
                        fields[11].getText(), selectCategory.getSelectedItem().toString());
            }
        }

        public void generateDefault() {
            checkElement = checkItem.checkValue(fields[10].getText().length(),
                    fields[10].getText());
            if (checkElement == false) {
                checkElement = checkItem.checkWeight(fields[9].getText().length(),
                        fields[9].getText());
                if (checkElement == true) {
                    addItemtoList(1, fields[6].getText(), fields[7].getText(),
                            fields[8].getText(), fields[9].getText(),
                            fields[17].getText(), fields[10].getText(),
                            fields[11].getText(), selectCategory.getSelectedItem().toString());
                } else {
                    addItemtoList(1, fields[6].getText(), fields[7].getText(),
                            fields[8].getText(), "",
                            fields[17].getText(), fields[10].getText(),
                            fields[11].getText(), selectCategory.getSelectedItem().toString());
                }
            } else {
                checkElement = checkItem.checkWeight(fields[9].getText().length(),
                        fields[9].getText());
                if (checkElement == true) {
                    addItemtoList(1, fields[6].getText(), fields[7].getText(),
                            fields[8].getText(), fields[9].getText(),
                            fields[17].getText(), fields[10].getText(),
                            fields[11].getText(), selectCategory.getSelectedItem().toString());
                } else {
                    addItemtoList(1, fields[6].getText(), fields[7].getText(),
                            fields[8].getText(), "",
                            fields[17].getText(), fields[10].getText(),
                            fields[11].getText(), selectCategory.getSelectedItem().toString());
                }
            }

        }

        @Override
        public void generateGamesForm() {
            int howMany = (Integer) spinner.getValue();

            if (howMany == 1) {
                // another if :(
                checkElement = gameFields[0].getText() != null
                        && !gameFields[0].getText().isEmpty();
                if (checkElement == true) {
                    addItemtoList(1, gameFields[0].getText(), gameFields[1].getText(),
                            "", "", gameFields[2].getText(), "", gameFields[3].getText(),
                            selectCategory.getSelectedItem().toString());
                }
            } else {
                // loop :(
                for (int i = 0; i < (howMany * 4); i += 4) {
                    // another if :(
                    int z = i;
                    checkElement = gameFields[z].getText() != null
                            && !gameFields[z].getText().isEmpty();
                    if (checkElement == true) {
                        addItemtoList(1, gameFields[z].getText(), gameFields[++z].getText(),
                                "", "", gameFields[++z].getText(), "", gameFields[++z].getText(),
                                selectCategory.getSelectedItem().toString());
                    }
                }
            }

        }

        public void generateItemtoForm() {
            Map<String, String> tmpItem = new HashMap<>();

            itemPanel.removeAll();
            itemPanel.setPreferredSize(new Dimension(200, itemsList.size() * 20));
            for (int count = 0; count < itemsList.size(); count++) {
                itemValue = new JLabel();
                itemValue.setPreferredSize(new Dimension(150, heightTextL));
                tmpItem.putAll(itemsList.get(count));
                if (tmpItem.get("Wartość") == null) {
                    tmpItem.put("Wartość", "");
                }

                if (tmpItem.get("Marka") == null) {
                    tmpItem.put("Marka", "");
                }

                itemValue.setText(tmpItem.get("Model") + " " + tmpItem.get("Marka")
                        + " " + tmpItem.get("Wartość"));
                itemPanel.add(itemValue);

                deleteItem = new JButton();
                deleteItem.setPreferredSize(new Dimension(150, heightTextL));
                deleteItem.addActionListener(new deleteItemFromList(count));
                deleteItem.setText("Usuń");

                itemPanel.add(deleteItem);
            }

            formFrame.repaint();

            itemPanel.setVisible(true);
            formFrame.setVisible(true);
        }

        public class deleteItemFromList implements ActionListener {

            int indexItem;

            deleteItemFromList(int index_) {
                indexItem = index_;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                int oldSize = itemsList.size();
                itemsList.remove(indexItem);

                // change collection elements :D
                if (oldSize == (indexItem + 1)) {
                    generateItemtoForm();
                } else {
                    Map<String, String> tmpItem2;
                    for (int i = indexItem; i < oldSize; i++) {
                        tmpItem2 = itemsList.get(i + 1);
                        itemsList.put(i, (HashMap) tmpItem2);
                    }
                    itemsList.remove(oldSize - 1);
                    generateItemtoForm();
                }
            }

        }

    }

    /**
     * Main action for this form, save or canel new paymant
     */
    public class CreatePayment implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // popup witch informate about save payment to DB
            int selectedOption = JOptionPane.showConfirmDialog(formFrame,
                    "Czy na pewno wszystkie dane są poprawne ?",
                    "Potwierdzenie wygenerowania formularza!",
                    JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION) {
                // validation

                selectedDate = (Date) datePicker.getModel().getValue();

                boolean checkElement
                        = checkItem.checkName(
                                fields[0].getText().length(), fields[0].getText())
                        && checkItem.checkSurename(
                                fields[1].getText().length(), fields[1].getText())
                        && checkItem.checkPesel(
                                fields[3].getText().length(), fields[3].getText())
                        && checkItem.checkWeight(
                                fields[15].getText().length(), fields[15].getText().replaceAll(",", "."))
                        && checkItem.checkValue(
                                fields[16].getText().length(), fields[16].getText().replaceAll(",", "."))
                        && checkItem.checkPrice(
                                fields[4].getText().length(), fields[4].getText().replaceAll(",", "."))
                        && !itemsList.isEmpty() && selectedDate != null
                        && itemPanel.getComponentCount() > 0
                        && checkItem.checkCreditFinishDate(curretDate, selectedDate);

                if (checkElement == true) {
                    ok.setEnabled(false);
                    cancel.setEnabled(false);
                    adRemValue = Double.parseDouble(fields[4].getText().replaceAll(",", "."));
                    moneySafe = new SelfCalc();
                    if (checkItem.checkPesel(
                            fields[3].getText().length(),
                            fields[3].getText()) == false) {
                        fields[3].setText("");
                    }
                    if (moneySafe.chackValue(formFrame,
                            Float.parseFloat(fields[4].getText().replaceAll(",", ".")))) {
                        if (update == false) {
                            moneySafe.delFtomSelf(formFrame,
                                    Float.parseFloat(fields[4].getText().replaceAll(",", ".")));
                        }

                        // prepare user 
                        userInfo = userInfo();
                        // prepare payment
                        paymentPorperies.putAll(agreementInfo());

                        // save to DB
                        if (update == false) {
                            saveToDB();
                        } else {
                            updateDB();
                        }
                        // run loader
                        newDoc = new CreditAgreement((HashMap) itemsList,
                                (HashMap) userInfo, (HashMap) paymentPorperies);

                        newDoc.putCa(newDoc);
                        try {
                            newDoc.docRecieve();

                            String dir = System.getProperty("user.dir");
                            File dirAgre = new File(dir);

                            if (Desktop.isDesktopSupported()) {
                                Desktop.getDesktop().open(dirAgre);
                            }
                            // close form
                            iClose = 1;
                            formFrame.dispose();

                        } catch (Exception ex) {
                            LombardiaLogger startLogging = new LombardiaLogger();
                            String text = startLogging.preparePattern("Error", ex.getMessage()
                                    + "\n" + Arrays.toString(ex.getStackTrace()));
                            startLogging.logToFile(text);
                        }
                    }

                    // open catolog where i save agreement
                }

            }

        }

        /**
         * @see create user in arraylist
         */
        private HashMap userInfo() {
            Map<String, String> tmpUser = new HashMap<>();
            tmpUser.put("Imie", fields[0].getText());
            tmpUser.put("Nazwisko", fields[1].getText());
            tmpUser.put("Adres", addresCustomer.getText());
            tmpUser.put("Pesel", fields[3].getText());
            tmpUser.put("Zaufany klient", Boolean.toString(goodClient.isSelected()));
            return (HashMap) tmpUser;
        }

        /**
         * @see create Credit Agreement information
         *
         */
        private HashMap agreementInfo() {
            Map<String, String> tmpAgreement = new HashMap<>();
            tmpAgreement.put("NR Umowy", createIndex());
            if (paymentPorperies.get("Data rozpoczecia") == null) {
                tmpAgreement.put("Data rozpoczecia", ft.format(curretDate));
            } else {
                tmpAgreement.put("Data rozpoczecia",
                        paymentPorperies.get("Data rozpoczecia"));
            }
            tmpAgreement.put("Data zwrotu", ft.format(selectedDate));
            tmpAgreement.put("Kwota", fields[4].getText());
            tmpAgreement.put("Rabat (zł)", fields[18].getText());
            tmpAgreement.put("Opłata magayznowania", fields[14].getText());
            tmpAgreement.put("Opłata manipulacyjna", fields[19].getText());
            tmpAgreement.put("Odsetki Terminowe", fields[20].getText());
            tmpAgreement.put("Łączne opłaty", fields[21].getText());
            tmpAgreement.put("Razem do zapłaty", fields[22].getText());
            tmpAgreement.put("RRO", fields[13].getText());
            tmpAgreement.put("RRSO", Float.toString(rrso));
            tmpAgreement.put("Łączna waga", fields[15].getText());
            tmpAgreement.put("Łączna wartosc", fields[16].getText().replaceAll(",", "."));
            tmpAgreement.put("Dzienny Profit", Double.toString(
                    precentCalc.dailyEarn(Float.parseFloat(fields[4].getText()))));
            return (HashMap) tmpAgreement;
        }

        private void saveToDB() {
            int customer = 0;
                // first save customer
            // check is customer exist :D
            getQuery = new MainDBQuierues();

            if (getQuery.checkUser(fields[0].getText(), fields[1].getText())) {
                // save customer to db
                int goodCustomer = (goodClient.isSelected()) ? 1 : 0;

                getQuery.saveUser(fields[0].getText(), fields[1].getText(), 
                        addresCustomer.getText(), goodCustomer, 
                        fields[3].getText(), fields[18].getText());

            }
                // next i save items
            // Agreements
            // get user id 
            customer = getQuery.getUserID(fields[0].getText(), fields[1].getText());

            //weight 
            if (fields[15].getText().isEmpty()) {
                fields[15].setText("0");
            }

            getQuery.saveAgreements(createIndex(), curretDate, selectedDate, fields[4].getText(),
                    fields[19].getText(), fields[16].getText(), fields[15].getText(),
                    fields[22].getText(), fields[14].getText(), customer);

                // i must know id last agreement
            // finnaly i save items in loop :(
            Map<String, String> tmpItem = new HashMap<>();
            // analyze cat id :D
            int catID = 0;
            howMany = fillPayID();
            for (int i = 0; i < itemsList.size(); i++) {
                tmpItem.putAll(itemsList.get(i));
                catID = getQuery.getCatID(tmpItem.get("Kategoria"));

                creatItemtoDB.setValues(tmpItem.get("Model"), tmpItem.get("Marka"),
                        tmpItem.get("Typ"), tmpItem.get("Waga"),
                        tmpItem.get("IMEI"), tmpItem.get("Wartość"),
                        tmpItem.get("Uwagi"), catID, howMany);
                getQuery.insertItem(creatItemtoDB.getInsertItem());
            }
        }

        private void updateDB() {
            try {
                int customer = 0;
                setQuerry = new QueryDB();
                conDB = setQuerry.getConnDB();
                stmt = conDB.createStatement();

                queryResult = setQuerry.dbSetQuery("SELECT ID FROM Customers WHERE"
                        + " NAME LIKE '"
                        + fields[0].getText() + "' AND SURNAME LIKE '"
                        + fields[1].getText() + "';");

                while (queryResult.next()) {
                    customer = queryResult.getInt("ID");
                }

                // update tables
                // agreements
                //weight 
                if (fields[15].getText().isEmpty()) {
                    fields[15].setText("0");
                }

                String sDate = paymentPorperies.get("Data rozpoczecia");
                queryResult = setQuerry.dbSetQuery("UPDATE Agreements SET STOP_DATE ='"
                        + ft.format(selectedDate) + "', VALUE = '"
                        + fields[4].getText().replaceAll(",", ".") + "', COMMISSION = "
                        + fields[19].getText().replaceAll(",", ".") + ", ITEM_VALUE = "
                        + fields[16].getText().replaceAll(",", ".") + ", ITEM_WEIGHT = "
                        + fields[15].getText().replaceAll(",", ".") + ", VALUE_REST = "
                        + fields[4].getText().replaceAll(",", ".") + ", SAVEPRICE = "
                        + ", ID_CUSTOMER = "
                        + customer + " WHERE ID_AGREEMENTS = '"
                        + paymentPorperies.get("NR Umowy") + "';");

                //items 
                setQuerry.closeDB();
            } catch (SQLException ex) {
                LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", ex.getMessage()
                        + "\n" + Arrays.toString(ex.getStackTrace()));
                startLogging.logToFile(text);
            }
        }
    }

    /**
     * @see close form
     */
    public class CloseForm implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            formFrame.dispose();
        }

    }

    public class JaweryForm implements ActionListener, ItemFormGenerator {

        String text = null;

        @Override
        public void actionPerformed(ActionEvent e) {
            text = selectCategory.getSelectedItem().toString();
            newItemPanel.removeAll();
            newItemPanel.repaint();

            switch (text.toLowerCase()) {
                case "wyroby jubilerskie":
                    generateJaweryForm();
                    break;
                case "laptop":
                    generateLaptopForm();
                    break;
                case "komputer":
                    generatePCForm();
                    break;
                case "monitor":
                    generateMonitorForm();
                    break;
                case "telewizor":
                    generateTVForm();
                    break;
                case "telefon":
                    generatePhoneForm();
                    break;
                case "tablet":
                    generateTabletForm();
                    break;
                case "gry":
                    generateGamesForm();
                    break;
                default:
                    generateDefaultItemForm();
                    break;
            }

            newItemPanel.setVisible(true);
            formFrame.setVisible(true);
        }

        @Override
        public void generateJaweryForm() {
            newItemGrid.insets = new Insets(0, 0, 0, 0);

            jewelleryItem = new JCheckBox[11];
            jewelleryField = new JTextField[11];

            jewelleryItem[0] = new JCheckBox();
            jewelleryItem[0].setText("Pierścionek");
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 0;
            newItemPanel.add(jewelleryItem[0], newItemGrid);

            jewelleryField[0] = new JTextField();
            jewelleryField[0].setPreferredSize(new Dimension(50, heightTextL));
            jewelleryField[0].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 0;
            newItemPanel.add(jewelleryField[0], newItemGrid);

            jewelleryItem[1] = new JCheckBox();
            jewelleryItem[1].setText("Obrączka");
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 1;
            newItemPanel.add(jewelleryItem[1], newItemGrid);

            jewelleryField[1] = new JTextField();
            jewelleryField[1].setPreferredSize(new Dimension(50, heightTextL));
            jewelleryField[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 1;
            newItemPanel.add(jewelleryField[1], newItemGrid);

            jewelleryItem[2] = new JCheckBox();
            jewelleryItem[2].setText("Bransoletka");
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 2;
            newItemPanel.add(jewelleryItem[2], newItemGrid);

            jewelleryField[2] = new JTextField();
            jewelleryField[2].setPreferredSize(new Dimension(50, heightTextL));
            jewelleryField[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 2;
            newItemPanel.add(jewelleryField[2], newItemGrid);

            jewelleryItem[3] = new JCheckBox();
            jewelleryItem[3].setText("Łancuszek");
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 3;
            newItemPanel.add(jewelleryItem[3], newItemGrid);

            jewelleryField[3] = new JTextField();
            jewelleryField[3].setPreferredSize(new Dimension(50, heightTextL));
            jewelleryField[3].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 3;
            newItemPanel.add(jewelleryField[3], newItemGrid);

            jewelleryItem[4] = new JCheckBox();
            jewelleryItem[4].setText("Wisiorek");
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 4;
            newItemPanel.add(jewelleryItem[4], newItemGrid);

            jewelleryField[4] = new JTextField();
            jewelleryField[4].setPreferredSize(new Dimension(50, heightTextL));
            jewelleryField[4].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 4;
            newItemPanel.add(jewelleryField[4], newItemGrid);

            jewelleryItem[5] = new JCheckBox();
            jewelleryItem[5].setText("Sygnet");
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 5;
            newItemPanel.add(jewelleryItem[5], newItemGrid);

            jewelleryField[5] = new JTextField();
            jewelleryField[5].setPreferredSize(new Dimension(50, heightTextL));
            jewelleryField[5].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 5;
            newItemPanel.add(jewelleryField[5], newItemGrid);

            jewelleryItem[6] = new JCheckBox();
            jewelleryItem[6].setText("Moneta");
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 6;
            newItemPanel.add(jewelleryItem[6], newItemGrid);

            jewelleryField[6] = new JTextField();
            jewelleryField[6].setPreferredSize(new Dimension(50, heightTextL));
            jewelleryField[6].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 6;
            newItemPanel.add(jewelleryField[6], newItemGrid);

            jewelleryItem[7] = new JCheckBox();
            jewelleryItem[7].setText("Kolczyk");
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 7;
            newItemPanel.add(jewelleryItem[7], newItemGrid);

            jewelleryField[7] = new JTextField();
            jewelleryField[7].setPreferredSize(new Dimension(50, heightTextL));
            jewelleryField[7].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 7;
            newItemPanel.add(jewelleryField[7], newItemGrid);

            jewelleryItem[8] = new JCheckBox();
            jewelleryItem[8].setText("Zegarek");
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 8;
            newItemPanel.add(jewelleryItem[8], newItemGrid);

            jewelleryField[8] = new JTextField();
            jewelleryField[8].setPreferredSize(new Dimension(50, heightTextL));
            jewelleryField[8].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 8;
            newItemPanel.add(jewelleryField[8], newItemGrid);

            jewelleryItem[9] = new JCheckBox();
            jewelleryItem[9].setText("Spinka");
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 9;
            newItemPanel.add(jewelleryItem[9], newItemGrid);

            jewelleryField[9] = new JTextField();
            jewelleryField[9].setPreferredSize(new Dimension(50, heightTextL));
            jewelleryField[9].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 9;
            newItemPanel.add(jewelleryField[9], newItemGrid);

            jewelleryItem[10] = new JCheckBox();
            jewelleryItem[10].setText("Broszka");
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 10;
            newItemPanel.add(jewelleryItem[10], newItemGrid);

            jewelleryField[10] = new JTextField();
            jewelleryField[10].setPreferredSize(new Dimension(50, heightTextL));
            jewelleryField[10].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 10;
            newItemPanel.add(jewelleryField[10], newItemGrid);
        }

        @Override
        public void generatePhoneForm() {
            newItemGrid.insets = new Insets(5, 5, 5, 5);
            namedField[8] = new JLabel();
            namedField[8].setText("Model:");
            namedField[8].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 0;
            newItemPanel.add(namedField[8], newItemGrid);

            fields[6] = new JTextField();
            fields[6].setPreferredSize(new Dimension(150, heightTextL));
            fields[6].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 0;
            newItemPanel.add(fields[6], newItemGrid);

            namedField[9] = new JLabel();
            namedField[9].setText("Marka:");
            namedField[9].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 1;
            newItemPanel.add(namedField[9], newItemGrid);

            fields[7] = new JTextField();
            fields[7].setPreferredSize(new Dimension(150, heightTextL));
            fields[7].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 1;
            newItemPanel.add(fields[7], newItemGrid);

            namedField[10] = new JLabel();
            namedField[10].setText("Typ:");
            namedField[10].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 2;
            newItemPanel.add(namedField[10], newItemGrid);

            fields[8] = new JTextField();
            fields[8].setPreferredSize(new Dimension(150, heightTextL));
            fields[8].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 2;
            newItemPanel.add(fields[8], newItemGrid);

            namedField[12] = new JLabel();
            namedField[12].setText("Wartość:");
            namedField[12].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 3;
            newItemPanel.add(namedField[12], newItemGrid);

            fields[10] = new JTextField();
            fields[10].setPreferredSize(new Dimension(150, heightTextL));
            fields[10].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 3;
            newItemPanel.add(fields[10], newItemGrid);

            namedField[20] = new JLabel();
            namedField[20].setText("IMEI:");
            namedField[20].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 4;
            newItemPanel.add(namedField[20], newItemGrid);

            fields[17] = new JTextField();
            fields[17].setPreferredSize(new Dimension(150, heightTextL));
            fields[17].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 4;
            newItemPanel.add(fields[17], newItemGrid);

            namedField[13] = new JLabel();
            namedField[13].setText("Uwagi:");
            namedField[13].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 5;
            newItemPanel.add(namedField[13], newItemGrid);

            fields[11] = new JTextField();
            fields[11].setPreferredSize(new Dimension(150, heightTextL));
            fields[11].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 5;
            newItemPanel.add(fields[11], newItemGrid);
        }

        @Override
        public void generateTabletForm() {
            generateLaptopForm();
        }

        @Override
        public void generateTVForm() {
            generateLaptopForm();
        }

        @Override
        public void generateLaptopForm() {
            newItemGrid.insets = new Insets(5, 5, 5, 5);
            namedField[8] = new JLabel();
            namedField[8].setText("Model:");
            namedField[8].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 0;
            newItemPanel.add(namedField[8], newItemGrid);

            fields[6] = new JTextField();
            fields[6].setPreferredSize(new Dimension(150, heightTextL));
            fields[6].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 0;
            newItemPanel.add(fields[6], newItemGrid);

            namedField[9] = new JLabel();
            namedField[9].setText("Marka:");
            namedField[9].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 1;
            newItemPanel.add(namedField[9], newItemGrid);

            fields[7] = new JTextField();
            fields[7].setPreferredSize(new Dimension(150, heightTextL));
            fields[7].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 1;
            newItemPanel.add(fields[7], newItemGrid);

            namedField[10] = new JLabel();
            namedField[10].setText("Typ:");
            namedField[10].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 2;
            newItemPanel.add(namedField[10], newItemGrid);

            fields[8] = new JTextField();
            fields[8].setPreferredSize(new Dimension(150, heightTextL));
            fields[8].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 2;
            newItemPanel.add(fields[8], newItemGrid);

            namedField[12] = new JLabel();
            namedField[12].setText("Wartość:");
            namedField[12].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 3;
            newItemPanel.add(namedField[12], newItemGrid);

            fields[10] = new JTextField();
            fields[10].setPreferredSize(new Dimension(150, heightTextL));
            fields[10].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 3;
            newItemPanel.add(fields[10], newItemGrid);

            namedField[13] = new JLabel();
            namedField[13].setText("Uwagi:");
            namedField[13].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 4;
            newItemPanel.add(namedField[13], newItemGrid);

            fields[11] = new JTextField();
            fields[11].setPreferredSize(new Dimension(150, heightTextL));
            fields[11].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 4;
            newItemPanel.add(fields[11], newItemGrid);
        }

        @Override
        public void generatePCForm() {
            generateLaptopForm();
        }

        @Override
        public void generateMonitorForm() {
            generateLaptopForm();
        }

        public void generateDefaultItemForm() {
            newItemGrid.insets = new Insets(5, 5, 5, 5);
            namedField[8] = new JLabel();
            namedField[8].setText("Model:");
            namedField[8].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 0;
            newItemPanel.add(namedField[8], newItemGrid);

            fields[6] = new JTextField();
            fields[6].setPreferredSize(new Dimension(150, heightTextL));
            fields[6].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 0;
            newItemPanel.add(fields[6], newItemGrid);

            namedField[9] = new JLabel();
            namedField[9].setText("Marka:");
            namedField[9].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 1;
            newItemPanel.add(namedField[9], newItemGrid);

            fields[7] = new JTextField();
            fields[7].setPreferredSize(new Dimension(150, heightTextL));
            fields[7].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 1;
            newItemPanel.add(fields[7], newItemGrid);

            namedField[10] = new JLabel();
            namedField[10].setText("Typ:");
            namedField[10].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 2;
            newItemPanel.add(namedField[10], newItemGrid);

            fields[8] = new JTextField();
            fields[8].setPreferredSize(new Dimension(150, heightTextL));
            fields[8].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 2;
            newItemPanel.add(fields[8], newItemGrid);

            namedField[11] = new JLabel();
            namedField[11].setText("Waga (gramy):");
            namedField[11].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 3;
            newItemPanel.add(namedField[11], newItemGrid);

            fields[9] = new JTextField();
            fields[9].setPreferredSize(new Dimension(150, heightTextL));
            fields[9].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 3;
            newItemPanel.add(fields[9], newItemGrid);

            namedField[12] = new JLabel();
            namedField[12].setText("Wartość:");
            namedField[12].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 4;
            newItemPanel.add(namedField[12], newItemGrid);

            fields[10] = new JTextField();
            fields[10].setPreferredSize(new Dimension(150, heightTextL));
            fields[10].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 4;
            newItemPanel.add(fields[10], newItemGrid);

            namedField[20] = new JLabel();
            namedField[20].setText("IMEI:");
            namedField[20].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 5;
            newItemPanel.add(namedField[20], newItemGrid);

            fields[17] = new JTextField();
            fields[17].setPreferredSize(new Dimension(150, heightTextL));
            fields[17].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 5;
            newItemPanel.add(fields[17], newItemGrid);

            namedField[13] = new JLabel();
            namedField[13].setText("Uwagi:");
            namedField[13].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 6;
            newItemPanel.add(namedField[13], newItemGrid);

            fields[11] = new JTextField();
            fields[11].setPreferredSize(new Dimension(150, heightTextL));
            fields[11].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 6;
            newItemPanel.add(fields[11], newItemGrid);
        }

        @Override
        public void generateGamesForm() {
            gameFields = new JTextField[4];
            newItemGrid.insets = new Insets(5, 5, 5, 5);

            namedField[8] = new JLabel();
            namedField[8].setText("Ilość gier:");
            namedField[8].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 0;
            newItemPanel.add(namedField[8], newItemGrid);

            modelSpinner = new SpinnerNumberModel(1, 1, 20, 1);
            spinner = new JSpinner(modelSpinner);
            spinner.setEditor(new JSpinner.DefaultEditor(spinner));
            spinner.addChangeListener(new createGamesForm());
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 0;
            newItemPanel.add(spinner, newItemGrid);

            // now i must generate loop :)
            namedField[9] = new JLabel();
            namedField[9].setText("Nazwa gry:");
            namedField[9].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 1;
            newItemPanel.add(namedField[9], newItemGrid);

            gameFields[0] = new JTextField();
            gameFields[0].setPreferredSize(new Dimension(150, heightTextL));
            gameFields[0].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 1;
            newItemPanel.add(gameFields[0], newItemGrid);

            namedField[10] = new JLabel();
            namedField[10].setText("Platforma:");
            namedField[10].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 2;
            newItemPanel.add(namedField[10], newItemGrid);

            gameFields[1] = new JTextField();
            gameFields[1].setPreferredSize(new Dimension(150, heightTextL));
            gameFields[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 2;
            newItemPanel.add(gameFields[1], newItemGrid);

            namedField[12] = new JLabel();
            namedField[12].setText("Wartość:");
            namedField[12].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 4;
            newItemPanel.add(namedField[12], newItemGrid);

            gameFields[2] = new JTextField();
            gameFields[2].setPreferredSize(new Dimension(150, heightTextL));
            gameFields[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 4;
            newItemPanel.add(gameFields[2], newItemGrid);

            namedField[13] = new JLabel();
            namedField[13].setText("Uwagi:");
            namedField[13].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 0;
            newItemGrid.gridy = 6;
            newItemPanel.add(namedField[13], newItemGrid);

            gameFields[3] = new JTextField();
            gameFields[3].setPreferredSize(new Dimension(150, heightTextL));
            gameFields[3].setFont(new Font("Dialog", Font.BOLD, fontSize));
            newItemGrid.gridx = 1;
            newItemGrid.gridy = 6;
            newItemPanel.add(gameFields[3], newItemGrid);
        }

        private class createGamesForm implements ChangeListener {

            @Override
            public void stateChanged(ChangeEvent e) {
                newItemPanel.removeAll();
                newItemPanel.repaint();
                gameFields = new JTextField[4 * Integer.parseInt(spinner.getValue().toString())];
                newItemGrid.insets = new Insets(5, 5, 5, 5);

                namedField[8] = new JLabel();
                namedField[8].setText("Ilość gier:");
                namedField[8].setFont(new Font("Dialog", Font.BOLD, fontSize));
                newItemGrid.gridx = 0;
                newItemGrid.gridy = 0;
                newItemPanel.add(namedField[8], newItemGrid);

                newItemGrid.gridx = 1;
                newItemGrid.gridy = 0;
                newItemPanel.add(spinner, newItemGrid);
                int value = 0, indexGame = 0;
                for (int i = 0; i < Integer.parseInt(spinner.getValue().toString()); i++) {

                    namedField[9] = new JLabel();
                    namedField[9].setText("Nazwa gry:");
                    namedField[9].setFont(new Font("Dialog", Font.BOLD, fontSize));
                    newItemGrid.gridx = 0;
                    newItemGrid.gridy = ++value;
                    newItemPanel.add(namedField[9], newItemGrid);

                    gameFields[indexGame] = new JTextField();
                    gameFields[indexGame].setPreferredSize(new Dimension(150, heightTextL));
                    gameFields[indexGame].setFont(new Font("Dialog", Font.BOLD, fontSize));
                    newItemGrid.gridx = 1;
                    newItemGrid.gridy = value;
                    newItemPanel.add(gameFields[indexGame], newItemGrid);

                    namedField[10] = new JLabel();
                    namedField[10].setText("Platforma:");
                    namedField[10].setFont(new Font("Dialog", Font.BOLD, fontSize));
                    newItemGrid.gridx = 0;
                    newItemGrid.gridy = ++value;
                    newItemPanel.add(namedField[10], newItemGrid);
                    indexGame++;
                    gameFields[indexGame] = new JTextField();
                    gameFields[indexGame].setPreferredSize(new Dimension(150, heightTextL));
                    gameFields[indexGame].setFont(new Font("Dialog", Font.BOLD, fontSize));
                    newItemGrid.gridx = 1;
                    newItemGrid.gridy = value;
                    newItemPanel.add(gameFields[indexGame], newItemGrid);

                    namedField[12] = new JLabel();
                    namedField[12].setText("Wartość:");
                    namedField[12].setFont(new Font("Dialog", Font.BOLD, fontSize));
                    newItemGrid.gridx = 0;
                    newItemGrid.gridy = ++value;
                    newItemPanel.add(namedField[12], newItemGrid);
                    indexGame++;
                    gameFields[indexGame] = new JTextField();
                    gameFields[indexGame].setPreferredSize(new Dimension(150, heightTextL));
                    gameFields[indexGame].setFont(new Font("Dialog", Font.BOLD, fontSize));
                    newItemGrid.gridx = 1;
                    newItemGrid.gridy = value;
                    newItemPanel.add(gameFields[indexGame], newItemGrid);

                    namedField[13] = new JLabel();
                    namedField[13].setText("Uwagi:");
                    namedField[13].setFont(new Font("Dialog", Font.BOLD, fontSize));
                    newItemGrid.gridx = 0;
                    newItemGrid.gridy = ++value;
                    newItemPanel.add(namedField[13], newItemGrid);
                    indexGame++;
                    gameFields[indexGame] = new JTextField();
                    gameFields[indexGame].setPreferredSize(new Dimension(150, heightTextL));
                    gameFields[indexGame].setFont(new Font("Dialog", Font.BOLD, fontSize));
                    newItemGrid.gridx = 1;
                    newItemGrid.gridy = value;
                    newItemPanel.add(gameFields[indexGame], newItemGrid);
                    indexGame++;
                    JSeparator gameFormSeparator = new JSeparator();
                    newItemGrid.gridwidth = 2;
                    newItemGrid.gridx = 0;
                    newItemGrid.gridy = ++value;
                    newItemPanel.add(gameFormSeparator, newItemGrid);
                    newItemGrid.gridwidth = 1;
                }
                newItemPanel.setVisible(true);
                formFrame.setVisible(true);

            }

        }
    }

    public boolean isClose() {
        return iClose == 1;

    }

    /**
     * Get elements from category. This method move to DB interface module
     */
    private String[] getCategories() {
        String[] categories = null;
        List<String> list = new ArrayList<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();
            queryResult = setQuerry.dbSetQuery("SELECT NAME FROM Category");
            //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist

            while (queryResult.next()) {
                list.add(queryResult.getString("NAME"));
            }

            setQuerry.closeDB();
            //addToDictionary("bye");//adds a single word
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
        categories = list.toArray(new String[list.size()]);
        return categories;
    }

    //never ending story in this form... 
    public class calculatePaymentsOfForm implements DocumentListener, ActionListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            calculateParam();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            calculateParam();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            calculateParam();
        }

        public void calculateParam() {
            precentCalc = new ValueCalc();
            selectedDate = (Date) datePicker.getModel().getValue();
            curretDate = new Date();

            boolean checkElement
                    = checkItem.checkValue(
                            fields[4].getText().length(), fields[4].getText(), 1)
                    && selectedDate != null;
            if (checkElement == true) {
                float val = Float.parseFloat(fields[4].getText().replaceAll(",", "."));
                fields[19].setText(Float.toString(precentCalc.handlingPayment(val)));
                if (paymentPorperies.get("Data rozpoczecia") == null) {
                    fields[21].setText(
                            Float.toString(precentCalc.dailyEarn(ft.format(selectedDate),
                                            ft.format(curretDate), val)));
                } else {
                    fields[21].setText(
                            Float.toString(precentCalc.dailyEarn(ft.format(selectedDate),
                                            paymentPorperies.get("Data rozpoczecia"), val)));
                }
                if (fields[18].getText().isEmpty()
                        || fields[18].getText() == null) {
                    discount = 0;
                } else {
                    discount = Float.parseFloat(fields[18].getText());
                }
                fields[14].setText(Float.toString(precentCalc.storagePayment(val)));
                fields[13].setText(Float.toString(precentCalc.RROCalc()));
                fields[20].setText(Float.toString(precentCalc.lombardRate(val)));
                discount = precentCalc.discountCalc(discount, formFrame);
                fields[22].setText(Float.toString(precentCalc.allPayment(val)));
                rrso = precentCalc.RRSOCalc(val);
            } else {
                fields[19].setText("");
                fields[21].setText("");
                fields[14].setText("");
                fields[13].setText("");
                fields[20].setText("");
                fields[22].setText("");
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            calculateParam();
        }

    }

    /**
     * @see another action. This time i fill customer fields.
     */
    public class fillCustomer implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            changedUpdate(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            // do nothing
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            // fill :D
            try {
                setQuerry = new QueryDB();
                conDB = setQuerry.getConnDB();
                stmt = conDB.createStatement();
                String[] nameSurnam = fields[2].getText().split(" ");
                //quer to db
                if (nameSurnam.length > 1) {
                    queryResult = setQuerry.dbSetQuery("SELECT NAME, SURNAME, ADDRESS,"
                            + "PESEL, TRUST, DISCOUNT FROM Customers WHERE NAME LIKE '"
                            + nameSurnam[0] + "' AND SURNAME LIKE '"
                            + nameSurnam[1] + "';");

                    while (queryResult.next()) {
                        fields[0].setText(queryResult.getString("NAME"));
                        fields[1].setText(queryResult.getString("SURNAME"));
                        addresCustomer.setText(queryResult.getString("ADDRESS"));
                        fields[3].setText(queryResult.getString("PESEL"));
                        boolean trust = queryResult.getInt("TRUST") != 0;
                        goodClient.setSelected(trust);
                        fields[18].setText(queryResult.getString("DISCOUNT"));
                    }
                    setQuerry.closeDB();
                    //fillPayID();
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
