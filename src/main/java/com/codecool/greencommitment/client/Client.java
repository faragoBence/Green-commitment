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

    private Client(int id, InetAddress serverAddress, int serverPort) throws Exception {
        this.id = id;
        this.socket = new Socket(serverAddress, serverPort);
        this.scanner = new Scanner(System.in);
    }

    private void start() throws IOException {
        String input;
        while (true) {
            input = scanner.nextLine();
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(input);
            out.flush();
        }
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client(Integer.parseInt(args[0]), InetAddress.getByName(args[1]), Integer.parseInt(args[2]));
        System.out.println("\r\nConnected to Server: " + client.socket.getInetAddress());
        client.start();
    }
}