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
import javax.swing.JLabel;
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

//to support for Events
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//to get current date
import java.util.Calendar;


//set initial date
import net.sourceforge.jdatepicker.impl.UtilDateModel;
        
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
        objHeaders.put(6, buildHeader("Agreements.Stop_date","Buy_Date","Data zakupu","2.6f"));
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
                    + " union all " 
                    + "SELECT Items.Model || ' (' || Items.Band || ')' as Description, "
                    + "Items.ID as ID, "
                    + "Items.Value as Value,"
                    + "Items.Buy_Date as Buy_Date FROM Items WHERE Buy_Date is not null AND "
                    + "( substr(Buy_Date,7,4) < '" + String.format("%04d", year) + "' or "
                    + "(substr(Buy_Date,7,4) = '" + String.format("%04d", year) +"' and substr(Buy_Date,4,2) < '" + String.format("%02d", month) + "') or"
                    + "(substr(Buy_Date,4,7) = '" + String.format("%02d", month) +"."+String.format("%04d", year)+"' and substr(Buy_Date,1,2) < '" + String.format("%02d", d) + "') )"
                
                    + "AND ( Items.Sold_date is null or "                
                
                    + "substr(Items.Sold_date,7,4) > '" +  String.format("%04d", year) + "' or "
                    + "(substr(Items.Sold_date,7,4) = '" + String.format("%04d", year) +"' and substr(Items.Sold_date,4,2) > '" + String.format("%02d", month) + "') or"
                    + "(substr(Items.Sold_date,4,7) = '" + String.format("%02d", month) +"."+ String.format("%04d", year) +"' and substr(Items.Sold_date,1,2) > '" + String.format("%02d", d) + "') )"
                    + ";";

        return result;
    }
    
    @Override
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
        initDate.setDate(year, month - 1, d);
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
            month = newDate.get(Calendar.MONTH) + 1;
            d = newDate.get(Calendar.DAY_OF_MONTH);
            date_mask_value = range;
            output_file_name = formname+"_"+range+".pdf";
            
            refresh();
        }
    }    
    
}
