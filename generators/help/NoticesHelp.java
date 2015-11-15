package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class NoticesHelp implements HelpStrategy {

    String textT = "<html>"
            + "<p>"
            + "Pokazuje lilstę z uwagami, kttóre<br/>"
            + "infromują o jakimś zarzeniu. Mozemy dodawać<br/> i usuwać uwagi. "
            + "Uwagi nie odnoszą się do umów."
            + "<br/>"
            + "<u>Dodaj</u> - dodajemy uwagę<br />"
            + "po przez wypełnienie formularza i kliknięcia<br />"
            + " na dodaj"
            + "<u>Usuń</u> - usuwa zaznaczoną uwagę<br/>"
            + "</p>"
            + "</html>";
    String titleT = "<html><h2>Uwagi</h2></html>";

    @Override
    public void getText(JLabel title, JLabel text) {
        title.setText(titleT);
        text.setText(textT);
    }

}
