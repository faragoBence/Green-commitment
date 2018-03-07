package com.codecool.greencommitment.server;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.Socket;

public class Handler extends Thread {

    private Socket socket;
    Document dom;

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


        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    public Document startRun() {
        run();
        return dom;
    }
}