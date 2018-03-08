package com.codecool.greencommitment.gui;

import com.codecool.greencommitment.client.Client;
import com.codecool.greencommitment.client.DataGenerator;
import com.codecool.greencommitment.client.Type;
import com.codecool.greencommitment.common.Measurement;
import com.codecool.greencommitment.common.XmlParser;
import com.codecool.greencommitment.server.Server;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindowManager {

    private final int width, height;
    private Server server;
    private boolean wasSelected;
    private Thread thread;
    private DataGenerator dg;
    private static JList<String> serverJlist = new JList<>();
    private static String[] serverMesArray = new String[0];
    private static JList<String> clientJlist = new JList<>();
    private static String[] clientMesArray = new String[0];

    public WindowManager(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void run() {
        JFrame frame = new JFrame("Green Commitment - KokeroTCP");
        frame.setVisible(true);
        frame.setSize(width, height);
        frame.add(mainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel mainPanel() {
        JPanel panel = new JPanel();
        JButton server = new JButton("server");
        JButton client = new JButton("client");
        JButton chart = new JButton(("chart"));

        panel.add(server);
        panel.add(client);
        panel.add(chart);
        ActionListener click = e -> {
            switch (((JButton) e.getSource()).getText()) {
                case "server": {
                    try {
                        handleServer();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
                case "client": {
                    handleClient();
                    break;
                }

                case "chart" : {
                    handleChart();
                    break;
                }
            }
        };
        server.addActionListener(click);
        client.addActionListener(click);
        chart.addActionListener(click);
        return panel;
    }

    private void handleServer() throws Exception {
        JFrame frame = new JFrame("Green Commitment - KokeroTCP - Server");
        frame.setVisible(true);
        frame.setSize(400, 520);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                server.setRunning(false);
                frame.setVisible(false);
            }
        });
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
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = .25;
        c.insets = new Insets(30, 30, 30, 30);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;

        JLabel serverInfo = new JLabel("<html>Running server on IP: " + server.getSocketAddress() + "<br>PORT: " + server.getPort() + "</html>");
        panel.add(serverInfo, c);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(serverJlist);
        panel.add(scrollPane, c);
        JButton start = new JButton("Start server");
        JButton stop = new JButton("Stop server");
        panel.add(start, c);
        panel.add(stop, c);
        thread = new Thread(server);
        ActionListener clickListen = e -> {
            switch (((JButton) e.getSource()).getText()) {
                case "Start server": {
                    if (!server.getRunning()) {
                        server.setRunning(true);
                        serverInfo.setText(serverInfo.getText() + "  NOW RUNNING");
                        System.out.println("should start");
                        thread.start();
                    }
                    break;
                }
                case "Stop server": {
                    if (server.getRunning()) {
                        System.out.println("should stop!");
                        server.setRunning(false);
                        serverInfo.setText(serverInfo.getText() + "  NOW STOPPED");
                        thread = new Thread(server);
                    }
                    break;
                }
            }
        };
        start.addActionListener(clickListen);
        stop.addActionListener(clickListen);
        frame.add(panel);
    }

    private void handleClient() {
        JFrame frame = new JFrame("Green Commitment - KokeroTCP - Client");
        frame.setVisible(true);
        frame.setSize(400, 520);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = .25;
        c.insets = new Insets(1, 30, 1, 30);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        JLabel ipLabel = new JLabel("IP: ");
        panel.add(ipLabel, c);
        JTextField ip = new JTextField();
        panel.add(ip, c);
        JLabel portLabel = new JLabel("PORT: ");
        panel.add(portLabel, c);
        JTextField port = new JTextField();
        panel.add(port, c);
        JLabel idLabel = new JLabel("ID: ");
        panel.add(idLabel, c);
        JTextField id = new JTextField();
        panel.add(id, c);
        JLabel dataLabel = new JLabel("DATA TYPE: ");
        panel.add(dataLabel, c);
        JComboBox data = new JComboBox(Type.values());

        panel.add(data, c);
        JLabel timeLabel = new JLabel("TIME: ");
        panel.add(timeLabel, c);
        JTextField time = new JTextField();
        panel.add(time, c);
        JButton connectButton = new JButton("CONNECT");
        c.insets = new Insets(20,30,5,30);
        panel.add(connectButton, c);
        JLabel connection = new JLabel("<html>No connection yet.<br><br><br>No data flow.</html>");
        panel.add(connection, c);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(clientJlist);
        c.insets = new Insets(10, 30, 10, 30);
        panel.add(scrollPane, c);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        boolean closeOperation = true;
        frame.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent windowEvent) {


            frame.setVisible(false);



            }
        });
        ActionListener click = e -> {
            Thread t1 = new Thread(new Runnable() {
                public void run() {
                    Type type;
                    int intPort;
                    int intTime;
                    if (!validIP(ip.getText())) {
                        connection.setText("<html>The IP entered is invalid.<br><br><br>No data flow.</html>");
                        ipLabel.setForeground(Color.red);
                        return;
                    } else {
                        ipLabel.setForeground(Color.black);
                    }
                    try {
                        intPort = Integer.parseInt(port.getText());
                        portLabel.setForeground(Color.black);
                    } catch (NumberFormatException nf) {
                        connection.setText("<html>The PORT entered is invalid.<br><br><br>No data flow.</html>");
                        portLabel.setForeground(Color.red);
                        return;
                    }
                    try {
                        //type = Type.valueOf(data.getText().toUpperCase());
                        type = (Type) data.getSelectedItem();
                        dataLabel.setForeground(Color.black);
                    } catch (IllegalArgumentException ill) {
                        connection.setText("<html>The TYPE entered is invalid.<br>ENTER [TEMPERATURE / MOISTURE]<br><br>No data flow.</html>");
                        dataLabel.setForeground(Color.red);
                        return;
                    }
                    try {
                        intTime = Integer.parseInt(time.getText());
                        timeLabel.setForeground(Color.black);
                    } catch (NumberFormatException nf) {
                        connection.setText("<html>The TIME entered is invalid.<br>ENTER THE NUMBER OF SECONDS<br><br>No data flow.</html>");
                        timeLabel.setForeground(Color.red);
                        return;
                    }
                    dg = new DataGenerator(type);
                    while (closeOperation) {
                        try {
                            if (server.getRunning()) {
                                Client client = new Client(id.getText(), InetAddress.getByName(ip.getText()), intPort);
                                client.setType(type);
                                client.runClient(dg.createData());
                                connection.setText("<html>CONNECTION INFORMATION<br>IP: " + ip.getText() + "<br>PORT: " + port.getText() + "<br>Sending data to server.</html>");
                                Thread.sleep(intTime * 1000);
                            }
                            else{
                                connection.setText("server is ded");
                            }
                        } catch (Exception f) {
                            connection.setText("<html>CONNECTION INFORMATION<br><br><br>Server stopped running.");
                            break;
                        }
                    }
              }
           });
           t1.start();
        };
        connectButton.addActionListener(click);
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

    public void handleChart() {
        JFrame frame = new JFrame("Green Commitment - KokeroTCP - Line Chart");
        frame.setVisible(true);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setVisible(true);
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = .25;
        c.insets = new Insets(1, 30, 1, 30);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Temperature measurements",
                "Time", "Temperature",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        //ApplicationFrame setContentPane( chartPanel );
        ChartPanel cp = new ChartPanel(lineChart);
        panel.add(cp, c);
        frame.add(panel);
        frame.setLayout(new GridLayout());

        //frame.add(createChartSettingsPanel());


    }

    private DefaultCategoryDataset createDataset() {
        Map<String, java.util.List<Measurement>> measurements = new HashMap<>();
        XmlParser xmlParser = new XmlParser();
        try {
            measurements = xmlParser.sort(xmlParser.readXMLFiles("resources"), "Celsius");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        for (Map.Entry<String, List<Measurement>> entry : measurements.entrySet()) {
            for (Measurement measurement : entry.getValue()) {
                System.out.println(new Date(measurement.getCurrentTime()));
                dataset.addValue(measurement.getUnit(), entry.getKey(), df.format(new Date(measurement.getCurrentTime())));
            }
        }
        return dataset;
    }

    public static void setServerJlist(Measurement mes) {
        String[] tempArray = new String[serverMesArray.length + 1];
        for (int i = 0; i < serverMesArray.length; i++) {
            tempArray[i] = serverMesArray[i];
        }
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        tempArray[tempArray.length - 1] = df.format(new Date(mes.getCurrentTime()))+", "+mes.getClass().getSimpleName().replace("Measurement", "") + ", "+mes.getUnit()+" "+mes.getUnitOfMeasurement();
        serverMesArray = tempArray;
        serverMesArray[serverMesArray.length - 1] = serverMesArray.length + ". " + serverMesArray[serverMesArray.length - 1];
        serverJlist.setListData(serverMesArray);
    }

    public static void setClientJlist(Measurement mes) {
        String[] tempArray = new String[clientMesArray.length + 1];
        for (int i = 0; i < clientMesArray.length; i++) {
            tempArray[i] = clientMesArray[i];
        }
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        tempArray[tempArray.length - 1] = df.format(new Date(mes.getCurrentTime()))+", "+mes.getClass().getSimpleName().replace("Measurement", "") + ", "+mes.getUnit()+" "+mes.getUnitOfMeasurement();
        clientMesArray = tempArray;
        clientMesArray[clientMesArray.length - 1] = clientMesArray.length + ". " + clientMesArray[clientMesArray.length - 1];
        clientJlist.setListData(clientMesArray);
    }

    private JPanel createChartSettingsPanel() {
        JPanel panel = new JPanel();
        JButton refreshChart = new JButton("refresh");
        JButton addElement = new JButton("add");

        panel.setLayout(new GridBagLayout());
        panel.setVisible(true);
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = .25;
        c.insets = new Insets(1, 30, 1, 30);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;

        panel.add(refreshChart, c);
        panel.add(addElement, c);
        return panel;

    }
}
