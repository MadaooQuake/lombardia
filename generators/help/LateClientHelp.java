package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class LateClientHelp implements HelpStrategy {

    String textT = "<html>"
            + "<p>"
            + "Pokazuje lilstę z umowami, kttóre przekroczyły<br/>"
            + "termin oddania. Po zaznaczeniu poszczególnej<br/>"
            + " umowy"
            + "można ją rozwiązać klikając na przycisk<br/> <u>Rozwiąż</u>."
            + "</p>"
            + "</html>";
    String titleT = "<html><h2>Spóźnieni</h2></html>";

    @Override
    public void getText(JLabel title, JLabel text) {
        title.setText(titleT);
        text.setText(textT);
    }

}
