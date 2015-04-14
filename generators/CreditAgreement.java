/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.generators;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import lombardia2014.ConfigRead;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.xml.sax.SAXException;

/**
 *
 * @author Mateusz
 */
public class CreditAgreement {

    Map<Integer, HashMap> Items = new HashMap<>();
    Map<String, String> userInfo = new HashMap<>();
    Map<String, String> agreementInfo = new HashMap<>();
    private OPCPackage docx;
    int count = 0;
    String[] value = new String[22];
    String document = null;
    CreditAgreement ca = null;
    ConfigRead readParameters = new ConfigRead();
    PriceParser createDigitWords = null;

    public CreditAgreement(HashMap<Integer, HashMap> _tmpMAp,
            HashMap<String, String> _temporaryMap, HashMap<String, String> _temporaryMap2) {
        Items = _tmpMAp;
        userInfo = _temporaryMap;
        agreementInfo = _temporaryMap2;
        createDigitWords = new PriceParser(agreementInfo.get("Kwota"));
    }

    public void putCa(CreditAgreement ca_) {
        ca = ca_;
    }

    public void extractDocx() throws FileNotFoundException,
            IOException,
            XmlException,
            InvalidFormatException,
            OpenXML4JException,
            ParserConfigurationException,
            SAXException {

        XWPFWordExtractor xw = new XWPFWordExtractor(docx);
        document = xw.getText();
    }

    public void fillTable() {

        readParameters.readFile();
        value[0] = agreementInfo.get("NR Umowy");
        value[1] = agreementInfo.get("Data rozpoczecia");
        value[2] = userInfo.get("Imie");
        value[3] = userInfo.get("Nazwisko");
        value[4] = userInfo.get("Adres");
        value[5] = userInfo.get("Pesel");
        value[6] = agreementInfo.get("Kwota");
        value[9] = agreementInfo.get("Odsetki Terminowe");
        value[10] = Float.toString(readParameters.getManFee()); // config
        value[11] = agreementInfo.get("Opłata manipulacyjna");
        value[12] = agreementInfo.get("Opłata magayznowania");
        value[13] = agreementInfo.get("Data zwrotu");
        value[14] = agreementInfo.get("Łączne opłaty");
        value[15] = agreementInfo.get("RRO");
        value[16] = agreementInfo.get("RRSO");
        value[17] = Float.toString(readParameters.getPrematureDevotion()); //from config
        value[18] = agreementInfo.get("Dzienny Profit"); //Dzienny Profit
        value[19] = Float.toString(readParameters.getLombardRate());
        value[20] = Float.toString(readParameters.getMinFee());
        value[21] = createDigitWords.getTranslationNumber();

    }

    public void extractHashMaps() {
        Map<String, String> tmpItem = new HashMap<>();
        value[7] = "";
        for (int i = 0; i <= Items.size(); i++) {
            if (tmpItem.isEmpty()) {
                int size = Items.size();
                for (int k = 0; k < size; k++) {
                    tmpItem.putAll(Items.get(i));
                    if (tmpItem.get("Wartość") == null) {
                        tmpItem.put("Wartość", "");
                    }

                    if (tmpItem.get("Marka") == null) {
                        tmpItem.put("Marka", "");
                    }

                    if (tmpItem.get("Typ") == null) {
                        tmpItem.put("Typ", "");
                    }
                    value[7] += tmpItem.get("Typ")
                            + tmpItem.get("Marka") + tmpItem.get("Model") + " ";
                }
            }
        }

        value[8] = agreementInfo.get("Łączna wartosc");
    }

    /**
     * This method parses the .docx files.
     *
     * @param ca
     * @throws FileNotFoundException
     * @throws IOException
     * @throws XmlException
     * @throws InvalidFormatException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public void docCreate(CreditAgreement ca) throws FileNotFoundException,
            IOException,
            XmlException,
            InvalidFormatException,
            OpenXML4JException,
            ParserConfigurationException,
            SAXException {
        docx = OPCPackage.open("example.docx");
        Date now = new Date();
        String exampleDocDirectory = "example.docx";
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String date = DATE_FORMAT.format(now);
        String docDirectory = "umowa.docx";
        File file = new File(exampleDocDirectory); //give your file name here of
        FileInputStream fs = new FileInputStream(file);
        //which you want to parse text
        if (file.getName().endsWith(".docx")) {

            ca.extractDocx();

            XWPFDocument doc = new XWPFDocument(OPCPackage.open(fs));

            for (int j = doc.getParagraphs().size(); j >= 0; j--) {
                doc.removeBodyElement(j);
            }
            String text = document;
            for (int j = 0; j < 2; j++) {
                XWPFParagraph p = doc.createParagraph();
                XWPFRun r = p.createRun();

                for (int i = 0; i < value.length; i++) {
                    String test = "<VARIABLE" + i + ">";
                    text = text.replace(test, value[i]);

                    // <VARIABLE11>
                }

                r.setText(text);
                r.setFontSize(9);
                r.setFontFamily("Arial");

                doc.write(new FileOutputStream(docDirectory));
            }
        }
    }

    public void docRecieve() {
        try {

            fillTable();
            extractHashMaps();

            docCreate(ca);
        } catch (IOException | XmlException | OpenXML4JException | ParserConfigurationException | SAXException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }
}
