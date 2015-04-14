/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014;

import lombardia2014.dataBaseInterface.QueryDB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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

    public SelfCalc() {
        // connect to db and get value
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();

            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT VALUE FROM Safe;");

            while (queryResult.next()) {
                value = queryResult.getFloat("VALUE");
            }
            setQuerry.closeDB();

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
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
        float tmpVal = value - setValue;
        boolean chioice = false;
        if (tmpVal >= 0) {
            chioice = true;
        } else {
            JOptionPane.showMessageDialog(frameSet,
                    "Brak wystarczających środków w kasie "
                    + "do przeprowadzenia tej transakcji",
                    "Brak wystarczających środków w kasie",
                    JOptionPane.ERROR_MESSAGE);
        }
        return chioice;
    }
// update self in db

    /**
     *
     * @param frameSet
     */
    public void updateValue() {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();

            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("UPDATE Safe SET VALUE ="
                    + value + ";");

            setQuerry.closeDB();

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    public void addToSelf(float setValue) {
        value += setValue;
        updateValue();
    }

    public void delFtomSelf(JFrame frameSet, float setValue) {
        boolean check = chackValue(frameSet, setValue);
        if (check == true) {
            value -= setValue;
            updateValue();
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

}
