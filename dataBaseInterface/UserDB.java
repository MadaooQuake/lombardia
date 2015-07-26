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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombardia2014.Interface.menu.ListUsers;

/**
 *
 * @author Domek
 */
public class UserDB {

    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;

    // get auth
    public Map<String, Integer> allAuth() {
        Map<String, Integer> auth = new HashMap<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT * FROM Auth;");

            while (queryResult.next()) {
                auth.put(queryResult.getString("NAME"), queryResult.getInt("ID"));
            }

            setQuerry.closeDB();
        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }

        return auth;
    }

    // add user
    public void addUser(String name, String surename, String phone, String adress,
            String login, String password, int idAuth) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("INSERT INTO Users (NAME, SURNAME,"
                    + " ADDRESS, PHONE, LOGIN, PASSWORD, ID_auth) VALUES"
                    + "('" + name + "','" + surename
                    + "','" + adress + "','" + phone
                    + "','" + login + "','" + password
                    + "'," + idAuth + ");");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //update user position 
    public void updateUserPosition(int newPosition, int idUser) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("UPDATE Users SET ID_auth = "
                    + newPosition + " WHERE ID = " + idUser + ";");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
