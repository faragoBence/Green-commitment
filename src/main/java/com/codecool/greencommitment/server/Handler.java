package com.codecool.greencommitment.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;

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

            while ((data = in.readLine()) != null ) {
                System.out.println("\r\nMessage from " + clientAddress + ": " + data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}