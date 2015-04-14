/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.dataBaseInterface;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class SaveDB {

    String orginalFile, newFile;                                                        
    // file name + patern

    public SaveDB(String patern) {
        newFile = patern;
        orginalFile = canonicalName();
        newFile = prepareNewFile(newFile);
    }

    // method witch get the cannonical location db file
    private String canonicalName() {
        String cName;
        cName = System.getProperty("user.dir");
        cName += "\\lombardia.db";
        return cName;
    }

    // prepare new file
    private String prepareNewFile(String name) {
        String fileName = name;
        if (!fileName.contains(".db")) {
            fileName += ".db";
        }
        return fileName;
    }

    // save file
    public void savingFille() {
        InputStream is;
        OutputStream os;
        try {
            is = new FileInputStream(orginalFile);
            os = new FileOutputStream(newFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }

            is.close();
            os.close();
        } catch (IOException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

    }
}
