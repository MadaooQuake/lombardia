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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public void insertNewNotices(String title,
            String content, String name, String surename) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("INSERT INTO Notices ("
                    + " Title, Content, Date, ID_CUSTOMER) VALUES ( '"
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

    // select from notices
    public List<HashMap<String, String>> getNotices() {
        List<HashMap<String, String>> notices = new ArrayList<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT Notices.* , "
                    + "Customers.NAME as Name, Customers.SURNAME as Surename"
                    + " FROM Notices, Customers "
                    + "WHERE Notices.ID_CUSTOMER = Customers.ID;");

            while (queryResult.next()) {
                Map<String, String> notice = new HashMap<>();

                notice.put("ID", queryResult.getString("ID"));
                notice.put("TITLE", queryResult.getString("TITLE"));
                notice.put("CONTENT", queryResult.getString("CONTENT"));
                notice.put("NAME", queryResult.getString("Name") + " " + queryResult.getString("Surename"));
                notice.put("DATE", queryResult.getString("DATE"));

                notices.add((HashMap<String, String>) notice);
            }

            setQuerry.closeDB();
        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }

        return notices;
    }

    // delete notices
    public void deleteNotices(int id) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("DELETE FROM Notices WHERE "
                    + "ID = " + id + ";");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // phonereport
    public void insertNewPhone(String number, String title,
            String content, String name, String surename ) {
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
    
        public List<HashMap<String, String>> getPhonesReport() {
        List<HashMap<String, String>> phonesReport = new ArrayList<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT PhoneReports.* , "
                    + "Customers.NAME as Name, Customers.SURNAME as Surename"
                    + " FROM PhoneReports, Customers "
                    + "WHERE PhoneReports.ID_CUSTOMER = Customers.ID;");

            while (queryResult.next()) {
                Map<String, String> phone = new HashMap<>();

                phone.put("ID", queryResult.getString("ID"));
                phone.put("TITLE", queryResult.getString("TITLE"));
                phone.put("CONTENT", queryResult.getString("CONTENT"));
                phone.put("NAME", queryResult.getString("Name") + " " + queryResult.getString("Surename"));
                phone.put("DATE", queryResult.getString("DATE"));
                phone.put("NUMBER", queryResult.getString("NUMBER"));

                phonesReport.add((HashMap<String, String>) phone);
            }

            setQuerry.closeDB();
        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }

        return phonesReport;
    }

        // delete phone report
            public void deletePhoneReport(int id) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("DELETE FROM PhoneReports WHERE "
                    + "ID = " + id + ";");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
