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

public class SettlementForm  extends MenuElementsList {
    String formname = "Rozliczenia ";
    String range = "roczne";
    DefaultTableModel model;
    JTable listSettlement = null;
    JScrollPane scrollPane = null;
    int selectRow = -1;
    int window_width = 660;
    int window_heigth = 500;
    ValueCalc rate = null;
    Map<Integer, HashMap>  objHeaders = new HashMap<>();

    public SettlementForm(String dateRange_) {
        if(dateRange_.equals("Month")) {
            range = "miesięczne";
        }
    }

    private void prepareHeaders() {
        objHeaders.put(1, (HashMap) buildHeader('','L.p.',''));
        objHeaders.put(2, (HashMap) buildHeader('Customers.Name','Imię',''));
        objHeaders.put(3, (HashMap) buildHeader('Customers.Surname','Nazwisko',''));
        objHeaders.put(4, (HashMap) buildHeader('Customers.Address','Adres',''));
        objHeaders.put(5, (HashMap) buildHeader('Agreements.Start_Date','Data pożyczki',''));
        objHeaders.put(6, (HashMap) buildHeader('Agreements.Value','Kwota pożyczki',''));
        objHeaders.put(7, (HashMap) buildHeader('','Odsetki',''));
        objHeaders.put(8, (HashMap) buildHeader('Items.Model','Opis zastawu',''));
        objHeaders.put(9, (HashMap) buildHeader('Items.Value','Wartość zastawu',''));
        objHeaders.put(10, (HashMap) buildHeader('Agreements.Stop_date','Termin zwrotu',''));
        objHeaders.put(11,(HashMap) buildHeader('Items.Sold_date','Data sprzedaży zastawu',''));
        objHeaders.put(12,(HashMap) buildHeader('','Kwota sprzedaży zastawu',''));
        objHeaders.put(13,(HashMap) buildHeader('','Kwota prowizji',''));

    }

    private Map<String, String> buildHeader(String dbName, String outputName, String size) {
        Map<String, String> tmpHead = new HashMap<>();
        tmpHead.put('size', size);
        tmpHead.put('outputName', outputName);
        tmpHead.put('dbName', dbName);

        return tmpHead;
    }

    
    private String[] getHeaders() {
        
        int headSize = objHeaders.size();
        String[] result = new String[];
        
        for (int i = 0; i < headSize; i++) {
            result.add( headSize.get(i + 1).get('outputName') );
        }

        return result;
    }

    private String[] getDbHeaders() {
        int headSize = objHeaders.size();
        String[] result = new String[];
        String elem;
        
        for (int i = 0; i < headSize; i++) {
            elem = headSize.get(i + 1).get('dbName');
            if (!elem.equals('')) {
                result.add( elem );
            }
        }

        return result;        
    }
    
    private String PrepareQuery() {
        String result = 'SELECT ';

        dbHeaders = getDbHeaders();
        for (int i = 0; i < dbHeaders.size(); i++) {
            result += dbHeaders[i];
            if (i < dbHeaders.size() - 1) { //skip for last element
                result += ','
            }
        }
        result = result + " FROM "
               + "Customers,"
               + "Items,"
               + "Agreements"
               + " WHERE "
                    + "Items.ID_AGREEMENT = Agreements.ID "
                    + "AND Agreements.ID_CUSTOMER = Customers.ID "
                    + ";"

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
        String[][][] convertedData = ConvertData(data, 5);
        
        Document document = new Document(PageSize.LETTER.rotate());
        PdfWriter.getInstance(document, new FileOutputStream("outputFile_itext.pdf"));
        document.open();
        for(int row = 0;row < convertedData.length;row++) {
            BaseFont bf = BaseFont.createFont("../fonts/arialuni.ttf", BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED);
            com.itextpdf.text.Font myFont = new com.itextpdf.text.Font(bf, 12);
            
            Paragraph p = new Paragraph(formname + range + " strona " + (row + 1), myFont);
            
            document.add(p);
            document.add(Chunk.NEWLINE);
            PdfPTable table = createPDFTable(convertedData[row], myFont);
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
        //table.setWidths(new float[]{0.6f, 1.4f, 0.8f,0.8f,1.8f,2.6f});
        
        //generate headers
        
        for (int i = 0; i < headers.length; i++) {
                PdfPCell cell = new PdfPCell(new Phrase(headers[i],myFont));
                table.addCell( cell );            
        }
        //generate content
        for (int row = 0;row < inputData.length;row++) {
            //we should filter out empty rows here
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

        precentCalc = new ValueCalc(); //precentCalc.lombardRate(val)
        
        String[] headers = getHeaders();
        for (int i=0; i<headers.length; i++) {
            result.addColumn(headers[i]);
        }
        
        try {
            QueryDB setQuerry = new QueryDB();
            Connection conDB = setQuerry.getConnDB();
            Statement stmt = conDB.createStatement();
            
            ResultSet queryResult = setQuerry.dbSetQuery( PrepareQuery() );
            
            int lp = 0;

            while (queryResult.next()) {
                lp++;
                float value = queryResult.getFloat( objHeaders.get(6).get('dbName')),
                float r = rate.lombardRate(value);
                Object[] data = {
                    lp,
                    queryResult.getString( objHeaders.get(2).get('dbName')),
                    queryResult.getString( objHeaders.get(3).get('dbName')),
                    queryResult.getString( objHeaders.get(4).get('dbName')),
                    queryResult.getString( objHeaders.get(5).get('dbName')),
                    value.toString(),
                    r.toString(),
                    queryResult.getString( objHeaders.get(8).get('dbName')),
                    queryResult.getString( objHeaders.get(9).get('dbName')),
                    queryResult.getString( objHeaders.get(10).get('dbName')),
                    queryResult.getString( objHeaders.get(11).get('dbName')),
                    "Not implemented",
                    "Not implemented"

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



       
    // get settlement list from database depreciated
    private DefaultTableModel getSettlement_old() {
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
            Statement stmt = conDB.createStatement();
            
            ResultSet queryResult = setQuerry.dbSetQuery(
                "SELECT "
                    + "Items.ID, Items.Model, Items.Band, Items.Type, "
                    + "Items.WEIGHT, Items.IMEI, Items.Value, Items.ATENCION, "
                    + "Items.ID_AGREEMENT, Category.Name as Category "
                    + "FROM Items, Category "
                    + "WHERE Items.ID_CATEGORY = Category.ID "
                    + "AND SOLD_DATE is NULL;"
            );
            
            while (queryResult.next()) {
                Object[] data = {queryResult.getString("ID"),
                    queryResult.getString("Model"),
                    queryResult.getString("Band"),
                    queryResult.getString("Type"),
                    queryResult.getString("ID_AGREEMENT"),
                    queryResult.getString("WEIGHT"),
                    queryResult.getString("IMEI"),
                    queryResult.getString("Value"),
                    queryResult.getString("ATENCION"),
                    queryResult.getString("Category")
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
