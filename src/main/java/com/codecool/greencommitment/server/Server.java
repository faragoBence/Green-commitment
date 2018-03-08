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
    private boolean running = false;

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
            new Handler(server.accept()).run();
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
}