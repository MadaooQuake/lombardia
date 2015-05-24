/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.forms;

import lombardia2014.dataBaseInterface.QueryDB;
import lombardia2014.generators.AutoSuggestor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import lombardia2014.Interface.menu.ListUsers;
import lombardia2014.SelfCalc;
import lombardia2014.generators.ItemChecker;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class BuyItem extends Forms implements ItemFormGenerator {

    GridBagConstraints newItemGrid = new GridBagConstraints();
    FormValidator checkItem = new FormValidator();
    ItemChecker creatItemtoDB = new ItemChecker();
    JButton cancel = null;
    JButton ok = null;
    JLabel[] namedField = null;
    JTextField[] fields = null;
    JCheckBox[] jewelleryItem = null;
    JTextField[] jewelleryField = null;
    AutoSuggestor selectCategory = null;
    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    JPanel newItemPanel = null;
    double adRemValue = 0.0;
    FormValidator validator = new FormValidator();

    int iClose = 0;
    int fontSize = 16;
    int heightTextL = 28;
    String text = null;
    String weight = "0";

    SelfCalc moneySafe = null;

    Map<Integer, HashMap> itemsList = new HashMap<>();

    @Override
    public void generateGui() {
        formFrame.setSize(400, 480);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setUndecorated(true);
        formFrame.setTitle("Skup przedmiot");
        mainPanel = new JPanel(new GridBagLayout());
        titleBorder = BorderFactory.createTitledBorder(blackline, "Skup przedmiot");
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        mainPanel.setBorder(titleBorder);

        generatePanels(c);

        formFrame.add(mainPanel);
        formFrame.setVisible(true);

    }

    // 1 panel this time :D
    @Override
    public void generatePanels(GridBagConstraints c) {
        //select category 
        namedField = new JLabel[18];
        fields = new JTextField[18];
        jewelleryItem = new JCheckBox[11];

        // combobox with list of all items
        namedField[0] = new JLabel();
        namedField[0].setText("Kategoria:");
        namedField[0].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2, 2, 2, 2);
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(namedField[0], c);

        // First i create jfield panel 
        fields[0] = new JTextField();
        fields[0].setPreferredSize(new Dimension(150, heightTextL));
        fields[0].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[0].getDocument().addDocumentListener(new RestItemForm());
        // Create selector;
        selectCategory = new AutoSuggestor(
                fields[0], formFrame, null, Color.WHITE.brighter(),
                Color.BLUE, Color.RED, 0.75f, 2, 28) {
                    @Override
                    public boolean wordTyped(String typedWord) {
                        //select all from category table
                        try {
                            setQuerry = new QueryDB();
                            conDB = setQuerry.getConnDB();
                            stmt = conDB.createStatement();

                            queryResult = setQuerry.dbSetQuery("SELECT NAME FROM Category");
                            //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
                            List<String> words = new ArrayList<>();

                            while (queryResult.next()) {
                                words.add(queryResult.getString("NAME"));
                            }

                            setQuerry.closeDB();
                            setDictionary((ArrayList<String>) words);
                            //addToDictionary("bye");//adds a single word
                        } catch (SQLException ex) {
                            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
                    }
                };
        c.gridx = 1;
        c.gridy = 1;
        mainPanel.add(fields[0], c);

        newItemPanel = new JPanel(new GridBagLayout());
        newItemPanel.setPreferredSize(new Dimension(330, 320));
        newItemPanel.setMinimumSize(new Dimension(330, 320));
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        mainPanel.add(newItemPanel, c);
        c.gridwidth = 1;

        namedField[9] = new JLabel();
        namedField[9].setText("Kupiono za:");
        namedField[9].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.gridx = 0;
        c.gridy = 3;
        mainPanel.add(namedField[9], c);

        fields[9] = new JTextField();
        fields[9].setPreferredSize(new Dimension(150, heightTextL));
        fields[9].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.gridx = 1;
        c.gridy = 3;
        mainPanel.add(fields[9], c);

        c.insets = new Insets(10, 15, 5, 10);
        ok = new JButton();
        ok.setText("Generuj");
        ok.setPreferredSize(new Dimension(150, heightTextL));
        ok.setFont(new Font("Dialog", Font.BOLD, 18));
        ok.addActionListener(new SsveItemForm());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        mainPanel.add(ok, c);

        cancel = new JButton();
        cancel.setText("Anuluj");
        cancel.setPreferredSize(new Dimension(150, heightTextL));
        cancel.setFont(new Font("Dialog", Font.BOLD, 18));
        cancel.addActionListener(new CloseForm());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        mainPanel.add(cancel, c);

    }

    // category 
    // elements of form :D
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
        namedField[1] = new JLabel();
        namedField[1].setText("Model:");
        namedField[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 0;
        newItemPanel.add(namedField[1], newItemGrid);

        fields[1] = new JTextField();
        fields[1].setPreferredSize(new Dimension(150, heightTextL));
        fields[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 0;
        newItemPanel.add(fields[1], newItemGrid);

        namedField[2] = new JLabel();
        namedField[2].setText("Marka:");
        namedField[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 1;
        newItemPanel.add(namedField[2], newItemGrid);

        fields[2] = new JTextField();
        fields[2].setPreferredSize(new Dimension(150, heightTextL));
        fields[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 1;
        newItemPanel.add(fields[2], newItemGrid);

        namedField[3] = new JLabel();
        namedField[3].setText("Typ:");
        namedField[3].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 2;
        newItemPanel.add(namedField[3], newItemGrid);

        fields[3] = new JTextField();
        fields[3].setPreferredSize(new Dimension(150, heightTextL));
        fields[3].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 2;
        newItemPanel.add(fields[3], newItemGrid);

        namedField[5] = new JLabel();
        namedField[5].setText("Wartość:");
        namedField[5].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 4;
        newItemPanel.add(namedField[5], newItemGrid);

        fields[5] = new JTextField();
        fields[5].setPreferredSize(new Dimension(150, heightTextL));
        fields[5].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 4;
        newItemPanel.add(fields[5], newItemGrid);

        namedField[6] = new JLabel();
        namedField[6].setText("IMEI:");
        namedField[6].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 5;
        newItemPanel.add(namedField[6], newItemGrid);

        fields[6] = new JTextField();
        fields[6].setPreferredSize(new Dimension(150, heightTextL));
        fields[6].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 5;
        newItemPanel.add(fields[6], newItemGrid);

        namedField[7] = new JLabel();
        namedField[7].setText("Uwagi:");
        namedField[7].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 7;
        newItemPanel.add(namedField[7], newItemGrid);

        fields[7] = new JTextField();
        fields[7].setPreferredSize(new Dimension(150, heightTextL));
        fields[7].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 7;
        newItemPanel.add(fields[7], newItemGrid);
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
        namedField[1] = new JLabel();
        namedField[1].setText("Model:");
        namedField[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 0;
        newItemPanel.add(namedField[1], newItemGrid);

        fields[1] = new JTextField();
        fields[1].setPreferredSize(new Dimension(150, heightTextL));
        fields[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 0;
        newItemPanel.add(fields[1], newItemGrid);

        namedField[2] = new JLabel();
        namedField[2].setText("Marka:");
        namedField[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 1;
        newItemPanel.add(namedField[2], newItemGrid);

        fields[2] = new JTextField();
        fields[2].setPreferredSize(new Dimension(150, heightTextL));
        fields[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 1;
        newItemPanel.add(fields[2], newItemGrid);

        namedField[3] = new JLabel();
        namedField[3].setText("Typ:");
        namedField[3].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 2;
        newItemPanel.add(namedField[3], newItemGrid);

        fields[3] = new JTextField();
        fields[3].setPreferredSize(new Dimension(150, heightTextL));
        fields[3].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 2;
        newItemPanel.add(fields[3], newItemGrid);

        namedField[4] = new JLabel();
        namedField[4].setText("Wartość:");
        namedField[4].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 3;
        newItemPanel.add(namedField[4], newItemGrid);

        fields[4] = new JTextField();
        fields[4].setPreferredSize(new Dimension(150, heightTextL));
        fields[4].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 3;
        newItemPanel.add(fields[4], newItemGrid);

        namedField[5] = new JLabel();
        namedField[5].setText("Uwagi:");
        namedField[5].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 4;
        newItemPanel.add(namedField[5], newItemGrid);

        fields[5] = new JTextField();
        fields[5].setPreferredSize(new Dimension(150, heightTextL));
        fields[5].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 4;
        newItemPanel.add(fields[5], newItemGrid);
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
        namedField[1] = new JLabel();
        namedField[1].setText("Model:");
        namedField[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 0;
        newItemPanel.add(namedField[1], newItemGrid);

        fields[1] = new JTextField();
        fields[1].setPreferredSize(new Dimension(150, heightTextL));
        fields[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 0;
        newItemPanel.add(fields[1], newItemGrid);

        namedField[2] = new JLabel();
        namedField[2].setText("Marka:");
        namedField[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 1;
        newItemPanel.add(namedField[2], newItemGrid);

        fields[2] = new JTextField();
        fields[2].setPreferredSize(new Dimension(150, heightTextL));
        fields[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 1;
        newItemPanel.add(fields[2], newItemGrid);

        namedField[3] = new JLabel();
        namedField[3].setText("Typ:");
        namedField[3].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 2;
        newItemPanel.add(namedField[3], newItemGrid);

        fields[3] = new JTextField();
        fields[3].setPreferredSize(new Dimension(150, heightTextL));
        fields[3].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 2;
        newItemPanel.add(fields[3], newItemGrid);

        namedField[4] = new JLabel();
        namedField[4].setText("Waga:");
        namedField[4].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 3;
        newItemPanel.add(namedField[4], newItemGrid);

        fields[4] = new JTextField();
        fields[4].setPreferredSize(new Dimension(150, heightTextL));
        fields[4].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 3;
        newItemPanel.add(fields[4], newItemGrid);

        namedField[5] = new JLabel();
        namedField[5].setText("Wartość:");
        namedField[5].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 4;
        newItemPanel.add(namedField[5], newItemGrid);

        fields[5] = new JTextField();
        fields[5].setPreferredSize(new Dimension(150, heightTextL));
        fields[5].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 4;
        newItemPanel.add(fields[5], newItemGrid);

        namedField[6] = new JLabel();
        namedField[6].setText("IMEI:");
        namedField[6].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 5;
        newItemPanel.add(namedField[6], newItemGrid);

        fields[6] = new JTextField();
        fields[6].setPreferredSize(new Dimension(150, heightTextL));
        fields[6].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 5;
        newItemPanel.add(fields[6], newItemGrid);

        namedField[7] = new JLabel();
        namedField[7].setText("Uwagi:");
        namedField[7].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 0;
        newItemGrid.gridy = 7;
        newItemPanel.add(namedField[7], newItemGrid);

        fields[7] = new JTextField();
        fields[7].setPreferredSize(new Dimension(150, heightTextL));
        fields[7].setFont(new Font("Dialog", Font.BOLD, fontSize));
        newItemGrid.gridx = 1;
        newItemGrid.gridy = 7;
        newItemPanel.add(fields[7], newItemGrid);
    }

    public boolean isClose() {
        return iClose == 1;
    }

    public Double getAddRemoValue() {
        return adRemValue;
    }

    @Override
    public void generateGamesForm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // actions :D
    /**
     * @see accept new item
     */
    public class SsveItemForm implements ActionListener, ItemFormGenerator {

        boolean checkElement = false;

        @Override
        public void actionPerformed(ActionEvent e) {
            // add item to container
            // save to DB, but before save i add notyfication
            int selectedOption = JOptionPane.showConfirmDialog(formFrame,
                    "Czy na pewno wszystkie dane są poprawne ?",
                    "Potwierdzenie wygenerowania formularza!",
                    JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION) {
                if (fields[0].getText().length() > 0) {
                    // selec some category :D
                    switch (fields[0].getText().toLowerCase()) {
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
                        default:
                            generateDefault();
                            break;
                    }

                    if (checkItem.checkPrice(fields[9].getText().length(),
                            fields[9].getText())) {
                        adRemValue = Double.parseDouble(fields[9].getText());
                        moneySafe = new SelfCalc();
                        checkElement = moneySafe.chackValue(formFrame,
                                Float.parseFloat(fields[9].getText()))
                                && validator.checkValue(fields[9].getText().length(),
                                        fields[9].getText());
                        moneySafe.delFtomSelf(formFrame,
                                Float.parseFloat(fields[9].getText()));

                        if (checkElement == true) {
                            ok.setEnabled(false);
                            cancel.setEnabled(false);
                            moneySafe.updateValue();
                            saveToDB();
                            formFrame.dispose();
                        }
                    }

                }
            }
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
            int ring = 0, ringM = 0, bracelet = 0, chain = 0, pendant = 0,
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
            if (fields[1].getText().isEmpty() && fields[2].getText().isEmpty()) {
                errorMessage("Model i Marka");
            } else {
                //fields[10]
                checkElement = checkItem.checkValue(fields[5].getText().length(),
                        fields[5].getText());
                if (checkElement == false) {
                    checkElement = checkItem.checkJaweryElements(fields[6].getText());

                    if (checkElement == true) {
                        addItemtoList(1, fields[1].getText(), fields[2].getText(),
                                fields[3].getText(), fields[4].getText(),
                                fields[5].getText(), fields[6].getText(),
                                fields[7].getText(), fields[0].getText());
                    } else {
                        addItemtoList(1, fields[1].getText(), fields[2].getText(),
                                fields[3].getText(), fields[4].getText(),
                                fields[5].getText(), fields[6].getText(),
                                fields[7].getText(), fields[0].getText());
                    }
                } else {
                    checkElement = checkItem.checkJaweryElements(fields[6].getText());

                    if (checkElement == true) {
                        addItemtoList(1, fields[1].getText(), fields[2].getText(),
                                fields[3].getText(), fields[4].getText(),
                                fields[5].getText(), fields[6].getText(),
                                fields[7].getText(), fields[0].getText());
                    } else {
                        addItemtoList(1, fields[1].getText(), fields[2].getText(),
                                fields[3].getText(), fields[4].getText(),
                                fields[5].getText(), fields[6].getText(),
                                fields[7].getText(), fields[0].getText());
                    }
                }
            }
        }

        @Override
        public void generateTabletForm() {
            //fields[10]
            checkElement = checkItem.checkValue(fields[4].getText().length(),
                    fields[4].getText());
            if (checkElement == false) {
                addItemtoList(1, fields[1].getText(), fields[2].getText(),
                        fields[3].getText(), "",
                        fields[4].getText(), "",
                        fields[5].getText(), fields[0].getText());
            } else {
                addItemtoList(1, fields[1].getText(), fields[2].getText(),
                        fields[3].getText(), "",
                        fields[4].getText(), "",
                        fields[5].getText(), fields[0].getText());
            }
        }

        @Override
        public void generateTVForm() {
            checkElement = checkItem.checkValue(fields[4].getText().length(),
                    fields[4].getText());
            if (checkElement == false) {
                addItemtoList(1, fields[1].getText(), fields[2].getText(),
                        fields[3].getText(), "",
                        fields[4].getText(), "",
                        fields[5].getText(), fields[0].getText());
            } else {
                addItemtoList(1, fields[1].getText(), fields[2].getText(),
                        fields[3].getText(), "",
                        fields[4].getText(), "",
                        fields[5].getText(), fields[0].getText());
            }
        }

        @Override
        public void generateLaptopForm() {
            checkElement = checkItem.checkValue(fields[4].getText().length(),
                    fields[4].getText());
            if (checkElement == false) {
                addItemtoList(1, fields[1].getText(), fields[2].getText(),
                        fields[3].getText(), "",
                        fields[4].getText(), "",
                        fields[5].getText(), fields[0].getText());
            } else {
                addItemtoList(1, fields[1].getText(), fields[2].getText(),
                        fields[3].getText(), "",
                        fields[4].getText(), "",
                        fields[5].getText(), fields[0].getText());
            }
        }

        /**
         *
         */
        @Override
        public void generatePCForm() {
            checkElement = checkItem.checkValue(fields[4].getText().length(),
                    fields[4].getText());
            if (checkElement == false) {
                addItemtoList(1, fields[1].getText(), fields[2].getText(),
                        fields[3].getText(), "",
                        fields[4].getText(), "",
                        fields[5].getText(), fields[0].getText());
            } else {
                addItemtoList(1, fields[1].getText(), fields[2].getText(),
                        fields[3].getText(), "",
                        fields[4].getText(), "",
                        fields[5].getText(), fields[0].getText());
            }
        }

        @Override
        public void generateMonitorForm() {
            checkElement = checkItem.checkValue(fields[4].getText().length(),
                    fields[4].getText());
            if (checkElement == false) {
                addItemtoList(1, fields[1].getText(), fields[2].getText(),
                        fields[3].getText(), "",
                        fields[4].getText(), "",
                        fields[5].getText(), fields[0].getText());
            } else {
                addItemtoList(1, fields[1].getText(), fields[2].getText(),
                        fields[3].getText(), "",
                        fields[4].getText(), "",
                        fields[5].getText(), fields[0].getText());
            }
        }

        public void generateDefault() {
            checkElement = checkItem.checkValue(fields[5].getText().length(),
                    fields[5].getText());
            if (checkElement == false) {
                checkElement = checkItem.checkWeight(fields[4].getText().length(),
                        fields[4].getText());
                if (checkElement = true) {
                    addItemtoList(1, fields[1].getText(), fields[2].getText(),
                            fields[3].getText(), "",
                            fields[4].getText(), fields[5].getText(),
                            fields[7].getText(), fields[0].getText());
                } else {
                    addItemtoList(1, fields[1].getText(), fields[2].getText(),
                            fields[3].getText(), fields[4].getText(),
                            fields[5].getText(), fields[6].getText(),
                            fields[7].getText(), fields[0].getText());
                }
            } else {
                checkElement = checkItem.checkWeight(fields[4].getText().length(),
                        fields[4].getText());
                if (checkElement = true) {
                    addItemtoList(1, fields[1].getText(), fields[2].getText(),
                            fields[3].getText(), fields[4].getText(),
                            fields[5].getText(), fields[6].getText(),
                            fields[7].getText(), fields[0].getText());
                } else {
                    addItemtoList(1, fields[1].getText(), fields[2].getText(),
                            fields[3].getText(), fields[4].getText(),
                            fields[5].getText(), fields[6].getText(),
                            fields[7].getText(), fields[0].getText());
                }
            }

        }

        @Override
        public void generateGamesForm() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    /**
     * @see close form
     */
    public class CloseForm implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            iClose = 1;
            formFrame.dispose();
        }

    }

    // item form eleemnts
    /**
     * @see event to generate jewelery form
     */
    public class RestItemForm implements DocumentListener {

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
            newItemGrid.fill = GridBagConstraints.HORIZONTAL;
            text = fields[0].getText();
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
                default:
                    generateDefaultItemForm();
                    break;
            }
            newItemPanel.setVisible(true);
            formFrame.setVisible(true);
        }

    }

    private void saveToDB() {
        Map<String, String> tmpItem = new HashMap<>();
        // analyze cat id :D
        Date curretDate = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.YYYY");
        int catID = 0;
        for (int i = 0; i < itemsList.size(); i++) {
            try {
                setQuerry = new QueryDB();
                conDB = setQuerry.getConnDB();
                stmt = conDB.createStatement();
                tmpItem.putAll(itemsList.get(i));
                queryResult = setQuerry.dbSetQuery("SELECT ID FROM Category WHERE"
                        + " NAME LIKE '" + tmpItem.get("Kategoria") + "';");

                while (queryResult.next()) {
                    catID = queryResult.getInt("ID");
                }

                creatItemtoDB.setValues(tmpItem.get("Model"), tmpItem.get("Marka"),
                        tmpItem.get("Typ"), tmpItem.get("Waga"),
                        tmpItem.get("IMEI"), tmpItem.get("Wartość"),
                        tmpItem.get("Uwagi"), catID, 0, ft.format(curretDate));
                queryResult = setQuerry.dbSetQuery(creatItemtoDB.getInsertItem());

            } catch (SQLException ex) {
                LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", ex.getMessage()
                        + "\n" + Arrays.toString(ex.getStackTrace()));
                startLogging.logToFile(text);
            }
        }
        iClose = 1;
        setQuerry.closeDB();

    }
}
