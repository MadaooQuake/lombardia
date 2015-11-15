package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class ObjectForSellListHelp implements HelpStrategy {
    
    String textT = "<html>"
            + "<p>"
            + "Pokazuje lilstę z przedmiotami, które nie są <br/>"
            + "podpięte do umów. Każdy depozyt możemy <br/>"
            + "edytować lub sprzedać, klikając dwukrotnie na <br />"
            + "wybrany przedmiot, wtedy pojawia się <br />"
            + "formularz, który można aktywowac <br /> "
            + "przez przycisk <u>Edytuj</u> <br/>"
            + " lub po kliknięciu na <u>Sprzedaj</u> pojawia się formularz<br />"
            + "sprzedaży tego przedmiotu. "
            + "</p>"
            + "</html>";
    String titleT = "<html><h2>Lista depozytów na sprzedaż</h2></html>";

    @Override
    public void getText(JLabel title, JLabel text) {
        title.setText(titleT);
        text.setText(textT);
    }
}
