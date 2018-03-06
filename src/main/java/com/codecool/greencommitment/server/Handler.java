package com.codecool.greencommitment.server;

import com.codecool.greencommitment.common.XmlParser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Handler extends Thread {

    private int id;
    private Socket socket;
    private BufferedReader in;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String clientAddress = socket.getInetAddress().getHostAddress();
            System.out.println("\r\nNew connection from " + clientAddress);
            String data;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            XmlParser xmlParser = new XmlParser();
            while (socket.isOutputShutdown()) {
                Document dom = dBuilder.parse(socket.getInputStream());
                xmlParser.readDoc(dom);
            }
            xmlParser.writeToXML(xmlParser.getMeasurements());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}