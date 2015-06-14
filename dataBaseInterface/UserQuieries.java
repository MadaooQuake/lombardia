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
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class UserQuieries {

    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    int privilegages, ID;

    public boolean checkUserAutorization(String login, String password) {
        boolean result = false;

        try {
            String user = null, passwordInDB = null;

            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT Users.ID, Users.NAME, Users.PASSWORD"
                    + ", Auth.ID AS ID_a, Users.ID_auth FROM Users, Auth "
                    + "WHERE LOGIN LIKE '" + login
                    + "' AND Auth.ID = Users.ID_auth "
                    + ";");

            while (queryResult.next()) {
                user = queryResult.getString("NAME");
                passwordInDB = queryResult.getString("PASSWORD");
                privilegages = queryResult.getInt("ID_a");
                ID = queryResult.getInt("ID");
            }

            result = !user.isEmpty() && passwordInDB.equals(password);

            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return result;
    }

    public Integer getPrivilages() {
        return privilegages;
    }

    public Integer getID() {
        return privilegages;
    }

}
