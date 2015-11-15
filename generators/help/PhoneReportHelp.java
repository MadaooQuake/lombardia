package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class PhoneReportHelp implements HelpStrategy {

    String textT = "<html>"
            + "<p>"
            + "Pokazuje lilstę ze zgłoszeniami, kttóre<br/>"
            + "infromują o jakimś zarzeniu. Mozemy dodawać<br/> i usuwać zdarzenia"
            + "<br/>"
            + "<u>Dodaj</u> - dodajemy zgłoszenie telefoniczne<br />"
            + "po przez wypełnienie formularza i kliknięcia<br />"
            + " na dodaj"
            + "<u>Usuń</u> - usuwa zaznaczone zgłoszenie<br/>"
            + "telefoniczne"
            + "</p>"
            + "</html>";
    String titleT = "<html><h2>Zgłoszenia Telefonicne</h2></html>";

    @Override
    public void getText(JLabel title, JLabel text) {
        title.setText(titleT);
        text.setText(textT);
    }

}
