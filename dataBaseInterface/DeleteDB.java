/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.dataBaseInterface;

import javax.swing.JOptionPane;
import lombardia2014.core.SetLocatnion;
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
public class DeleteDB {

    SetLocatnion polishSign = new SetLocatnion();
    private ResultSet queryResult = null;
    private QueryDB setQuerry = null;
    private Connection conDB = null;
    private Statement stmt = null;

    // only two methods 
    // warning message 
    public void deleteWarning() {
        int selectedOption = JOptionPane.showConfirmDialog(null,
                "Czy na pewno chcesz usunąc bazę danych ?",
                "Usuwanie bazy danych!",
                JOptionPane.WARNING_MESSAGE);
        if (selectedOption == JOptionPane.OK_OPTION) {
            deleteDB();
            selectedOption = JOptionPane.showConfirmDialog(null,
                    "Baza została usunięta, przez to aplikacja się wyłączy.",
                    "Usuwanie bazy danych!",
                    JOptionPane.PLAIN_MESSAGE);
            if (selectedOption == JOptionPane.OK_OPTION) {
                System.exit(0);

            }
        }

    }

    // truncate 
    public void deleteDB() {
        try {
            ConnectDB conn = new ConnectDB();
        // drop tables

            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();

            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("DROP TABLE Auth;");
            queryResult = setQuerry.dbSetQuery("DROP TABLE Users;");
            queryResult = setQuerry.dbSetQuery("DROP TABLE Customers;");
            queryResult = setQuerry.dbSetQuery("DROP TABLE Category;");
            queryResult = setQuerry.dbSetQuery("DROP TABLE Agreements;");
            queryResult = setQuerry.dbSetQuery("DROP TABLE Items;");
            queryResult = setQuerry.dbSetQuery("DROP TABLE Safe;");
            queryResult = setQuerry.dbSetQuery("DROP TABLE Cautions;");
            queryResult = setQuerry.dbSetQuery("DROP TABLE Late_Customers;");
            queryResult = setQuerry.dbSetQuery("DROP TABLE OPerations;");
            queryResult = setQuerry.dbSetQuery("DROP TABLE Notices;");
            queryResult = setQuerry.dbSetQuery("DROP TABLE PhoneReports;");

            //create new DB
            CreateDB newDB = new CreateDB();
            newDB.createAuthTable(conn.getConnection());
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

    }
}
