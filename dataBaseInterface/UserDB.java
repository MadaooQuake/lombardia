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
import java.util.ArrayList;
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

    //change password
    public void changePassword(String password, int idUser) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("UPDATE Users SET "
                    + "PASSWORD = '" + password
                    + "' WHERE ID =" + idUser + ";");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //get all users
    public List<HashMap<String, String>> getAllUsers() {
        List<HashMap<String, String>> users = new ArrayList();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT Users.NAME AS NAME, Users.SURNAME AS SURNAME, "
                    + "Auth.NAME AS POSITION FROM Users,Auth"
                    + " WHERE Users.ID_auth = Auth.ID;");

            while (queryResult.next()) {
                Map<String, String> user = new HashMap<>();

                user.put("NAME", queryResult.getString("NAME"));
                user.put("SURNAME", queryResult.getString("SURNAME"));
                user.put("POSITION", queryResult.getString("POSITION"));
                users.add((HashMap<String, String>) user);
            }

            setQuerry.closeDB();
        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }

        return users;
    }

    //get user id
    public Integer getIDUser(String Name, String Surename) {
        int id = 0;

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT * FROM Users WHERE NAME LIKE '"
                    + Name
                    + "' and SURNAME LIKE '" + Surename + "';");

            while (queryResult.next()) {
                System.out.println(queryResult.getInt("ID"));
                id = queryResult.getInt("ID");
            }

            setQuerry.closeDB();
        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }

        return id;
    }

    public void deleteUser(String Name, String Surename) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            setQuerry.dbSetQuery("DELETE FROM Users WHERE NAME LIKE '"
                    + Name
                    + "' and SURNAME LIKE '" + Surename + "';");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //delete user
}
