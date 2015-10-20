/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import java.util.HashMap;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import lombardia2014.Interface.menu.AppInfo;
import lombardia2014.Interface.menu.GroupDelete;
import lombardia2014.Interface.menu.Help;
import lombardia2014.Interface.menu.StatisticsForm;
import lombardia2014.Interface.menu.ListUsers;
import lombardia2014.Interface.menu.OperationList;
import lombardia2014.Interface.menu.Settings;
import lombardia2014.Interface.menu.SettlementForm;
import lombardia2014.Interface.menu.StocktakingForm;

import lombardia2014.dataBaseInterface.UserOperations;
import lombardia2014.dataBaseInterface.DeleteDB;
import lombardia2014.dataBaseInterface.JoinDB;
import lombardia2014.dataBaseInterface.LoadDB;
import lombardia2014.dataBaseInterface.SaveDB;
import lombardia2014.dataBaseInterface.UserDB;
import lombardia2014.generators.DailyReport;
import lombardia2014.generators.LombardiaLogger;

/**
 * @see this class generate all elements of gui (Include on this menu panel, and
 * three tabbed pannels)
 * @author marcin
 */
public class MainInterface {

    JFrame mainFrame = null;
    JMenuBar menuBar = null;
    JMenu menu, submenu = null;
    JMenuItem menuItem = null;
    JRadioButtonMenuItem rbMenuItem = null;
    JCheckBoxMenuItem cbMenuItem = null;
    JTabbedPane tabbedPane = null;
    JPanel panel = null;
    GridBagConstraints c = new GridBagConstraints();
    JFileChooser chooser;
    int authID = 0, userID = 0;
    public static String userName, userSurename;
    UserOperations sniffOperations = null;
    UserDB queryUser = new UserDB();
    SwingWorker<Void, Void> worker = null;
    ObjectList objects = null;
    AgreementsList listOfAgrr = null;
    DailyReport generateReport = null;

    /**
     * @param authID_
     * @param userID_
     * @see constructor open new methods, which create sme elements of GUI
     */
    public MainInterface(int authID_, int userID_) {
        // create frame
        authID = authID_;
        userID = userID_;

        createMainFrame();
        createMenuPanel();
        createTabbs();
        setUserNames();
        // create tabbedPanel

        // create main panel
        mainFrame.add(panel);
        mainFrame.setVisible(true);
        sniffOperations = new UserOperations(userName, userSurename);
    }

