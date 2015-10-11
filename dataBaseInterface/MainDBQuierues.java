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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombardia2014.generators.DateTools;
import lombardia2014.generators.LombardiaLogger;
import lombardia2014.core.ValueCalc;

/**
 *
 * @author Domek
 */
public class MainDBQuierues {

    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    DateTools convertDate = null;

    //==========================================================================
    // Quueries for Items table
    //==========================================================================
    // items
    // categories 
    // add new categories 
    public void addCategoeirs(String name) {

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("INSERT INTO Category (NAME) VALUES"
                    + "('" + name + "');");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

    }

    public List<String> getCategories() {
        List<String> words = new ArrayList<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT NAME FROM Category");
            //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist

            while (queryResult.next()) {
                words.add(queryResult.getString("NAME"));
            }

            setQuerry.closeDB();

            //addToDictionary("bye");//adds a single word
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return words;
    }

    // categories with id
    public Map<Integer, String> getCategoriesWithID() {
        Map<Integer, String> categories = new HashMap<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT * FROM Category;");

            while (queryResult.next()) {
                categories.put(queryResult.getInt("ID"),
                        queryResult.getString("NAME"));
            }

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
        setQuerry.closeDB();

        return categories;
    }

    public Map<Integer, HashMap> itemsElement(Map<Integer, String> categories, int agrID) {
        Map<Integer, HashMap> itemsList = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        int i = 0;
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT * FROM Items WHERE "
                    + "ID_AGREEMENT = " + agrID + ";");

