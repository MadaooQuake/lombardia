/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.menu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import lombardia2014.generators.help.BuyItemHelp;
import lombardia2014.generators.help.ConfigurationHelp;
import lombardia2014.generators.help.FileHelp;
import lombardia2014.generators.help.HelpContext;
import lombardia2014.generators.help.HelpStrategy;
import lombardia2014.generators.help.LastAgreementHelp;
import lombardia2014.generators.help.NewCreditHelp;
import lombardia2014.generators.help.PeymentHelp;
import lombardia2014.generators.help.PrologationHelp;
import lombardia2014.generators.help.SettlementHelp;

/**
 *
 * @author Domek
 */
public class Help extends MenuElementsList {

    private JTree tree = null;
    private JPanel information = null;
    private JLabel title = null;
    private JLabel text = null;
    private JScrollPane scrollPane = null;
    private Map<String, Object> helpList = new HashMap<>();

    @Override
    public void generateGui() {
        formFrame.setSize(new Dimension(540, 600));
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("Instrukcja obsługi");

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(titleBorder);

        generatePanels(c);
        formFrame.add(mainPanel);
        formFrame.setVisible(true);
    }

    @Override
    public void generatePanels(GridBagConstraints c) {
        createListInMap();
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        tree = new JTree(generateMenu());
        tree.setPreferredSize(new Dimension(180, 560));
        tree.addTreeSelectionListener(new SelectMEnu());
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(tree, c);

        // create jpanel for text 
        information = new JPanel(new GridBagLayout());
        scrollPane = new JScrollPane(information);
        scrollPane.setPreferredSize(new Dimension(300, 560));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainInformation();
        c.gridx = 1;
        c.gridy = 0;
        mainPanel.add(scrollPane, c);

    }

    private DefaultMutableTreeNode generateMenu() {
        DefaultMutableTreeNode top
                = new DefaultMutableTreeNode("Lombardia");
        DefaultMutableTreeNode book, page, page2;

        //elements menu
        book = new DefaultMutableTreeNode("Pasek Menu");
        top.add(book);
        page = new DefaultMutableTreeNode("Plik");
        book.add(page);
        page = new DefaultMutableTreeNode("Konfiguracja");
        book.add(page);
        page = new DefaultMutableTreeNode("Rozliczenia");
        book.add(page);

        book = new DefaultMutableTreeNode("Menu główne");
        top.add(book);
        page = new DefaultMutableTreeNode("Kasa");
        book.add(page);
        page2 = new DefaultMutableTreeNode("Wpłata/Wypłata");
        page.add(page2);
        page = new DefaultMutableTreeNode("Pożyczka");
        book.add(page);
        page2 = new DefaultMutableTreeNode("Nowa");
        page.add(page2);
        page2 = new DefaultMutableTreeNode("Zwrot");
        page.add(page2);
        page2 = new DefaultMutableTreeNode("Skup");
        page.add(page2);
        page2 = new DefaultMutableTreeNode("Ostatnia");
        page.add(page2);
        page2 = new DefaultMutableTreeNode("Przedłużenie");
        page.add(page2);
        page = new DefaultMutableTreeNode("Inne");
        book.add(page);
        page2 = new DefaultMutableTreeNode("Spóźnieni");
        page.add(page2);
        page2 = new DefaultMutableTreeNode("Zgłoszenia tel.");
        page.add(page2);
        page2 = new DefaultMutableTreeNode("Uwagi");
        page.add(page2);

        book = new DefaultMutableTreeNode("Lista klientów");
        top.add(book);
        page = new DefaultMutableTreeNode("Wyszukiwarka");
        book.add(page);
        page = new DefaultMutableTreeNode("Lista");
        book.add(page);

        book = new DefaultMutableTreeNode("Lista depozytów");
        top.add(book);
        page = new DefaultMutableTreeNode("Wyszukiwarka");
        book.add(page);
        page = new DefaultMutableTreeNode("Lista");
        book.add(page);
        
        book = new DefaultMutableTreeNode("Lista przedmiotów na sprzedaż");
        top.add(book);
        page = new DefaultMutableTreeNode("Wyszukiwarka");
        book.add(page);
        page = new DefaultMutableTreeNode("Lista");
        book.add(page);
        
        book = new DefaultMutableTreeNode("Umowy");
        top.add(book);
        page = new DefaultMutableTreeNode("Wyszukiwarka");
        book.add(page);
        page = new DefaultMutableTreeNode("Lista");
        book.add(page);

        return top;
    }
    
    public void createListInMap() {
        helpList.put("Plik", new FileHelp());
        helpList.put("Konfiguracja", new ConfigurationHelp());
        helpList.put("Rozliczenia", new SettlementHelp());
        helpList.put("Ostatnia", new LastAgreementHelp());
        helpList.put("Skup", new BuyItemHelp());
        helpList.put("Nowa", new NewCreditHelp());
        helpList.put("Wpłata/Wypłata", new PeymentHelp());
        helpList.put("Przedłużenie", new PrologationHelp());
    }

    public void mainInformation() {
        GridBagConstraints c = new GridBagConstraints();
        title = new JLabel("<html><b>Lombardia - instrukcja obsługi</b><br /></html>");
        title.setFont(new Font("Dialog", Font.BOLD, 18));
        title.setSize(new Dimension(150, 40));
        c.gridx = 0;
        c.gridy = 0;
        information.add(title, c);

        text = new JLabel("<html>"
                + "Instrukcja zawiera podstawowe informacje <br/>"
                + "na temat poszczególnych funkcjonalności."
                + "</html>");
        text.setFont(new Font("Dialog", Font.BOLD, 12));
        text.setSize(new Dimension(200, 40));
        c.gridx = 0;
        c.gridy = 1;
        information.add(text, c);
    }

    private class SelectMEnu implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            HelpContext ctx = new HelpContext();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            String menuElement = node.getUserObject().toString();

            ctx.setHelpStrategy((HelpStrategy) helpList.get(menuElement));
            ctx.getText(title, text);
        }

    }

}
