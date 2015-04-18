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
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import lombardia2014.dataBaseInterface.QueryDB;
import lombardia2014.generators.LombardiaLogger;

//to Generate PDF iText
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.Phrase;
import java.io.IOException;
import com.itextpdf.text.Chunk;

//to support for Events
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

//to calculate rates
import lombardia2014.ValueCalc;

//support headers
import java.util.HashMap;
import java.util.Map;

//to get current date
import java.util.Calendar;

public class SettlementForm  extends MenuElementsList {
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
    Map<Integer, HashMap<String, String>>  objHeaders = new HashMap<>();

    public SettlementForm(String dateRange_) {
        if(dateRange_.equals("Month")) {
            range = "miesięczne";            
            date_mask = "substr(Agreements.Stop_date,4,7)";
            int month = now.get(Calendar.MONTH);
            int year = now.get(Calendar.YEAR);
            if (month < 1) {
                month = 12;
                year --;
            }
            date_mask_value = String.format("%02d.%04d", month, year);
        }
        output_file_name = formname+"_"+range+".pdf";
    }

    private void prepareHeaders() {
        // to split to two separated config hash-maps (separatelly for DB and Report
        // Yes, i known convert Str to Float is stupied in this place ;P
        objHeaders.put(1, buildHeader("","","L.p.","0.9f"));
        objHeaders.put(2, buildHeader("Customers.Name","Name","Imię","3.0f"));
        objHeaders.put(3, buildHeader("Customers.Surname","Surname","Nazwisko","5.0f"));
        objHeaders.put(4, buildHeader("Customers.Address","Address","Adres","5.0f"));
        objHeaders.put(5, buildHeader("Agreements.Start_Date","Start_Date","Data pożyczki","2.6f"));
        objHeaders.put(6, buildHeader("Agreements.Value","Lean_Value","Kwota pożyczki","2.6f"));
        objHeaders.put(7, buildHeader("Items.Model || ' (' || Items.Band || ')'","Description","Opis zastawu","5.0f"));
        objHeaders.put(8, buildHeader("Items.Value","Item_Value","Wartość zastawu","2.6f"));
        objHeaders.put(9, buildHeader("Agreements.Stop_date","Stop_date","Termin zwrotu","2.6f"));
        objHeaders.put(10,buildHeader("","","Odsetki","1.8f"));
    }

    private HashMap<String, String> buildHeader(
            String dbName, 
            String shortcut, 
            String outputName, 
            String size) {
        HashMap<String, String> tmpHead = new HashMap<>();
        tmpHead.put("size", size);
        tmpHead.put("outputName", outputName);
        tmpHead.put("shortcut", shortcut);
        tmpHead.put("dbName", dbName);

        return tmpHead;
    }

    private float[] getHeadersWidth() {
        int headSize = objHeaders.size();
        float[] result = new float[headSize];
        
        for (int i = 0; i < headSize; i++) {
            try {
                result[i] = Float.parseFloat( objHeaders.get(i + 1).get("size") );
            } catch (NumberFormatException ex) {
                LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
                startLogging.logToFile(text);
                result[i] = 0f; //hide column ?
            }
        }

        return result;
    }
    
    private String[] getHeaders() {
        
        int headSize = objHeaders.size();
        String[] result = new String[headSize];
        
        for (int i = 0; i < headSize; i++) {
            result[i] = objHeaders.get(i + 1).get("outputName");
        }

        return result;
    }
    
    private String[] getShortDBHeaders() {
        return getDbHeaders(2);
    }

    private String[] getDbHeaders() {
        return getDbHeaders(0);
    }
    
    private String[] getDbHeaders(int variant) {
        // variant 0 - dbName as shortcat
        // variant 1 - only dbName
        // cariant 2 - only shortcat (dbName if shortcat not exists)
        int headSize = objHeaders.size();
        int emptySize = 0;
        String[] tmpArray = new String[headSize];
        String elem, shortcat, full;
        
        for (int i = 0; i < headSize; i++) {
            elem = objHeaders.get(i + 1).get("dbName");
            shortcat = objHeaders.get(i + 1).get("shortcut");
            if (elem.equals("")) {
                emptySize ++;
            }
            if (! shortcat.equals("")) {
                full = elem + " as " + shortcat;
            } else {
                shortcat = elem;
                full = elem;
            }
            switch (variant) {
                case 0:
                    tmpArray[i] = full;
                    break;
                case 1:
                    tmpArray[i] = elem;
                    break;
                case 2:
                    tmpArray[i] = shortcat; 
            }
        }
        
        String[] result = new String[headSize - emptySize];
        int magix = 0;
        for (int i = 0; i < headSize; i++) {
            if (!tmpArray[i].equals("")) {
                result[i - magix] = tmpArray[i];
            } else {
                magix ++;
            }
        }

        return result;        
    }
    
    private String PrepareQuery() {
        String result = "SELECT ";

        String[] dbHeaders = getDbHeaders();
            
        
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
                    + ";";

        return result;
    }

