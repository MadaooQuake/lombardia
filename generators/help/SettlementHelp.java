package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class SettlementHelp implements HelpStrategy {
    
    String textT = "tekst", titleT = "Tytuł 3";

    @Override
    public void getText(JLabel title, JLabel text) {
        title.setText(titleT);
        text.setText(textT);
    }
}
