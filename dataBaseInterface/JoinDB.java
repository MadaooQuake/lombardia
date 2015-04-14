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
public class JoinDB {

    private ResultSet queryResult = null;
    private QueryDB setQuerry = null;
    private Connection conDB = null;
    private Statement stmt = null;
    private final String newDB;

    public JoinDB(String dbName) {
        newDB = dbName;
    }

    public void insertDB() {
        try {
            ConnectDB conn = new ConnectDB();
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

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
                + "Users(NAME, SURNAME, ADDRESS, PHONE, "
                + "LOGIN, PASSWORD, ID_auth) "
                + "SELECT NAME, SURNAME, ADDRESS, PHONE, "
                + "LOGIN, PASSWORD, ID_auth FROM candidate.Users;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "Customers(NAME, SURNAME, ADDRESS, "
                + "PESEL, TRUST) "
                + "SELECT NAME, SURNAME, ADDRESS, "
                + "PESEL, TRUST FROM candidate.Customers;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "Agreements(ID_AGREEMENTS, START_DATE,"
                + "STOP_DATE, VALUE, COMMISSION, ITEM_VALUE,"
                + "ITEM_WEIGHT, VALUE_REST, SAVEPRICE,"
                + "DISCOUNT, ID_CUSTOMER) "
                + "SELECT ID_AGREEMENTS, START_DATE,"
                + "STOP_DATE, VALUE, COMMISSION, ITEM_VALUE,"
                + "ITEM_WEIGHT, VALUE_REST, SAVEPRICE,"
                + "DISCOUNT, ID_CUSTOMER FROM candidate.Agreements;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "Items(MODEL, BAND, TYPE, WEIGHT,"
                + "IMEI, VALUE, SOLD_DATE, ATENCION, "
                + "ID_CATEGORY, ID_AGREEMENT) "
                + "SELECT MODEL, BAND, TYPE, WEIGHT,"
                + "IMEI, VALUE, SOLD_DATE, ATENCION, "
                + "ID_CATEGORY, ID_AGREEMENT FROM candidate.Items;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "Cautions(TITLE, USER, DATE) "
                + "SELECT TITLE, USER, DATE"
                + " FROM candidate.Cautions;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "Late_Customers(NAME, SURNAME, ID_AGREEMENT, "
                + "ID_CUSTOMER) "
                + "SELECT NAME, SURNAME, ID_AGREEMENT, "
                + "ID_CUSTOMER FROM candidate.Late_Customers;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "OPerations(USER, DATE, OPERATIONS) "
                + "SELECT USER, DATE, OPERATIONS"
                + " FROM candidate.OPerations;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "Notices(TITLE, CONTENT, DATE, USER) "
                + "SELECT TITLE, CONTENT, DATE, USER"
                + " FROM candidate.Notices;");
        queryResult = setQuerry.dbSetQuery("INSERT INTO "
                + "PhoneReports(TITLE, CONTENT, DATE, NUMBER, USER) "
                + "SELECT TITLE, CONTENT, DATE, NUMBER, USER"
                + " FROM candidate.PhoneReports;");
    }

}
