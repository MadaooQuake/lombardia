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
import java.sql.SQLException;
import java.sql.ResultSet;

import lombardia2014.dataBaseInterface.QueryDB;
import lombardia2014.generators.LombardiaLogger;
import lombardia2014.generators.PDFCreator;
import lombardia2014.generators.HeadersHelper;

//to Generate PDF iText
import com.itextpdf.text.DocumentException;
import java.io.IOException;

//to support for Events
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

//to calculate rates
import lombardia2014.core.ValueCalc;

//to get current date
import java.util.Calendar;

//to open output dir
import java.io.File;
import java.awt.Desktop;

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
    String date_mask = "substr(Agreements.Stop_date,7,4)";
    String date_mask_value = Integer.toString( now.get(Calendar.YEAR) );
    String output_file_name = "_be_changed.pdf";
    ValueCalc rate = new ValueCalc();
    HeadersHelper Headers;

    public SettlementForm(String dateRange_) {
        int month = now.get(Calendar.MONTH) + 1;
        int year = now.get(Calendar.YEAR);
        Headers = new HeadersHelper(10);
        
        if(dateRange_.equals("Month")) {
            range = "miesięczne";            
            date_mask = "substr(Agreements.Stop_date,4,7)";
            if (month < 1) {
                month = 12;
                year --;
            }
            date_mask_value = String.format("%02d.%04d", month, year);
        }
        output_file_name = formname+"_"+range+".pdf";
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
            float[] widths = Headers.getHeadersWidth();
            for (int i=0; i < widths.length; i++) {
                int int_val = Math.round(widths[i] * 40);
                listSettlement.getColumnModel().getColumn(i).setPreferredWidth(int_val);
            }

            listSettlement.setAutoCreateRowSorter(true);
    }
    
    private void prepareHeaders() {
        Headers.BuildHeader("","","L.p.",0.9f);
        Headers.BuildHeader("Customers.Name","Name","Imię",3.0f);
        Headers.BuildHeader("Customers.Surname","Surname","Nazwisko",5.0f);
        Headers.BuildHeader("Customers.Address","Address","Adres",5.0f);
        Headers.BuildHeader("Agreements.Start_Date","Start_Date","Data pożyczki",2.6f);
        Headers.BuildHeader("Agreements.Value","Lean_Value","Kwota pożyczki",2.6f);
        Headers.BuildHeader("GROUP_CONCAT(Items.Model || IFNULL(' (' || Items.Band || ') ', ', '))","Description","Opis zastawu",5.0f);
        Headers.BuildHeader("Items.Value","Item_Value","Wartość zastawu",2.6f);
        Headers.BuildHeader("Agreements.Stop_date","Stop_date","Termin zwrotu",2.6f);
        Headers.BuildHeader("","","Odsetki",1.8f);
    }

    private String PrepareQuery() {
        String result = "SELECT ";

        String[] dbHeaders = Headers.getDbHeaders();
            
        
        for (int i = 0; i < dbHeaders.length; i++) {
            result += dbHeaders[i];
            if (i < dbHeaders.length - 1) { //skip for last element
                result += ",";
            }
        }
        result = result + " FROM "
               + "Customers,"
               + "Items,"
               + "Agreements"
               + " WHERE "
                    + "Items.ID_AGREEMENT = Agreements.ID "
                    + "AND Agreements.ID_CUSTOMER = Customers.ID "
                    + "AND " + date_mask + " = '" + date_mask_value + "' "
                    + " GROUP BY Agreements.ID;";

        return result;
    }

    @Override
    public void generateGui() {
        prepareHeaders();
        
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

        String[] headers = Headers.getHeaders();
        for (int i=0; i<headers.length; i++) {
            result.addColumn(headers[i]);
        }
        
        try {
            QueryDB setQuerry = new QueryDB(); 
            ResultSet queryResult = setQuerry.dbSetQuery( PrepareQuery() );
            int lp = 0;
            
            while (queryResult.next()) {
                lp++;
                result.addRow(buildData(queryResult, lp));
            }

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
        
        return result;
    }
    
    private Object[] buildData(ResultSet queryResult, int lp) throws SQLException {
        String[] SQLHeaders = Headers.getShortDBHeaders();
        
        float value = queryResult.getFloat( SQLHeaders[4] );
        String stopdate = queryResult.getString( SQLHeaders[7] );
        String startdate = queryResult.getString( SQLHeaders[3] );
        rate.dailyEarn(stopdate, startdate, value);
        float r = rate.lombardRate(value);   
        
        Object[] result = {
                    lp,
                    queryResult.getString( SQLHeaders[0] ),
                    queryResult.getString( SQLHeaders[1] ),
                    queryResult.getString( SQLHeaders[2] ),
                    startdate,
                    Float.toString(value),
                    queryResult.getString( SQLHeaders[5] ),
                    queryResult.getString( SQLHeaders[6] ),
                    stopdate,
                    Float.toString(r),
                    };
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
                pdf.CreatePDF(model, Headers);
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
