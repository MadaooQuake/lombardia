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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombardia2014.Interface.menu.ListUsers;

/**
 *
 * @author Domek
 */
public class NoticesDBQueries {

    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    SimpleDateFormat ft = new SimpleDateFormat("dd.MM.YYYY HH:mm");

    // notices

    public void insertNewNotices(String name, String surename, String number, String title,
            String content) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("INSERT INTO Notices ("
                    + "Number, Title, Content, Date, ID_CUSTOMER) VALUES ( '"
                    + number + "','"
                    + title + "','"
                    + content + "','"
                    + ft.format(new Date()) + "',"
                    + "(SELECT Customers.ID FROM Customers WHERE Customers.NAME LIKE '"
                    + name + "' "
                    + "AND Customers.SURNAME LIKE '"
                    + surename
                    + "'));");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // phonereport
    public void insertNewPhone(String name, String surename, String number, String title,
            String content) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("INSERT INTO PhoneReports ("
                    + "Number, Title, Content, Date, ID_CUSTOMER) VALUES ( '"
                    + number + "','"
                    + title + "','"
                    + content + "','"
                    + ft.format(new Date()) + "',"
                    + "(SELECT Customers.ID FROM Customers WHERE Customers.NAME LIKE '"
                    + name + "' "
                    + "AND Customers.SURNAME LIKE '"
                    + surename
                    + "'));");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
