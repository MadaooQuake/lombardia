package lombardia2014.generators;

import com.itextpdf.text.DocumentException;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import lombardia2014.core.ConfigRead;
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
    ConfigRead config = new ConfigRead();

    List<HashMap<String, String>> operations = new ArrayList<>();
    List<HashMap<String, String>> agrrements = new ArrayList<>();

    public DailyReport() {
        agrrements = getQuery.getDailyAgreements();
        operations = operationList.getDailyOperations();
        prepareAgrrements();
        prepareOperations();
        generateDocument();
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
            String[] dataTable = {"", "", "", "", "", "", "", ""};
            dataTable[0] = operation.get("Data");
            String data = operation.get("Operacje");
            String[] elements = data.split(":");
            if (elements.length > 2) {
                dataTable[1] = elements[0];
                dataTable[2] = elements[1];
                switch (elements[0]) {
                    case "Wpłata":
                        dataTable[3] = elements[2];
                        dataTable[7] = dataTable[3];
                        break;
                    case "Wypłata":
                        dataTable[3] = "-" + elements[2];
                        dataTable[7] = dataTable[3];
                        break;
                    default:
                        dataTable[5] = elements[2];
                        dataTable[7] = dataTable[5];
                        break;
                }

                if (elements.length == 4) {
                    dataTable[5] = elements[3];
                    double commission = Double.parseDouble(elements[2]) - Double.parseDouble(elements[3]);
                    dataTable[6] = Double.toString(commission);
                    dataTable[7] = elements[2];
                }
                model.addRow(dataTable);
            } else if (elements[0].equals("Sprzedano za")) {
                dataTable[1] = elements[0];
                dataTable[5] = elements[1];
                float value = Float.parseFloat(elements[1]);
                config.readFile();
                float vat = config.getVat();
                dataTable[6] = Float.toString(value * vat);
                model.addRow(dataTable);
            }

        }

    }

    public void generateDocument() {
        try {
            pdf = new PDFCreator(outputFileName, formname);
            pdf.SetLandscapeView();
            pdf.SetRowsPerPage(rowsPerPage);
            pdf.CreatePDF(model, headers, headersWidth);

            JOptionPane.showMessageDialog(null, "Operacje PDF został wygenerowany.",
                    "Generowanie PDF", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | DocumentException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
            JOptionPane.showMessageDialog(null, "Błąd podczas generowania operacji.",
                    "Generowanie PDF", JOptionPane.ERROR_MESSAGE);
        }

        try {
            String dir = System.getProperty("user.dir");

            File dirAgre = new File(dir);

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(dirAgre);
            }
        } catch (Exception ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }
}
