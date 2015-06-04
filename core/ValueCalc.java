/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.core;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Domek
 */
public class ValueCalc {

    ConfigRead readParam = null;
    float ans, minFee, dailyProfit, handlingPay, discount = 0, storagePay = 0;
    CalcDays countDays = null;
    int days = 0;
    float all = 0, rrso = 0;
// this is the constructor

    public ValueCalc() {
        readParam = new ConfigRead();
        readParam.readFile();
    }

    /**
     * @param startDate
     * @param stopDate
     * @param value
     * @return
     * @see calculate daily earnings from payment
     *
     * example 1000 * 17 dni * 0,9% = 153 zł where in 0.9% = handlingPayment +
     * storage
     */
    public float dailyEarn(String stopDate, String startDate, float value) {
        countDays = new CalcDays(stopDate, startDate);
        days = countDays.calculateDays();

        float percent = readParam.getDailyProfit();

        dailyProfit = (value * days);
        dailyProfit = dailyProfit * percent / 100;
        if (dailyProfit < readParam.getMinFee()) {
            dailyProfit = readParam.getMinFee();
        }

        if (dailyProfit < handlingPayment(value)) {
            dailyProfit += handlingPayment(value);
        }
        dailyProfit *= 100;
        dailyProfit = Math.round(dailyProfit);
        dailyProfit /= 100;
        return dailyProfit;
    }

    /**
     * Daily profit
     * @param value
     */
    public double dailyEarn(float value) {
        double dailyEarn = 0.0;

        float percent = readParam.getDailyProfit();

        dailyEarn = value * percent / 100;

        dailyEarn *= 100;
        dailyEarn = Math.round(dailyEarn);
        dailyEarn /= 100;

        return dailyEarn;
    }

    /**
     * @param value
     * @return @see calculate handling fee
     *
     * example calculate: 1000*5%=50 zł // 50zł is handling fee
     */
    public float handlingPayment(float value) {
        handlingPay = readParam.getManFee();
        ans = value;
        float val = (value * (handlingPay / 100));
        val *= 100;
        val = Math.round(val);
        val /= 100;
        return val;
    }

    /**
     * @param value
     * @return @see lombard rate
     *
     * // 1000zł * 17dni * 0,055%=9,35 zł
     */
    public float lombardRate(float value) {
        float rate = readParam.getLombardRate();
        float val = (value * days) * rate / 100;
        val *= 100;
        val = Math.round(val);
        val /= 100;
        return val;
    }

    /**
     * @param value
     * @return @see interest
     *
     * example calculate
     */
    public float storagePayment(float value) {
        float val = (dailyProfit - lombardRate(value)
                - (value * (handlingPay / 100)));
        val *= 100;
        val = Math.round(val);
        val /= 100;
        val = Math.abs(val);
        storagePay = val;
        return val;
    }

    /**
     * @return @see rro
     *
     * stopa from xml
     *
     * rom config
     *
     */
    public float RROCalc() {
        return readParam.getRSO();
    }

    /**
     * @param value
     * @return
     * @all payment
     */
    public float allPayment(float value) {
        all = (value + dailyProfit) - discount;
        all *= 100;
        all = Math.round(all);
        all /= 100;
        return all;
    }

    /**
     * @param value
     * @return @see RRSOCalc
     *
     * example
     *
     */
    public float RRSOCalc(float value) {
        // calculate power
        if (days == 0) {
            days = 1;
        }
        double daysInYear = 365 / days;
        rrso = (float) (value * daysInYear);
        rrso = all / rrso;
        rrso = (float) Math.pow(rrso, -1);

        return rrso;

    }

    /**
     * @param value
     * @param form_
     * @return
     * @see calculate discount
     */
    public float discountCalc(float value, JFrame form_) {
        // calculate power
        // value in percent 
        
        discount = value;
        // calculate discount 
        discount = (discount * dailyProfit)/100;
        
        if (storagePay < discount) {
            discount = 0;
            // notyfication
            JOptionPane.showMessageDialog(form_,
                    "Zbyt wysoki Rabat",
                    "Zbyt wysoki Rabat - zmień wartość, bo w przeciwnym wypadku"
                    + "wyniesie on 0!",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            discount *= 100;
            discount = Math.round(discount);
            discount /= 100;
        }

        return discount;
    }
}
