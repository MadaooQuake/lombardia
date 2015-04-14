/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014;

import lombardia2014.dataBaseInterface.ConnectDB;
import lombardia2014.Interface.Authorization;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author marcin
 */
public class Lombardia2014 {

    static Authorization authPanel = new Authorization();
    static ConnectDB conDB = null;


    /**
     * @see run all aplication
     * @param args the command line arguments
     */

    public static void main(String[] args) {

        //Create DB
        conDB = new ConnectDB();

        LombardiaLogger startLogging = new LombardiaLogger();
        String text = startLogging.preparePattern("Info", "Uruchomienie aplikacji");
        startLogging.logToFile(text);
        // in this method i run all main elements, and crerate class
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                authPanel.createPanel();
            }
        });

    }

}
