package com.codecool.greencommitment.common;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
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

    private DOMSource source;
    public Document doc;
    private Map<String, List<Measurement>> measurementsMap = new HashMap<>();

    public Map<String, List<Measurement>> getMeasurements() {
        return measurementsMap;
    }
    public DOMSource getSource() {
        return source;
    }

    public void createDoc(Measurement measurement, String id) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // root elements

            Element rootElement = doc.createElement("measurement");
            Attr attr = doc.createAttribute("id");
            attr.setValue(id);
            rootElement.setAttributeNode(attr);
            doc.appendChild(rootElement);

            doc = appendChild(rootElement, doc, measurement, id);
            source = new DOMSource(doc);
            this.doc = doc;

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }

    public void readDoc(Document document, Map<String, List<Measurement>> measurementsMap) {
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

                Measurement mes;
                if (type.equals("Celsius")) {
                    mes = new TemperatureMeasurement(time, unit, type);
                } else {
                    mes = new MoistureMeasurement(time, unit, type);
                }
                if (measurementsMap.containsKey(id)) {
                    measurementsMap.get(id).add(mes);
                } else {
                    List<Measurement> tempList = new ArrayList<>();
                    tempList.add(mes);
                    measurementsMap.put(id, tempList);
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
                Document doc = docBuilder.newDocument();

                if (!new File("resources/" + id + ".xml").exists()) {
                    rootElement = doc.createElement("measurements");
                    doc.appendChild(rootElement);
                } else {
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    Document document = documentBuilder.parse("resources/" + id + ".xml");
                    Element ror = document.getDocumentElement();
                    rootElement = (Element) doc.importNode(ror, true);
                    doc.appendChild(rootElement);
                }
                Attr attr = doc.createAttribute("id");
                attr.setValue(id);
                rootElement.setAttributeNode(attr);
                List<Measurement> mesures = measurementsMap.get(id);
                for (Measurement measurement : mesures) {
                    Element measure = doc.createElement("measurement");
                    rootElement.appendChild(measure);
                    doc = appendChild(measure, doc, measurement, id);
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File("resources/" + id + ".xml"));


                transformer.transform(source, result);
            }

        } catch (ParserConfigurationException | TransformerException | IOException | SAXException pce) {
            pce.printStackTrace();
        }
    }

    private Document appendChild(Element rootElement, Document doc, Measurement measurement, String id) {

        Element time = doc.createElement("time");
        time.appendChild(doc.createTextNode(Long.toString(measurement.getCurrentTime())));
        rootElement.appendChild(time);

        Element unit = doc.createElement("unit");
        unit.appendChild(doc.createTextNode(Integer.toString(measurement.getUnit())));
        rootElement.appendChild(unit);

        Element type = doc.createElement("type");
        type.appendChild(doc.createTextNode(measurement.getUnitOfMeasurement()));
        rootElement.appendChild(type);
        return doc;
    }

    public Map<String, List<Measurement>> readXMLFile(String path) throws ParserConfigurationException, IOException, SAXException {
        Map<String, List<Measurement>> measurementsMap = new HashMap<>();
        File[] files = new File(path).listFiles();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        for (File file : files) {
            Document doc = dBuilder.parse(path + "/" + file.getName());
            readDoc(doc, measurementsMap);
        }
        return measurementsMap;
    }

}
