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
public class QueryDB {

    ConnectDB connDB = null;
    Connection c = null;
    Statement stmt = null;
    private ResultSet rs;

    // connect to db
    public QueryDB() {
        connDB = new ConnectDB();
        c = connDB.getConnection();
    }

    public Connection getConnDB() {
        return c;
    }

    // set query
    public ResultSet dbSetQuery(String query) {
        try {
            stmt = c.createStatement();
            System.out.println(query);
            if (query.contains("SELECT") && !query.contains("INSERT")) {
                rs = stmt.executeQuery(query);
            } else {
                stmt.executeUpdate(query);
            }
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
            rs = null;
        }

        return rs;
    }

    // close DB
    /**
     *
     */
    public void closeDB() {
        try {
            stmt.close();
            c.close();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
            System.exit(0);
        }
    }
}
