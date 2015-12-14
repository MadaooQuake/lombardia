package lombardia2014.Interface.menu;

import java.awt.GridBagConstraints;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    int selectRow = -1, windowWidth = 860, windowHeigth = 500, rowsPerPage = 20;
    String outputFileName = "_be_changed.pdf";
    String[] headers = {"Data", "Dane", "Sprzedaż", "Zwrot pożyczki", "Suma Końcowa"};
    List<Integer> sumMe = new ArrayList();
    List<Integer> translateDateColumn = new ArrayList();
    float[] headersWidth = {};
    String summaryText = "Podsumowanie";
    int summaryColumnIndex = 2;
    MainDBQuierues DB = new MainDBQuierues();

    @Override
    public void generateGui() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void generatePanels(GridBagConstraints c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
