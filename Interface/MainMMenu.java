/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 *
 * @author marcin
 */
public class MainMMenu extends javax.swing.JPanel {

    JPanel mainPanel;
    JPanel[] buttonPanels;
    TitledBorder title;
    Border blackline;
    JButton[] butonAction = new JButton[12];
    JLabel money;

    /**
     * @see construtrop create main panel on this tab
     */
    MainMMenu() {
        blackline = BorderFactory.createLineBorder(Color.black);
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        createPanels(c);

        this.add(mainPanel);
        setVisible(true);
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
        money.setText("00000000.00" + "zł");
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
        c.insets = new Insets(10, 485, 10, 10);
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

        butonAction[7] = new JButton();
        butonAction[7].setText("Pożyczka");
        butonAction[7].setFont(new Font("Dialog", Font.BOLD, 32));
        butonAction[7].setSize(new Dimension(200, 100));
        butonAction[7].setMinimumSize(new Dimension(200, 100));
        butonAction[7].setPreferredSize(new Dimension(200, 120));

        c.gridx = 2;
        c.gridy = 0;
        c.gridheight = 2;
        c.gridwidth = 2;
        c.insets = new Insets(10, 325, 10, 10);
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
        c.insets = new Insets(10, 470, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        buttonPanels[2].add(butonAction[11], c);
    }
}
