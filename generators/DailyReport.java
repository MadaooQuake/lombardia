package lombardia2014.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.table.DefaultTableModel;
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
    DefaultTableModel model = new DefaultTableModel();

    List<HashMap<String, String>> operations = new ArrayList<>();
    List<HashMap<String, String>> agrrements = new ArrayList<>();

    public DailyReport() {
        agrrements = getQuery.getDailyAgreements();
        operations = operationList.getDailyOperations();
        prepareAgrrements();
        prepareOperations();
    }

    //check agreements
    public void prepareAgrrements() {
        for (HashMap<String, String> agreement : agrrements) {
            String[] dataTable = {agreement.get("Data Zawarcia"), "Pożyczka", agreement.get("NR Umowy"), "", agreement.get("Wartosc"), "", "", "-" + agreement.get("Wartosc")};
            model.addRow(dataTable);
        }
    }

    // check operations
    public void prepareOperations() {
        for (HashMap<String, String> operation : operations) {
            String[] dataTable = new String[8];

            dataTable[0] = operation.get("Data");

            String data = operation.get("Operacje");
            String[] elements = data.split(":");
            if (elements.length > 2) {
                dataTable[1] = elements[0];
                dataTable[2] = elements[1];
                if (elements[0].equals("Wpłata")) {
                    dataTable[3] = elements[2];
                    dataTable[4] = "";
                    dataTable[5] = "";
                    dataTable[6] = "";
                    dataTable[7] = dataTable[3];
                } else if (elements[0].equals("Wypłata")){
                    dataTable[3] = "-" + elements[2];
                    dataTable[4] = "";
                    dataTable[5] = "";
                    dataTable[6] = "";
                    dataTable[7] = dataTable[3];
                } else {
                    dataTable[3] = "";
                    dataTable[4] = "";
                    dataTable[5] = elements[2];
                    dataTable[6] = "";
                    dataTable[7] = dataTable[5];
                }

                if (elements.length == 4) {
                    dataTable[3] = "";
                    dataTable[4] = "";
                    dataTable[5] = elements[3];
                    double commission = Double.parseDouble(elements[2]) - Double.parseDouble(elements[3]);
                    dataTable[6] = Double.toString(commission);
                    dataTable[7] = elements[2];
                }

                model.addRow(dataTable);
            }

        }

    }
}
