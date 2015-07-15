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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import lombardia2014.dataBaseInterface.MainDBQuierues;
import lombardia2014.dataBaseInterface.NoticesDBQueries;
import lombardia2014.dataBaseInterface.QueryDB;
import lombardia2014.generators.AutoSuggestor;
import lombardia2014.generators.LombardiaLogger;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author Domek
 */
public class NewNotices extends Forms {

    JLabel[] namedField = null;
    JTextField[] fields = null;
    AutoSuggestor selectCustomer = null;
    GridBagConstraints[] cTab = new GridBagConstraints[2];
    int fontSize = 12, heightTextL = 20;
    int iClose = 0;
    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    JDatePickerImpl datePicker = null;
    UtilDateModel model = new UtilDateModel();
    Date curretDate = null;
    SimpleDateFormat ft = new SimpleDateFormat("dd.MM.YYYY HH:mm");
    MainDBQuierues getQuery = new MainDBQuierues();

    @Override
    public void generateGui() {
        formFrame.setSize(350, 300);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("Nowa uwaga");
        mainPanel = new JPanel(new GridBagLayout());
        titleBorder = BorderFactory.createTitledBorder(blackline, "Nowa uwaga");
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
        namedField = new JLabel[5];
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
                        List<String> words = getQuery.getUsersByNameAndSurname();
                        setDictionary((ArrayList<String>) words);
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

        namedField[3] = new JLabel();
        namedField[3].setText("Tytuł:");
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

        namedField[4] = new JLabel();
        namedField[4].setText("Treść:");
        namedField[4].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 0;
        cTab[0].gridy = 4;
        fieldsPanel.add(namedField[4], cTab[0]);

        fields[3] = new JTextField();
        fields[3].setPreferredSize(new Dimension(150, heightTextL));
        fields[3].setFont(new Font("Dialog", Font.BOLD, fontSize));
        cTab[0].gridx = 1;
        cTab[0].gridy = 4;
        fieldsPanel.add(fields[3], cTab[0]);

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
        save.addActionListener(new SaveChanges());
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

    public boolean isClose() {
        return iClose == 1;
    }

    private class SaveChanges implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            boolean result = false;
            FormValidator checkItem = new FormValidator();
            // validation
            result = (!fields[0].getText().isEmpty()
                    && !fields[3].getText().isEmpty()
                    && !fields[2].getText().isEmpty());

            if (result) {
                new NoticesDBQueries().insertNewNotices(fields[2].getText(),
                        fields[3].getText(), fields[0].getText().
                        substring(0, fields[0].getText().lastIndexOf(" ")),
                        fields[0].getText().
                        substring(fields[0].getText().indexOf(" ") + 1,
                                fields[0].getText().length()));

                JOptionPane.showMessageDialog(null,
                        "Raport został zapisany",
                        "Raport został zapisany",
                        JOptionPane.INFORMATION_MESSAGE);
                formFrame.dispose();
                iClose = 1;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Jedno lub wiele pól jest pusta",
                        "Nieprawidłowa warotść!",
                        JOptionPane.ERROR_MESSAGE);
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
