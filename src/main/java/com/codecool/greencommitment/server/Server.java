package com.codecool.greencommitment.server;

import com.codecool.greencommitment.common.Measurement;
import com.codecool.greencommitment.common.XmlParser;
import org.w3c.dom.Document;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Server extends Thread {

    private ServerSocket server;
    private int port;
    XmlParser xmlParser = new XmlParser();
    Map<String, List<Measurement>> measurements = new HashMap<>();
    Scanner scanner = new Scanner(System.in);
    private boolean running = true;

    public Server(InetAddress ipAddress) throws Exception {
        this.server = new ServerSocket(0, 1, ipAddress);
        port = this.server.getLocalPort();
    }

    public boolean getRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void listen() throws Exception {
        while (running) {
            Document dom = new Handler(server.accept()).startRun();
            xmlParser.readDoc(dom, measurements);
            xmlParser.writeToXML(measurements);
        }
    }

    public InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }

    public int getPort() {
        return port;
    }

    public void run() {
        try {
            this.listen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runServer() throws Exception {
        System.out.println("\r\nRunning Server: " +
                "Host=" + this.getSocketAddress().getHostAddress() +
                " Port=" + port);

        this.listen();
    }

    public Map<String, List<Measurement>> getMeasurements() {
        return measurements;
    }
}