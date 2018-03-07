package com.codecool.greencommitment.client;

import com.codecool.greencommitment.common.Measurement;
import com.codecool.greencommitment.common.XmlParser;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private String id;
    private Socket socket;
    private Type type;

    public Client(String id, InetAddress serverAddress, int serverPort) throws Exception {
        this.id = id;
        this.socket = new Socket(serverAddress, serverPort);
    }

    private void start(Measurement mes) throws IOException {
        XmlParser parser = new XmlParser();
        parser.createDoc(mes, id);

        StreamResult result = new StreamResult(socket.getOutputStream() );
        TransformerFactory tFactory = TransformerFactory.newInstance();
        DOMSource domsource = parser.getSource();
        try {
            Transformer transformer =
                    tFactory.newTransformer();
            transformer.transform(domsource, result);

        } catch (TransformerException e) {
            e.printStackTrace();
        }

        socket.close();
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void runClient(Measurement mes) throws Exception {
        System.out.println("\r\nConnected to Server: " + this.socket.getInetAddress());
        this.start(mes);
    }
}