/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import lombardia2014.Interface.forms.BuyItem;
import lombardia2014.Interface.forms.CreditForm;
import lombardia2014.Interface.forms.LastAgreement;
import lombardia2014.Interface.forms.LateClientsForm;
import lombardia2014.Interface.forms.Notices;
import lombardia2014.Interface.forms.PaymentForms;
import lombardia2014.Interface.forms.PhoneReports;
import lombardia2014.Interface.forms.RepaymentCreditForm;
import lombardia2014.Interface.forms.ProlongationForm;
import lombardia2014.core.SelfCalc;
import lombardia2014.core.UserOperations;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author marcin
 */
public final class MainMMenu extends javax.swing.JPanel {

    JPanel mainPanel;
    JPanel[] buttonPanels;
    TitledBorder title;
    Border blackline;
    JButton[] butonAction = new JButton[13];
    public static JLabel money;
    CustomersList customers = null;
    ObjectList objects = null;
    SwingWorker<Void, Void> worker = null;
    SelfCalc checkValue = null;
    JFrame mainFrame = null;
    UserOperations sniffOperations = new UserOperations();

    /**
     * @see construtrop create main panel on this tab
     */
    MainMMenu(UserOperations sniffOperations_) {
        checkValue = new SelfCalc();
        blackline = BorderFactory.createLineBorder(Color.black);
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        createPanels(c);
        setBttonsEnabledDisabled();
        this.add(mainPanel);
        setVisible(true);
        // set self
        checkValue.chackValue(mainFrame);
        sniffOperations = sniffOperations_;
    }

    /**
     * @see create panels
     */
    private void createPanels(GridBagConstraints c) {
        buttonPanels = new JPanel[3];
        buttonPanels[0] = new JPanel(new GridBagLayout());
        title = BorderFactory.createTitledBorder(blackline, "Kasa");
        title.setTitleJustification(TitledBorder.RIGHT);
        title.setBorder(blackline);
        buttonPanels[0].setBorder(title);
        // to do method to create buttons

        createElementsInfirstPanel(c);

        c.gridheight = 1;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 0;
        c.ipady = 0;
        mainPanel.add(buttonPanels[0], c);

        // nest button create
        buttonPanels[1] = new JPanel(new GridBagLayout());

        title = BorderFactory.createTitledBorder(blackline, "Podstawowe Operacje");
        title.setTitleJustification(TitledBorder.RIGHT);
        title.setBorder(blackline);
        buttonPanels[1].setBorder(title);

        // to do method to create buttons
        createElementsInSecoundPanel(c);
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 10;
        c.ipady = 10;
        c.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(buttonPanels[1], c);

        buttonPanels[2] = new JPanel(new GridBagLayout());

        title = BorderFactory.createTitledBorder(blackline, "Dodatkowe listy");
        title.setTitleJustification(TitledBorder.RIGHT);
        title.setBorder(blackline);
        buttonPanels[2].setBorder(title);

        createElementsInThirdPanel(c);

        // to do method to create buttons
        c.insets = new Insets(10, 10, 10, 10);
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        c.ipadx = 10;
        c.ipady = 10;

        mainPanel.add(buttonPanels[2], c);
    }

    /**
     * @see this method create elements on first panel, buttons and others
     * elements
     *
     */
    private void createElementsInfirstPanel(GridBagConstraints c) {
        money = new JLabel();
        money.setEnabled(false);
        money.setText(checkValue.getValue() + " zł");
        title = BorderFactory.createTitledBorder(blackline, "Stan Kasy:");
        title.setTitleJustification(TitledBorder.LEFT);
        title.setBorder(blackline);
        money.setBorder(title);
        money.setHorizontalTextPosition(JLabel.RIGHT);
        money.setSize(new Dimension(50, 16));
        money.setMinimumSize(new Dimension(50, 16));
        money.setPreferredSize(new Dimension(150, 34));
        money.setFont(new Font("Dialog", Font.BOLD, 16));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        buttonPanels[0].add(money, c);

        butonAction[0] = new JButton();
        butonAction[0].setText("Wpłata");
        butonAction[0].setPreferredSize(new Dimension(150, 40));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 1;

        buttonPanels[0].add(butonAction[0], c);

        butonAction[1] = new JButton();
        butonAction[1].setText("Wypłata");
        butonAction[1].setPreferredSize(new Dimension(150, 40));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 0);
        c.gridx = 0;
        c.gridy = 2;

