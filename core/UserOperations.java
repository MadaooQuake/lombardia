/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombardia2014.dataBaseInterface.QueryDB;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class UserOperations {

    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    String userName, userSuerename;
    Date curretDate = null;
    SimpleDateFormat ft = new SimpleDateFormat("dd.MM.YYYY HH:mm");

    /**
     * @param user
     * @see constucrot witch get null or more elements in string, and set user
     * info for object
     */
    public UserOperations(String... user) {
        if (user.length == 2) {
            userName = user[0];
            userSuerename = user[1];
        } else {
            userName = "System";
            userSuerename = "";
        }
    }

    /**
     * @param operation
     * @see save elements to db
     */
    public void saveOperations(String operation) {
        try {
            curretDate = new Date();
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("INSERT INTO OPerations "
                    + "(USER, DATE, OPERATIONS) VALUES ("
                    + "'" + userName + " " + userSuerename + "','"
                    + ft.format(curretDate) + "','"
                    + operation + "'"
                    + ");");
            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    /**
     *
     * @param DataRange
     * @return
     * @see this element read all operations
     */
    public List<String[]> readOperation(String DataRange) {
        List<String[]> operationList = new ArrayList<>();
        try {
            curretDate = new Date();
            String currentMounth = ft.format(curretDate);
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT USER, DATE, OPERATIONS"
                    + " FROM OPerations ");

            if (DataRange.equals("Mięsiąc") || DataRange.equals("Month")) {
                while (queryResult.next()) {
                    String dateOperation = queryResult.getString("DATE");
                    if (currentMounth.substring(3, 5)
                            .equals(dateOperation.subSequence(3, 5))) {

                        String[] tmpOperation = {queryResult.getString("USER"),
                            dateOperation, queryResult.getString("OPERATIONS")};
                        operationList.add(tmpOperation);
                    }
                }
            } else {
                while (queryResult.next()) {
                    String[] tmpOperation = {queryResult.getString("USER"),
                        queryResult.getString("DATE"),
                        queryResult.getString("OPERATIONS")};
                    operationList.add(tmpOperation);
                }
            }

            setQuerry.closeDB();
            return operationList;
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return operationList;

    }

    /**
     * Remove operations from list
     *
     * @param properies
     */
    public void removeOperation(String... properies) {
        // date, user, operation
        try {
            curretDate = new Date();
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("DELETE FROM OPerations"
                    + " WHERE USER LIKE '" + properies[0]
                    + "' AND DATE LIKE '" + properies[1]
                    + "' AND OPERATIONS LIKE '" + properies[2] + "';");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

}
