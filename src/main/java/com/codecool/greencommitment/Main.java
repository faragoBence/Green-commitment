package com.codecool.greencommitment;

import com.codecool.greencommitment.client.Client;
import com.codecool.greencommitment.client.DataGenerator;
import com.codecool.greencommitment.client.Type;
import com.codecool.greencommitment.server.Server;
import java.net.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DataGenerator dg;
        Scanner scan = new Scanner(System.in);
        if (args.length == 0) {
            System.out.println("Enter server or client as argument!");
            System.exit(0);
        }
        try {
            if (args[0].equalsIgnoreCase("server")) {
                Socket socket;
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress("google.com", 80));
                } catch (UnknownHostException unknown) {
                    socket = new Socket();
                }
                Server server;
                if (!socket.getLocalAddress().toString().equals("0.0.0.0/0.0.0.0")) {
                    server = new Server(socket.getLocalAddress());
                } else {
                    server = new Server(InetAddress.getLocalHost());
                }
                System.out.print("Provide port (0 to use default): ");
                String port = scan.nextLine();
                try {
                    int intPort = Integer.parseInt(port);
                    if (intPort == 0) {
                        System.out.println("Starting server with default port.");
                    } else {
                        server.setPort(intPort);
                    }
                } catch (NumberFormatException nf) {
                    System.out.println("Invalid port entered. Starting server with default port.");
                }
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
                    System.out.print("Enter your id: ");
                    String id = scan.nextLine();
                    while (true) {
                            System.out.print("Enter the data type [TEMPERATURE / MOISTURE]:");
                            String type = scan.nextLine();
                        try {
                            Type enumType = Type.valueOf(type.toUpperCase());
                            dg = new DataGenerator(enumType);
                            while (true) {
                                try {
                                    Client client = new Client(id, ipAddress, intPort);
                                    client.runClient(dg.createData());
                                    Thread.sleep(2000);
                                } catch (Exception f) {
                                    System.out.println("Server stopped running.");
                                    System.exit(0);
                                }
                            }
                        } catch (IllegalArgumentException ill) {
                            System.out.println("Please enter a valid data type.");
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Enter server or client as argument!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
