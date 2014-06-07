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
import java.awt.event.KeyEvent;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

/**
 * @see yhis class generate all elements of gui (Include on this menu panel, and
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
    

    /**
     * @see constructor open new methods, which create sme elements of GUI
     */
    public MainInterface() {
        // create frame
        createMainFrame();
        createMenuPanel();
        createTabbs();
        // create tabbedPanel

        // create main panel
        mainFrame.add(panel);
        mainFrame.setVisible(true);
    }

    /**
     * @see methods who create and defined frame
     */
    private void createMainFrame() {
        mainFrame = new JFrame("Lombardia v1.0.0");
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

        
        menuFile();
        menuConfig();
        menuCalculations();
        menuHelp();
        c.fill = GridBagConstraints.FIRST_LINE_START;
        c.ipadx = 900;
        c.ipady = 30;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(menuBar,c);

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
        menu.add(menuItem);

        //a group of radio button menu items
        menuItem = new JMenuItem("Wczytaj bazę");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_2, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Wczytuje bazę danych z pliku");

//Build second menu in the menu bar.
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("Połącz bazę");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_3, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "łaczy bazy danych pobrane z pliku");
        menu.add(menuItem);

        menuItem = new JMenuItem("Wyczyść bazę");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_4, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Czyści bazę danych");
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("Wyjdz z aplikacji");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_5, ActionEvent.ALT_MASK));
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
        menu.add(menuItem);

        menuItem = new JMenuItem("Uprawnienia");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_7, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Zmiana uprawnień użytkowników");
        menu.add(menuItem);

        mainFrame.add(menuBar);
    }

    public void menuCalculations() {
        //Build the first menu.
        menu = new JMenu("Rozliczenia");
        menu.setMnemonic(KeyEvent.VK_R);
        menu.getAccessibleContext().setAccessibleDescription(
                "Rozliczenie mieśięczne, roczne i statystyiki");
        menuBar.add(menu);
        
        //elements menu
        menuItem = new JMenuItem("rozliczenia miesięczne");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_6, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Tworzy dokument z rozliczeniami miesieczynmi");
        menu.add(menuItem);
        
                //elements menu
        menuItem = new JMenuItem("rozliczenia roczne");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_7, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Tworzy dokument z rozliczenia roczne");
        menu.add(menuItem);
        
        menu.addSeparator();
        
        menuItem = new JMenuItem("Statystyki miesięczne");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_8, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Tworzy wykres statystyk w ciagu miesiaca");
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Statystyki roczne");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_9, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Tworzy wykres statystyk w ciagu miesiaca");
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
        menu.add(menuItem); 
        
        menu.addSeparator();
        
        menuItem = new JMenuItem("O Programie");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_I, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Informacje o programie");
        menu.add(menuItem); 
    }
    
    private void createTabbs() {
        tabbedPane = new JTabbedPane();

        MainMMenu mainPanel = new MainMMenu();
        CustomersList customers = new CustomersList();
        ObjectList objects = new ObjectList();
        tabbedPane.addTab("Menu glówne", mainPanel);
        tabbedPane.addTab("Lista Klientów", customers);
        tabbedPane.addTab("Lista Depozytów", objects);
        //tabbedPane.addTab("Ogólne", panel1);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 0;
        c.ipady = 100;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(tabbedPane,c);
        
    }

}
