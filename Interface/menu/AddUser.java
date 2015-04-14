/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.menu;

import lombardia2014.dataBaseInterface.QueryDB;
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
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class AddUser extends MenuElementsList {

    JPanel[] buttonPanels;
    JTextField[] dataFields;
    JLabel[] dataLabels;
    TitledBorder title;
    JComboBox<String> position = null;
    String[] positionList = {"Kierowonik", "Użytkownik"};
    JButton addU = null;
    JButton cancel = null;
    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    boolean status = false, edit = false;
    int idUser = -1;

    public AddUser() {
    }

    public AddUser(boolean edit_, int idUser_) {
        edit = edit_;
        idUser = idUser_;
    }

    @Override
    public void generateGui() {
        formFrame.setSize(new Dimension(500, 500));
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("Dodaj użytkownika");

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
        buttonPanels = new JPanel[2];

        // fields panel
        buttonPanels[0] = new JPanel(new GridBagLayout());
        if (edit == false) {
            title = BorderFactory.createTitledBorder(blackline, "Dane użytkownika");
        } else {
            title = BorderFactory.createTitledBorder(blackline, "Zmien uprawnienia");
        }
        title.setTitleJustification(TitledBorder.RIGHT);
        title.setBorder(blackline);
        buttonPanels[0].setBorder(title);

        generateUserFields();

        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(buttonPanels[0], c);

        // action panel
        buttonPanels[1] = new JPanel(new GridBagLayout());

        generateActionButtons();

        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(buttonPanels[1], c);

    }

    public void generateUserFields() {
        dataFields = new JTextField[6];
        dataLabels = new JLabel[7];
        GridBagConstraints userPanels = new GridBagConstraints();
        // name
        if (edit == false) {
            dataLabels[0] = new JLabel();
            dataLabels[0].setText("Imię:");
            dataLabels[0].setFont(new Font("Dialog", Font.BOLD, 20));
            userPanels.insets = new Insets(10, 10, 10, 10);
            userPanels.gridx = 0;
            userPanels.gridy = 0;
            buttonPanels[0].add(dataLabels[0], userPanels);

            dataFields[0] = new JTextField();
            dataFields[0].setPreferredSize(new Dimension(150, 30));
            dataFields[0].setFont(new Font("Dialog", Font.BOLD, 20));
            userPanels.gridx = 1;
            userPanels.gridy = 0;
            buttonPanels[0].add(dataFields[0], userPanels);
            //surname
            dataLabels[1] = new JLabel();
            dataLabels[1].setText("Nazwisko:");
            dataLabels[1].setFont(new Font("Dialog", Font.BOLD, 20));
            userPanels.gridx = 0;
            userPanels.gridy = 1;
            buttonPanels[0].add(dataLabels[1], userPanels);

            dataFields[1] = new JTextField();
            dataFields[1].setPreferredSize(new Dimension(150, 30));
            dataFields[1].setFont(new Font("Dialog", Font.BOLD, 20));
            userPanels.gridx = 1;
            userPanels.gridy = 1;
            buttonPanels[0].add(dataFields[1], userPanels);
            // ADDRESS
            dataLabels[2] = new JLabel();
            dataLabels[2].setText("Adres:");
            dataLabels[2].setFont(new Font("Dialog", Font.BOLD, 20));
            userPanels.gridx = 0;
            userPanels.gridy = 2;
            buttonPanels[0].add(dataLabels[2], userPanels);

            dataFields[2] = new JTextField();
            dataFields[2].setPreferredSize(new Dimension(150, 30));
            dataFields[2].setFont(new Font("Dialog", Font.BOLD, 20));
            userPanels.gridx = 1;
            userPanels.gridy = 2;
            buttonPanels[0].add(dataFields[2], userPanels);
            // Phone
            dataLabels[3] = new JLabel();
            dataLabels[3].setText("Telefon:");
            dataLabels[3].setFont(new Font("Dialog", Font.BOLD, 20));
            userPanels.gridx = 0;
            userPanels.gridy = 3;
            buttonPanels[0].add(dataLabels[3], userPanels);

            dataFields[3] = new JTextField();
            dataFields[3].setPreferredSize(new Dimension(150, 30));
            dataFields[3].setFont(new Font("Dialog", Font.BOLD, 20));
            userPanels.gridx = 1;
            userPanels.gridy = 3;
            buttonPanels[0].add(dataFields[3], userPanels);
            // LOGIN
            dataLabels[4] = new JLabel();
            dataLabels[4].setText("Login:");
            dataLabels[4].setFont(new Font("Dialog", Font.BOLD, 20));
            userPanels.gridx = 0;
            userPanels.gridy = 4;
            buttonPanels[0].add(dataLabels[4], userPanels);

            dataFields[4] = new JTextField();
            dataFields[4].setPreferredSize(new Dimension(150, 30));
            dataFields[4].setFont(new Font("Dialog", Font.BOLD, 20));
            userPanels.gridx = 1;
            userPanels.gridy = 4;
            buttonPanels[0].add(dataFields[4], userPanels);
            // PASSWORD
            dataLabels[5] = new JLabel();
            dataLabels[5].setText("Hasło:");
            dataLabels[5].setFont(new Font("Dialog", Font.BOLD, 20));
            userPanels.gridx = 0;
            userPanels.gridy = 5;
            buttonPanels[0].add(dataLabels[5], userPanels);

            dataFields[5] = new JPasswordField();
            dataFields[5].setPreferredSize(new Dimension(150, 30));
            dataFields[5].setFont(new Font("Dialog", Font.BOLD, 20));
            userPanels.gridx = 1;
            userPanels.gridy = 5;
            buttonPanels[0].add(dataFields[5], userPanels);
            // Position
        }
        dataLabels[6] = new JLabel();
        dataLabels[6].setText("Stanowisko:");
        dataLabels[6].setFont(new Font("Dialog", Font.BOLD, 20));
        userPanels.gridx = 0;
        userPanels.gridy = 6;
        buttonPanels[0].add(dataLabels[6], userPanels);

        position = new JComboBox<>(positionList);
        position.setPreferredSize(new Dimension(150, 30));
        position.setFont(new Font("Dialog", Font.BOLD, 20));
        userPanels.gridx = 1;
        userPanels.gridy = 6;
        buttonPanels[0].add(position, userPanels);
    }

    /**
     * @see method witch create action buttons
     */
    public void generateActionButtons() {
        GridBagConstraints actionPanels = new GridBagConstraints();
        if (edit == false) {
            addU = new JButton();
            addU.setText("Dodaj");
            addU.addActionListener(new AddButtonAction());
            addU.setPreferredSize(new Dimension(150, 40));
            addU.setFont(new Font("Dialog", Font.BOLD, 20));
            actionPanels.insets = new Insets(0, 0, 0, 10);
            actionPanels.gridx = 0;
            actionPanels.gridy = 0;
            buttonPanels[1].add(addU, actionPanels);
        } else {
            addU = new JButton();
            addU.setText("Zmień");
            addU.addActionListener(new UpdateUser());
            addU.setPreferredSize(new Dimension(150, 40));
            addU.setFont(new Font("Dialog", Font.BOLD, 20));
            actionPanels.insets = new Insets(0, 0, 0, 10);
            actionPanels.gridx = 0;
            actionPanels.gridy = 0;
            buttonPanels[1].add(addU, actionPanels);
        }

        cancel = new JButton();
        cancel.setText("Anuluj");
        cancel.addActionListener(new CancelButtonAction());
        cancel.setPreferredSize(new Dimension(150, 40));
        cancel.setFont(new Font("Dialog", Font.BOLD, 20));
        actionPanels.gridx = 1;
        actionPanels.gridy = 0;
        buttonPanels[1].add(cancel, actionPanels);
    }

    /**
     * @param what
     * @param pattString
     * @param text
     * @return
     * @see check validation
     */
    public boolean checkValue(String what, String pattString, int text) {
        boolean op = false;
        Pattern pattern = Pattern.compile(pattString);
        Matcher matcher = pattern.matcher(dataFields[text].getText());
        if (matcher.matches()) {
            op = true;
        } else {
            JOptionPane.showMessageDialog(formFrame,
                    "Wprowadziłes złe dane do pola " + what,
                    "Nieprawidłowe dane!",
                    JOptionPane.ERROR_MESSAGE);
        }
        return op;
    }

    /**
     * @param what
     * @param text
     * @return
     * @chek lenght
     */
    public boolean isNotNull(String what, int text) {
        boolean val;
        val = !dataFields[text].getText().isEmpty();
        if (val == false) {
            JOptionPane.showMessageDialog(formFrame,
                    "Wprowadziłes złe dane do pola " + what,
                    "Nieprawidłowe dane!",
                    JOptionPane.ERROR_MESSAGE);
        }
        return val;
    }

    /**
     * @return @see get information about addding user
     */
    public boolean getStatusNewUser() {
        return status;
    }

    /**
     * @return @see actualize table of user list
     */
    public boolean addUsertoDB() {
        boolean[] valUser = new boolean[6];
        int IDPosition = 0;
        try {

            // validation options :D
            // check name
            String pattString = "[a-zA-Z-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{3,20}";
            valUser[0] = checkValue("Imię", pattString, 0);
            // check surename
            valUser[1] = checkValue("Nazwisko", pattString, 1);
            // check phone
            pattString = "[0-9]{9}";
            valUser[2] = checkValue("Telefon", pattString, 3);
            // select DB
            valUser[3] = isNotNull("Adres", 2);
            valUser[4] = isNotNull("Login", 4);
            valUser[5] = isNotNull("Hasło", 5);
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();

            stmt = conDB.createStatement();
            queryResult = setQuerry.dbSetQuery("SELECT ID FROM Auth WHERE NAME like '"
                    + position.getSelectedItem().toString() + "';");
            while (queryResult.next()) {
                IDPosition = queryResult.getInt("ID");
            }

            // now I add new user to DB
            if (valUser[0] && valUser[1] && valUser[2] && valUser[3]
                    && valUser[4] && valUser[5]) {
                queryResult = setQuerry.dbSetQuery("INSERT INTO Users (NAME, SURNAME,"
                        + " ADDRESS, PHONE, LOGIN, PASSWORD, ID_auth) VALUES"
                        + "('" + dataFields[0].getText() + "','" + dataFields[1].getText()
                        + "','" + dataFields[2].getText() + "','" + dataFields[3].getText()
                        + "','" + dataFields[4].getText() + "','" + dataFields[5].getText()
                        + "'," + IDPosition + ");");
                status = true;
            }
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
        return valUser[0] && valUser[1] && valUser[2] && valUser[3]
                && valUser[4] && valUser[5];
    }

    /**
     * @return @see get information about new user
     */
    public Object[] getUser() {
        Object[] data = {dataFields[0].getText(), dataFields[1].getText(),
            position.getSelectedItem().toString()};
        return data;
    }

    /**
     * @see add user with privilages to Database
     */
    private class AddButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //add user to db
            boolean checkValidation;
            checkValidation = addUsertoDB();
            if (checkValidation == true) {
                JOptionPane.showMessageDialog(formFrame,
                        "Użytkownik został dodany",
                        "Dodano użytkownika!",
                        JOptionPane.INFORMATION_MESSAGE);
                formFrame.dispose();
            }
        }

    }

    private class CancelButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            formFrame.dispose();
        }

    }

    // update application
    private class UpdateUser implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int IDPosition = 0;
                setQuerry = new QueryDB();
                conDB = setQuerry.getConnDB();

                stmt = conDB.createStatement();
                stmt = conDB.createStatement();
                queryResult = setQuerry.dbSetQuery("SELECT ID FROM Auth WHERE NAME like '"
                        + position.getSelectedItem().toString() + "';");
                while (queryResult.next()) {
                    IDPosition = queryResult.getInt("ID");
                }
                queryResult = setQuerry.dbSetQuery("UPDATE Users SET ID_auth = "
                        + IDPosition + " WHERE ID = " + idUser + ";");

                setQuerry.closeDB();
                status = true;
                formFrame.dispose();

            } catch (SQLException ex) {
                LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", ex.getMessage()
                        + "\n" + Arrays.toString(ex.getStackTrace()));
                startLogging.logToFile(text);
            }
        }

    }
}
