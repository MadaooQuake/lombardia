/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.menu;

/**
 *
 * @author jarek_000
 */
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;        
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import lombardia2014.dataBaseInterface.MainDBQuierues;

import lombardia2014.generators.LombardiaLogger;
import lombardia2014.generators.PDFCreator;

//to Generate PDF iText
import com.itextpdf.text.DocumentException;
import java.io.IOException;

//to support for Events
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

//to get current date
import java.util.Calendar;

//to open output dir
import java.io.File;
import java.awt.Desktop;
import java.util.HashMap;
import java.util.List;

public class SettlementForm extends MenuElementsList {
    Calendar now = Calendar.getInstance();
    String formname = "Rozliczenia ";
    String range = "roczne";
    DefaultTableModel model;
    JTable listSettlement = null;
    JScrollPane scrollPane = null;
    int selectRow = -1;
    int window_width = 860;
    int window_heigth = 500;
    int rows_per_page = 20;
    String output_file_name = "_be_changed.pdf";
    String[] headers = {"L.p.", "Imię", "Nazwisko", "Adres", "Data pożyczki", "Kwota pożyczki", "Opis zastawu", "Wartość zastawu", "Termin zwrotu", "Odsetki"};
    float[] headers_width = {0.9f, 3.0f, 5.0f, 5.0f, 2.6f, 2.6f, 5.0f, 2.6f, 2.6f, 1.8f};
    MainDBQuierues DB = new MainDBQuierues();
    String from,to;

    public SettlementForm(String dateRange_) {
        int month = now.get(Calendar.MONTH) + 1;
        int year = now.get(Calendar.YEAR);
        int days = now.getActualMaximum(Calendar.DAY_OF_MONTH);
        from = String.format("%04d%02d%02d", year, 1, 1);
        to = String.format("%04d%02d%02d", year, 12, 31);
        
        if(dateRange_.equals("Month")) {
            range = "miesięczne";            
            from = String.format("%04d%02d%02d", year, month, 1);
            to = String.format("%04d%02d%02d", year, month, days);
        }
        output_file_name = formname + "_" + range + ".pdf";
        listSettlement = new JTable(new DefaultTableModel());
    }

    private void refresh() {
        
            //get new Data (refresh data)
            model = getSettlement();
            model.fireTableDataChanged();
            
            //set title of window
            formFrame.setTitle(formname + range);
            
            //set label on frame
            titleBorder = BorderFactory.createTitledBorder(blackline, formname + range);
            titleBorder.setTitleJustification(TitledBorder.RIGHT);
            mainPanel.setBorder(titleBorder);
            
            //fill table
            listSettlement.setModel(model);
                    
            //set width of form columns
            listSettlement.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            for (int i=0; i < headers_width.length; i++) {
                int int_val = Math.round(headers_width[i] * 40);
                listSettlement.getColumnModel().getColumn(i).setPreferredWidth(int_val);
            }

            listSettlement.setAutoCreateRowSorter(true);
    }
    
    @Override
    public void generateGui() {
        
        formFrame.setSize(window_width, window_heigth);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        mainPanel = new JPanel(new GridBagLayout());

        generatePanels(new GridBagConstraints());

        formFrame.add(mainPanel);
        formFrame.setVisible(true);
    }
    
    private void generateButtons(GridBagConstraints ct) {    
        JPanel buttonPanel = new JPanel(new GridBagLayout());

        TitledBorder title = BorderFactory.createTitledBorder(blackline, "Polecenia");
        title.setTitleJustification(TitledBorder.RIGHT);
        title.setBorder(blackline);
        buttonPanel.setBorder(title);
        buttonPanel.setPreferredSize(new Dimension(window_width - 100, 100));
        
        JButton printList = new JButton();
        printList.setText("Drukuj listę");
        //addU.addActionListener(new AddButtonAction());
        printList.setPreferredSize(new Dimension(150, 25));
        printList.setFont(new Font("Dialog", Font.BOLD, 12));

        printList.addActionListener(new PrintList());
        GridBagConstraints actionPanel = new GridBagConstraints();
        actionPanel.insets = new Insets(0, 0, 0, 10);
        actionPanel.gridx = 0;
        actionPanel.gridy = 0;
        
        buttonPanel.add(printList, actionPanel);
        
        ct.fill = GridBagConstraints.HORIZONTAL;
        ct.insets = new Insets(10, 10, 10, 10);
        ct.gridx = 0;
        ct.gridy = 0;
        ct.ipadx = 0;
        ct.ipady = 0;
        
        mainPanel.add(buttonPanel,ct);        
    }
    
    @Override
    public void generatePanels(GridBagConstraints ct) {
        generateTable(new GridBagConstraints());
        generateButtons(new GridBagConstraints());
    }
    
    private void generateTable(GridBagConstraints ct) {
        
        refresh();
        scrollPane = new JScrollPane(listSettlement);
        listSettlement.setFillsViewportHeight(true);

        scrollPane.setPreferredSize(new Dimension(window_width - 100, window_heigth - 200));

        ct.fill = GridBagConstraints.HORIZONTAL;
        ct.insets = new Insets(10, 10, 10, 10);
        ct.gridx = 0;
        ct.gridy = 1;
        ct.ipadx = 8;
        ct.ipady = 8;
        mainPanel.add(scrollPane, ct);
    }

    private DefaultTableModel getSettlement() {
        DefaultTableModel result = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };
      
        List<HashMap<String, String>> data = DB.getSettlementsFromDateRange(from, to);
               
        //build headers
        for (String header : headers) {
            result.addColumn(header);
        }
                
        //build data
        for (HashMap<String, String> dbrow : data) {
            Object[] row = new Object[headers.length];
            for (int i=0; i < headers.length; i++) {
                row[i] = dbrow.get(headers[i]);
            }
            result.addRow(row);
        }
        
        return result;
    }
    
    // Buttons actions
    private class PrintList implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                PDFCreator pdf = new PDFCreator(output_file_name, formname + range);
                pdf.SetLandscapeView();
                pdf.SetRowsPerPage(rows_per_page);
                pdf.CreatePDF(model, headers, headers_width);
                JOptionPane.showMessageDialog(null, "Raport PDF został wygenerowany.",
                        "Generowanie PDF", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | DocumentException ex) {
                LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
                startLogging.logToFile(text);
                JOptionPane.showMessageDialog(null, "Błąd podczas generowania raportu.", 
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
