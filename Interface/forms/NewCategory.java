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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import lombardia2014.Interface.menu.ListUsers;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class NewCategory extends Forms {

    JLabel[] namedField = null;
    JTextField[] fields = null;
    JComboBox listOldUser = null;
    JButton cancel = null;
    JButton add = null;
    int fontSize = 16;
    int heightTextL = 28;
    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    List<String> listCategories = new ArrayList<>();

    // for test only
    @Override
    public void generateGui() {
        formFrame.setSize(400, 200);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("Nowa kategoria");

        mainPanel = new JPanel(new GridBagLayout());
        titleBorder = BorderFactory.createTitledBorder(blackline, "Nowa kategoria");
        titleBorder.setBorder(blackline);
        mainPanel.setBorder(titleBorder);
        c.insets = new Insets(10, 10, 10, 10);
        generatePanels(c);

        formFrame.add(mainPanel);
        formFrame.setVisible(true);
    }

    @Override
    public void generatePanels(GridBagConstraints c) {
        namedField = new JLabel[1];
        fields = new JTextField[1];

        namedField[0] = new JLabel();
        namedField[0].setText("Nazwa:");
        namedField[0].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(namedField[0], c);

        fields[0] = new JTextField();
        fields[0].setPreferredSize(new Dimension(150, heightTextL));
        fields[0].setFont(new Font("Dialog", Font.BOLD, fontSize));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        mainPanel.add(fields[0], c);

        add = new JButton();
        add.setText("Dodaj");
        add.setPreferredSize(new Dimension(150, heightTextL));
        add.setFont(new Font("Dialog", Font.BOLD, 18));
        add.addActionListener(new AddCategory());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        mainPanel.add(add, c);

        cancel = new JButton();
        cancel.setText("Anuluj");
        cancel.setPreferredSize(new Dimension(150, heightTextL));
        cancel.setFont(new Font("Dialog", Font.BOLD, 18));
        cancel.addActionListener(new CancelDepozit());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        mainPanel.add(cancel, c);

    }

    // action class
    public class AddCategory implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                setQuerry = new QueryDB();
                conDB = setQuerry.getConnDB();
                stmt = conDB.createStatement();

                // select all elements in DB
                queryResult = setQuerry.dbSetQuery("SELECT NAME FROM Category");

                while (queryResult.next()) {
                    String data = queryResult.getString("NAME");
                    listCategories.add(data);
                }

                if (listCategories.isEmpty()) {
                    // save and close  
                    queryResult = setQuerry.dbSetQuery("INSERT INTO Category (NAME) VALUES"
                            + "('" + fields[0].getText() + "');");
                    JOptionPane.showMessageDialog(formFrame,
                            "Ta kategoria została dodana ",
                            "Kategoria została dodana!",
                            JOptionPane.OK_OPTION);
                } else {
                    // check if contains  
                    if (listCategories.contains(fields[0].getText())) {
                        JOptionPane.showMessageDialog(formFrame,
                                "Ta kategoria już istnieje w bazie danych ",
                                "Kategoria już istnieje!",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(formFrame,
                                "Ta kategoria została dodana ",
                                "Kategoria została dodana!",
                                JOptionPane.INFORMATION_MESSAGE);
                        queryResult = setQuerry.dbSetQuery("INSERT INTO Category (NAME) VALUES"
                                + "('" + fields[0].getText() + "');");
                    }
                }
                setQuerry.closeDB();

                formFrame.dispose();
            } catch (SQLException ex) {
                LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", ex.getMessage()
                        + "\n" + Arrays.toString(ex.getStackTrace()));
                startLogging.logToFile(text);
            }
        }

    }

    public class CancelDepozit implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            formFrame.dispose();
        }

    }
}
