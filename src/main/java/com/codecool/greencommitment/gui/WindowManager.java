package com.codecool.greencommitment.gui;

import com.codecool.greencommitment.server.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class WindowManager {

    private final int width, height;
    JPanel panel;
    Server server;
    JFrame frame;

    WindowManager(int width, int height) {
        this.width = width;
        this.height = height;
    }

    void run() {
        frame = new JFrame("Green Commitment - KokeroTCP");
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

        ActionListener click = e -> {
            switch (((JButton) e.getSource()).getText()) {
                case "server": {
                    try {
                        handleServer();
                        this.server.runServer();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
                case "client": {
                    //panel.add(handleClient());
                    System.out.println("nembuzi");
                }
            }

        };
        server.addActionListener(click);
        client.addActionListener(click);

        return panel;
    }

    private void handleServer() throws Exception {
        JFrame frame = new JFrame("Green Commitment - KokeroTCP - Server");
        frame.setVisible(true);
        frame.add(serverPanel());
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Socket socket;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("google.com", 80));
        } catch (UnknownHostException unknown) {
            socket = new Socket();
        }
        if (!socket.getLocalAddress().toString().equals("0.0.0.0/0.0.0.0")) {
            server = new Server(socket.getLocalAddress());
        } else {
            server = new Server(InetAddress.getLocalHost());
        }
        JPanel panel = new JPanel();
        panel.add(new JLabel("<html>Running server on IP: "+server.getSocketAddress()+"<br>PORT: "+server.getPort()+"</html>"));
        this.frame.add(panel);
    }

    private JFrame handleClient() {
        return null;
    }

    private JPanel serverPanel() throws Exception {
        JPanel panel = new JPanel();
        JLabel ServerLabel = new JLabel("<html>Running server on IP:<br>PORT: ");
        panel.add(ServerLabel);
        if (server != null)
            ServerLabel.setText("<html>Running server on IP: "+server.getSocketAddress()+"<br>PORT: "+server.getPort()+"</html>");
        return panel;
    }
}
