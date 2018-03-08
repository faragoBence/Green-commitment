package com.codecool.greencommitment.server;

import com.codecool.greencommitment.common.Measurement;
import com.codecool.greencommitment.common.XmlParser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Handler extends Thread {

    private Socket socket;
    Document dom;
    private XmlParser xmlParser = new XmlParser();
    private Map<String, List<Measurement>> measurements = new HashMap<>();


    Handler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            String clientAddress = socket.getInetAddress().getHostAddress();
            System.out.println("\r\nNew measurement from " + clientAddress);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            dom = dBuilder.parse(socket.getInputStream());
            xmlParser.readDoc(dom, measurements);
            xmlParser.writeToXML(measurements);


        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
}