        buttonPanels[0].add(butonAction[1], c);

        //buttonPanels[0].add(,c);
        butonAction[2] = new JButton();
        butonAction[2].setText("Kasa");
        butonAction[2].setFont(new Font("Dialog", Font.BOLD, 32));
        butonAction[2].setSize(new Dimension(200, 100));
        butonAction[2].setMinimumSize(new Dimension(200, 100));
        butonAction[2].setPreferredSize(new Dimension(200, 120));

        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 3;
        c.insets = new Insets(10, 480, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        buttonPanels[0].add(butonAction[2], c);
    }

    /**
     * @see method crerate next panel with 4 buttons to operation and one button
     * to active buttons
     *
     */
    private void createElementsInSecoundPanel(GridBagConstraints c) {
        butonAction[3] = new JButton();
        butonAction[3].setText("Nowa");
        butonAction[3].setPreferredSize(new Dimension(150, 40));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        buttonPanels[1].add(butonAction[3], c);

        butonAction[4] = new JButton();
        butonAction[4].setText("Zwrot");
        butonAction[4].setPreferredSize(new Dimension(150, 40));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 0, 0);
        c.gridx = 1;
        c.gridy = 0;
        buttonPanels[1].add(butonAction[4], c);

        butonAction[5] = new JButton();
        butonAction[5].setText("Ostatnia");
        butonAction[5].setPreferredSize(new Dimension(150, 40));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 1;
        buttonPanels[1].add(butonAction[5], c);

