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
                server.runServer();
                server.listen();
            } else if (args[0].equalsIgnoreCase("client")) {
                InetAddress ipAddress;
                while (true) {
                    System.out.print("Enter IP: ");
                    String IP = scan.nextLine();
                    if (validIP(IP)) {
                        ipAddress = InetAddress.getByName(IP);
                        break;
                    } else {
                        System.out.println("Please enter a valid IP!");
                    }
                }
                int intPort = getIntInput("Enter PORT: ");
                System.out.print("Enter your id: ");
                String id = scan.nextLine();
                Type enumType;
                while (true) {
                    System.out.print("Enter the data type [TEMPERATURE / MOISTURE]: ");
                    String type = scan.nextLine();
                    try {
                        enumType = Type.valueOf(type.toUpperCase());
                        break;
                    } catch (IllegalArgumentException ill) {
                        System.out.println("Please enter a valid data type.");
                    }
                }
                dg = new DataGenerator(enumType);
                int intSeconds = getIntInput("Enter the number of seconds between measurements: ");
                while (true) {
                    try {
                        Client client = new Client(id, ipAddress, intPort);
                        client.setType(enumType);
                        client.runClient(dg.createData());
                        Thread.sleep(intSeconds * 1000);
                    } catch (Exception f) {
                        System.out.println("Server stopped running.");
                        System.exit(0);
                    }
                }
            } else {
                System.out.println("Enter server or client as argument!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getIntInput(String message) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print(message);
            String input = scan.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException nf) {
                System.out.println("Please enter numbers!");
            }
        }
    }

    private static boolean validIP(String ip) {
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }

            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }

            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ((i < 0) || (i > 255)) {
                    return false;
                }
            }
            return !ip.endsWith(".");
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
