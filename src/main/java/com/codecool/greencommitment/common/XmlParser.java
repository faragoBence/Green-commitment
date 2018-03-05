package com.codecool.greencommitment.common;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlParser {
    Document doc;
    Map<String, List<Measurement>> measurementsMap = new HashMap<>();

    public Document getDoc() {
        return doc;
    }

    public Map<String, List<Measurement>> getMeasurements() {
        return measurementsMap;
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
                    if (measurementsMap.containsKey(id)) {
                        measurementsMap.get(id).add(new TemperatureMeasurement(time, unit, type));
                    } else {
                        List<Measurement> tempList = new ArrayList<>();
                        tempList.add(new TemperatureMeasurement(time, unit, type));
                        measurementsMap.put(id, tempList);
                    }
                } else {
                    if (measurementsMap.containsKey(id)) {
                        measurementsMap.get(id).add(new MoistureMeasurement(time, unit, type));
                    } else {
                        List<Measurement> tempList = new ArrayList<>();
                        tempList.add(new MoistureMeasurement(time, unit, type));
                        measurementsMap.put(id, tempList);
                    }
                }


            }
        }
    }

    public void writeToXML(Map<String, List<Measurement>> measurementsMap) {
        try {
            for (Map.Entry<String, List<Measurement>> entry : measurementsMap.entrySet()) {
                String id = entry.getKey();
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Element rootElement;
                doc = docBuilder.newDocument();

                if (!new File("src/main/java/com/codecool/greencommitment/" + id + ".xml").exists()) {
                rootElement = doc.createElement("measurements");
                doc.appendChild(rootElement);
            } else {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    Document document = documentBuilder.parse("src/main/java/com/codecool/greencommitment/" + id + ".xml");
                Element ror = document.getDocumentElement();
                rootElement = (Element) doc.importNode(ror, true);
                doc.appendChild(rootElement);
            }
                List<Measurement> mesures = measurementsMap.get(id);
                for (Measurement measurement : mesures) {
                    Element measure = doc.createElement("measurement");
                    Attr attr = doc.createAttribute("id");
                    attr.setValue(id);
                    measure.setAttributeNode(attr);
                    rootElement.appendChild(measure);

                    Element time = doc.createElement("time");
                    time.appendChild(doc.createTextNode(Long.toString(measurement.getCurrentTime())));
                    measure.appendChild(time);

                    Element unit = doc.createElement("unit");
                    unit.appendChild(doc.createTextNode(Integer.toString(measurement.getUnit())));
                    measure.appendChild(unit);

                    Element type = doc.createElement("type");
                    type.appendChild(doc.createTextNode(measurement.getUnitOfMeasurement()));
                    measure.appendChild(type);
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File("src/main/java/com/codecool/greencommitment/" + id + ".xml"));

                transformer.transform(source, result);

                System.out.println("File saved!");
            }

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