        butonAction[6] = new JButton();
        butonAction[6].setText("Przedłużenie");
        butonAction[6].setPreferredSize(new Dimension(150, 40));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 0, 0);
        c.gridx = 1;
        c.gridy = 1;
        buttonPanels[1].add(butonAction[6], c);

        butonAction[12] = new JButton();
        butonAction[12].setText("Skup");
        butonAction[12].setPreferredSize(new Dimension(150, 40));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 0, 0);
        c.gridx = 2;
        c.gridy = 0;
        buttonPanels[1].add(butonAction[12], c);

        butonAction[7] = new JButton();
        butonAction[7].setText("Pożyczka");
        butonAction[7].setFont(new Font("Dialog", Font.BOLD, 32));
        butonAction[7].setSize(new Dimension(200, 100));
        butonAction[7].setMinimumSize(new Dimension(200, 100));
        butonAction[7].setPreferredSize(new Dimension(200, 120));

        c.gridx = 3;
        c.gridy = 0;
        c.gridheight = 2;
        c.gridwidth = 2;
        c.insets = new Insets(10, 150, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        buttonPanels[1].add(butonAction[7], c);

    }

    /**
     * @see this method create button who generate other list, which i d not add
     * in tabbed panel
     */
    private void createElementsInThirdPanel(GridBagConstraints c) {
        butonAction[8] = new JButton();
        butonAction[8].setText("Spóźnieni");
        butonAction[8].setPreferredSize(new Dimension(150, 40));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        buttonPanels[2].add(butonAction[8], c);

        butonAction[9] = new JButton();
        butonAction[9].setText("Zgłoszenia tel.");
        butonAction[9].setPreferredSize(new Dimension(150, 40));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 1;
        buttonPanels[2].add(butonAction[9], c);

        butonAction[10] = new JButton();
        butonAction[10].setText("Uwagi");
        butonAction[10].setPreferredSize(new Dimension(150, 40));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 2;
        buttonPanels[2].add(butonAction[10], c);

        butonAction[11] = new JButton();
        butonAction[11].setText("Inne");
        butonAction[11].setFont(new Font("Dialog", Font.BOLD, 32));
        butonAction[11].setSize(new Dimension(200, 100));
        butonAction[11].setMinimumSize(new Dimension(200, 100));
        butonAction[11].setPreferredSize(new Dimension(200, 120));

        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 3;
        c.insets = new Insets(10, 465, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        buttonPanels[2].add(butonAction[11], c);
    }

    // Disable and enable some butttons
    public void setBttonsEnabledDisabled() {
        // first panel  
        butonAction[0].setEnabled(true);
        butonAction[1].setEnabled(true);
        butonAction[2].setEnabled(false);

        // actions 
        // payment
        butonAction[0].addActionListener(new SetPeyment(butonAction[0].getText()));
        butonAction[1].addActionListener(new SetPeyment(butonAction[1].getText()));
        // button too turn on secound panels
        butonAction[2].addActionListener(new SetBankMenu());
        // new credit
        butonAction[3].addActionListener(new AddNewCredit());
        // 
        butonAction[4].addActionListener(new SetNewRepayment());
        butonAction[5].addActionListener(new LastAgreement());
        butonAction[6].addActionListener(new Prolongation());
        //
        butonAction[8].addActionListener(new SetLateClients());
        butonAction[9].addActionListener(new SetPhoneReports());
        butonAction[10].addActionListener(new SetNotices());
        butonAction[12].addActionListener(new BuyNewItem());
        // secound panels
        butonAction[3].setEnabled(false);
        butonAction[4].setEnabled(false);
        butonAction[5].setEnabled(false);
        butonAction[6].setEnabled(false);
        butonAction[7].setEnabled(true);
        butonAction[12].setEnabled(false);

        // actions
        butonAction[7].addActionListener(new SetLoanButtons());
        // third panels
        butonAction[8].setEnabled(false);
        butonAction[9].setEnabled(false);
        butonAction[10].setEnabled(false);
        butonAction[11].setEnabled(true);

        //actions
        butonAction[11].addActionListener(new SetOthersbutton());

        //swing worker when i turn on class, and wait for dispose :)
    }

    /**
     * @param customers_
     * @param objects_
     * @param mfr
     * @see method wo get information about customers list and items list
     */
    public void putObjects(CustomersList customers_, ObjectList objects_, JFrame mfr) {
        mainFrame = mfr;
        customers = customers_;
        objects = objects_;

    }

    // time for actions:D
    // bank :D
    class SetBankMenu implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            butonAction[0].setEnabled(true);

            butonAction[1].setEnabled(true);
            butonAction[2].setEnabled(false);
            butonAction[3].setEnabled(false);
            butonAction[4].setEnabled(false);
            butonAction[5].setEnabled(false);
            butonAction[6].setEnabled(false);
            butonAction[7].setEnabled(true);
            butonAction[12].setEnabled(false);
            butonAction[8].setEnabled(false);
            butonAction[9].setEnabled(false);
            butonAction[10].setEnabled(false);
            butonAction[11].setEnabled(true);
        }

    }

    // loan
    class SetLoanButtons implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            butonAction[0].setEnabled(false);
            butonAction[1].setEnabled(false);
            butonAction[2].setEnabled(true);

            butonAction[3].setEnabled(true);
            butonAction[4].setEnabled(true);
            butonAction[5].setEnabled(true);
            butonAction[6].setEnabled(true);
            butonAction[7].setEnabled(false);
            butonAction[12].setEnabled(true);

            butonAction[8].setEnabled(false);
            butonAction[9].setEnabled(false);
            butonAction[10].setEnabled(false);
            butonAction[11].setEnabled(true);
        }

    }

    // others elements
    class SetOthersbutton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            butonAction[0].setEnabled(false);
            butonAction[1].setEnabled(false);
            butonAction[2].setEnabled(true);

            butonAction[3].setEnabled(false);
            butonAction[4].setEnabled(false);
            butonAction[5].setEnabled(false);
            butonAction[6].setEnabled(false);
            butonAction[7].setEnabled(true);
            butonAction[12].setEnabled(false);

            butonAction[8].setEnabled(true);
            butonAction[9].setEnabled(true);
            butonAction[10].setEnabled(true);
            butonAction[11].setEnabled(false);
        }

    }

    // Buttons actions 
    // Creatre forms objects
    class SetPeyment implements ActionListener {

        String titleForm = "";
        PaymentForms paymanet = null;

        SetPeyment(String titleForm_) {
            titleForm = titleForm_;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            paymanet = new PaymentForms(titleForm);

            worker = new SwingWorker() {

                @Override
                protected Object doInBackground() throws Exception {
                    while (true) {
                        if (paymanet.getAddRemoValue() > 0) {
                            sniffOperations.saveOperations(titleForm
                                    + ":" + paymanet.getAddRemoValue());
                            break;
                        }
                        Thread.sleep(10);
                    }
                    return null;
                }
            };
            worker.execute();

        }

    }

    class AddNewCredit implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            final CreditForm newCredit = new CreditForm();
            newCredit.generateGui();
            worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() {
                    try {
                        while (true) {
                            if (newCredit.isClose() == true) {
                                objects.updateItemTable();
                                customers.updateCustomerstoTable();
                                checkValue = null;
                                checkValue = new SelfCalc();
                                money.setText(checkValue.getValue() + " zł");
                                sniffOperations.saveOperations("Wystawiono umowe kredytu na:"
                                        + newCredit.getAddRemoValue());
                                break;
                            }
                            Thread.sleep(100);
                        }
                    } catch (Exception ex) {
                        LombardiaLogger startLogging = new LombardiaLogger();
                        String text = startLogging.preparePattern("Error", ex.getMessage()
                                + "\n" + Arrays.toString(ex.getStackTrace()));
                        startLogging.logToFile(text);
                    }
                    return null;
                }

            };
            worker.execute();
        }

    }

    class SetNewRepayment implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            final RepaymentCreditForm newRepayment = new RepaymentCreditForm();
            newRepayment.generateGui();

            worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() {
                    try {
                        while (true) {
                            if (newRepayment.isClose() == true) {
                                objects.updateItemTable();
                                sniffOperations.saveOperations("Spłacono umowe kredytu za:"
                                        + newRepayment.getAddRemoValue());
                                break;
                            }
                            Thread.sleep(100);
                        }

                    } catch (Exception ex) {
                        LombardiaLogger startLogging = new LombardiaLogger();
                        String text = startLogging.preparePattern("Error", ex.getMessage()
                                + "\n" + Arrays.toString(ex.getStackTrace()));
                        startLogging.logToFile(text);
                    }
                    return null;
                }

            };
            worker.execute();

        }
    }

    class SetLateClients implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            LateClientsForm newLateClients = new LateClientsForm();
            newLateClients.generateGui();
        }
    }

    class SetNotices implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Notices newNotices = new Notices();
            newNotices.generateGui();
        }
    }

    class SetPhoneReports implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            PhoneReports newPhoneReports = new PhoneReports();
            newPhoneReports.generateGui();
        }
    }

    class Prolongation implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            final ProlongationForm newProlongation = new ProlongationForm();
            newProlongation.generateGui();

            worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() {
                    try {
                        boolean updatePol = false, UpdateNewCredit = false;
                        
                        while (true) {
                            // update operations table for Prolongation form
                            
                            // update operations table for new credit form
                            
                            // last form
                            if (newProlongation.isClose() == true) {
                                objects.updateItemTable();
                                break;
                            }
                            Thread.sleep(100);
                        }
                        
                        

                    } catch (Exception ex) {
                        LombardiaLogger startLogging = new LombardiaLogger();
                        String text = startLogging.preparePattern("Error", ex.getMessage()
                                + "\n" + Arrays.toString(ex.getStackTrace()));
                        startLogging.logToFile(text);
                    }
                    return null;
                }

            };
            worker.execute();

        }
    }

    class BuyNewItem implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            final BuyItem newItem = new BuyItem();
            newItem.generateGui();
            //swing worker :)
            worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() {
                    try {
                        while (true) {
                            if (newItem.isClose() == true) {
                                objects.updateItemTable();
                                checkValue = null;
                                checkValue = new SelfCalc();
                                money.setText(checkValue.getValue() + " zł");
                                sniffOperations.saveOperations("Zakupiono przedmiot za:"
                                        + newItem.getAddRemoValue());
                                break;
                            }
                            Thread.sleep(100);
                        }
                    } catch (Exception ex) {
                        LombardiaLogger startLogging = new LombardiaLogger();
                        String text = startLogging.preparePattern("Error", ex.getMessage()
                                + "\n" + Arrays.toString(ex.getStackTrace()));
                        startLogging.logToFile(text);
                    }
                    return null;
                }

            };
            worker.execute();
        }

    }
}
