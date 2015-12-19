package lombardia2014.Interface.menu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import lombardia2014.dataBaseInterface.MainDBQuierues;
import lombardia2014.generators.DateTools;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author Domek
 */
public class FinancialResults extends MenuElementsList {

    DateTools startDate = new DateTools(new Date());
    DateTools finishDate = new DateTools(new Date());
    JDatePickerImpl datePicker = null, datePicker2 = null;
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
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 0;
        c.ipady = 0;
        mainPanel.add(generateButtons(new GridBagConstraints()));

        generateTable(new GridBagConstraints());
    }

    private JPanel generateButtons(GridBagConstraints ct) {
        JPanel buttonPanel = new JPanel(new GridBagLayout());

        TitledBorder title = BorderFactory.createTitledBorder(blackline, "Polecenia");
        title.setTitleJustification(TitledBorder.RIGHT);
        title.setBorder(blackline);
        buttonPanel.setBorder(title);
        buttonPanel.setPreferredSize(new Dimension(windowWidth - 100, 100));

        JLabel label = new JLabel("Od:");
        label.setFont(new Font("Dialog", Font.BOLD, 12));
        ct.insets = new Insets(0, 0, 0, 10);
        ct.gridx = 0;
        ct.gridy = 0;
        buttonPanel.add(label, ct);

        UtilDateModel fromDate = new UtilDateModel();
        fromDate.setDate(startDate.GetYear(), startDate.GetMonth() - 1, startDate.GetDay());
        fromDate.setSelected(true);

        JDatePanelImpl datePanel = new JDatePanelImpl(fromDate);
        datePicker = new JDatePickerImpl(datePanel);
        datePicker.setPreferredSize(new Dimension(150, 27));
        datePicker.setFont(new Font("Dialog", Font.BOLD, 12));

        ct.insets = new Insets(0, 0, 0, 10);
        ct.gridx = 1;
        ct.gridy = 0;
        buttonPanel.add(datePicker, ct);

        JLabel label2 = new JLabel("Do:");
        label.setFont(new Font("Dialog", Font.BOLD, 12));
        ct.insets = new Insets(0, 0, 0, 10);
        ct.gridx = 2;
        ct.gridy = 0;
        buttonPanel.add(label2, ct);

        UtilDateModel finishDate2 = new UtilDateModel();
        finishDate2.setDate(finishDate.GetYear(), finishDate.GetMonth(), finishDate.GetDay());
        finishDate2.setSelected(true);

        JDatePanelImpl datePanel2 = new JDatePanelImpl(finishDate2);
        datePicker2 = new JDatePickerImpl(datePanel2);
        datePicker2.setPreferredSize(new Dimension(150, 27));
        datePicker2.setFont(new Font("Dialog", Font.BOLD, 12));

        ct.insets = new Insets(0, 0, 0, 10);
        ct.gridx = 3;
        ct.gridy = 0;
        buttonPanel.add(datePicker2, ct);

        JButton printList = new JButton();
        printList.setText("Drukuj listę");
        printList.setPreferredSize(new Dimension(150, 26));
        printList.setFont(new Font("Dialog", Font.BOLD, 12));

        ct.insets = new Insets(0, 20, 0, 0);
        ct.gridx = 4;
        ct.gridy = 0;
        buttonPanel.add(printList, ct);

        return buttonPanel;

    }

    private void generateTable(GridBagConstraints ct) {
        listSettlement = new JTable(new DefaultTableModel()); 
        scrollPane = new JScrollPane(listSettlement);
        listSettlement.setFillsViewportHeight(true);

        scrollPane.setPreferredSize(new Dimension(windowWidth - 100, windowHeigth - 200));

        ct.fill = GridBagConstraints.HORIZONTAL;
        ct.gridx = 0;
        ct.gridy = 1;
        mainPanel.add(scrollPane, ct);
    }
    
    private void loadData() {
        
    }

}
