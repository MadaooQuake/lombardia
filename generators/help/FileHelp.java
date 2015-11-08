package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class FileHelp implements HelpStrategy {
    
    String textT = "<html><p> <u>Zapisz bazę</u>"
            + " - Zapisuje bazę do osobnego pliku.</p>"
            + "<p><u>Wczytaj bazę</u>"
            + " - Wczytuje bazę z pliku <br>(tylko pliki typu db)</p>"
            + "<br />"
            + "<p><u>Połącz bazę</u> - Łączy bazę z wybraną bazą <br />"
            + "(tylko pliki typu db)</p>"
            + "<p><u>Wyczyść bazę</u> - Usuwa wszystkie wpisy zawarte w bazie."
            + "</p>"
            + "</html>";
    String titleT = "<html><h2>Menu - Plik</h2></html>";
    
    @Override
    public void getText(JLabel title, JLabel text) {
       title.setText(titleT);
       text.setText(textT);
    }
        
}
