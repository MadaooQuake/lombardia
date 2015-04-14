/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.forms;

import lombardia2014.dataBaseInterface.QueryDB;
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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class CustomerForm extends Forms {

    int customerID = 0, fontSize = 16, heightTextL = 28;
    JLabel[] namedField = null;
    JTextField[] fields = null;
    JCheckBox goodClient = null;
    JTextArea addresCustomer = null;
    JButton okCancel = null;
    JButton update = null;
    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    boolean valdiate = false;
    FormValidator checkItem = new FormValidator();
    boolean iclose = false;

    public CustomerForm(int cusID) {
        customerID = cusID;
    }

    @Override
    public void generateGui() {
        formFrame.setSize(400, 480);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("Dane klienta");
        mainPanel = new JPanel(new GridBagLayout());
        titleBorder = BorderFactory.createTitledBorder(blackline, "Dane klienta");
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        mainPanel.setBorder(titleBorder);

        generatePanels(c);

        formFrame.add(mainPanel);
        formFrame.setVisible(true);

    }

    @Override
    public void generatePanels(GridBagConstraints c) {
        namedField = new JLabel[5];
        fields = new JTextField[5];

        c.insets = new Insets(5, 5, 5, 5);

        namedField[0] = new JLabel();
        namedField[0].setText("Imię:");
        namedField[0].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(namedField[0], c);

        fields[0] = new JTextField();
        fields[0].setPreferredSize(new Dimension(150, heightTextL));
        fields[0].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[0].setEditable(false);
        fields[0].setText(null);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        mainPanel.add(fields[0], c);

        namedField[1] = new JLabel();
        namedField[1].setText("Nazwisko:");
        namedField[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(namedField[1], c);

        fields[1] = new JTextField();
        fields[1].setPreferredSize(new Dimension(150, heightTextL));
        fields[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[1].setEditable(false);
        fields[1].setText(null);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        mainPanel.add(fields[1], c);

        namedField[2] = new JLabel();
        namedField[2].setText("Adres:");
        namedField[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        mainPanel.add(namedField[2], c);

        addresCustomer = new JTextArea();
        addresCustomer.setPreferredSize(new Dimension(140, 60));
        addresCustomer.setFont(new Font("Dialog", Font.BOLD, fontSize));
        addresCustomer.setEditable(false);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        mainPanel.add(addresCustomer, c);

        namedField[3] = new JLabel();
        namedField[3].setText("Pesel:");
        namedField[3].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        mainPanel.add(namedField[3], c);

        fields[2] = new JTextField();
        fields[2].setPreferredSize(new Dimension(150, heightTextL));
        fields[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
        fields[2].setEditable(false);
        fields[2].setText(null);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        mainPanel.add(fields[2], c);

        namedField[4] = new JLabel();
        namedField[4].setText("Zaufany:");
        namedField[4].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        mainPanel.add(namedField[4], c);

        goodClient = new JCheckBox();
        goodClient.setSelected(false);
        goodClient.setEnabled(false);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        mainPanel.add(goodClient, c);

        // now buttons
        // edit on/off
        // ok/update
        update = new JButton();
        update.setText("Edytuj");
        update.setPreferredSize(new Dimension(150, heightTextL));
        update.setFont(new Font("Dialog", Font.BOLD, 18));
        update.addActionListener(new EditSaveAction());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        mainPanel.add(update, c);

        okCancel = new JButton();
        okCancel.setText("Anuluj");
        okCancel.setPreferredSize(new Dimension(150, heightTextL));
        okCancel.setFont(new Font("Dialog", Font.BOLD, 18));
        okCancel.addActionListener(new CancelAction());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 5;
        mainPanel.add(okCancel, c);

        fillFields();
    }

    /**
     * @see method with query to db
     */
    private void fillFields() {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();

            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT NAME, SURNAME, ADDRESS, "
                    + "PESEL, TRUST FROM Customers WHERE ID = " + customerID + ";");

            while (queryResult.next()) {
                fields[0].setText(queryResult.getString("NAME"));
                fields[1].setText(queryResult.getString("SURNAME"));
                addresCustomer.setText(queryResult.getString("ADDRESS"));
                fields[2].setText(queryResult.getString("PESEL"));
                boolean trust = queryResult.getInt("TRUST") == 1;
                goodClient.setSelected(trust);
            }
            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    public boolean isClose() {
        return iclose;
    }

    /**
     * @see action to close this form
     */
    private class CancelAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            formFrame.dispose();
        }

    }

    private class EditSaveAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (update.getText()) {
                case "Edytuj":
                    fields[0].setEditable(true);
                    fields[1].setEditable(true);
                    addresCustomer.setEditable(true);
                    fields[2].setEditable(true);
                    goodClient.setEnabled(true);
                    update.setText("Aktualizuj");
                    break;
                case "Aktualizuj":
                    // validate

                    valdiate = checkItem.checkName(
                            fields[0].getText().length(), fields[0].getText())
                            && checkItem.checkSurename(fields[1].getText().length(),
                                    fields[1].getText())
                            && checkItem.checkPesel(fields[2].getText().length(),
                                    fields[2].getText());
                    //update
                    if (valdiate == true) {
                        updateCustomer();
                        // notyfucation
                        JOptionPane.showMessageDialog(formFrame,
                                "Klient został zaktualizowany",
                                "Klient został zaktualizowany",
                                JOptionPane.INFORMATION_MESSAGE);
                        // close :)
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

        private boolean updateCustomer() {
            try {
                setQuerry = new QueryDB();
                conDB = setQuerry.getConnDB();

                stmt = conDB.createStatement();

                if (fields[2].getText().isEmpty()) {
                    queryResult = setQuerry.dbSetQuery("UPDATE Customers SET"
                            + " NAME = '" + fields[0].getText() + "',"
                            + " SURNAME = '" + fields[1].getText() + "', "
                            + "ADDRESS = '" + addresCustomer.getText() + "'"
                            + ", TRUST =" + (goodClient.isSelected() ? 1 : 0)
                            + " WHERE ID = " + customerID + ";");
                } else {
                    queryResult = setQuerry.dbSetQuery("UPDATE Customers SET"
                            + " NAME = '" + fields[0].getText() + "',"
                            + " SURNAME = '" + fields[1].getText() + "', "
                            + "ADDRESS = '" + addresCustomer.getText() + "', "
                            + "PESEL =" + fields[2].getText()
                            + " , TRUST =" + (goodClient.isSelected() ? 1 : 0)
                            + " WHERE ID = " + customerID + ";");
                }
                setQuerry.closeDB();
            } catch (SQLException ex) {
                LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", ex.getMessage()
                        + "\n" + Arrays.toString(ex.getStackTrace()));
                startLogging.logToFile(text);
            }
            return true;
        }

    }

}
