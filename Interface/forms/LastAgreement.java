/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.forms;

import lombardia2014.dataBaseInterface.QueryDB;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombardia2014.Interface.menu.ListUsers;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class LastAgreement implements ActionListener {

    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    CreditForm lastCredit = null;

    //maps
    Map<String, String> paymentPorperies = new HashMap<>();
    Map<Integer, HashMap> itemsList = new HashMap<>();
    Map<String, String> userInfo = new HashMap<>();
    Map<Integer, String> categories = new HashMap<>();

    public LastAgreement() {
        //first i do select to db :D

        // next i create payment form with parameters:D
    }

    // select about last agreement
    /**
     * @see method select last id agreement from table, next get id about
     * clients
     *
     */
    private Map<String, Integer> lastAgreement() {
        Map<String, Integer> ids = new HashMap<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT * FROM Agreements ORDER BY "
                    + "ID DESC LIMIT 1");

            while (queryResult.next()) {
                paymentPorperies.put("NR Umowy", queryResult.getString("ID_AGREEMENTS"));
                paymentPorperies.put("Data rozpoczecia", queryResult.getString("START_DATE"));
                paymentPorperies.put("Data zwrotu", queryResult.getString("STOP_DATE"));
                paymentPorperies.put("Kwota", queryResult.getString("VALUE"));
                paymentPorperies.put("Rabat", queryResult.getString("DISCOUNT"));
                paymentPorperies.put("Opłata magayznowania", queryResult.getString("SAVEPRICE"));
                paymentPorperies.put("Opłata manipulacyjna", queryResult.getString("COMMISSION"));
                paymentPorperies.put("Razem do zapłaty", queryResult.getString("VALUE_REST"));
                paymentPorperies.put("Łączna waga", queryResult.getString("ITEM_WEIGHT"));
                paymentPorperies.put("Łączna wartosc", queryResult.getString("ITEM_VALUE"));
                // get ids
                ids.put("CustID", queryResult.getInt("ID_CUSTOMER"));
                ids.put("AgrID", queryResult.getInt("ID"));
            }

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
        setQuerry.closeDB();
        return ids;
    }

    /**
     * @see this class get user info and save to hashmap
     *
     */
    private void getUserInfo(int id) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT * FROM Customers WHERE "
                    + "ID = " + id + ";");

            while (queryResult.next()) {
                userInfo.put("Imie", queryResult.getString("NAME"));
                userInfo.put("Nazwisko", queryResult.getString("SURNAME"));
                userInfo.put("Adres", queryResult.getString("ADDRESS"));
                userInfo.put("Pesel", queryResult.getString("PESEL"));
                userInfo.put("Zaufany klient", queryResult.getString("TRUST"));
            }

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
        setQuerry.closeDB();
    }

    /**
     * @see this method get items from
     */
    private void getItemsFromAgreement(int id) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();
            int i = 0; //inrement this value to save item to list

            // first i select categories and save to db :)
            queryResult = setQuerry.dbSetQuery("SELECT * FROM Category;");

            while (queryResult.next()) {
                categories.put(queryResult.getInt("ID"),
                        queryResult.getString("NAME"));
            }

            // now action to items
            queryResult = setQuerry.dbSetQuery("SELECT * FROM Items WHERE "
                    + "ID_AGREEMENT = " + id + ";");
            Map<String, String> items = new HashMap<>();

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

    }

    /**
     * @see @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // map
        Map<String, Integer> Ids = new HashMap<>();

        Ids.putAll(lastAgreement());
        if (Ids.get("CustID") != null) {
            getUserInfo(Ids.get("CustID"));
            getItemsFromAgreement(Ids.get("AgrID"));

            // next i create new payment with constructor :)
            lastCredit = new CreditForm(itemsList, paymentPorperies, userInfo, true);
        }

    }
}
