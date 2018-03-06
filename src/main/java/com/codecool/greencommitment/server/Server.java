package com.codecool.greencommitment.server;

import java.net.InetAddress;
import java.net.ServerSocket;

public class Server {

    private ServerSocket server;
    private int port;


    public Server(InetAddress ipAddress) throws Exception {
        this.server = new ServerSocket(0, 1, ipAddress);
        port = 6969;
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

    public void setPort(int port) {
        this.port = port;
    }

    public void runServer() throws Exception {
        System.out.println("\r\nRunning Server: " +
                "Host=" + this.getSocketAddress().getHostAddress() +
                " Port=" + port);

        this.listen();
    }
}
