package lombardia2014.Interface.menu.migration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import lombardia2014.generators.LombardiaLogger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Domek
 */
public class MigrationFromOld {

    String file;
    FileInputStream fileInputStream = null;
    HSSFWorkbook workbook = null;

    // firs open to read file
    public MigrationFromOld(String File) {
        file = File;
        try {
            fileInputStream = new FileInputStream(file);

        } catch (IOException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    // go to ZESTAW elements
    // now i read customer info and items elements from agreements
    public void readMainElements() throws IOException {
        workbook = new HSSFWorkbook(fileInputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            Iterator cellIterator = row.cellIterator();
            while(cellIterator.hasNext()) {
                Cell cell = (Cell) cellIterator.next();
                if(!cell.equals(""))
                    System.out.println(cell);
            }
        }
    }
    
    // go to LISTA1 and add items
    
    
    // insert elements to db -> in new file maybe ?
}
