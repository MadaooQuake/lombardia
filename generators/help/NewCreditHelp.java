package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class NewCreditHelp implements HelpStrategy {

    String textT = "<html>"
            + "<p>"
            + "Formularz nowej pożyczki posiada "
            + "<br/>wiele pól i zależności gdzie<br />"
            + "poniżej zostają one opisane dokładniej."
            + "<ol>"
            + "<li>Wybieramy klienta lub tworzymy nowego, <br/>"
            + "gdzie formularz klienta ma wymagane pola: <br />"
            + "Imię i nazwisko</li>"
            + "<li>Formularz przedmiotów, gdzie po wybraniu<br/> "
            + "przedmiotu klilkamy na przycisk Dodaj, <br /> by dodać go do umowy"
            + "</li>"
            + "<li>Ostatnim podformularzem są warunki <br /> umowy"
            + "</li>"
            + "<li>Klikamy Generuj ipo chwili pojawi<br />"
            + " nam się dokument umowy o pożyczkę</li>"
            + "</ol>"
            + "</p>"
            + "</html>";
    String titleT = "<html>Nowa pożyczka<br /></html>";

    @Override
    public void getText(JLabel title, JLabel text) {
        title.setText(titleT);
        text.setText(textT);
    }

}
