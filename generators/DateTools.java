/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.generators;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.Arrays;
import lombardia2014.generators.LombardiaLogger;
/**
 *
 * @author jarek_000
 */
public class DateTools {
    Date loadedDate;
    int year;
    int month;
    int day;
    String dateHumanString;
    String dateDBString;
    Calendar calendarDate;
    
    public DateTools(Date inputDate) {
        loadedDate = inputDate;
        Process(loadedDate);
    }

    public DateTools(String rawData) throws ParseException {
        ProcessString(rawData);
    }
    
    public DateTools(ResultSet queryResult, String header) 
            throws SQLException, ParseException {
        String rawData = queryResult.getString( header );
        ProcessString(rawData);
    }
    
    
    public int GetYear() {
        return year;
    }
    
    public int GetMonth() {
        return month;
    }
    
    public int GetDay() {
        return day;
    }
    
    public String GetDateAsString() {
        return dateHumanString;
    }
    
    public String GetDateForDB() {
        return dateDBString;
    }
    
    public Calendar GiveMeSomeCalendar() {
        return calendarDate;
    }
    
    public void SetNewDate(Date newDate) {
        loadedDate = newDate;
        Process(loadedDate);       
    }
    
    public void SetNewDate(String newDate) throws ParseException {
        ProcessString(newDate);
    }
    
    private boolean isDBFormat(String data) {
        boolean result = false;
        if (data.length() == 8) {
            Pattern p = Pattern.compile("^[1-2][0-9][0-9][0-9][0-1][0-9][0-3][0-9]");
            Matcher m = p.matcher(data);
            result = m.matches();
        }
        
        return result;
    }
    
    private void ProcessString(String rawData) throws ParseException {
        if (isDBFormat(rawData)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            loadedDate = formatter.parse(rawData);
        } else {
            if (rawData.length() > 10) { //its mean that we have hours after date
                rawData = rawData.substring(0,10);
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            loadedDate = formatter.parse(rawData);
        }
        Process(loadedDate);      
    }
    
    private void Process(Date inputDate) { 
        calendarDate = Calendar.getInstance();
        calendarDate.setTime(inputDate);
        year = calendarDate.get(Calendar.YEAR);
        month = calendarDate.get(Calendar.MONTH) + 1;
        day = calendarDate.get(Calendar.DAY_OF_MONTH);
        dateHumanString = String.format("%02d.%02d.%04d", day, month, year);
        dateDBString = String.format("%04d%02d%02d", year, month, day);
    }
    
}
