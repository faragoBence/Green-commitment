package com.codecool.greencommitment.common;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XmlParser {

    public void writeToXml(Measurement measurement, String id) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("measurement");
            Attr attr = doc.createAttribute("id");
            attr.setValue(id);
            rootElement.setAttributeNode(attr);
            doc.appendChild(rootElement);

            Element time = doc.createElement("time");
            time.appendChild(doc.createTextNode(Long.toString(measurement.getCurrentTime())));
            rootElement.appendChild(time);

            Element unit = doc.createElement("unit");
            unit.appendChild(doc.createTextNode(Integer.toString(measurement.getUnit())));
            rootElement.appendChild(unit);

            Element type = doc.createElement("type");
            type.appendChild(doc.createTextNode(measurement.getUnitOfMeasurement()));
            rootElement.appendChild(type);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);
            StreamResult kaka = new StreamResult(new File("src/main/java/com/codecool/greencommitment/Test.xml"));
            transformer.transform(source, kaka);

            System.out.println("File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}
