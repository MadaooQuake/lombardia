package lombardia2014.dataBaseInterface;

import java.sql.*;
import java.util.Arrays;
import lombardia2014.generators.LombardiaLogger;

/**
 * create table for database
 *
 * @author Domek
 */
public class CreateDB {

    Statement stmt = null;

    /**
     * @param c
     * @see create two tables
     * <li>
     * <ul>Table with users</ul>
     * <ul>Table with restrigtions for users</ul>
     * </li>
     *
     * @return
     */
    public String createAuthTable(Connection c) {
        // create auth table
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:lombardia.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE Auth "
                    + "(ID INTEGER(4) PRIMARY KEY NOT NULL ,"
                    + "NAME    TEXT   NOT NULL)";
            stmt.executeUpdate(sql);

            // create users table
            sql = "CREATE TABLE Users"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "NAME TEXT   NOT NULL,"
                    + "SURNAME TEXT    NOT NULL,"
                    + "ADDRESS TEXT,"
                    + "PHONE   INTEGER(9),"
                    + "LOGIN   TEXT    NOT NULL,"
                    + "PASSWORD    TEXT    NOT NULL,"
                    + "ID_auth INTEGER(4) NOT NULL,"
                    + "FOREIGN KEY(ID_auth) REFERENCES Auth(ID))";
            stmt.executeUpdate(sql);

            // Client table
            sql = "CREATE TABLE Customers"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "NAME TEXT   NOT NULL,"
                    + "SURNAME TEXT    NOT NULL,"
                    + "ADDRESS TEXT,"
                    + "PESEL   TEXT,"
                    + "TRUST   INTEGER(1)    NOT NULL,"
                    + "DISCOUNT REAL"
                    + ")";
            stmt.executeUpdate(sql);

            // category table
            sql = "CREATE TABLE Category"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "NAME TEXT   NOT NULL"
                    + ")";
            stmt.executeUpdate(sql);

            // Agreements table
            sql = "CREATE TABLE Agreements"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "ID_AGREEMENTS TEXT NOT NULL,"
                    + "START_DATE TEXT NOT NULL,"
                    + "STOP_DATE TEXT NOT NULL,"
                    + "VALUE REAL NOT NULL,"
                    + "COMMISSION TEXT NOT NULL,"
                    + "ITEM_VALUE REAL NOT NULL,"
                    + "ITEM_WEIGHT REAL NOT NULL,"
                    + "VALUE_REST REAL NOT NULL,"
                    + "SAVEPRICE REAL NOT NULL,"
                    + "SELL INTEGER NOT NULL,"
                    + "NOTICE TEXT NULL,"
                    + "ID_CUSTOMER INTEGER(4) NOT NULL,"
                    + "FOREIGN KEY(ID_CUSTOMER) REFERENCES Customers(ID)"
                    + ")";
            stmt.executeUpdate(sql);

            // items table
            sql = "CREATE TABLE Items"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "MODEL TEXT   NOT NULL,"
                    + "BAND TEXT,"
                    + "TYPE TEXT,"
                    + "WEIGHT REAL,"
                    + "IMEI INTEGER(15),"
                    + "VALUE REAL,"
                    + "BUY_DATE TEXT,"
                    + "SOLD_DATE TEXT,"
                    + "ATENCION TEXT,"
                    + "ID_CATEGORY INTEGER(4) NOT NULL,"
                    + "ID_AGREEMENT INTEGER(4),"
                    + "FOREIGN KEY(ID_CATEGORY) REFERENCES Category(ID),"
                    + "FOREIGN KEY(ID_AGREEMENT) REFERENCES Agreements(ID)"
                    + ")";
            stmt.executeUpdate(sql);

            // safe table:D
            sql = "CREATE TABLE Safe"
                    + "("
                    + "VALUE REAL NOT NULL"
                    + ")";
            stmt.executeUpdate(sql);

            //Cautions table
            sql = "CREATE TABLE Cautions"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "TITLE TEXT NOT NULL,"
                    + "USER TEXT NOT NULL,"
                    + "DATE TEXT NOT NULL"
                    + ")";
            stmt.executeUpdate(sql);

            //Late Customers
            sql = "CREATE TABLE Late_Customers"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "NAME TEXT,"
                    + "SURNAME TEXT,"
                    + "ID_AGREEMENT INTEGER(4) NOT NULL,"
                    + "ID_CUSTOMER INTEGER(4) NOT NULL,"
                    + "FOREIGN KEY (ID_AGREEMENT) REFERENCES Agreements(ID),"
                    + "FOREIGN KEY (ID_CUSTOMER) REFERENCES Customers(ID)"
                    + ")";
            stmt.executeUpdate(sql);

            //operations table
            sql = "CREATE TABLE OPerations"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "USER TEXT NOT NULL,"
                    + "DATE TEXT NOT NULL,"
                    + "OPERATIONS TEXT NOT NULL)";
            stmt.executeUpdate(sql);

            /* 
             Matsu why this table don't have any ralation to User table ?
             */
            sql = "CREATE TABLE Notices"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "TITLE TEXT NOT NULL,"
                    + "CONTENT TEXT NOT NULL,"
                    + "DATE TEXT NOT NULL,"
                    + "ID_CUSTOMER INTEGER(4) NOT NULL,"
                    + "FOREIGN KEY (ID_CUSTOMER) REFERENCES Customers(ID)"
                    + ")";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE PhoneReports"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "TITLE TEXT NOT NULL,"
                    + "CONTENT TEXT NOT NULL,"
                    + "DATE TEXT NOT NULL,"
                    + "NUMBER INTEGER NOT NULL,"
                    + "ID_CUSTOMER INTEGER(4) NOT NULL,"
                    + "FOREIGN KEY (ID_CUSTOMER) REFERENCES Customers(ID)"
                    + ")";
            stmt.executeUpdate(sql);

            // insert data
            sql = "INSERT INTO Auth VALUES"
                    + "(1, 'Administrator');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Auth VALUES"
                    + "(2, 'Kierowonik');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Auth VALUES"
                    + "(3, 'Użytkownik');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Users (NAME, SURNAME, ADDRESS, PHONE, LOGIN, PASSWORD, ID_auth) VALUES "
                    + "('Admin', 'Admin','Brak danych',507506429,'Admin','4321Qwer',1);";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Users (NAME, SURNAME, ADDRESS, PHONE, LOGIN, PASSWORD, ID_auth) VALUES "
                    + "('user', 'user','Brak danych',507506429,'user','user',3);";
            stmt.executeUpdate(sql);

            /**
             * Wyroby jubilerskie, Telefon, Tablet, Telewizor, Laptop, Komputer,
             * Monitor
             */
            sql = "INSERT INTO Category (NAME) VALUES ('Wyroby jubilerskie');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Category (NAME) VALUES ('Telefon');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Category (NAME) VALUES ('Tablet');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Category (NAME) VALUES ('Telewizor');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Category (NAME) VALUES ('Laptop');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Category (NAME) VALUES ('Komputer');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Category (NAME) VALUES ('Monitor');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Category (NAME) VALUES ('Gry');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Category (NAME) VALUES ('Inne');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Safe VALUES(0);";
            stmt.executeUpdate(sql);

            stmt.close();
            c.close();
        } catch (ClassNotFoundException | SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
            System.exit(0);
        }

        return "Authoryzation Table Created";
    }

}
