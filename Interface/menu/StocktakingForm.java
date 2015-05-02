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
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;        
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.SQLException;
import java.sql.ResultSet;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import lombardia2014.generators.LombardiaLogger;

//to support for Events
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//to get current date
import java.util.Calendar;

public class StocktakingForm extends SettlementForm {
    int month = now.get(Calendar.MONTH) + 1;
    int year = now.get(Calendar.YEAR);
    int d = now.get(Calendar.DAY_OF_MONTH);
    JDatePickerImpl datePicker = null;
    
    public StocktakingForm(String dateRange_) {
        super(dateRange_);
        rotate = 0;
        rows_per_page = 50;
        formname = "Inwentaryzacja na dzień ";
        range = String.format("%02d.%02d.%04d", d, month, year);
        date_mask_value = range;

        output_file_name = formname+"_"+range+".pdf";
    }
    
    @Override
    protected void prepareHeaders() {
        // to split to two separated config hash-maps (separatelly for DB and Report
        // Yes, i known convert Str to Float is stupied in this place ;P
        objHeaders.put(1, buildHeader("","","L.p.","0.9f"));
        objHeaders.put(2, buildHeader("Items.Model || ' (' || Items.Band || ')'","Description","Opis towaru","5.0f"));
        objHeaders.put(3, buildHeader("Items.ID","ID","Identyfikator","3.0f"));
        objHeaders.put(4, buildHeader("Items.Value","Value","Cena netto","3.0f"));
        objHeaders.put(5, buildHeader("","","Wartość netto","3.0f"));
        objHeaders.put(6, buildHeader("Agreements.Start_date","Start_date","Data zakupu","2.6f"));
    }
    
    @Override
    protected String PrepareQuery() {
        String result = "SELECT ";

        String[] dbHeaders = getDbHeaders();
            
        
        for (int i = 0; i < dbHeaders.length; i++) {
            result += dbHeaders[i];
            if (i < dbHeaders.length - 1) { //skip for last element
                result += ",";
            }
        }
        result = result + " FROM "
               + "Items,"
               + "Agreements"
               + " WHERE "
                    + "Items.ID_AGREEMENT = Agreements.ID "
                    + "AND ( substr(Agreements.Stop_date,7,4) < '" + String.format("%04d", year) + "' or "
                    + "(substr(Agreements.Stop_date,7,4) = '" + String.format("%04d", year) +"' and substr(Agreements.Stop_date,4,2) < '" + String.format("%02d", month) + "') or"
                    + "(substr(Agreements.Stop_date,4,7) = '" + String.format("%02d", month) +"."+String.format("%04d", year)+"' and substr(Agreements.Stop_date,1,2) < '" + String.format("%02d", d) + "') )"
                
                    + "AND ( Items.Sold_date is null or "

                    + "substr(Items.Sold_date,7,4) > '" +  String.format("%04d", year) + "' or "
                    + "(substr(Items.Sold_date,7,4) = '" + String.format("%04d", year) +"' and substr(Items.Sold_date,4,2) > '" + String.format("%02d", month) + "') or"
                    + "(substr(Items.Sold_date,4,7) = '" + String.format("%02d", month) +"."+ String.format("%04d", year) +"' and substr(Items.Sold_date,1,2) > '" + String.format("%02d", d) + "') )"
                    + ";";

        return result;
    }
    
    protected Object[] buildData(ResultSet queryResult, int lp) throws SQLException {
        String[] SQLHeaders = getShortDBHeaders();
        
        Object[] result = {
                    lp,
                    queryResult.getString( SQLHeaders[0] ),
                    queryResult.getString( SQLHeaders[1] ),
                    queryResult.getString( SQLHeaders[2] ),
                    queryResult.getString( SQLHeaders[2] ),
                    queryResult.getString( SQLHeaders[3] ),
                    };
        return result;
    }
    
    @Override
    protected void generateButtons(GridBagConstraints ct) {    
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
        printList.setFont(new Font("Dialog", Font.BOLD, 12));

        printList.addActionListener(new PrintList());
              
        
        GridBagConstraints actionPanel = new GridBagConstraints();
        actionPanel.insets = new Insets(0, 0, 0, 10);
        actionPanel.gridx = 0;
        actionPanel.gridy = 0;
        
        buttonPanel.add(printList, actionPanel);
        UtilDateModel model = new UtilDateModel();
        model.setDate(year, month - 1, d);
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        datePicker = new JDatePickerImpl(datePanel);
        datePicker.setPreferredSize(new Dimension(150, 40));
        datePicker.setFont(new Font("Dialog", Font.BOLD, 12));
        datePicker.addActionListener(new changeDate());
        
        GridBagConstraints actionPanel1 = new GridBagConstraints();
        actionPanel1.insets = new Insets(0, 0, 0, 10);
        actionPanel1.gridx = 1;
        actionPanel1.gridy = 0;
                
        buttonPanel.add(datePicker, actionPanel1);
        
        ct.fill = GridBagConstraints.HORIZONTAL;
        ct.insets = new Insets(10, 10, 10, 10);
        ct.gridx = 0;
        ct.gridy = 0;
        ct.ipadx = 0;
        ct.ipady = 0;
        
        mainPanel.add(buttonPanel,ct);        
    }
    // Buttons actions
    protected class changeDate implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Date selectedDate = (Date) datePicker.getModel().getValue();
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            range = dateFormat.format(selectedDate);
            Calendar newDate = Calendar.getInstance();
            newDate.setTime(selectedDate);
            year = newDate.get(Calendar.YEAR);
            month = newDate.get(Calendar.MONTH);
            d = newDate.get(Calendar.DAY_OF_MONTH);
            date_mask_value = range;
            output_file_name = formname+"_"+range+".pdf";
            
            //I need to refresh page but i don't known how at this moment
        }
    }    
    
}