    /**
     * @see methods who create and defined frame
     */
    private void createMainFrame() {
        mainFrame = new JFrame("Lombardia v1.0.0 Kandydat 1");
        mainFrame.setSize(new Dimension(900, 700));
        panel = new JPanel(new GridBagLayout());

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    /**
     * @see create menu panel
     */
    private void createMenuPanel() {
        //Create the menu bar.
        menuBar = new JMenuBar();

        menuBar.setMaximumSize(new Dimension(30, 700));
        //addmenubar to Frame
        if (authID < 2) {
            menuFile();
            menuConfig();
        }
        menuCalculations();
        menuHelp();
        // new method to block/allowed some elements in form

        c.fill = GridBagConstraints.FIRST_LINE_START;
        c.ipadx = 900;
        c.ipady = 30;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(menuBar, c);
        setPrivileges();
    }

    public void menuFile() {
        menu = new JMenu("Plik");
        menu.setMnemonic(KeyEvent.VK_P);
        menu.getAccessibleContext().setAccessibleDescription(
                "Tutaj znajdują sie metody do zapisywania i lączenia baz danych");
        menuBar.add(menu);

        //a group of JMenuItems
        menuItem = new JMenuItem("Zapisz bazę");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Zapisuje bazę danych do pliku");
        menuItem.addActionListener(new SaveDatabase());
        menu.add(menuItem);

        //a group of radio button menu items
        menuItem = new JMenuItem("Wczytaj bazę");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_2, ActionEvent.ALT_MASK));
        menuItem.addActionListener(new ReadDatabase());
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Wczytuje bazę danych z pliku");

//Build second menu in the menu bar.
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("Połącz bazę");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_3, ActionEvent.ALT_MASK));
        menuItem.addActionListener(new JoinDatabase());
        menuItem.getAccessibleContext().setAccessibleDescription(
                "łaczy bazy danych pobrane z pliku");
        menu.add(menuItem);

        menuItem = new JMenuItem("Wyczyść bazę");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_4, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Czyści bazę danych");
        menuItem.addActionListener(new RemoveDatabase());
        menu.add(menuItem);
        menu.addSeparator();

        menuItem = new JMenuItem("Wyjdz z aplikacji");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_5, ActionEvent.ALT_MASK));
        menuItem.addActionListener(new ExitApplication());
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Zamyka aplikacjie");
        menu.add(menuItem);

    }

    public void menuConfig() {
        //Build the first menu.
        menu = new JMenu("Konfiguracja");
        menu.setMnemonic(KeyEvent.VK_C);
        menu.getAccessibleContext().setAccessibleDescription(
                "Konfiguracja aplikacji");
        menuBar.add(menu);

        //a group of JMenuItems
        menuItem = new JMenuItem("Użytkownicy");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_6, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Dodawanie, usuwanie i blokowanie uzytkowników");
        menuItem.addActionListener(new UsersList(false));
        menu.add(menuItem);

        menuItem = new JMenuItem("Uprawnienia");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_7, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Zmiana uprawnień użytkowników");
        menuItem.addActionListener(new UsersList(true));
        menu.add(menuItem);

        menuItem = new JMenuItem("Operacje");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Zmiana uprawnień użytkowników");
        menuItem.addActionListener(new OperationsList());
        menu.add(menuItem);

        menuItem = new JMenuItem("Ustawienia");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Zmiana uprawnień użytkowników");
        menuItem.addActionListener(new SettingsOption());
        menu.add(menuItem);
        menu.addSeparator();

        menuItem = new JMenuItem("Uwuwanie wielu");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Zmiana uprawnień użytkowników");
        menuItem.addActionListener(new DeleteElemnts());
        menu.add(menuItem);

        mainFrame.add(menuBar);
    }

    public void menuCalculations() {
        //Build the first menu.
        menu = new JMenu("Rozliczenia");
        menu.setMnemonic(KeyEvent.VK_R);
        menu.getAccessibleContext().setAccessibleDescription(
                "Rozliczenia miesięczne, roczne i statystyki");
        menuBar.add(menu);

        //elements menu
        menuItem = new JMenuItem("Rozliczenia miesięczne");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_6, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Tworzy dokument z rozliczeniami miesięcznymi");
        menuItem.addActionListener(new MonthlySettlements());
        menu.add(menuItem);

        //elements menu
        menuItem = new JMenuItem("Rozliczenia roczne");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_7, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Tworzy dokument z rozliczeniami rocznymi");
        menuItem.addActionListener(new YearlySettlements());
        menu.add(menuItem);

        //elements menu
        menuItem = new JMenuItem("Inwentaryzacja");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_7, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Tworzy liste przedmiotów na zadany dzień");
        menuItem.addActionListener(new Stocktaking());
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("Statystyki miesięczne");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_9, ActionEvent.ALT_MASK));
        // now i disable this function - when the rest of functions is implement i enable that function again
        menuItem.setEnabled(false);
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Tworzy wykres statystyk w ciagu miesiaca");
        menuItem.addActionListener(new MonthlyStatistics());
        menu.add(menuItem);

        menuItem = new JMenuItem("Statystyki roczne");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_0, ActionEvent.ALT_MASK));
        menuItem.setEnabled(false);
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Tworzy wykres statystyk w ciagu miesiaca");
        menuItem.addActionListener(new YearlyStatistics());
        menu.add(menuItem);

        menuItem = new JMenuItem("Raport dzienny");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_0, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Tworzy wykres statystyk w ciagu miesiaca");
        menuItem.addActionListener(new ActionDailyRaport());
        menu.add(menuItem);
    }

    public void menuHelp() {
        menu = new JMenu("Pomoc");
        menu.setMnemonic(KeyEvent.VK_P);
        menu.getAccessibleContext().setAccessibleDescription(
                "Pomoc i informacje o programie");
        menuBar.add(menu);

        menuItem = new JMenuItem("Instrukcja obslugi");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_0, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Podstawy korzystanai z aplikacji");
        menuItem.addActionListener(new UserManual());
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("O Programie");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_I, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Informacje o programie");
        menuItem.addActionListener(new AboutApp());
        menu.add(menuItem);
    }

    private void createTabbs() {
        tabbedPane = new JTabbedPane();
        setUserNames();

        sniffOperations = new UserOperations(userName, userSurename);
        MainMMenu mainPanel = new MainMMenu(sniffOperations);
        CustomersList customers = new CustomersList();
        objects = new ObjectList(sniffOperations);
        ObjectForSellList objectToSell = new ObjectForSellList(sniffOperations);
        objects.getObjectToSell(objectToSell);
        objectToSell.getObjects(objects);
        listOfAgrr = new AgreementsList();

        tabbedPane.addTab("Menu glówne", mainPanel);
        tabbedPane.addTab("Lista Klientów", customers);
        tabbedPane.addTab("Lista Depozytów", objects);
        tabbedPane.addTab("Przedmioty na sprzedaż", objectToSell);
        tabbedPane.addTab("Umowy", listOfAgrr);

        mainPanel.putObjects(customers, objectToSell, objects, mainFrame);

        //tabbedPane.addTab("Ogólne", panel1);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 0;
        c.ipady = 100;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(tabbedPane, c);

    }

    // method to check privilages and set app
    public void setPrivileges() {
        switch (authID) {
            case 3:
                menuBar.setVisible(false);
                break;
        }
    }

    // method get name and surname of user
    public void setUserNames() {
        HashMap<String, String> user = (HashMap<String, String>) queryUser.getNameSurnemeByID(userID);
        userName = user.get("NAME");
        userSurename = user.get("SURNAME");
    }

    /**
     * ########################################################################
     * This class is only for file section, and he cand do: - save file - import
     * database - marge database - clean database
     */
    // now create events for menu
    // simple class to save file :D
    public class SaveDatabase implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // start file chooser 
            chooser = new JFileChooser();
            FileNameExtensionFilter dbFileType
                    = new FileNameExtensionFilter("db pliki (*.db)", ".db");
            chooser.setFileFilter(dbFileType);
            int result = chooser.showSaveDialog(chooser);
            if (result == JFileChooser.APPROVE_OPTION) {
                SaveDB saveDB = new SaveDB(
                        chooser.getSelectedFile().toString());
                saveDB.savingFille();
            }

        }

    }

    // clase to read database
    public class ReadDatabase implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // start file chooser 
            chooser = new JFileChooser();

            FileNameExtensionFilter dbFileType
                    = new FileNameExtensionFilter("db pliki (*.db)", "db");
            chooser.setFileFilter(dbFileType);
            int returnVal = chooser.showOpenDialog(chooser);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                LoadDB load = new LoadDB(
                        chooser.getSelectedFile().toString());
                load.createCopy();
                load.deleteDB();
                load.loadnewDB();
                // method create 
            }

        }

    }

    // Join database to curreect DB
    public class JoinDatabase implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // start file chooser 
            chooser = new JFileChooser();
            FileNameExtensionFilter dbFileType
                    = new FileNameExtensionFilter("db pliki (*.db)", "db");
            chooser.setFileFilter(dbFileType);
            int returnVal = chooser.showOpenDialog(chooser);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                JoinDB join = new JoinDB(
                        chooser.getSelectedFile().toString());
                join.insertDB();
            } else {
                //log.append("Open command cancelled by user." + newline);
            }

        }

    }

    // remove elements from database
    public class RemoveDatabase implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            DeleteDB delete = new DeleteDB();
            delete.deleteWarning();
        }

    }

    public class SettingsOption implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Settings settings = new Settings();
            settings.generateGui();
        }

    }

    public class DeleteElemnts implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            final GroupDelete delteteElements = new GroupDelete();
            delteteElements.generateGui();

            worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() {
                    try {
                        while (true) {
                            if (delteteElements.isClose() == true) {
                                objects.updateItemTable();
                                listOfAgrr.updateItemTable();
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

    // class to exit application
    public class ExitApplication implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    /**
     * ########################################################################
     * This class section is defined secound menu - about configuration user and
     * user privilages
     */
    // list of users 
    public class UsersList implements ActionListener {

        boolean priv = false;

        UsersList(boolean priv_) {
            priv = priv_;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ListUsers showList = new ListUsers(priv);
        }

    }

    // operations
    public class OperationsList implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            OperationList showOperationlist = new OperationList();
        }

    }

    // stocktaking
    public class Stocktaking implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            StocktakingForm newStockTaking = new StocktakingForm("");
            newStockTaking.generateGui();
        }

    }

    // settlements
    public class MonthlySettlements implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            SettlementForm newSettlement = new SettlementForm("Month");
            newSettlement.generateGui();
        }

    }

    public class YearlySettlements implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            SettlementForm newSettlement = new SettlementForm("");
            newSettlement.generateGui();
        }

    }

    // statistics
    public class MonthlyStatistics implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            StatisticsForm newStatistic = new StatisticsForm("Month");
            newStatistic.generateGui();
        }

    }

    public class YearlyStatistics implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            StatisticsForm newStatistic = new StatisticsForm("Roczne");
            newStatistic.generateGui();
        }

    }

    public class ActionDailyRaport implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            generateReport = new DailyReport();
        }

    }

    // help menu
    public class AboutApp implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            AppInfo aboutApp = new AppInfo();
            aboutApp.generateGui();
        }

    }

    public class UserManual implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Help newHelp = new Help();
            newHelp.generateGui();
        }

    }

}
