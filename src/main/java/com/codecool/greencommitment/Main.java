package com.codecool.greencommitment;

import com.codecool.greencommitment.client.Client;
import com.codecool.greencommitment.server.Server;
import java.net.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        if (args.length == 0) {
            System.out.println("Enter server or client as argument!");
            System.exit(0);
        }
        try {
            if (args[0].equalsIgnoreCase("server")) {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress("google.com", 80));
                Server server = new Server(socket.getLocalAddress());
                server.runServer();
                server.listen();
            } else if (args[0].equalsIgnoreCase("client")) {
                System.out.print("Enter IP: ");
                String IP = scan.nextLine();
                try {
                    InetAddress ipAddress = InetAddress.getByName(IP);
                    System.out.print("Enter PORT: ");
                    String port = scan.nextLine();
                    int intPort = Integer.parseInt(port);
                    Client client = new Client(123, ipAddress, intPort);
                    client.start();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Enter server or client as argument!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
