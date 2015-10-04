package lombardia2014.generators;

import lombardia2014.dataBaseInterface.MainDBQuierues;
import lombardia2014.dataBaseInterface.UserOperations;

/**
 *
 * @author Domek
 */
public class DailyReport {

    MainDBQuierues getQuery = new MainDBQuierues();
    UserOperations operationList = new UserOperations();
    String[] headers = {"Data", "Operacja", "Identyfikator", "Z/DO kasy", "Na pożyczki", "Sprzedaż", "Prowizja", "Razzem"};
    float[] headersWidth = {3.0f, 5.0f, 2.0f, 2.0f, 2.0f, 2.0f, 2.0f, 2.0f};
    String formname = "Dzienny Raport";
    String outputFileName = "RaportDzienny.pdf";
    PDFCreator pdf = null;
    int windowWidth = 860;
    int windowHeigth = 500;
    int rowsPerPage = 40;

    public DailyReport() {
        System.out.println(getQuery.getDailyAgreements());
        System.out.println(operationList.getDailyOperations());
    }
    
    //check agreements
    
    
    // check operations

}
