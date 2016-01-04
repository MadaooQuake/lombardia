package lombardia2014.Interface.menu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import lombardia2014.core.ConfigRead;
import lombardia2014.dataBaseInterface.UserOperations;
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
    JTable listFinacial = null;
    JScrollPane scrollPane = null;
    int selectRow = -1, windowWidth = 860, windowHeigth = 600, rowsPerPage = 20;
    String outputFileName = "_be_changed.pdf";
    String[] headers = {"Data", "Dane", "Sprzedaż", "Zwrot pożyczki", "Suma Końcowa"};
    float[] headersWidth = {1.0f, 7.0f, 3.0f, 3.0f, 3.0f};
    List<Integer> sumMe = new ArrayList();
    List<Integer> translateDateColumn = new ArrayList();
    String summaryText = "Podsumowanie";
    int summaryColumnIndex = 2;
    UserOperations operationList = new UserOperations();
    String firstDate = null, lastDate = null;
    ConfigRead config = new ConfigRead();

    DefaultTableModel model = new DefaultTableModel() {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

    };

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
        config.readFile();
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

        JButton changeDataRange = new JButton("Ustaw");
        changeDataRange.setPreferredSize(new Dimension(150, 26));
        changeDataRange.setFont(new Font("Dialog", Font.BOLD, 12));
        changeDataRange.addActionListener(new UpdateDataRange());

        ct.insets = new Insets(0, 0, 0, 0);
        ct.gridx = 4;
        ct.gridy = 0;
        buttonPanel.add(changeDataRange, ct);

        JButton printList = new JButton();
        printList.setText("Drukuj listę");
        printList.setPreferredSize(new Dimension(150, 26));
        printList.setFont(new Font("Dialog", Font.BOLD, 12));

        ct.insets = new Insets(0, 20, 0, 0);
        ct.gridx = 5;
        ct.gridy = 0;
        buttonPanel.add(printList, ct);

        return buttonPanel;
    }

    private void generateTable(GridBagConstraints ct) {
        model.addColumn("Data");
        model.addColumn("Dane");
        model.addColumn("Sprzedaż");
        model.addColumn("Zwrot pożyczki");
        model.addColumn("Suma Końcowa");

        listFinacial = new JTable(model);
        scrollPane = new JScrollPane(listFinacial);
        listFinacial.setFillsViewportHeight(true);

        scrollPane.setPreferredSize(new Dimension(windowWidth - 100, windowHeigth - 200));

        ct.fill = GridBagConstraints.HORIZONTAL;
        ct.gridx = 0;
        ct.gridy = 1;
        mainPanel.add(scrollPane, ct);

        loadData();
    }

    private void loadData() {
        // get days
        Date fDate = (Date) datePicker.getModel().getValue();
        firstDate = new DateTools(fDate).GetDateAsString();

        fDate = (Date) datePicker2.getModel().getValue();
        lastDate = new DateTools(fDate).GetDateAsString();

        addData((Date) datePicker.getModel().getValue());
        // calculate
        // load table
    }

    private void addData(Date date) {
        now.setTime(date);
        Date changeDate = now.getTime();
        String selectedDay = null;
        while (selectedDay == null || !selectedDay.equals(lastDate)) {
            //operation
            selectedDay = new DateTools(changeDate).GetDateAsString();
            // calculate day 
            addRow(operationList.getOperationsByDay(selectedDay));

            //change day
            now.add(Calendar.DATE, 1);
            changeDate = now.getTime();

        }

    }

    private Object[] addRow(List<HashMap<String, String>> operations) {
        Object[] data = {""};
        Map<String, String> operationData = new HashMap<>();
        float commissionBrutto = 0, commissionNetto = 0, sellBrutto = 0, sellNetto = 0,
                profitNetto = 0, ProfitBrutto = 0, commissionBrutto2 = 0, commissionNetto2 = 0,
                loans = 0, vat = 0;
        //calculate one day
        for (HashMap<String, String> operation : operations) {

            String[] dataTable = {"", "", "", "", "", "", "", ""};
            dataTable[0] = operation.get("Data");
            String op = operation.get("Operacje");
            String[] elements = op.split(":");
            switch (elements[0]) {
                case "Sprzedano za":
                    System.out.println("Sprzedano");

                    break;
                case "Zwrot pożyczki":
                    float vatTMP = ((Float.parseFloat(elements[2]) - Float.parseFloat(elements[3])) * config.getVat());
                    loans += Float.parseFloat(elements[2]);
                    commissionBrutto2 += (Float.parseFloat(elements[2]) - Float.parseFloat(elements[3]));
                    commissionNetto2 += (Float.parseFloat(elements[2]) - Float.parseFloat(elements[3]))
                            - vatTMP;
                    commissionNetto2 *= 100;
                    commissionNetto2 = Math.round(commissionNetto2);
                    commissionNetto2 /= 100;
                    vat += vatTMP;
                    vat *= 100;
                    vat = Math.round(vat);
                    vat /= 100;
                    System.out.println(loans + "::" + commissionBrutto2 + "::" + commissionNetto2 + "::" + vat);
                    break;
                default:
                    break;
            }

        }
        return data;
    }

    public void updateTable() {
        // clear model 
        model.setRowCount(0);
        loadData();
        repaint();
    }

    private class UpdateDataRange implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            updateTable();
        }

    }

}
