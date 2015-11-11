package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class LastAgreementHelp implements HelpStrategy {

    String textT = "<html>"
            + "<p>"
            + "Formularz wyświetla ostatnio dodaną "
            + "pożyczkę.<br/>"
            + "</p>"
            + "</html>";
    String titleT = "<html>Ostatnia umowa<br /></html>";

    @Override
    public void getText(JLabel title, JLabel text) {
        title.setText(titleT);
        text.setText(textT);
    }

}
