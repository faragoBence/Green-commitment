package com.codecool.greencommitment.server;

import java.util.Scanner;

public class InputRead extends Thread {
    private final Scanner scanner = new Scanner(System.in);

    private Boolean input = true;

    public void run() {
        while (true) {
            String str = scanner.nextLine();
            if (str.equals("q")) {
                input = false;
            }
        }
    }

    public Boolean getInput() {
        return input;
    }
}
