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
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class ChangePassword extends MenuElementsList {

    JPanel[] buttonPanels;
    JTextField[] dataFields;
    JLabel[] dataLabels;
    TitledBorder title;
    JComboBox position = null;
    JButton changeP = null;
    JButton cancel = null;
    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    boolean status = false;
    int user = 0;

    ChangePassword(int usserID) {
        user = usserID;
    }

    @Override
    public void generateGui() {
        formFrame.setSize(new Dimension(400, 250));
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("Nowe hasło");

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
        title = BorderFactory.createTitledBorder(blackline, "Hasło użytkownika");
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
        dataFields = new JTextField[1];
        dataLabels = new JLabel[1];
        GridBagConstraints userPanels = new GridBagConstraints();

        dataLabels[0] = new JLabel();
        dataLabels[0].setText("Podaj hasło:");
        dataLabels[0].setFont(new Font("Dialog", Font.BOLD, 20));
        userPanels.insets = new Insets(10, 10, 10, 10);
        userPanels.gridx = 0;
        userPanels.gridy = 0;
        buttonPanels[0].add(dataLabels[0], userPanels);

        dataFields[0] = new JPasswordField();
        dataFields[0].setPreferredSize(new Dimension(150, 30));
        dataFields[0].setFont(new Font("Dialog", Font.BOLD, 20));
        userPanels.gridx = 1;
        userPanels.gridy = 0;
        buttonPanels[0].add(dataFields[0], userPanels);
    }

    /**
     * @see method witch create action buttons
     */
    public void generateActionButtons() {
        GridBagConstraints actionPanels = new GridBagConstraints();

        changeP = new JButton();
        changeP.setText("Dodaj");
        changeP.addActionListener(new ChangUserPassword());
        changeP.setPreferredSize(new Dimension(150, 40));
        changeP.setFont(new Font("Dialog", Font.BOLD, 20));
        actionPanels.insets = new Insets(0, 0, 0, 10);
        actionPanels.gridx = 0;
        actionPanels.gridy = 0;
        buttonPanels[1].add(changeP, actionPanels);

        cancel = new JButton();
        cancel.setText("Anuluj");
        cancel.addActionListener(new CancelButtonAction());
        cancel.setPreferredSize(new Dimension(150, 40));
        cancel.setFont(new Font("Dialog", Font.BOLD, 20));
        actionPanels.gridx = 1;
        actionPanels.gridy = 0;
        buttonPanels[1].add(cancel, actionPanels);
    }

    private class ChangUserPassword implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                setQuerry = new QueryDB();
                conDB = setQuerry.getConnDB();

                stmt = conDB.createStatement();
                if (dataFields[0].getText().isEmpty()) {
                    JOptionPane.showMessageDialog(formFrame,
                            "Pole jest puste",
                            "Zmieniono hasło!",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    queryResult = setQuerry.dbSetQuery("UPDATE Users SET "
                            + "PASSWORD = '" + dataFields[0].getText()
                            + "' WHERE ID =" + user + ";");

                    JOptionPane.showMessageDialog(formFrame,
                            "Hasło zostało zmienione",
                            "Zmieniono hasło!",
                            JOptionPane.INFORMATION_MESSAGE);
                    formFrame.dispose();
                }
            } catch (SQLException ex) {
                LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", ex.getMessage()
                        + "\n" + Arrays.toString(ex.getStackTrace()));
                startLogging.logToFile(text);
            }
        }

    }

    private class CancelButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            formFrame.dispose();
        }

    }

}
