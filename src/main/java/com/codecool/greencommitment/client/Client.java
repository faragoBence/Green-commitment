package com.codecool.greencommitment.client;

import com.codecool.greencommitment.common.Measurement;
import com.codecool.greencommitment.common.TemperatureMeasurement;
import com.codecool.greencommitment.common.XmlParser;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class Client {

    private String id;
    private Socket socket;
    private XmlParser parser;

    public Client(String id, InetAddress serverAddress, int serverPort) throws Exception {
        this.id = id;
        this.socket = new Socket(serverAddress, serverPort);
        this.parser = new XmlParser();
    }

    private void start() throws IOException, InterruptedException {
        Measurement mes = new TemperatureMeasurement(12, 30, "celsius");
        parser.createDoc(mes, id);

        StreamResult result = new StreamResult(socket.getOutputStream());
        TransformerFactory tFactory = TransformerFactory.newInstance();
        DOMSource domsource = parser.getSource();
        try {
            Transformer transformer =
                    tFactory.newTransformer();
            transformer.transform(domsource, result);

        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


    public void runClient() throws Exception {
        System.out.println("\r\nConnected to Server: " + this.socket.getInetAddress());
        this.start();
    }
}