package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class ObjectListHelp implements HelpStrategy {

    String textT = "<html>"
            + "<p>"
            + "Pokazuje lilstę z przedmiotami, kttóre są podpięte<br/>"
            + "umowy, lub tez nie. Każdy depozyt możemy <br/>"
            + "edytować lub sprzedać, klikając dwukrotnie na <br />"
            + "wybrany przedmiot, wtedy pojawia się <br />"
            + "formularz, który można aktywowac <br /> "
            + "przez przycisk <u>Edytuj</u> <br/>"
            + " lub po kliknięciu na <u>Sprzedaj</u> pojawia się formularz<br />"
            + "sprzedaży tego przedmiotu. "
            + "<br/> Uwaga: Depozyty podpięte do umowy nie możemy "
            + "<br/>sprzedać"
            + "</p>"
            + "</html>";
    String titleT = "<html><h2>Lista depozytów</h2></html>";

    @Override
    public void getText(JLabel title, JLabel text) {
        title.setText(titleT);
        text.setText(textT);
    }

}
