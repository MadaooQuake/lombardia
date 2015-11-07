package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class HelpContext {

    private HelpStrategy strategy;

    public void setHelpStrategy(HelpStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void getText(JLabel title, JLabel text) {
        strategy.getText(title, text);
    }
    
}