    @Override
    public void generateGui() {  
        
        formFrame.setSize(window_width, window_heigth);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle(formname + range);
        mainPanel = new JPanel(new GridBagLayout());
        titleBorder = BorderFactory.createTitledBorder(blackline, formname + range);
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        mainPanel.setBorder(titleBorder);
        
        prepareHeaders();
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
        printList.setPreferredSize(new Dimension(150, 40));
        printList.setFont(new Font("Dialog", Font.BOLD, 20));

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

        model = getSettlement();
        
        listSettlement = new JTable(model);
        
        //set width of form columns
        listSettlement.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        float[] widths = getHeadersWidth();
        for (int i=0; i < widths.length; i++) {
            int int_val = Math.round(widths[i] * 40);
            listSettlement.getColumnModel().getColumn(i).setPreferredWidth(int_val);
        }

        listSettlement.setAutoCreateRowSorter(true);
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
    
    private void CreatePDF(DefaultTableModel data) throws 
            DocumentException, IOException {
        String[][][] convertedData = ConvertData(data, rows_per_page);
        
        Document document = new Document(PageSize.LETTER.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(output_file_name));
        document.open();
        for(int row = 0;row < convertedData.length;row++) {
            BaseFont bf = BaseFont.createFont("Lombardia2014/fonts/arialuni.ttf", BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED);
            com.itextpdf.text.Font myFont = new com.itextpdf.text.Font(bf, 12);
            com.itextpdf.text.Font smallFont = new com.itextpdf.text.Font(bf, 6);
            
            Paragraph p = new Paragraph(formname + range + " strona " + (row + 1), myFont);
            
            document.add(p);
            document.add(Chunk.NEWLINE);
            PdfPTable table = createPDFTable(convertedData[row], smallFont);
            document.add(table);
            document.newPage();
        }
        document.close();
    }
    
    private PdfPTable createPDFTable(String[][] inputData, com.itextpdf.text.Font myFont) 
            throws DocumentException {

        String[] headers = getHeaders();
        PdfPTable table = new PdfPTable(headers.length);
        table.setWidthPercentage(90);
        // set relative columns width
        
        table.setWidths( getHeadersWidth() );
        
        //generate headers
        
        for (int i = 0; i < headers.length; i++) {
                PdfPCell cell = new PdfPCell(new Phrase(headers[i],myFont));
                table.addCell( cell );            
        }
        //generate content
        for (int row = 0;row < inputData.length;row++) {
            //hack to not present empty rows on the last page
            if ( inputData[row][0] == null ) {
                break;
            }
            for (int col = 0; col < inputData[row].length;col++) {
                PdfPCell cell = new PdfPCell(new Phrase(inputData[row][col],myFont));
                table.addCell( cell );
            }
        }
        return table;
    }
    
    private String[][][] ConvertData(DefaultTableModel model, int obj_per_page) {
        int row_count = model.getRowCount();
        int column_count = model.getColumnCount();
        int page_count = (row_count / obj_per_page) + 1;
               
        String[][][] convertedData = new String[page_count][obj_per_page][column_count];
        int new_row = 0;
        int page = 1;
        for(int row = 0;row <row_count;row++) {
            new_row = (row % obj_per_page);
            page = (row / obj_per_page) + 1;
            
	    for(int col = 0;col < column_count;col++) {
                if (model.getValueAt(row, col) != null) {
                    convertedData[page-1][new_row][col] = model.getValueAt(row, col).toString();
                } else {
                    convertedData[page-1][new_row][col] = "";
                }
	    }
	}
        
        // Fill tail of table with empty cells
//        for (;new_row < 5; new_row++) {
//            for (int col = 0; col < column_count;col++) {
//                convertedData[page-1][new_row][col] = "";
//            }
//        }
        
        return convertedData;
    }


    private DefaultTableModel getSettlement() {
        DefaultTableModel result = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };

        
        String[] headers = getHeaders();
        for (int i=0; i<headers.length; i++) {
            result.addColumn(headers[i]);
        }
        
        try {
            QueryDB setQuerry = new QueryDB();
            Connection conDB = setQuerry.getConnDB();
            //Statement stmt = conDB.createStatement();
            
            ResultSet queryResult = setQuerry.dbSetQuery( PrepareQuery() );
            int lp = 0;
            
            String[] SQLHeaders = getShortDBHeaders();

            while (queryResult.next()) {
                lp++;
                
                float value = queryResult.getFloat( SQLHeaders[4] );
                String stopdate = queryResult.getString( SQLHeaders[7] );
                String startdate = queryResult.getString( SQLHeaders[3] );
                rate.dailyEarn(stopdate, startdate, value);
                float r = rate.lombardRate(value);
                                
                Object[] data = {
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
                
                result.addRow(data);
            }

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
        
        return result;
    }  
    
    // Buttons actions
    private class PrintList implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                CreatePDF(getSettlement());
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
        }
    }
}
