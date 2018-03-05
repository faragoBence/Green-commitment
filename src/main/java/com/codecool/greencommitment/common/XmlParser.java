package com.codecool.greencommitment.common;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {
    Document doc;


    List<Measurement> measurements = new ArrayList<Measurement>();

    public Document getDoc() {
        return doc;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void createDoc(Measurement measurement, String id) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            doc = docBuilder.newDocument();
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

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }

    public void readDoc(Document document) {
        NodeList nList = document.getElementsByTagName("measurement");
        Element measurement = document.getDocumentElement();
        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                String id = measurement.getAttribute("id");
                long time = Long.parseLong(eElement.getElementsByTagName("time").item(0).getTextContent());
                String type = eElement.getElementsByTagName("type").item(0).getTextContent();
                int unit = Integer.parseInt(eElement.getElementsByTagName("unit").item(0).getTextContent());
                if (type.equals("Celsius")) {
                    measurements.add(new TemperatureMeasurement(time, unit, type));
                } else {
                    measurements.add(new MoistureMeasurement(time, unit, type));
                }


            }
        }
    }
}
