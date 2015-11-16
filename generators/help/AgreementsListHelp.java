/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class AgreementsListHelp implements HelpStrategy {

    String textT = "<html>"
            + "<p>"
            + "Pokazuje lilstę umow. Możemy<br/>"
            + "mżemy wyszukac konretną umowę po jej <br />"
            + "identyfikatorze lub po przez Imię i Nazwisko <br />"
            + "klienta <br /> "
            + "</p>"
            + "</html>";
    String titleT = "<html><h2>Lista umów</h2></html>";

    @Override
    public void getText(JLabel title, JLabel text) {
        title.setText(titleT);
        text.setText(textT);
    }
    
}
