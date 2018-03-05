package com.codecool.greencommitment;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        if (args[0].equalsIgnoreCase("server")) {
            System.out.print("Enter server IP: ");
            String serverIP = scan.nextLine();
            System.out.print("Enter server port: ");
            String serverPort = scan.nextLine();
        } else if (args[0].equalsIgnoreCase("client")) {
            System.out.println("Enter client ID: ");

        } else {

        }
    }
}
