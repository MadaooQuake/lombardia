/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.core;

import lombardia2014.dataBaseInterface.QueryDB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import lombardia2014.dataBaseInterface.SafeOperations;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class SelfCalc {

    float value = 0;
    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    SafeOperations safe = new SafeOperations();

    public SelfCalc() {
        // connect to db and get value
        value = safe.getValue();
    }

    // 1 chceck self 
    public void chackValue(JFrame frameSet) {
        if (value == 0) {
            JOptionPane.showMessageDialog(frameSet,
                    "Wpłać pieniądze do kasy",
                    "Kasa jest Pusta",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean chackValue(JFrame frameSet, float setValue) {      
        return true;
    }
// update self in db

    public void addToSelf(float setValue) {
        value += setValue;
        safe.updateValue(value);
    }

    public void delFtomSelf(JFrame frameSet, float setValue) {
        boolean check = chackValue(frameSet, setValue);
        if (check == true) {
            value -= setValue;
            safe.updateValue(value);
        }
    }

    public float getValue() {
        value *= 100;
        value = Math.round(value);
        value /= 100;
        return value;
    }

    public void setValue(float val) {
        value = val;
    }
    
    public void updateValue() {
        safe.updateValue(value);
    }

}
