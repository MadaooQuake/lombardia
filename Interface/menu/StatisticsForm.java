/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.menu;

import com.googlecode.charts4j.LineChart;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import lombardia2014.generators.ChartGenerator;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class StatisticsForm extends MenuElementsList {

    LineChart chart = null;
    ChartGenerator chatrGen = null;
    JLabel drawChart = null, sumIncrese = null, sumDecrese = null,
            totalExpenditure = null, totalEarnings = null;
    String range = "roczne";

    public StatisticsForm(String dateRange_) {
        chatrGen = new ChartGenerator(dateRange_);
        if(dateRange_.equals("Month")) {
            range = "miesięczne";
        }
    }

    @Override
    public void generateGui() {
        
        formFrame.setSize(500, 500);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("Statystyki miesięczne");
        mainPanel = new JPanel(new GridBagLayout());
        titleBorder = BorderFactory.createTitledBorder(blackline, "Statystyki " 
                + range);
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        mainPanel.setBorder(titleBorder);

        generatePanels(c);

        formFrame.add(mainPanel);
        formFrame.setVisible(true);
    }

    @Override
    public void generatePanels(GridBagConstraints c) {
        try {
        // i generate only two panels :)

            // chart panel 
            chart = chatrGen.generateChart();

            // and now very brutal creating chart on jpanel
            c.insets = new Insets(10, 10, 10, 10);
            drawChart = new JLabel(
                    new ImageIcon(ImageIO.read(new URL(chart.toURLString()))));
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            mainPanel.add(drawChart, c);

            sumIncrese = new JLabel("Ilość przedmiotów "
                    + "i zwróconych pożyczek: \t"  + chatrGen.getSumDecrese());
            c.insets = new Insets(0, 20, 0, 0);
            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 1;
            mainPanel.add(sumIncrese, c);

            sumDecrese = new JLabel("Ilość sprzedanych przedmiotów "
                    + "i kupionych przedmiotów: \t" + chatrGen.getSumIncrese());
            c.gridx = 0;
            c.gridy = 2;
            mainPanel.add(sumDecrese, c);

            totalEarnings = new JLabel("Kwota na plus: \t" 
                    + chatrGen.getTotalEarnings());
            c.gridx = 0;
            c.gridy = 3;
            mainPanel.add(totalEarnings, c);

            totalExpenditure = new JLabel("Kwota na minus: \t"
                    + chatrGen.getTotalExpenditure());
            c.gridx = 0;
            c.gridy = 4;
            mainPanel.add(totalExpenditure, c);

            // table panel
        } catch (IOException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

    }

}
