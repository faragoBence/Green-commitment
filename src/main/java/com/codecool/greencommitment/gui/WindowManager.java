package com.codecool.greencommitment.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowManager {

    private final int width, height;
    JPanel panel;

    WindowManager(int width, int height) {
        this.width = width;
        this.height = height;
    }

    void run() {
        JFrame frame = new JFrame("greencommitment");
        frame.setVisible(true);
        frame.setSize(width, height);
        frame.add(mainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    JPanel mainPanel() {
        JPanel panel = new JPanel();
        JButton server = new JButton("server");
        JButton client = new JButton("client");

        panel.add(server);
        panel.add(client);

        ActionListener click = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (e.getActionCommand()) {
                    case "server": {
                        panel.add(handleServer());
                        System.out.println("buzi");
                    }

                    case "client": {
                        panel.add(handleClient());
                        System.out.println("nembuzi");
                    }
                }

            }
        };
        server.addActionListener(click);
        client.addActionListener(click);

        return panel;
    }

    private JFrame handleServer() {
        return null;
    }

    private JFrame handleClient() {
        return null;
    }
}