            while (queryResult.next()) {
                items.put("Kategoria", categories.get(
                        queryResult.getInt("ID_CATEGORY")));
                items.put("Model", queryResult.getString("MODEL"));
                items.put("Marka", queryResult.getString("BAND"));
                items.put("Typ", queryResult.getString("TYPE"));
                items.put("Waga", queryResult.getString("WEIGHT"));
                items.put("Wartość", queryResult.getString("VALUE"));
                items.put("IMEI", queryResult.getString("IMEI"));
                items.put("Uwagi", queryResult.getString("ATENCION"));
                itemsList.put(i, (HashMap) items);
                i++;
            }

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return itemsList;
    }

    public void insertItem(String item) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery(item);

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
        setQuerry.closeDB();
    }

    public Integer getCatID(String category) {
        int catID = 0;
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT ID FROM Category WHERE"
                    + " NAME LIKE '" + category + "';");

            while (queryResult.next()) {
                catID = queryResult.getInt("ID");
            }

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
        setQuerry.closeDB();

        return catID;
    }

    // select all items
    public List<HashMap<String, String>> getAllItems() {
        List<HashMap<String, String>> allItems = new ArrayList<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT Items.ID AS ID_ITEM, NAME, MODEL, BAND, TYPE, "
                    + "WEIGHT, Items.VALUE AS VALUE, IMEI, ATENCION, Agreements.ID_AGREEMENTS AS ID_AGREEMENTS"
                    + " FROM Items"
                    + " INNER JOIN Category"
                    + " ON Items.ID_CATEGORY = Category.ID"
                    + " LEFT OUTER JOIN Agreements"
                    + " ON Agreements.ID = Items.ID_AGREEMENT;");

            while (queryResult.next()) {
                Map<String, String> item = new HashMap<>();

                item.put("ID_ITEM", queryResult.getString("ID_ITEM"));
                item.put("NAME", queryResult.getString("NAME"));
                item.put("MODEL", queryResult.getString("MODEL"));
                item.put("BAND", queryResult.getString("BAND"));
                item.put("TYPE", queryResult.getString("TYPE"));
                item.put("WEIGHT", queryResult.getString("WEIGHT"));
                item.put("VALUE", queryResult.getString("VALUE"));
                item.put("IMEI", queryResult.getString("IMEI"));
                item.put("ID_AGREEMENTS", queryResult.getString("ID_AGREEMENTS"));

                allItems.add((HashMap<String, String>) item);
            }

            setQuerry.closeDB();

            //addToDictionary("bye");//adds a single word
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return allItems;
    }

    // search item
    public List<HashMap<String, String>> searchItem(String text) {
        List<HashMap<String, String>> allItems = new ArrayList<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT Items.ID AS ID_ITEM, NAME, MODEL, BAND, TYPE, "
                    + "WEIGHT, Items.VALUE AS VALUE, IMEI, ATENCION, Agreements.ID_AGREEMENTS AS ID_AGREEMENTS"
                    + " FROM Items"
                    + " INNER JOIN Category"
                    + " ON Items.ID_CATEGORY = Category.ID"
                    + " LEFT OUTER JOIN Agreements"
                    + " ON Agreements.ID = Items.ID_AGREEMENT"
                    + " WHERE"
                    + " MODEL LIKE '%" + text
                    + "%' OR BAND LIKE '%" + text
                    + "%'"
                    + ";");

            while (queryResult.next()) {
                Map<String, String> item = new HashMap<>();

                item.put("ID_ITEM", queryResult.getString("ID_ITEM"));
                item.put("NAME", queryResult.getString("NAME"));
                item.put("MODEL", queryResult.getString("MODEL"));
                item.put("BAND", queryResult.getString("BAND"));
                item.put("TYPE", queryResult.getString("TYPE"));
                item.put("WEIGHT", queryResult.getString("WEIGHT"));
                item.put("VALUE", queryResult.getString("VALUE"));
                item.put("IMEI", queryResult.getString("IMEI"));
                item.put("ID_AGREEMENTS", queryResult.getString("ID_AGREEMENTS"));

                allItems.add((HashMap<String, String>) item);
            }

            setQuerry.closeDB();

            //addToDictionary("bye");//adds a single word
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String textE = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(textE);
        }

        return allItems;
    }

    public Map<String, String> getItem(int id) {
        Map<String, String> item = new HashMap();
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT Category.ID, Items.ID AS ID_ITEM, NAME, MODEL, BAND, TYPE, WEIGHT, VALUE, IMEI, ATENCION FROM Items, Category WHERE  Items.ID_CATEGORY = Category.ID  AND Items.ID = " + id + ";");
            while (queryResult.next()) {
                item.put("ID_ITEM", this.queryResult.getString("ID_ITEM"));
                item.put("NAME", this.queryResult.getString("NAME"));
                item.put("MODEL", this.queryResult.getString("MODEL"));
                item.put("BAND", this.queryResult.getString("BAND"));
                item.put("TYPE", this.queryResult.getString("TYPE"));
                item.put("WEIGHT", this.queryResult.getString("WEIGHT"));
                item.put("VALUE", this.queryResult.getString("VALUE"));
                item.put("IMEI", this.queryResult.getString("IMEI"));
            }
            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
        return item;
    }

    public void deleteObject(int id) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("DELETE FROM Items  WHERE ID = " + id + ";");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    //==========================================================================
    // Quueries for users 
    //==========================================================================
    //get list of users
    public List<String> getUsersByNameAndSurname() {
        List<String> words = new ArrayList<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT NAME,SURNAME"
                    + " FROM Customers;");

            while (queryResult.next()) {
                words.add(queryResult.getString("SURNAME") + " "
                        + queryResult.getString("NAME"));
            }
            setQuerry.closeDB();

            //addToDictionary("bye");//adds a single word
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return words;
    }

    // get users
    public Map<String, String> getUser(String name, String surname) {
        Map<String, String> user = new HashMap<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT NAME, SURNAME, ADDRESS,"
                    + "PESEL, TRUST, DISCOUNT FROM Customers WHERE NAME LIKE '"
                    + name + "' AND SURNAME LIKE '"
                    + surname + "';");

            while (queryResult.next()) {
                user.put("NAME", queryResult.getString("NAME"));
                user.put("SURNAME", queryResult.getString("SURNAME"));
                user.put("ADDRESS", queryResult.getString("ADDRESS"));
                user.put("PESEL", queryResult.getString("PESEL"));
                user.put("TRUST", queryResult.getString("TRUST"));
                user.put("DISCOUNT", queryResult.getString("DISCOUNT"));
            }

            setQuerry.closeDB();

            //addToDictionary("bye");//adds a single word
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return user;
    }

    public Map<String, String> getUserByID(int id) {
        Map<String, String> user = new HashMap<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT NAME, SURNAME, ADDRESS,"
                    + "PESEL, TRUST, DISCOUNT FROM Customers WHERE ID = " + id + ";");

            while (queryResult.next()) {
                user.put("NAME", queryResult.getString("NAME"));
                user.put("SURNAME", queryResult.getString("SURNAME"));
                user.put("ADDRESS", queryResult.getString("ADDRESS"));
                user.put("PESEL", queryResult.getString("PESEL"));
                user.put("TRUST", queryResult.getString("TRUST"));
                user.put("DISCOUNT", queryResult.getString("DISCOUNT"));
            }

            setQuerry.closeDB();

            //addToDictionary("bye");//adds a single word
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return user;
    }

    // check if user exist 
    public boolean checkUser(String name, String surname) {
        int customer = 0;

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT Count(*) AS CustomerCount "
                    + "FROM Customers WHERE NAME LIKE '"
                    + name + "' AND SURNAME LIKE '"
                    + surname + "';");

            while (queryResult.next()) {
                customer = queryResult.getInt("CustomerCount");
            }

            setQuerry.closeDB();

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return customer == 0;
    }

    public Integer getUserID(String name, String surname) {
        int id = 0;

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT ID FROM Customers WHERE"
                    + " NAME LIKE '"
                    + name + "' AND SURNAME LIKE '"
                    + surname + "';");
            // save to db

            while (queryResult.next()) {
                id = queryResult.getInt("ID");
            }

            setQuerry.closeDB();

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return id;
    }

    // get all users <- hmmmm
    public List<HashMap<String, String>> getAllCustomers() {
        List<HashMap<String, String>> getAllCustomers = new ArrayList<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT NAME, SURNAME, ADDRESS, "
                    + "PESEL, TRUST FROM Customers;");

            while (queryResult.next()) {
                Map<String, String> user = new HashMap<>();

                user.put("NAME", queryResult.getString("NAME"));
                user.put("SURNAME", queryResult.getString("SURNAME"));
                user.put("ADDRESS", queryResult.getString("ADDRESS"));
                user.put("PESEL", queryResult.getString("PESEL"));
                user.put("TRUST", queryResult.getString("TRUST"));

                getAllCustomers.add((HashMap<String, String>) user);
            }

            setQuerry.closeDB();

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return getAllCustomers;
    }

    // search customer
    public List<HashMap<String, String>> searchCustomer(String searchText) {
        List<HashMap<String, String>> getAllCustomers = new ArrayList<>();
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT * FROM Customers"
                    + " WHERE NAME LIKE '%" + searchText
                    + "%' OR SURNAME LIKE '%" + searchText
                    + "%';");

            while (queryResult.next()) {
                Map<String, String> user = new HashMap<>();

                user.put("NAME", queryResult.getString("NAME"));
                user.put("SURNAME", queryResult.getString("SURNAME"));
                user.put("ADDRESS", queryResult.getString("ADDRESS"));
                user.put("PESEL", queryResult.getString("PESEL"));
                user.put("TRUST", queryResult.getString("TRUST"));

                getAllCustomers.add((HashMap<String, String>) user);
            }

            setQuerry.closeDB();

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return getAllCustomers;
    }

    // save user :)
    public void saveUser(String name, String surename, String addres, int Trust,
            String pesel, String discount) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();
            discount = discount == null ? "0" : discount;
            if (!pesel.isEmpty()) {
                queryResult = setQuerry.dbSetQuery("INSERT INTO Customers (NAME, "
                        + "SURNAME, ADDRESS, PESEL, TRUST, DISCOUNT) VALUES"
                        + "('" + name + "','"
                        + surename + "','"
                        + addres + "','"
                        + pesel + "',"
                        + Trust + ",'"
                        + discount + "');");
            } else {
                queryResult = setQuerry.dbSetQuery("INSERT INTO Customers (NAME, "
                        + "SURNAME, ADDRESS, TRUST, DISCOUNT) VALUES"
                        + "('" + name + "','"
                        + surename + "','"
                        + addres + "',"
                        + Trust + ",'"
                        + discount + "');");
            }

            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    // update customer
    public void updateUser(String name, String surename, String addres, int Trust,
            String pesel, String discount, int id) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            discount = discount.isEmpty() ? "0" : discount;
            if (pesel.isEmpty()) {
                queryResult = setQuerry.dbSetQuery("UPDATE Customers SET"
                        + " NAME = '" + name + "',"
                        + " SURNAME = '" + surename + "', "
                        + "ADDRESS = '" + addres + "'"
                        + ", TRUST =" + Trust
                        + ", DISCOUNT = " + discount
                        + " WHERE ID = " + id + ";");
            } else {
                queryResult = setQuerry.dbSetQuery("UPDATE Customers SET"
                        + " NAME = '" + name + "',"
                        + " SURNAME = '" + surename + "', "
                        + "ADDRESS = '" + addres + "', "
                        + "PESEL =" + pesel
                        + " , TRUST =" + Trust
                        + ", DISCOUNT =" + discount
                        + " WHERE ID = " + id + ";");
            }

            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    // late client
    public List<HashMap<String, String>> lateClients() {
        List<HashMap<String, String>> lateClients = new ArrayList<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT Customers.NAME AS NAME, "
                    + "Customers.SURNAME AS SURNAME, "
                    + "Agreements.ID_AGREEMENTS AS AGREEMENT_ID,"
                    + "Agreements.STOP_DATE AS END_DATE FROM Customers, Agreements"
                    + " WHERE Agreements.ID_CUSTOMER = Customers.ID AND END_DATE < '"
                    + new DateTools(new Date()).GetDateForDB() + "' AND Agreements.SELL = 0;");

            while (queryResult.next()) {
                Map<String, String> user = new HashMap<>();

                user.put("NAME", queryResult.getString("NAME"));
                user.put("SURNAME", queryResult.getString("SURNAME"));
                user.put("AGREEMENT_ID", queryResult.getString("AGREEMENT_ID"));
                user.put("END_DATE", new DateTools(queryResult.getString("END_DATE")).GetDateAsString());
                lateClients.add((HashMap<String, String>) user);
            }

            setQuerry.closeDB();
        } catch (SQLException | ParseException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return lateClients;
    }

    public void removeItemFromAgreement(String aggID) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("UPDATE Items SET ID_AGREEMENT = NULL WHERE "
                    + "ID_AGREEMENT = (SELECT ID FROM Agreements WHERE ID_AGREEMENTS = '" + aggID + "');");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    public void removeItems(String aggID) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("DELETE FROM Items WHERE "
                    + "ID_AGREEMENT = (SELECT ID FROM Agreements WHERE ID_AGREEMENTS = '" + aggID + "');");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    // 
    //==========================================================================
    // Quueries for Agreements 
    //==========================================================================
    public Integer getMaxIDAgreements() {
        int maxID = 0;
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT MAX(ID) FROM Agreements;");

            while (queryResult.next()) {
                maxID = queryResult.getInt(1);
            }
            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return maxID;
    }

    public String[] selectRestValue(String aggID) {
        String[] value = new String[2];
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT VALUE_REST, VALUE, ID_AGREEMENTS FROM Agreements"
                    + " WHERE ID_AGREEMENTS LIKE '" + aggID + "';");

            while (queryResult.next()) {
                value[0] = queryResult.getString("VALUE_REST");
                value[1] = queryResult.getString("VALUE");
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

    public void saveAgreements(String idAgreements, Date startDate, Date stopDate,
            String value, String commision, String itemValue, String itemWeigth, String valueRest,
            String saveprice, int custoerID) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("INSERT INTO Agreements (ID_AGREEMENTS,"
                    + " START_DATE, STOP_DATE, VALUE, COMMISSION, ITEM_VALUE, ITEM_WEIGHT,"
                    + " VALUE_REST, SAVEPRICE, ID_CUSTOMER, SELL)"
                    + " VALUES ('"
                    + idAgreements + "','" + new DateTools(startDate).GetDateForDB() + "','"
                    + new DateTools(stopDate).GetDateForDB() + "','"
                    + value.replaceAll(",", ".") + "',"
                    + commision.replaceAll(",", ".") + ","
                    + itemValue.replaceAll(",", ".") + ","
                    + itemWeigth.replaceAll(",", ".") + ","
                    + valueRest.replaceAll(",", ".") + ","
                    + saveprice.replaceAll(",", ".") + ","
                    + custoerID + "," + 0 + ");");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    public void updateAgreements(String idAgreements, Date stopDate,
            String value, String commision, String itemValue, String itemWeigth, String valueRest,
            String saveprice, int custoerID) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("UPDATE Agreements SET STOP_DATE ='"
                    + new DateTools(stopDate).GetDateForDB() + "', VALUE = '"
                    + value.replaceAll(",", ".") + "', COMMISSION = '"
                    + commision.replaceAll(",", ".") + "', ITEM_VALUE = '"
                    + itemValue.replaceAll(",", ".") + "', ITEM_WEIGHT = '"
                    + itemWeigth.replaceAll(",", ".") + "', VALUE_REST = '"
                    + valueRest.replaceAll(",", ".") + "', SAVEPRICE = '"
                    + saveprice.replaceAll(",", ".") + "', ID_CUSTOMER = '"
                    + custoerID + " WHERE ID_AGREEMENTS = '"
                    + idAgreements + "';");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    //get all stocktakings
    public List<HashMap<String, String>> getStockTakingsFromDateRange(String point_of_time) {
        List<HashMap<String, String>> StockTakings = new ArrayList<>();
        String query = "SELECT Items.Model, Items.Band, Items.ID, Items.Value, Agreements.Stop_date as Buy_date "
                + "FROM Items, Agreements WHERE Items.ID_AGREEMENT = Agreements.ID "
                + "AND Agreements.Stop_date < '" + point_of_time + "' "
                + "AND ( Items.Sold_date is null or Items.Sold_date > '" + point_of_time + "') "
                + "union all "
                + "SELECT Model, Band, ID, Value, Buy_Date "
                + " FROM Items WHERE Buy_Date is not null AND Buy_Date < '" + point_of_time + "' "
                + "AND ( Sold_date is null or Sold_date > '" + point_of_time + "');";

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery(query);
            int lp = 0;

            while (queryResult.next()) {
                lp++;
                Map<String, String> st = new HashMap<>();
                st.put("L.p.", Integer.toString(lp));
                st.put("Opis towaru", queryResult.getString("Model") + '(' + queryResult.getString("Band") + ')');
                st.put("Identyfikator", queryResult.getString("ID"));
                st.put("Cena netto", queryResult.getString("Value"));
                st.put("Wartość netto", queryResult.getString("Value"));
                st.put("Data zakupu", queryResult.getString("Buy_date"));
                StockTakings.add((HashMap<String, String>) st);
            }
            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return StockTakings;
    }

    //get all settlements
    public List<HashMap<String, String>> getSettlementsFromDateRange(String from, String to) {
        List<HashMap<String, String>> Settlements = new ArrayList<>();
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            String query = "SELECT Name, Surname, Address, Start_Date, Agreements.Value as avalue, Model, Band, Items.Value as ivalue, Stop_Date "
                    + "FROM Customers, Items, Agreements WHERE "
                    //join conditions
                    + "Items.ID_AGREEMENT = Agreements.ID "
                    + "AND Agreements.ID_CUSTOMER = Customers.ID "
                    //date filter
                    + "AND Agreements.Stop_date BETWEEN '"
                    + from + "' AND '" + to + "' "
                    //finish him
                    + "GROUP BY Agreements.ID;";

            queryResult = setQuerry.dbSetQuery(query);

            int lp = 0;

            while (queryResult.next()) {
                lp++;
                Map<String, String> agreement = new HashMap<>();
                agreement.put("L.p.", Integer.toString(lp));
                agreement.put("Imię", queryResult.getString("Name"));
                agreement.put("Nazwisko", queryResult.getString("Surname"));
                agreement.put("Adres", queryResult.getString("Address"));
                agreement.put("Data pożyczki", queryResult.getString("Start_Date"));
                agreement.put("Kwota pożyczki", queryResult.getString("avalue"));
                String desc = queryResult.getString("Model");
                String Brand = queryResult.getString("Band");
                if (!queryResult.wasNull()) {
                    desc = desc + '(' + Brand + ')';
                }
                agreement.put("Opis zastawu", desc);
                float value = queryResult.getFloat("ivalue");

                agreement.put("Wartość zastawu", Float.toString(value));
                agreement.put("Termin zwrotu", queryResult.getString("Stop_date"));

                ValueCalc rate = new ValueCalc();
                agreement.put("Odsetki", Float.toString(rate.lombardRate(value)));

                Settlements.add((HashMap<String, String>) agreement);
            }

            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
        return Settlements;
    }

    // get last agreement
    public Map<String, String> getLastAgreement() {
        Map<String, String> paymentPorperies = new HashMap<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT * FROM Agreements ORDER BY "
                    + "ID DESC LIMIT 1");

            while (queryResult.next()) {
                paymentPorperies.put("NR Umowy", queryResult.getString("ID_AGREEMENTS"));

                paymentPorperies.put("Data rozpoczecia", new DateTools(queryResult.getString("START_DATE")).GetDateAsString());
                paymentPorperies.put("Data zwrotu", new DateTools(queryResult.getString("STOP_DATE")).GetDateAsString());
                paymentPorperies.put("Kwota", queryResult.getString("VALUE"));
                paymentPorperies.put("Opłata magayznowania", queryResult.getString("SAVEPRICE"));
                paymentPorperies.put("Opłata manipulacyjna", queryResult.getString("COMMISSION"));
                paymentPorperies.put("Razem do zapłaty", queryResult.getString("VALUE_REST"));
                paymentPorperies.put("Łączna waga", queryResult.getString("ITEM_WEIGHT"));
                paymentPorperies.put("Łączna wartosc", queryResult.getString("ITEM_VALUE"));
                paymentPorperies.put("CustID", queryResult.getString("ID_CUSTOMER"));
                paymentPorperies.put("AgrID", queryResult.getString("ID"));
            }
            setQuerry.closeDB();
        } catch (SQLException | ParseException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return paymentPorperies;
    }

    public Map<String, String> getAgreementByID(String ID) {
        Map<String, String> paymentPorperies = new HashMap<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT * FROM Agreements "
                    + "WHERE ID_AGREEMENTS LIKE '" + ID + "';");

            while (queryResult.next()) {
                paymentPorperies.put("NR Umowy", queryResult.getString("ID_AGREEMENTS"));
                paymentPorperies.put("Data rozpoczecia", new DateTools(queryResult.getString("START_DATE")).GetDateAsString());
                paymentPorperies.put("Data zwrotu", new DateTools(queryResult.getString("STOP_DATE")).GetDateAsString());
                paymentPorperies.put("Kwota", queryResult.getString("VALUE"));
                paymentPorperies.put("Opłata magayznowania", queryResult.getString("SAVEPRICE"));
                paymentPorperies.put("Opłata manipulacyjna", queryResult.getString("COMMISSION"));
                paymentPorperies.put("Razem do zapłaty", queryResult.getString("VALUE_REST"));
                paymentPorperies.put("Łączna waga", queryResult.getString("ITEM_WEIGHT"));
                paymentPorperies.put("Łączna wartosc", queryResult.getString("ITEM_VALUE"));
                paymentPorperies.put("CustID", queryResult.getString("ID_CUSTOMER"));
                paymentPorperies.put("AgrID", queryResult.getString("ID"));
            }
            setQuerry.closeDB();
        } catch (SQLException | ParseException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return paymentPorperies;
    }

    // get agreement by agreements id copy paste method :( - next time i do good
    public List<HashMap<String, String>> getAgreementsByID(String ID) {
        List<HashMap<String, String>> Settlements = new ArrayList<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT * FROM Agreements "
                    + "WHERE ID_AGREEMENTS LIKE '%" + ID + "%';");

            while (queryResult.next()) {
                Map<String, String> paymentPorperies = new HashMap<>();
                paymentPorperies.put("NR Umowy", queryResult.getString("ID_AGREEMENTS"));
                paymentPorperies.put("Data rozpoczecia", new DateTools(queryResult.getString("START_DATE")).GetDateAsString());
                paymentPorperies.put("Data zwrotu", new DateTools(queryResult.getString("STOP_DATE")).GetDateAsString());
                paymentPorperies.put("Kwota", queryResult.getString("VALUE"));
                paymentPorperies.put("Opłata magayznowania", queryResult.getString("SAVEPRICE"));
                paymentPorperies.put("Opłata manipulacyjna", queryResult.getString("COMMISSION"));
                paymentPorperies.put("Razem do zapłaty", queryResult.getString("VALUE_REST"));
                paymentPorperies.put("Łączna waga", queryResult.getString("ITEM_WEIGHT"));
                paymentPorperies.put("Łączna wartosc", queryResult.getString("ITEM_VALUE"));
                paymentPorperies.put("CustID", queryResult.getString("ID_CUSTOMER"));
                paymentPorperies.put("AgrID", queryResult.getString("ID"));
                Settlements.add((HashMap<String, String>) paymentPorperies);
            }
            setQuerry.closeDB();
        } catch (SQLException | ParseException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return Settlements;
    }

    public List<HashMap<String, String>> getAgreementsAndCustomers() {
        List<HashMap<String, String>> Settlements = new ArrayList<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT Customers.NAME AS NAME, "
                    + "Customers.SURNAME AS SURNAME, Customers.ID AS CustomerID, "
                    + "Agreements.ID_AGREEMENTS AS AGREEMENT_ID, Agreements.ID AS ID,"
                    + " Agreements.STOP_DATE AS END_DATE, Agreements.SELL AS SELL"
                    + " FROM Customers, Agreements WHERE Customers.ID = Agreements.ID_CUSTOMER ;");

            while (queryResult.next()) {
                Map<String, String> paymentPorperies = new HashMap<>();
                paymentPorperies.put("NR Umowy", queryResult.getString("AGREEMENT_ID"));
                paymentPorperies.put("Data zwrotu", new DateTools(queryResult.getString("END_DATE")).GetDateAsString());
                paymentPorperies.put("Imie", queryResult.getString("NAME"));
                paymentPorperies.put("Nazwisko", queryResult.getString("SURNAME"));
                paymentPorperies.put("AgrID", queryResult.getString("ID"));
                paymentPorperies.put("Rozwiazana", queryResult.getString("SELL"));
                Settlements.add((HashMap<String, String>) paymentPorperies);
            }
            setQuerry.closeDB();
        } catch (SQLException | ParseException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return Settlements;
    }

    //search agreements by id, user name or surename :)
    public List<HashMap<String, String>> searchAgrementByIDorCustomerName(String aggID, String name, String surname) {
        List<HashMap<String, String>> Settlements = new ArrayList<>();
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            String query = "SELECT Customers.NAME AS NAME, "
                    + "Customers.SURNAME AS SURNAME, Customers.ID AS CustomerID, "
                    + "Agreements.ID_AGREEMENTS AS AGREEMENT_ID, Agreements.ID AS ID,"
                    + " Agreements.STOP_DATE AS END_DATE"
                    + " FROM Customers, Agreements WHERE Customers.ID = Agreements.ID_CUSTOMER";

            if (!aggID.isEmpty()) {
                query += " AND Agreements.ID_AGREEMENTS Like '%" + aggID + "%';";
            } else if (!name.isEmpty() && !surname.isEmpty()) {
                query += "AND Customers.Name LIKE '" + name + "' AND "
                        + "Customers.Surname LIKE '" + surname + "';";
            } else {
                query += ";";
            }
            queryResult = setQuerry.dbSetQuery(query);

            while (queryResult.next()) {
                Map<String, String> paymentPorperies = new HashMap<>();
                paymentPorperies.put("NR Umowy", queryResult.getString("AGREEMENT_ID"));
                paymentPorperies.put("Data zwrotu", new DateTools(queryResult.getString("END_DATE")).GetDateAsString());
                paymentPorperies.put("Imie", queryResult.getString("NAME"));
                paymentPorperies.put("Nazwisko", queryResult.getString("SURNAME"));
                paymentPorperies.put("AgrID", queryResult.getString("ID"));
                Settlements.add((HashMap<String, String>) paymentPorperies);
            }
            setQuerry.closeDB();
        } catch (SQLException | ParseException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return Settlements;
    }

    // update status in agreement
    public void updateAgreementStatus(String idAgreements) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("UPDATE Agreements SET SELL = 1 WHERE ID_AGREEMENTS = '"
                    + idAgreements + "';");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    // delete agreement
    public void deleteAgreement(String ID) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("DELETE FROM Agreements WHERE "
                    + "ID_AGREEMENTS LIKE '" + ID + "';");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    // query about safa agreements and items :o
    public List<HashMap<String, String>> getDailyAgreements() {
        List<HashMap<String, String>> operations = new ArrayList<>();
        Date curretDate = new Date();
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT ID_AGREEMENTS, START_DATE, VALUE, "
                    + "VALUE_REST, SELL "
                    + "FROM Agreements "
                    + "WHERE START_DATE LIKE '" + new DateTools(curretDate).GetDateForDB() + "';");
            
            while (queryResult.next()) {
                Map<String, String> paymentPorperies = new HashMap<>();
                paymentPorperies.put("NR Umowy", queryResult.getString("ID_AGREEMENTS"));
                paymentPorperies.put("Data Zawarcia", queryResult.getString("START_DATE"));
                paymentPorperies.put("Wartosc", queryResult.getString("VALUE"));
                paymentPorperies.put("Do zwrotu", queryResult.getString("VALUE_REST"));
                paymentPorperies.put("Splacona", queryResult.getString("SELL"));
                operations.add((HashMap<String, String>) paymentPorperies);
            }

            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return operations;
    }
}
