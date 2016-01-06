package lombardia2014.Interface.menu;

import com.itextpdf.text.DocumentException;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import lombardia2014.core.ConfigRead;
import lombardia2014.dataBaseInterface.UserOperations;
import lombardia2014.generators.DateTools;
import lombardia2014.generators.LombardiaLogger;
import lombardia2014.generators.PDFCreator;
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
    int selectRow = -1, windowWidth = 860, windowHeigth = 600, rowsPerPage = 40;
    String outputFileName = "WynikiFinansowe.pdf";
    String[] headers = {"Data", "Dane", "Sprzedaż", "Zwrot pożyczki", "Suma Końcowa"};
    float[] headersWidth = {1.0f, 7.0f, 3.0f, 3.0f, 3.0f};
    List<Integer> sumMe = new ArrayList();
    List<Integer> translateDateColumn = new ArrayList();
    String summaryText = "Podsumowanie";
    int summaryColumnIndex = 2;
    UserOperations operationList = new UserOperations();
    String firstDate = null, lastDate = null;
    ConfigRead config = new ConfigRead();
    float sumLoans = 0, sumSellNetto = 0, sumSellBrutto = 0, sCommissionNetto = 0, sCommissionBrutto = 0,
            commisionVAT = 0;
    PDFCreator pdf = null;

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
        printList.addActionListener(new PrintFinancalResult());

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
        createSummary();
    }

    private void addRow(List<HashMap<String, String>> operations) {
        String data = null;
        float commissionBrutto = 0, commissionNetto = 0, sellBrutto = 0, sellNetto = 0,
                profitNetto = 0, commissionBrutto2 = 0, commissionNetto2 = 0,
                loans = 0, vat = 0, sellVat = 0;
        //calculate one day
        for (HashMap<String, String> operation : operations) {
            data = operation.get("Data");
            String op = operation.get("Operacje");
            String[] elements = op.split(":");
            switch (elements[0]) {
                case "Sprzedano za":
                    sellBrutto += Float.parseFloat(elements[1]);
                    sellNetto += Float.parseFloat(elements[1]) - (Float.parseFloat(elements[1]) * config.getVat());
                    commissionBrutto += (Float.parseFloat(elements[1]) * config.getVat());
                    commissionNetto += (Float.parseFloat(elements[1]) * config.getVat())
                            - ((Float.parseFloat(elements[1]) * config.getVat()) * config.getVat());
                    commissionBrutto *= 100;
                    commissionBrutto = Math.round(commissionBrutto);
                    commissionBrutto /= 100;
                    commissionNetto *= 100;
                    commissionNetto = Math.round(commissionNetto);
                    commissionNetto /= 100;
                    sellVat = commissionBrutto - commissionNetto;
                    profitNetto = commissionNetto;
                    sellVat *= 100;
                    sellVat = Math.round(sellVat);
                    sellVat /= 100;
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
                    break;
                default:
                    break;
            }
        }

        // put data to table - looks horible, add to new method
        if (data != null) {
            List<String> dataToTable = new ArrayList<>();
            dataToTable.add(data);
            dataToTable.add("3. Zwroty");
            dataToTable.add("");
            dataToTable.add(Float.toString(loans));
            dataToTable.add(Float.toString(loans));
            model.addRow((Object[]) dataToTable.toArray());
            dataToTable.clear();
            dataToTable.add("");
            dataToTable.add("4. Prowizje brutto");
            dataToTable.add(Float.toString(commissionBrutto));
            dataToTable.add(Float.toString(commissionBrutto2));
            dataToTable.add(Float.toString(commissionBrutto + commissionBrutto2));
            model.addRow((Object[]) dataToTable.toArray());
            dataToTable.clear();
            dataToTable.add("");
            dataToTable.add("5. Prowizje netto");
            dataToTable.add(Float.toString(commissionNetto));
            dataToTable.add(Float.toString(commissionNetto2));
            dataToTable.add(Float.toString(commissionNetto + commissionNetto2));
            model.addRow((Object[]) dataToTable.toArray());
            dataToTable.clear();
            dataToTable.add("");
            dataToTable.add("6. Sprzedaż brutto");
            dataToTable.add(Float.toString(sellBrutto));
            dataToTable.add("");
            dataToTable.add(Float.toString(sellBrutto));
            model.addRow((Object[]) dataToTable.toArray());
            dataToTable.clear();
            dataToTable.add("");
            dataToTable.add("7. Sprzedaż netto");
            dataToTable.add(Float.toString(sellNetto));
            dataToTable.add("");
            dataToTable.add(Float.toString(sellNetto));
            model.addRow((Object[]) dataToTable.toArray());
            dataToTable.clear();
            dataToTable.add("");
            dataToTable.add("8. Przychód ze sprzedażu netto");
            dataToTable.add(Float.toString(profitNetto));
            dataToTable.add("");
            dataToTable.add(Float.toString(profitNetto));
            model.addRow((Object[]) dataToTable.toArray());
            dataToTable.clear();
            dataToTable.add("");
            dataToTable.add("9. Przychody netto");
            dataToTable.add(Float.toString(profitNetto));
            dataToTable.add(Float.toString(commissionNetto2));
            dataToTable.add(Float.toString(profitNetto + commissionNetto2));
            model.addRow((Object[]) dataToTable.toArray());
            dataToTable.clear();
            dataToTable.add("");
            dataToTable.add("10. VAT");
            dataToTable.add(Float.toString(sellVat));
            dataToTable.add(Float.toString(vat));
            dataToTable.add(Float.toString(sellVat + vat));
            model.addRow((Object[]) dataToTable.toArray());

            sumLoans += commissionBrutto2;
            sumSellNetto += sellNetto;
            sumSellBrutto += sellBrutto;
            sCommissionNetto += commissionNetto;
            sCommissionBrutto += commissionBrutto;
            commisionVAT += sellVat;
        }
    }

    public void createSummary() {
        model.addRow(new Object[5]);
        model.addRow(new Object[5]);
        model.addRow(new Object[5]);
        model.addRow(new Object[5]);
        List<String> summary = new ArrayList<>();
        summary.add("");
        summary.add("Prowizje za zwrotów pożyczek:");
        summary.add("");
        summary.add(Float.toString(sumLoans));
        summary.add("ptk. 4");
        model.addRow((Object[]) summary.toArray());
        model.addRow(new Object[5]);
        model.addRow(new Object[5]);
        model.addRow(new Object[5]);
        model.addRow(new Object[5]);
        model.addRow(new Object[5]);
        summary.clear();
        summary.add("");
        summary.add("Sprzedaż brutto:");
        summary.add("");
        summary.add(Float.toString(sumSellBrutto));
        summary.add("ptk. 6");
        model.addRow((Object[]) summary.toArray());
        summary.clear();
        summary.add("");
        summary.add("Sprzedaż netto:");
        summary.add("");
        summary.add(Float.toString(sumSellNetto));
        summary.add("ptk. 7");
        model.addRow((Object[]) summary.toArray());
        model.addRow(new Object[5]);
        summary.clear();
        summary.add("");
        summary.add("Marża brutto:");
        summary.add("");
        summary.add(Float.toString(sCommissionBrutto));
        summary.add("ptk. 4");
        model.addRow((Object[]) summary.toArray());
        summary.clear();
        summary.add("");
        summary.add("Marża netto:");
        summary.add("");
        sCommissionNetto *= 100;
        sCommissionNetto = Math.round(sCommissionNetto);
        sCommissionNetto /= 100;
        summary.add(Float.toString(sCommissionNetto));
        summary.add("ptk. 5");
        model.addRow((Object[]) summary.toArray());
        summary.clear();
        summary.add("");
        summary.add("VAT( " + (config.getVat() < 0 ? config.getVat() : config.getVat() * 100) + "%) - od marży:");
        summary.add("");
        summary.add(Float.toString(commisionVAT));
        summary.add("ptk. 10");
        model.addRow((Object[]) summary.toArray());
    }

    public void updateTable() {
        // clear model 
        sumLoans = sumSellNetto = sumSellBrutto = sCommissionNetto 
                = sCommissionBrutto = commisionVAT = 0;
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

    private class PrintFinancalResult implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                pdf = new PDFCreator(outputFileName, formname);
                pdf.SetLandscapeView();
                pdf.SetRowsPerPage(rowsPerPage);
                pdf.CreatePDF(model, headers, headersWidth);

                JOptionPane.showMessageDialog(null, "Operacje PDF został wygenerowany.",
                        "Generowanie PDF", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | DocumentException ex) {
                LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", ex.getMessage()
                        + "\n" + Arrays.toString(ex.getStackTrace()));
                startLogging.logToFile(text);
                JOptionPane.showMessageDialog(null, "Błąd podczas generowania operacji.",
                        "Generowanie PDF", JOptionPane.ERROR_MESSAGE);
            }

            try {
                String dir = System.getProperty("user.dir");

                File dirAgre = new File(dir);

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(dirAgre);
                }
            } catch (Exception ex) {
                LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", ex.getMessage()
                        + "\n" + Arrays.toString(ex.getStackTrace()));
                startLogging.logToFile(text);
            }
        }

    }

}
