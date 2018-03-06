package com.codecool.greencommitment.server;

import com.codecool.greencommitment.common.XmlParser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.Socket;

public class Handler extends Thread {

    private Socket socket;

    Handler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            String clientAddress = socket.getInetAddress().getHostAddress();
            System.out.println("\r\nNew connection from " + clientAddress);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            XmlParser xmlParser = new XmlParser();
            while (!socket.isClosed()) {
                Document dom = dBuilder.parse(socket.getInputStream());
                xmlParser.readDoc(dom);
            }
            xmlParser.writeToXML(xmlParser.getMeasurements());

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
}