package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class BuyItemHelp implements HelpStrategy {

    String textT = "<html>"
            + "<p>"
            + "Formularz od skupowania przedmiotów z <br />"
            + "czego pola gnerują się po wygraniu odpowiedniej <br />"
            + "kategori. Poczatkowo jest wybrane wyroby jubilerskie.<br/>"
            + "Wyroby jubilerskie i gry można skupować w większej <br/>"
            + " ilośći. Pozostałe obiekty skupuje się pojedynczo."
            + "<br/>"
            + "<ul>"
            + ""
            + "</ul>"
            + "</p>"
            + "</html>";
    String titleT = "<html>Skup przedmiotu<br /></html>";

    @Override
    public void getText(JLabel title, JLabel text) {
        title.setText(titleT);
        text.setText(textT);
    }

}
