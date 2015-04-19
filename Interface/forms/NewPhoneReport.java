/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.forms;

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
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import lombardia2014.dataBaseInterface.QueryDB;
import lombardia2014.generators.AutoSuggestor;
import lombardia2014.generators.LombardiaLogger;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author Katagi
 */
public class NewPhoneReport extends Forms {

    JLabel[] namedField = null;
    JTextField[] fields = null;
    AutoSuggestor selectCustomer = null;
    GridBagConstraints[] cTab = new GridBagConstraints[2];
    int fontSize = 12, heightTextL = 20;
    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    JDatePickerImpl datePicker = null;
    UtilDateModel model = new UtilDateModel();
    Date curretDate = null;
    SimpleDateFormat ft = new SimpleDateFormat("dd.MM.YYYY HH:mm");

    @Override
    public void generateGui() {
        formFrame.setSize(350, 300);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("Nowe zgłoszenie");
        mainPanel = new JPanel(new GridBagLayout());
        titleBorder = BorderFactory.createTitledBorder(blackline, "Nowe zgłoszenie");
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        mainPanel.setBorder(titleBorder);

        generatePanels(c);

        formFrame.add(mainPanel);
        formFrame.setVisible(true);
    }

    @Override
    public void generatePanels(GridBagConstraints c) {
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        // main panel
        mainPanel.add(generateFields(), c);
        //save panel
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(generateActionButton(), c);
    }

    private JPanel generateFields() {
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        namedField = new JLabel[4];
        fields = new JTextField[4];
        cTab[0] = new GridBagConstraints();
        cTab[0].insets = new Insets(5, 5, 5, 5);
        cTab[0].ipady = 10;

        //list of old customers
        namedField[0] = new JLabel();
        namedField[0].setText("Wybierz klienta:");
        namedField[0].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 0;
        cTab[0].gridy = 0;
        fieldsPanel.add(namedField[0], cTab[0]);

        // new filds
        fields[0] = new JTextField();
        fields[0].setPreferredSize(new Dimension(150, heightTextL));
        fields[0].setFont(new Font("Dialog", Font.BOLD, fontSize));

        selectCustomer = new AutoSuggestor(
                fields[0], formFrame, null, Color.WHITE.brighter(),
                Color.BLUE, Color.RED, 0.75f, 45, 80) {
                    @Override
                    public boolean wordTyped(String typedWord) {
                        try {
                            setQuerry = new QueryDB();
                            conDB = setQuerry.getConnDB();
                            stmt = conDB.createStatement();

                            queryResult = setQuerry.dbSetQuery("SELECT NAME,SURNAME"
                                    + " FROM Customers;");
                            //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
                            List<String> words = new ArrayList<>();

                            while (queryResult.next()) {
                                words.add(queryResult.getString("NAME") + " "
                                        + queryResult.getString("SURNAME"));
                            }

                            setQuerry.closeDB();
                            setDictionary((ArrayList<String>) words);
                            //addToDictionary("bye");//adds a single word
                        } catch (SQLException ex) {
                            LombardiaLogger startLogging = new LombardiaLogger();
                            String text = startLogging.preparePattern("Error", ex.getMessage()
                                    + "\n" + Arrays.toString(ex.getStackTrace()));
                            startLogging.logToFile(text);
                        }
                        return super.wordTyped(typedWord);
                        //now call super to check for any matches against newest dictionary
                    }
                };

        cTab[0].gridx = 1;
        cTab[0].gridy = 0;
        fieldsPanel.add(fields[0], cTab[0]);

        namedField[1] = new JLabel();
        namedField[1].setText("Data zgłoszenia:");
        namedField[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 0;
        cTab[0].gridy = 1;
        fieldsPanel.add(namedField[1], cTab[0]);

        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        datePicker = new JDatePickerImpl(datePanel);
        datePicker.setPreferredSize(new Dimension(150, heightTextL));
        datePicker.setFont(new Font("Dialog", Font.BOLD, fontSize));
        curretDate = new Date();
        model.setValue(curretDate);
        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 1;
        cTab[0].gridy = 1;
        fieldsPanel.add(datePicker, cTab[0]);

        namedField[2] = new JLabel();
        namedField[2].setText("Nr. telefonu :");
        namedField[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 0;
        cTab[0].gridy = 2;
        fieldsPanel.add(namedField[2], cTab[0]);

        fields[1] = new JTextField();
        fields[1].setPreferredSize(new Dimension(150, heightTextL));
        fields[1].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 1;
        cTab[0].gridy = 2;
        fieldsPanel.add(fields[1], cTab[0]);

        namedField[3] = new JLabel();
        namedField[3].setText("Treść:");
        namedField[3].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 0;
        cTab[0].gridy = 3;
        fieldsPanel.add(namedField[3], cTab[0]);

        fields[2] = new JTextField();
        fields[2].setPreferredSize(new Dimension(150, heightTextL));
        fields[2].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 1;
        cTab[0].gridy = 3;
        fieldsPanel.add(fields[2], cTab[0]);

        return fieldsPanel;
    }

    private JPanel generateActionButton() {
        JPanel actionButtons = new JPanel(new GridBagLayout());
        cTab[1] = new GridBagConstraints();
        cTab[1].insets = new Insets(5, 5, 5, 5);
        cTab[1].ipady = 10;

        JButton save = new JButton();
        save.setText("Zapisz");
        save.setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[1].fill = GridBagConstraints.HORIZONTAL;
        cTab[1].gridx = 0;
        cTab[1].gridy = 0;
        actionButtons.add(save, cTab[1]);

        JButton cancel = new JButton();
        cancel.setText("Anuluj");
        cancel.setFont(new Font("Dialog", Font.BOLD, fontSize));
        cancel.addActionListener(new CancelForm());
        cTab[1].fill = GridBagConstraints.HORIZONTAL;
        cTab[1].gridx = 1;
        cTab[1].gridy = 0;
        actionButtons.add(cancel, cTab[1]);

        return actionButtons;
    }

    private class SaveChanges implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            try {
                boolean result = false;
                setQuerry = new QueryDB();
                conDB = setQuerry.getConnDB();
                stmt = conDB.createStatement();
                
                // validation
                FormValidator checkItem = new FormValidator();
                result = (!fields[0].getText().isEmpty() );
                // save
//                queryResult = setQuerry.dbSetQuery("INSERT INTO PhoneReports ("
//                        + " Title,Content,Date,Number, User) VALUES ('" + Title
//                        + "','" + Content + "','" + date + "','" + Number + "','"
//                        + userName + " " + userSurname + "');");
            } catch (SQLException ex) {
                LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", ex.getMessage()
                        + "\n" + Arrays.toString(ex.getStackTrace()));
                startLogging.logToFile(text);
            }
        }

    }

    private class CancelForm implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            formFrame.dispose();
        }

    }

}