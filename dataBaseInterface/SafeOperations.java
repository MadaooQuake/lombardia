/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.dataBaseInterface;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class SafeOperations {

    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;

    public float getValue() {
        float value = 0;
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
        return value;
    }
    
      /**
     *
     * @param frameSet
     */
    public void updateValue(Float newValue) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();

            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("UPDATE Safe SET VALUE ="
                    + newValue + ";");

            setQuerry.closeDB();

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }
    
    
}
