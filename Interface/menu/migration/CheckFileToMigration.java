/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.menu.migration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import lombardia2014.generators.LombardiaLogger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author Domek
 */
public class CheckFileToMigration {

    String file;

    public CheckFileToMigration(String file_) {
        file = file_;
        
        
    }
    
    public boolean checkElements() {
        boolean ans = false;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            
            ans = workbook.getSheet("ZESTAW") != null &&
                    workbook.getSheet("LISTA1") != null ; 
            
        } catch (IOException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", ex.getMessage()
                        + "\n" + Arrays.toString(ex.getStackTrace()));
                startLogging.logToFile(text);
        }
        return ans;
    }

}
