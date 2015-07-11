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
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import lombardia2014.dataBaseInterface.MainDBQuierues;


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
    MainDBQuierues getQuery = new MainDBQuierues();

    //maps
    Map<String, String> paymentPorperies = getQuery.getLastAgreement();
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

        // get ids
        ids.put("CustID", Integer.parseInt(paymentPorperies.get("CustID")));
        ids.put("AgrID", Integer.parseInt(paymentPorperies.get("AgrID")));

        return ids;
    }

    /**
     * @see this class get user info and save to hashmap
     *
     */
    private void getUserInfo(int id) {
        userInfo = getQuery.getUserByID(id);
    }

    /**
     * @see this method get items from
     */
    private void getItemsFromAgreement(int id) {
        categories = getQuery.getCategoriesWithID();
        itemsList = getQuery.itemsElement(categories, id);
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
