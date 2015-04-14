/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.dataBaseInterface;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class ConnectDB {

    Connection c = null;
    CreateDB createTab = null;
    String dbFile = "lombardia.db";

    public ConnectDB() {
        conect();
        // file name from config
        //System.out.println("Opened database successfully");
    }

    public ConnectDB(String dbName) {
        dbFile = dbName;
        conect();
    }

    public Connection getConnection() {
        return c;
    }

    private void conect() {
        File f = new File(dbFile);
        try {
            Class.forName("org.sqlite.JDBC");
            // name use with config application
            c = DriverManager.getConnection("jdbc:sqlite:" + dbFile);

            if (f.length() == 0) {
                createTab = new CreateDB();
                createTab.createAuthTable(c);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
            System.exit(0);
        }
    }

}
