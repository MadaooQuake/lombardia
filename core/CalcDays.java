package lombardia2014.core;

import java.util.Calendar;
import java.util.GregorianCalendar;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Domek
 */
public class CalcDays {

    int year = 0, mounthStart = 0, mountEnd = 0, dffYears = 0, diffMounth = 0,
            daysCount = 0, day = 0;
    String startDate, endDate;
    Calendar mycal = null;
    GregorianCalendar cal
            = (GregorianCalendar) GregorianCalendar.getInstance();

    /**
     * example calc start date: 10.09.2014 stop date: 05.03.2015
     *
     * first i check years, if i have yearas > > but < 1 then i calculate yow
     * many days i have to end years next i calculate how many days i have in
     * new years to state date
     *
     * ans all and end calculate :D
     *
     */
    /**
     *
     * @param startDate_
     * @param endDate_
     */
    public CalcDays(String endDate_, String startDate_) {
        if(endDate_.contains(" ") && startDate_.contains(" ")) {
            endDate = endDate_.substring(0, endDate_.indexOf(" "));
            startDate = startDate_.substring(0, startDate_.indexOf(" ")); 
        } else {
            endDate = endDate_;
            startDate = startDate_;
        }
        
        if(endDate.equals(startDate)) {
            daysCount = 0;
        } else if (startDate.length() == 10 && endDate.length() == 10) {
            // now i find year :D i create static position 12,45,

            year = Integer.parseInt(startDate.substring(6, startDate.length()));
            // calculate  years diff
            dffYears = Integer.parseInt(endDate.substring(6, endDate.length()))
                    - Integer.parseInt(startDate.substring(6, startDate.length()));

            // itnerface years :D
            mycal = new GregorianCalendar(year, 1, 1);
            if (dffYears >= 1) {
                daysCount = CalcDaysInYears(dffYears);
            } else {
                daysCount = calculateDaysInThiYears(dffYears);
            }

        }
    }

    /**
     * @return 
     * @see main calculator :D
     */
    public int calculateDays() {
        return daysCount;
    }

    // check how many days in mounth
    /**
     *
     * @return
     */
    int calculateDaysInThiYears(int howMany) {

        // check actually date
        int eDay = Integer.parseInt(endDate.substring(0, 2));
        int sDay = Integer.parseInt(startDate.substring(0, 2));
        //mounth
        int eMounth = Integer.parseInt(endDate.substring(3, 5));
        int sMounth = Integer.parseInt(startDate.substring(3, 5));
        // if we have in that same year 

        if (eMounth > sMounth) {
            for (int i = sMounth + 1; i < eMounth; i++) {
                mycal = new GregorianCalendar(year, i, 1);
                int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                day += daysInMonth;
            }
            
            // now time to calculate currenth mounth 
            mycal = new GregorianCalendar(year, sMounth, 1);
            int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
            day += (daysInMonth - sDay);
            
            int days = Integer.parseInt(endDate.substring(0, 2));
            day += days;
            
        } else if (eMounth == sMounth) {
            
            int days = Integer.parseInt(endDate.substring(0, 2));
            day = (eDay - sDay);
        }
        return day;
    }

    // check how many days in year
    /**
     *
     */
    private int CalcDaysInYears(int howMany) {
        if (howMany > 1) {
            for (int i = year + 1; i < year + howMany; i++) {
                boolean isLeapYear = cal.isLeapYear(i);
                if (isLeapYear == true) {
                    day += 366;
                } else {
                    day += 365;
                }
            }

            // last year and another loop :(
            for (int i = 0; i < Integer.parseInt(endDate.substring(3, 5)) - 1;
                    i++) {
                mycal = new GregorianCalendar(year + howMany, i, 1);
                int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                day += daysInMonth;
            }
            // and last :D
            int days = Integer.parseInt(endDate.substring(0, 2));
            day += days;

            // now i calculate how many days in start :D
            int sDay = Integer.parseInt(startDate.substring(0, 2));
            int sMounth = Integer.parseInt(endDate.substring(3, 5));

            mycal = new GregorianCalendar(year, sMounth, 1);
            int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
            day += (daysInMonth - sDay);

            // mounth calc
            for (int i = sMounth + 1; i < 12; i++) {
                mycal = new GregorianCalendar(year, i, 1);
                daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                day += daysInMonth;
            }

        } else {
            for (int i = 0; i < Integer.parseInt(endDate.substring(3, 5)) - 1;
                    i++) {
                mycal = new GregorianCalendar(year + howMany, i, 1);
                int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                day += daysInMonth;
            }
            int days = Integer.parseInt(endDate.substring(0, 2));
            day += days;
        }
        return day;
    }

    // calculate days in mounth
}
