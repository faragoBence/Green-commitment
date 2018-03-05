package com.codecool.greencommitment.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Server {

    private ServerSocket server;
    private HashSet<PrintWriter> writers;


    public Server(InetAddress ipAddress) throws Exception {
        this.server = new ServerSocket(0, 1, ipAddress);
    }

    public void listen() throws Exception {

        try {
            while (true) {
                new Handler(server.accept()).start();
            }
        } finally {
            server.close();
        }

    }

    private InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }

    private int getPort() {
        return this.server.getLocalPort();
    }

    public void runServer() throws Exception {
        System.out.println("\r\nRunning Server: " +
                "Host=" + this.getSocketAddress().getHostAddress() +
                " Port=" + this.getPort());

        this.listen();
    }

    public HashSet<PrintWriter> getWriters() {
        return writers;
    }
}
