package com.codecool.greencommitment.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private int id;
    private Socket socket;
    private Scanner scanner;

    public Client(int id, InetAddress serverAddress, int serverPort) throws Exception {
        this.id = id;
        this.socket = new Socket(serverAddress, serverPort);
        this.scanner = new Scanner(System.in);
    }

    public void start() throws IOException {
        String input;
        while (true) {
            input = scanner.nextLine();
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(input);
            out.flush();
        }
    }

    public void runClient() throws Exception {
        System.out.println("\r\nConnected to Server: " + this.socket.getInetAddress());
        this.start();
    }
}