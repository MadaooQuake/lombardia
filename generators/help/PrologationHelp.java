package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class PrologationHelp implements HelpStrategy {

    String textT = "<html>"
            + "<p>"
            + "Lista wyświetla nam umowy które możemy  <br />"
            + "przedłużyć po przez dwukrotne kliknięcie na<br />"
            + "wybranej umowe lub wybraniu jej i kliknięciu<br/>"
            + " na przycisk <u>przedłuż</u>.<br/>"
            + "<ul>"
            + ""
            + "</ul>"
            + "</p>"
            + "</html>";
    String titleT = "<html>Przedłużenie pożyczki<br /></html>";

    @Override
    public void getText(JLabel title, JLabel text) {
        title.setText(titleT);
        text.setText(textT); 
    }

}
