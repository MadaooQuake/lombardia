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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import static lombardia2014.Interface.MainMMenu.money;
import lombardia2014.core.SelfCalc;

/**
 *
 * @author Domek
 */
public class PaymentForms extends Forms {

    String title;
    JButton cancel = null;
    JButton ok = null;
    JTextField price = null;
    JLabel fieldName = null;
    SelfCalc valueDB = null;
    double adRemValue = 0.0;

    public PaymentForms(String title_) {
        title = title_;
        generateGui();
    }

    @Override
    public void generateGui() {
        valueDB = new SelfCalc();
        formFrame.setSize(300, 300);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle(title);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setPreferredSize(new Dimension(200, 100));
        titleBorder = BorderFactory.createTitledBorder(blackline, title + " pieniędzy");
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        titleBorder.setBorder(blackline);
        mainPanel.setBorder(titleBorder);

        generatePanels(c);

        formFrame.add(mainPanel);
        formFrame.setVisible(true);
    }

    /**
     *
     * @param c
     */
    @Override
    public void generatePanels(GridBagConstraints c) {
        // ok it time to develop this method
        fieldName = new JLabel();
        fieldName.setText("Podaj kwote");
        fieldName.setFont(new Font("Dialog", Font.BOLD, 20));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 10, 40, 0);
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(fieldName, c);

        // field
        price = new JTextField();
        price.setPreferredSize(new Dimension(120, 30));
        price.setFont(new Font("Dialog", Font.BOLD, 20));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 10, 40, 10);
        c.gridx = 1;
        c.gridy = 0;
        mainPanel.add(price, c);

        // butons
        ok = new JButton();
        ok.setText("Ok");
        ok.setFont(new Font("Dialog", Font.BOLD, 20));
        ok.setPreferredSize(new Dimension(100, 30));
        ok.addActionListener(new SetMoney());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(30, 40, 0, 20);
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(ok, c);

        cancel = new JButton();
        cancel.setText("Anuluj");
        cancel.setFont(new Font("Dialog", Font.BOLD, 20));
        cancel.setPreferredSize(new Dimension(100, 30));
        cancel.addActionListener(new CloseForm());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(30, 40, 0, 20);
        c.gridx = 1;
        c.gridy = 1;
        mainPanel.add(cancel, c);

    }

    public Double getAddRemoValue() {
        return adRemValue;
    }

    // class to create actions for buttons (polimorfizm)
    /**
     * Class check and set new value
     */
    public class SetMoney implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // first i must get currett valu from payment
            float currVal = valueDB.getValue();
            String value = price.getText();
            // check in regex :D
            //pattern
            String pattern = "[(\\d*)[.]?\\d{1,2}]{1,7}";

            Pattern replace = Pattern.compile(pattern);
            Matcher matcher = replace.matcher(value);

            if (matcher.matches()) {
                float floatValue = Float.parseFloat(value);
                adRemValue = floatValue;
                if (title.equals("Wypłata")) {
                    if (currVal > floatValue) {
                        currVal -= floatValue;
                        valueDB.setValue(currVal);
                        valueDB.updateValue();
                    } else {
                        JOptionPane.showMessageDialog(formFrame,
                                "Podana kwoata jest zbyt wysoka",
                                "Nie można wypłacić pieniędzy",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    currVal += floatValue;
                    valueDB.setValue(currVal);
                    valueDB.updateValue();
                }
                money.setText(currVal + "zł");
                formFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(formFrame,
                        "Wartość musi być podana w postaci liczbowej np. 100, 99,99, "
                                + "lub podana kwaota jest za duża",
                        "Zostało podane nieprawidłowe dane",
                        JOptionPane.ERROR_MESSAGE);
                price.setText("");
            }
            //float setValue = Float.parseFloat(); 
            // next i add regex to check add, delete some value in paymane

        }

    }

    /**
     * class close this form
     */
    public class CloseForm implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            formFrame.dispose();
        }

    }

}
