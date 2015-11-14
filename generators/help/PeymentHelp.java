package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class PeymentHelp implements HelpStrategy {

    String textT = "<html>"
            + "<p>"
            + "Po naciśniećiu Wpłata lub wypłata, generuje<br/>"
            + "się formularz gdzie posiada on dwa pola:"
            + "<br/>"
            + "<ul>"
            + "<li><u>Podaj Kwotę</u> - podajemy wartość"
            + "<br/>jaką wpłącamy lub wypłacamy</li>"
            + "<li><u>Podaj Opis</u> - podajemy w opisie"
            + "<br/>cel wpłaty lub wypłaty</li>"
            + "</ul>"
            + "</p>"
            + "</html>";
    String titleT = "<html>Wpłata/Wypłata <br /></html>";

    @Override
    public void getText(JLabel title, JLabel text) {
        title.setText(titleT);
        text.setText(textT);
    }

}
