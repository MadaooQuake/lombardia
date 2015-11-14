package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class ReturnCreditHelp implements HelpStrategy {

    String textT = "<html>"
            + "<p>"
            + "Pojawia nam się lista z umoami, gdzie <br />"
            + "by filtrować możemy użyć wyszukiwarki, <br />"
            + "która skłąda się z dwóch pól. Pierwsze<br/>"
            + "pole pozwala nam wyszukiwac po id umowy,  <br/>"
            + "rugie natomiast po imieniu i nazwisku."
            + "<br/> Wyszukiwani zadziała doppiero po <br/>"
            + "kliknięciu naprzycisk <u>Wyszukaj</u>."
            + "Przycisk <u>Anuluj</u><br/> wyłącza listę <u>"
            + "Zwrot</u>."
            + "<ul>"
            + ""
            + "</ul>"
            + "</p>"
            + "</html>";
    String titleT = "<html>Zwrot pożyczki<br /></html>";

    @Override
    public void getText(JLabel title, JLabel text) {
        title.setText(titleT);
        text.setText(textT);
    }

}
