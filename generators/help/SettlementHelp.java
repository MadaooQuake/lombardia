package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class SettlementHelp implements HelpStrategy {

    String textT = "<html>"
            + "<p>"
            + "<u>Rozliczenia miesięczne</u> - "
            + "Wyświetla table z<br/> rozliczeniami miesięcznymi.<br/>"
            + " Mamy możliwość wygenrowania pliku<br/> na postawie tej tabeli. "
            + "</p>"
            + "<p>"
            + "<u>Rozliczenia roczne</u> - "
            + "Wyświetla table z<br/> rozliczeniami do wybranego dnia.<br/>"
            + " Mamy możliwość wygenrowania pliku<br/> na postawie tej tabeli. "
            + "</p>"
            + "<p>"
            + "<u>Inwentaryzacja</u> - "
            + "Wyświetla table z<br/> przedmiotami do wybranego dnia.<br/>"
            + " Mamy możliwość wygenrowania pliku<br/> na postawie tej tabeli. "
            + "</p>"
            + "<p>"
            + "<u>Raport dzienny</u> - "
            + "Generuje nam plik z operacjami<br/> jakie zostały przeprowadzone danego dnia."
            + "</p>"
            + "</html>";
    String titleT = "<html><h2>Rozlizenia</h2></html>";

    @Override
    public void getText(JLabel title, JLabel text) {
        title.setText(titleT);
        text.setText(textT);
    }
}
