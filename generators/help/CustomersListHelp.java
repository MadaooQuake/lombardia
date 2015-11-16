package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class CustomersListHelp implements HelpStrategy {

    String textT = "<html>"
            + "<p>"
            + "Pokazuje lilstę z klientów. Możemy ich<br/>"
            + "edytować, klikając dwukrotnie na <br />"
            + "wybraną osobę, wtedy pojawia się <br />"
            + "formularz, który można aktywowac <br /> "
            + "przez przycisk <u>Edytuj</u> <br/>"
            + "</p>"
            + "</html>";
    String titleT = "<html><h2>Lista klientów</h2></html>";

    @Override
    public void getText(JLabel title, JLabel text) {
        title.setText(titleT);
        text.setText(textT);
    }
}
