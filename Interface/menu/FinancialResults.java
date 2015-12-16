package lombardia2014.Interface.menu;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import lombardia2014.dataBaseInterface.MainDBQuierues;

/**
 *
 * @author Domek
 */
public class FinancialResults extends MenuElementsList {
    Calendar now = Calendar.getInstance();
    String formname = "Wyniki finanoswe";
    JTable listSettlement = null;
    JScrollPane scrollPane = null;
    int selectRow = -1, windowWidth = 860, windowHeigth = 600, rowsPerPage = 20;
    String outputFileName = "_be_changed.pdf";
    String[] headers = {"Data", "Dane", "Sprzedaż", "Zwrot pożyczki", "Suma Końcowa"};
    float[] headersWidth = {1.0f, 7.0f, 3.0f, 3.0f, 3.0f};
    List<Integer> sumMe = new ArrayList();
    List<Integer> translateDateColumn = new ArrayList();
    String summaryText = "Podsumowanie";
    int summaryColumnIndex = 2;
    MainDBQuierues DB = new MainDBQuierues();

    @Override
    public void generateGui() {
       formFrame.setSize(windowWidth, windowHeigth);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        mainPanel = new JPanel(new GridBagLayout()); 
        
        generatePanels(new GridBagConstraints());

        formFrame.add(mainPanel);
        formFrame.setVisible(true);
    }

    @Override
    public void generatePanels(GridBagConstraints c) {
        
    }
    
}
