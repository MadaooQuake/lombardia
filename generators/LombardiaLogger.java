/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.generators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Domek
 */
public class LombardiaLogger {

    File logFile = null;
    BufferedWriter writeToFile = null;
    Date curretDate = null;
    SimpleDateFormat ft = new SimpleDateFormat("dd.MM.YYYY HH:mm");

    /**
     * log format [dd.MM.YYYY HH:mm] [INFO] [Treść] \n
     */
    // Create File
    public LombardiaLogger() {
        try {
            logFile = new File("log.txt");

            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException io) {
            System.err.println("Problem z otworzeniem pliku logu");
        }

    }
    
    // Prepare log file
    public String preparePattern(String logType, String text) {
        String fullPattern = null;
        curretDate = new Date();
        // create date
        fullPattern = "[" + ft.format(curretDate) + "] ";
        // rest element
        fullPattern += "[" + logType + "]:" + text;  
        
        return fullPattern;
    }

    // Fine
    public boolean logToFile(String Pattern) {
        try {
            writeToFile = new BufferedWriter(new FileWriter(logFile, true));
            writeToFile.write(Pattern);
            writeToFile.newLine();
            System.err.println(Pattern);
            writeToFile.close();
        } catch (IOException io) {
            System.err.println("Problem z zapisem do pliku logu");
            return false;
        }
        return true;
    }


}
