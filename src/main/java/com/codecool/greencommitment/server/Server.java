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
    Boolean run = true;
    Scanner scanner = new Scanner(System.in);


    public Server(InetAddress ipAddress) throws Exception {
        this.server = new ServerSocket(0, 1, ipAddress);
        port = this.server.getLocalPort();
    }

    public void listen() throws Exception {
        run();
        try {
            while (run) {
                Document dom = new Handler(server.accept()).startRun();
                xmlParser.readDoc(dom, measurements);
            }
        } finally {
            server.close();
        }
        xmlParser.writeToXML(measurements);
        System.exit(0);

    }

    public InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }

    public int getPort() {
        return port;
    }

    public void runServer() throws Exception {
        System.out.println("\r\nRunning Server: " +
                "Host=" + this.getSocketAddress().getHostAddress() +
                " Port=" + port);

        this.listen();
    }

    public void run() {
        String str = scanner.nextLine();
        if (str.equals("q")) {
            run = false;
        }
    }
}
