package com.codecool.greencommitment.client;

import com.codecool.greencommitment.common.Measurement;
import com.codecool.greencommitment.common.XmlParser;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {

    private String id;
    private Socket socket;
    private Scanner scanner;
    private XmlParser parser;

    public Client(String id, InetAddress serverAddress, int serverPort) throws Exception {
        this.id = id;
        this.socket = new Socket(serverAddress, serverPort);
        this.scanner = new Scanner(System.in);
        this.parser = new XmlParser();
    }

    public void start() throws IOException, InterruptedException {
        String input;
        List<Measurement> measurements = new DataGenerator(20, 10, 25, "temp").createData();
        for (Measurement mes : measurements) {
            Thread.sleep(2000);
            parser.createDoc(mes, id);
            DOMSource domsource = parser.getSource();
            StreamResult result = new StreamResult(socket.getOutputStream());
            TransformerFactory tFactory =
                    TransformerFactory.newInstance();
            try {
                Transformer transformer =
                        tFactory.newTransformer();
                transformer.transform(domsource, result);

            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }


    }

    public void runClient() throws Exception {
        System.out.println("\r\nConnected to Server: " + this.socket.getInetAddress());
        this.start();
    }
}