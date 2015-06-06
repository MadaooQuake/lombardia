/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.core;

import javax.swing.UIManager;

/**
 *
 * @author Domek
 */
public class SetLocatnion {

    public SetLocatnion() {
        UIManager.put("OptionPane.yesButtonText", "Tak");
        UIManager.put("OptionPane.noButtonText", "Nie");
        UIManager.put("OptionPane.cancelButtonText", "Anuluj");
        UIManager.put("OptionPane.titleText", "Tytuł");
    }
}
