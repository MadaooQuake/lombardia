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
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;        
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Font;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

//to support for Events
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//to get current date
import java.util.Calendar;

import lombardia2014.generators.PDFCreator;
import lombardia2014.generators.LombardiaLogger;
import lombardia2014.generators.DateTools;


//set initial date
import net.sourceforge.jdatepicker.impl.UtilDateModel;

//to open output dir
import java.io.File;
import java.awt.Desktop;

//to Generate PDF iText
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import lombardia2014.dataBaseInterface.MainDBQuierues;
        
public class StocktakingForm extends MenuElementsList {
    DateTools formDate;
    String formname = "Inwentaryzacja na dzień ";;
    DefaultTableModel model;
    JTable listSettlement = null;
    JScrollPane scrollPane = null;
    int selectRow = -1;
    int window_width = 860;
    int window_heigth = 500;
    int rows_per_page = 50;
    String output_file_name = "_be_changed.pdf";
    String[]  headers = {"L.p.", "Opis towaru", "Identyfikator", "Cena netto", "Wartość netto", "Data zakupu"};
    float[] headers_width = {0.9f, 5.0f, 3.0f, 3.0f, 3.0f, 2.6f};
    List<Integer> sumMe = new ArrayList();
    List<Integer> translateDateColumn = new ArrayList();
    String summaryText = "Podsumowanie";
    int summaryColumnIndex = 1;
    MainDBQuierues DB = new MainDBQuierues();
    String from,to;
    
    
    JDatePickerImpl datePicker = null;
    
    public StocktakingForm(String dateRange_) {
        formDate = new DateTools(new Date());
        output_file_name = formname+formDate.GetDateAsString()+".pdf";
        listSettlement = new JTable(new DefaultTableModel());    
        
        //Configure which column need to be summarize
        sumMe.add(3); //Cena netto
        sumMe.add(4); //Wartość netto
        
        //Configure column to date translate
        translateDateColumn.add(5); //Data zakupu
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
    
    @Override
    public void generatePanels(GridBagConstraints ct) {
        generateTable(new GridBagConstraints());
        generateButtons(new GridBagConstraints());
    }
        protected void generateTable(GridBagConstraints ct) {
        
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
    
    private void generateButtons(GridBagConstraints ct) {    
        JPanel buttonPanel = new JPanel(new GridBagLayout());

        TitledBorder title = BorderFactory.createTitledBorder(blackline, "Polecenia");
        title.setTitleJustification(TitledBorder.RIGHT);
        title.setBorder(blackline);
        buttonPanel.setBorder(title);
        buttonPanel.setPreferredSize(new Dimension(window_width - 100, 100));
        
        JButton printList = new JButton();
        printList.setText("Drukuj listę");
        printList.setPreferredSize(new Dimension(150, 26));
        printList.setFont(new Font("Dialog", Font.BOLD, 12));
        printList.addActionListener(new PrintList());
              
        GridBagConstraints[] actionPanel = new GridBagConstraints[] {
            new GridBagConstraints(),
            new GridBagConstraints(),
            new GridBagConstraints(),
        };
        actionPanel[0].insets = new Insets(0, 0, 0, 10);
        actionPanel[0].gridx = 2;
        actionPanel[0].gridy = 0;
        
        buttonPanel.add(printList, actionPanel[0]);
        
        JLabel label = new JLabel("Wybierz dzień:");
        label.setFont(new Font("Dialog",Font.BOLD,12));
        actionPanel[2].insets = new Insets(0, 0, 0, 10);
        actionPanel[2].gridx = 0;
        actionPanel[2].gridy = 0;
                
        buttonPanel.add(label, actionPanel[2]);
        
        UtilDateModel initDate = new UtilDateModel();
        initDate.setDate(formDate.GetYear(), formDate.GetMonth() - 1, formDate.GetDay());
        initDate.setSelected(true);
        JDatePanelImpl datePanel = new JDatePanelImpl(initDate);

        datePicker = new JDatePickerImpl(datePanel);
        datePicker.setPreferredSize(new Dimension(150, 27));
        datePicker.setFont(new Font("Dialog", Font.BOLD, 12));
        datePicker.addActionListener(new changeDate());
        
        actionPanel[1].insets = new Insets(0, 0, 0, 10);
        actionPanel[1].gridx = 1;
        actionPanel[1].gridy = 0;
                
        buttonPanel.add(datePicker, actionPanel[1]);
        
        ct.fill = GridBagConstraints.HORIZONTAL;
        ct.insets = new Insets(10, 10, 10, 10);
        ct.gridx = 0;
        ct.gridy = 0;
        ct.ipadx = 0;
        ct.ipady = 0;
        
        mainPanel.add(buttonPanel,ct);        
    }
    
    private DefaultTableModel getStocktaking() {
        DefaultTableModel result = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };
        
        List<HashMap<String, String>> data = DB.getStockTakingsFromDateRange(formDate.GetDateForDB());
        
//        if (data.size() > 0) {
//            headers.addAll( data.get(0).keySet() );
//        }
        
        //build headers
        for (String header : headers) {
            result.addColumn(header);
        }
                
        String[] sumarize = new String[headers.length];
        //build data
        for (HashMap<String, String> dbrow : data) {
            Object[] row = new Object[headers.length];
            for (int i = 0; i < headers.length; i++) {
                String dbdata = dbrow.get(headers[i]);
                if (translateDateColumn.contains(i)) {
                    try {
                        DateTools dateobj = new DateTools(dbdata);
                        dbdata = dateobj.GetDateAsString();
                    } catch (Exception ex) {
                        LombardiaLogger startLogging = new LombardiaLogger();
                        String text = startLogging.preparePattern("Error", ex.getMessage()
                            + "\n" + Arrays.toString(ex.getStackTrace()));
                        startLogging.logToFile(text);                        
                    }
                }
                row[i] = dbdata;
                if (dbdata == null) {
                    dbdata = "0";
                }
                if (sumMe.contains(i)) {
                    if (sumarize[i] == null) {
                        sumarize[i] = dbdata;
                    } else {
                        Float addMe = Float.parseFloat(sumarize[i])+Float.parseFloat(dbdata);
                        sumarize[i] = addMe.toString();
                    }
                }
            }
            result.addRow(row);
        }
        sumarize[summaryColumnIndex] = summaryText;
        result.addRow(sumarize);
        return result;
    }

    private void refresh() {
        
            //get new Data (refresh data)
            model = getStocktaking();
            model.fireTableDataChanged();
            
            //set title of window
            formFrame.setTitle(formname + formDate.GetDateAsString());
            
            //set label on frame
            titleBorder = BorderFactory.createTitledBorder(blackline, formname + formDate.GetDateAsString());
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
    
    
    // Buttons actions
    private class PrintList implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                PDFCreator pdf = new PDFCreator(output_file_name, formname + formDate.GetDateAsString());
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
    
    private class changeDate implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Date selectedDate = (Date) datePicker.getModel().getValue();
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            formDate.SetNewDate(selectedDate);
            Calendar newDate = Calendar.getInstance();
            newDate.setTime(selectedDate);
            output_file_name = formname+"_"+formDate.GetDateAsString()+".pdf";
            
            refresh();
        }
    }    
    
}
