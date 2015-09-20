/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class ProgressBarThread extends Thread {

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
    public void run() {
        Runnable runner = new Runnable() {

            @Override
            public void run() {
                try {
                    for (int i = 0; i < max; i++) {
                        progress.updateBar();
                        Thread.sleep(50);
                    }
                } catch (InterruptedException ex) {
                    LombardiaLogger startLogging = new LombardiaLogger();
                    String text = startLogging.preparePattern("Error", ex.getMessage()
                            + "\n" + Arrays.toString(ex.getStackTrace()));
                    startLogging.logToFile(text);
                }
            }

        };

        progress.closePbar();
    }

}
