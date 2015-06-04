/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.core;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.xml.parsers.ParserConfigurationException;
import lombardia2014.generators.LombardiaLogger;
import org.xml.sax.SAXException;

/**
 *
 * @author Domek
 */
public class ConfigRead {

    File fXmlFile = null;
    DocumentBuilderFactory dbFactory = null;
    DocumentBuilder dBuilder = null;
    Document doc = null;
    NodeList nList = null;
    Node nNode = null;
    Element eElement = null;

    public void readFile() {
        try {
            fXmlFile = new File("config.xml");
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);

            nList = doc.getElementsByTagName("kongiguracja");
            nNode = nList.item(0);
            eElement = (Element) nNode;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    /**
     * start read config :D
     */
    /**
     * @return @see read database name
     */
    public String dbName() {
        return eElement.getElementsByTagName("nazwaBazy")
                .item(0).getTextContent();
    }

    /**
     * @return @see read minimal fee
     */
    public float getMinFee() {
        String minFee = eElement.getElementsByTagName("oplataMinimalna")
                .item(0).getTextContent();
        minFee = minFee.replace(",", ".");
        return Float.parseFloat(minFee);
    }

    /**
     * @return @see manipulation fee
     */
    public float getManFee() {
        String manFee = eElement.getElementsByTagName("oplataManipulacyjna")
                .item(0).getTextContent();
        manFee = manFee.replace(",", ".");
        return Float.parseFloat(manFee);
    }

    /**
     * @return @see daily profit
     */
    public float getDailyProfit() {
        String dailyProfit = eElement.getElementsByTagName("zyskNaDzien")
                .item(0).getTextContent();
        dailyProfit = dailyProfit.replace(",", ".");
        return Float.parseFloat(dailyProfit);
    }

    /**
     * @return @see get vat
     */
    public float getVat() {
        String vat = eElement.getElementsByTagName("vat")
                .item(0).getTextContent();
        vat = vat.replace(",", ".");
        vat = "0." + vat;
        return Float.parseFloat(vat);
    }

    /**
     *
     * @return
     */
    public float getLombardRate() {
        String vat = eElement.getElementsByTagName("stopa")
                .item(0).getTextContent();
        return Float.parseFloat(vat);
    }

    /**
     *
     * @return
     */
    public float getRSO() {
        String vat = eElement.getElementsByTagName("rso")
                .item(0).getTextContent();
        return Float.parseFloat(vat);
    }
    
    public float getPrematureDevotion() {
        String vat = eElement.getElementsByTagName("kwotzaPrzedterminowa")
                .item(0).getTextContent();
        return Float.parseFloat(vat);
    }
    
}
