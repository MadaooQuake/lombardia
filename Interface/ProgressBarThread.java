/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface;

import java.util.Arrays;
import javax.swing.SwingWorker;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class ProgressBarThread extends SwingWorker<Void, Void> {

    ProgressBar progress = null;
    int max = 10;

    public ProgressBarThread() {
        progress = new ProgressBar(max);
    }

    public ProgressBarThread(int max) {
        this.max = max;
        progress = new ProgressBar(max);
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            for (int i = 0; i < max; i++) {
                progress.updateBar();
                Thread.sleep(50);
            }
            progress.activeButton();
        } catch (InterruptedException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
        return null;
    }

}
