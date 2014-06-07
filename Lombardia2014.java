/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014;

import lombardia2014.Interface.Authorization;

/**
 *
 * @author marcin
 */
public class Lombardia2014 {

    static Authorization authPanel = new Authorization();

    /**
     * @see run all aplication
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // in this method i run all main elements, and crerate class
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                authPanel.createPanel();
            }
        });

    }

}
