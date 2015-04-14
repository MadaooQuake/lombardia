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
import javax.swing.JOptionPane;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class LoadDB {

    private ResultSet queryResult = null;
    private QueryDB setQuerry = null;
    private Connection conDB = null;
    private Statement stmt = null;
    String backupFile = System.getProperty("user.dir")
            + "\\lombardiaKopia.db", newDB;

    public LoadDB(String newDB_) {
        newDB = newDB_;
    }

    // create copy
    public void createCopy() {
        SaveDB backup = new SaveDB(backupFile);
        backup.savingFille();
        JOptionPane.showConfirmDialog(null,
                "Kopia bazy została zapisana pod nazwą lombardiaKopia.db",
                "Kopia starej bazy!",
                JOptionPane.PLAIN_MESSAGE);
    }

    // truncate old db
    public void deleteDB() {
        DeleteDB deleteOldDB = new DeleteDB();
        deleteOldDB.deleteDB();
    }

    // insert new db
    public void loadnewDB() {
        try {
            ConnectDB conn = new ConnectDB();
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();
            // truncate users
            queryResult = setQuerry.dbSetQuery("DELETE FROM Users;");
            queryResult = setQuerry.dbSetQuery("DELETE FROM Category;");

            insertToDB();

            JOptionPane.showConfirmDialog(null,
                    "Baza została wczytana i teraz aplikacja się wyłączy i proszę"
                    + " ją ponownie uruchomić.",
                    "Baza została wczytana!",
                    JOptionPane.PLAIN_MESSAGE);
            conDB.close();
            System.exit(0);
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text2 = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text2);

        }
    }
    
    private void insertToDB() {
        queryResult = setQuerry.dbSetQuery("ATTACH DATABASE '"
                + newDB + "' AS candidate;");
        // now i create inserts :)
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "Users(ID, NAME, SURNAME, ADDRESS, PHONE, "
                + "LOGIN, PASSWORD, ID_auth) "
                + "SELECT * FROM candidate.Users;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "Customers(ID, NAME, SURNAME, ADDRESS, "
                + "PESEL, TRUST) "
                + "SELECT * FROM candidate.Customers;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "Category(ID, NAME) "
                + "SELECT * FROM candidate.Category;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "Agreements(ID, ID_AGREEMENTS, START_DATE,"
                + "STOP_DATE, VALUE, COMMISSION, ITEM_VALUE,"
                + "ITEM_WEIGHT, VALUE_REST, SAVEPRICE,"
                + "DISCOUNT, ID_CUSTOMER) "
                + "SELECT * FROM candidate.Agreements;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "Items(ID, MODEL, BAND, TYPE, WEIGHT,"
                + "IMEI, VALUE, SOLD_DATE, ATENCION, "
                + "ID_CATEGORY, ID_AGREEMENT) "
                + "SELECT * FROM candidate.Items;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "Safe(VALUE) "
                + "SELECT * FROM candidate.Safe;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "Cautions(ID, TITLE, USER, DATE) "
                + "SELECT * FROM candidate.Cautions;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "Late_Customers(ID, NAME, SURNAME, ID_AGREEMENT, "
                + "ID_CUSTOMER) "
                + "SELECT * FROM candidate.Late_Customers;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "OPerations(ID, USER, DATE, OPERATIONS) "
                + "SELECT * FROM candidate.OPerations;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "Notices(ID, TITLE, CONTENT, DATE, USER) "
                + "SELECT * FROM candidate.Notices;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "PhoneReports(ID, TITLE, CONTENT, DATE, NUMBER, USER) "
                + "SELECT * FROM candidate.PhoneReports;");
    }
}
