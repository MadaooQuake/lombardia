/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.forms;

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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import lombardia2014.Interface.menu.ListUsers;
import lombardia2014.SelfCalc;
import static lombardia2014.Interface.MainMMenu.money;
import lombardia2014.Interface.ObjectList;
import lombardia2014.dataBaseInterface.QueryDB;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class ItemForm extends Forms {

    int id = 0;
    int itemBaseValue = 0;
    JLabel[] namedField = null;
    JTextField[] fields = null;
    JButton okCancel = null;
    JButton update = null;
    JButton sell = null;
    boolean iclose = false, valdiate = false;
    FormValidator checkItem = new FormValidator();
    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;

    int iClose = 0;
    int fontSize = 16;
    int heightTextL = 28;
    String text = null;
    String weight = "0";
    ObjectList objects = null;
    double adRemValue = 0.0;

    public ItemForm(int id_) {
        id = id_;
    }

    @Override
    public void generateGui() {
        formFrame.setSize(400, 480);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("Dane przedmoitu");
        mainPanel = new JPanel(new GridBagLayout());
        titleBorder = BorderFactory.createTitledBorder(blackline, "Dane Przedmoitu");
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        mainPanel.setBorder(titleBorder);

        generatePanels(c);
        fillItemForm();
        formFrame.add(mainPanel);
        formFrame.setVisible(true);

    }

    @Override
    public void generatePanels(GridBagConstraints c) {
        namedField = new JLabel[8];
        fields = new JTextField[8];

        namedField[0] = new JLabel();
        namedField[0].setText("Kategoria:");
        namedField[0].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(namedField[0], c);

        fields[0] = new JTextField();
        fields[0].setPreferredSize(new Dimension(150, heightTextL));
        fields[0].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[0].setEditable(false);
        c.gridx = 1;
        c.gridy = 1;
        mainPanel.add(fields[0], c);

        namedField[1] = new JLabel();
        namedField[1].setText("Model:");
        namedField[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.gridx = 0;
        c.gridy = 2;
        mainPanel.add(namedField[1], c);

        fields[1] = new JTextField();
        fields[1].setPreferredSize(new Dimension(150, heightTextL));
        fields[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[1].setEditable(false);
        c.gridx = 1;
        c.gridy = 2;
        mainPanel.add(fields[1], c);

        namedField[2] = new JLabel();
        namedField[2].setText("Marka:");
        namedField[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.gridx = 0;
        c.gridy = 3;
        mainPanel.add(namedField[2], c);

        fields[2] = new JTextField();
        fields[2].setPreferredSize(new Dimension(150, heightTextL));
        fields[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[2].setEditable(false);
        c.gridx = 1;
        c.gridy = 3;
        mainPanel.add(fields[2], c);

        namedField[3] = new JLabel();
        namedField[3].setText("Typ:");
        namedField[3].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.gridx = 0;
        c.gridy = 4;
        mainPanel.add(namedField[3], c);

        fields[3] = new JTextField();
        fields[3].setPreferredSize(new Dimension(150, heightTextL));
        fields[3].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[3].setEditable(false);
        c.gridx = 1;
        c.gridy = 4;
        mainPanel.add(fields[3], c);

        namedField[4] = new JLabel();
        namedField[4].setText("Waga:");
        namedField[4].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.gridx = 0;
        c.gridy = 5;
        mainPanel.add(namedField[4], c);

        fields[4] = new JTextField();
        fields[4].setPreferredSize(new Dimension(150, heightTextL));
        fields[4].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[4].setEditable(false);
        c.gridx = 1;
        c.gridy = 5;
        mainPanel.add(fields[4], c);

        namedField[5] = new JLabel();
        namedField[5].setText("Wartość:");
        namedField[5].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.gridx = 0;
        c.gridy = 6;
        mainPanel.add(namedField[5], c);

        fields[5] = new JTextField();
        fields[5].setPreferredSize(new Dimension(150, heightTextL));
        fields[5].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[5].setEditable(false);
        c.gridx = 1;
        c.gridy = 6;
        mainPanel.add(fields[5], c);

        namedField[6] = new JLabel();
        namedField[6].setText("IMEI:");
        namedField[6].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.gridx = 0;
        c.gridy = 7;
        mainPanel.add(namedField[6], c);

        fields[6] = new JTextField();
        fields[6].setPreferredSize(new Dimension(150, heightTextL));
        fields[6].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[6].setEditable(false);
        c.gridx = 1;
        c.gridy = 7;
        mainPanel.add(fields[6], c);

        namedField[7] = new JLabel();
        namedField[7].setText("Uwagi:");
        namedField[7].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.gridx = 0;
        c.gridy = 8;
        mainPanel.add(namedField[7], c);

        fields[7] = new JTextField();
        fields[7].setPreferredSize(new Dimension(150, heightTextL));
        fields[7].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[7].setEditable(false);
        c.gridx = 1;
        c.gridy = 8;
        mainPanel.add(fields[7], c);

        update = new JButton();
        update.setText("Edytuj");
        update.setPreferredSize(new Dimension(150, heightTextL));
        update.setFont(new Font("Dialog", Font.BOLD, 18));
        update.addActionListener(new EditSaveAction());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 9;
        mainPanel.add(update, c);

        sell = new JButton();
        sell.setText("Sprzedaj");
        sell.setPreferredSize(new Dimension(150, heightTextL));
        sell.setFont(new Font("Dialog", Font.BOLD, 18));
        sell.addActionListener(new SellingAction());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 10;
        mainPanel.add(sell, c);

        okCancel = new JButton();
        okCancel.setText("Anuluj");
        okCancel.setPreferredSize(new Dimension(150, heightTextL));
        okCancel.setFont(new Font("Dialog", Font.BOLD, 18));
        okCancel.addActionListener(new CancelButton());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 9;
        mainPanel.add(okCancel, c);

    }

    /**
     * @see close workes witch listner this form
     * @return
     */
    public boolean isClose() {
        return iclose;
    }

    public Double getAddRemoValue() {
        return adRemValue;
    }

    /**
     * @see fill fields in item field
     */
    private void fillItemForm() {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();
            queryResult = setQuerry.dbSetQuery("SELECT Category.ID, "
                    + "Items.ID, NAME, MODEL, BAND, TYPE, "
                    + "WEIGHT, VALUE, IMEI, ATENCION"
                    + " FROM Items, Category WHERE "
                    + " Items.ID_CATEGORY = Category.ID  AND Items.ID = " + id + ";");

            while (queryResult.next()) {
                fields[0].setText(queryResult.getString("NAME"));
                fields[1].setText(queryResult.getString("MODEL"));
                fields[2].setText(queryResult.getString("BAND"));
                fields[3].setText(queryResult.getString("TYPE"));
                fields[4].setText(queryResult.getString("WEIGHT"));
                fields[5].setText(queryResult.getString("VALUE"));
                fields[6].setText(queryResult.getString("IMEI"));
                fields[7].setText(queryResult.getString("ATENCION"));
            }

            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    /**
     *
     */
    private void updateItem() {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();

            stmt = conDB.createStatement();

            //set values
            if (fields[6].getText().isEmpty() && fields[4].getText().isEmpty()) {
                queryResult = setQuerry.dbSetQuery("UPDATE Items SET"
                        + " MODEL = '" + fields[1].getText() + "',"
                        + " BAND = '" + fields[2].getText() + "',"
                        + " TYPE = '" + fields[3].getText() + "',"
                        + " VALUE = " + fields[5].getText() + ","
                        + " ATENCION = '" + fields[7].getText()
                        + "' WHERE ID = " + id + ";");
            } else if (fields[6].getText().isEmpty()) {
                queryResult = setQuerry.dbSetQuery("UPDATE Items SET"
                        + " MODEL = '" + fields[1].getText() + "',"
                        + " BAND = '" + fields[2].getText() + "',"
                        + " TYPE = '" + fields[3].getText() + "',"
                        + " WEIGHT = " + fields[4].getText() + ","
                        + " VALUE = " + fields[5].getText() + ","
                        + " ATENCION = '" + fields[7].getText()
                        + "' WHERE ID = " + id + ";");
            } else if (fields[4].getText().isEmpty()) {
                queryResult = setQuerry.dbSetQuery("UPDATE Items SET"
                        + " MODEL = '" + fields[1].getText() + "',"
                        + " BAND = '" + fields[2].getText() + "',"
                        + " TYPE = '" + fields[3].getText() + "',"
                        + " VALUE = " + fields[5].getText() + ","
                        + " ATENCION = '" + fields[7].getText()
                        + "' WHERE ID = " + id + ";");
            } else {
                queryResult = setQuerry.dbSetQuery("UPDATE Items SET"
                        + " MODEL = '" + fields[1].getText() + "',"
                        + " BAND = '" + fields[2].getText() + "',"
                        + " TYPE = '" + fields[3].getText() + "',"
                        + " WEIGHT = " + fields[4].getText() + ","
                        + " IMEI = " + fields[6].getText() + ","
                        + " VALUE = " + fields[5].getText() + ","
                        + " ATENCION = '" + fields[7].getText()
                        + "' WHERE ID = " + id + ";");
            }

            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    /**
     * @see close form
     */
    private class CancelButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            formFrame.dispose();
        }

    }

    private class SellingAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                setQuerry = new QueryDB();
                conDB = setQuerry.getConnDB();
                stmt = conDB.createStatement();

                queryResult = setQuerry.dbSetQuery("SELECT VALUE FROM Items"
                        + " WHERE ID = " + id);

                itemBaseValue = queryResult.getInt("VALUE");

                String dialogWindow = (String) JOptionPane.showInputDialog(formFrame,
                        "Podaj cenę za jaką sprzedano przedmiot",
                        "0");

                FormValidator validate = new FormValidator();
                if (validate.checkPrice(dialogWindow.length(), dialogWindow) == false) {
                    JOptionPane.showMessageDialog(formFrame,
                            "Wprowadź liczbę",
                            "Niepoprawna wartość",
                            JOptionPane.ERROR_MESSAGE);
                } else if (Float.valueOf(dialogWindow) < 1) {
                    JOptionPane.showMessageDialog(formFrame,
                            "Musisz wpisać wartość większą od 0",
                            "Niepoprawna wartość",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    queryResult = setQuerry.dbSetQuery("DELETE FROM Items "
                            + " WHERE ID = " + id + "");
                    setQuerry.closeDB();

                    Float sellingPrice = Float.valueOf(dialogWindow);
                    adRemValue = sellingPrice;
                    SelfCalc moneys = new SelfCalc();
                    moneys.addToSelf(sellingPrice);

                    SelfCalc actualCalc = new SelfCalc();
                    actualCalc.chackValue(formFrame);
                    money.setText(actualCalc.getValue() + " zł");

                    fillItemForm();

                }

            } catch (SQLException ex) {
                LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", ex.getMessage()
                        + "\n" + Arrays.toString(ex.getStackTrace()));
                startLogging.logToFile(text);
            }

            iclose = true;
            formFrame.dispose();
        }

    }

    private class EditSaveAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (update.getText()) {
                case "Edytuj":
                    fields[1].setEditable(true);
                    fields[2].setEditable(true);
                    fields[3].setEditable(true);
                    fields[4].setEditable(true);
                    fields[5].setEditable(true);
                    if (fields[0].getText().equals("Telefon")) {
                        fields[6].setEditable(true);
                    }
                    fields[7].setEditable(true);
                    update.setText("Aktualizuj");
                    break;
                case "Aktualizuj":
                    valdiate = checkItem.checkValue(fields[5].getText().length(),
                            fields[5].getText()) && (!fields[1].getText().isEmpty());
                    if (valdiate == true) {
                        updateItem();
                        // notyfucation
                        JOptionPane.showMessageDialog(formFrame,
                                "Przedmiot został zaktualizowany",
                                "Przedmiot został zaktualizowany",
                                JOptionPane.INFORMATION_MESSAGE);
                        iclose = true;
                        formFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(formFrame,
                                "Błędne dane",
                                "Któreś z pól jest nie odpowiednie",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }
        }

    }

}
