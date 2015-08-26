/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.core;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombardia2014.generators.LombardiaLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Domek
 */
public class SaveConfig {

    File fXmlFile = null;
    DocumentBuilderFactory dbFactory = null;
    DocumentBuilder dBuilder = null;
    Document doc = null;
    NodeList nList = null;
    Node nNode = null;
    Element eElement = null;

    // read file
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
    
    // change atrigutes
    public void changeElement(String element, String value) {
        NamedNodeMap attr = nNode.getAttributes();
        attr.getNamedItem(element).setNodeValue(value);
    }

}
