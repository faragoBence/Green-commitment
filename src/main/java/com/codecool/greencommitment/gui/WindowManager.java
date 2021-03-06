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
import java.util.*;
import java.util.List;

public class WindowManager {

    private final int width, height;
    private Server server;
    private Thread thread;
    private DataGenerator dg;
    private static JList<String> serverList = new JList<>();
    private static String[] serverMesArray = new String[0];
    private static JList<String> clientList = new JList<>();
    private static String[] clientMesArray = new String[0];
    private int files = 0;
    private List<String> stringList;
    private JPanel chartButtonPanel, chartPanel;
    private JFrame frame;
    private ButtonGroup bGroup;
    private DefaultCategoryDataset dataset;
    private ChartPanel cp;
    private String unitOfMeasurement = "";

    public WindowManager(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void run() {
        JFrame frame = new JFrame("Green Commitment - KokeroTCP");
        frame.setSize(width, height);
        frame.add(mainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private JPanel mainPanel() {
        JPanel panel = new JPanel();
        JButton server = new JButton("server");
        JButton client = new JButton("client");
        JButton chart = new JButton("chart");

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
                case "chart": {
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
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
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

        JLabel serverInfo = new JLabel("<html>Server isn't currently running.<br></html>");
        panel.add(serverInfo, c);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(serverList);
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
                        serverInfo.setText(("<html>Running server on IP: " + server.getSocketAddress() + "<br>PORT: " + server.getPort() + "</html>"));
                        thread.start();
                    }
                    break;
                }
                case "Stop server": {
                    if (server.getRunning()) {
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
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
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
        JComboBox<Type> data = new JComboBox<>(Type.values());
        panel.add(data, c);
        JLabel timeLabel = new JLabel("TIME: ");
        panel.add(timeLabel, c);
        JTextField time = new JTextField();
        panel.add(time, c);
        JButton connectButton = new JButton("CONNECT");
        c.insets = new Insets(20, 30, 5, 30);
        panel.add(connectButton, c);
        JLabel connection = new JLabel("<html>No connection yet.<br><br><br>No data flow.</html>");
        panel.add(connection, c);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(clientList);
        c.insets = new Insets(10, 30, 10, 30);
        panel.add(scrollPane, c);
        frame.add(panel);
        ActionListener click = e -> {
            Thread t1 = new Thread(() -> {
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
                if (id.getText().equals("")) {
                    idLabel.setForeground(Color.red);
                    connection.setText("<html>The ID entered is invalid.<br><br><br>No data flow.</html>");
                    return;
                } else {
                    idLabel.setForeground(Color.black);
                }
                type = (Type) data.getSelectedItem();
                try {
                    intTime = Integer.parseInt(time.getText());
                    timeLabel.setForeground(Color.black);
                } catch (NumberFormatException nf) {
                    connection.setText("<html>The TIME entered is invalid.<br>ENTER THE NUMBER OF SECONDS<br><br>No data flow.</html>");
                    timeLabel.setForeground(Color.red);
                    return;
                }
                dg = new DataGenerator(type);
                while (true) {
                    try {
                        Client client = new Client(id.getText(), InetAddress.getByName(ip.getText()), intPort);
                        client.setType(type);
                        client.runClient(dg.createData());
                        connection.setText("<html>CONNECTION INFORMATION<br>IP: " + ip.getText() + "<br>PORT: " + port.getText() + "<br>Sending data to server.</html>");
                        Thread.sleep(intTime * 1000);
                    } catch (Exception f) {
                        connection.setText("<html>CONNECTION INFORMATION<br><br><br>Server stopped running.");
                        break;
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

    private void handleChart() {
        if (unitOfMeasurement.equals("")) {
            unitOfMeasurement = "Unit of Measurement";
        }
        if (frame == null) {
            frame = new JFrame("Green Commitment - KokeroTCP - Line Chart");
            frame.setSize(1000, 640);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(null);
            frame.setVisible(true);
        }
        if (chartPanel == null) {
            chartPanel = new JPanel();
            chartPanel.setLayout(new GridBagLayout());
            chartPanel.setBounds(10, 10, 800, 590);
            chartPanel.setVisible(true);
        }
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        if (dataset == null)
            createDataset("");
        JFreeChart lineChart;
        switch (unitOfMeasurement) {
            case "Celsius":
                lineChart = ChartFactory.createLineChart(
                        "Temperature measurements",
                        "Time", "Temperature",
                        dataset,
                        PlotOrientation.VERTICAL,
                        true, true, false);
                break;
            case "%":
                lineChart = ChartFactory.createLineChart(
                        "Humidity measurements",
                        "Time", "Humidity (%)",
                        dataset,
                        PlotOrientation.VERTICAL,
                        true, true, false);
                break;
            default:
                lineChart = ChartFactory.createLineChart(
                        "Measurements",
                        "Time", "Unit of Measurements",
                        dataset,
                        PlotOrientation.VERTICAL,
                        true, true, false);
                break;
        }

        if (cp != null)
            cp.setVisible(false);
        cp = new ChartPanel(lineChart);
        cp.setVisible(true);
        chartPanel.add(cp, c);
        frame.add(chartPanel);
        createChartSettingsPanel();
        chartButtonPanel.setBounds(820, 10, 160, 590);
        frame.add(chartButtonPanel);
    }


    private void createChartSettingsPanel() {
        chartButtonPanel = new JPanel();
        chartButtonPanel.setLayout(new GridBagLayout());
        chartButtonPanel.setVisible(true);
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        bGroup = createButtons(c);
        Enumeration<AbstractButton> abs = bGroup.getElements();
        ActionListener click = e -> {
            switch (((JButton) e.getSource()).getText()) {
                case "▲":
                    if (files > 0) {
                        files -= 10;
                        bGroup = createButtons(c);
                        handleChart();
                    }
                    break;
                case "▼":
                    if (files < stringList.size() + 10) {
                        files += 10;
                        bGroup = createButtons(c);
                        handleChart();
                    }
                    break;
                default:
                    createDataset(((JButton) e.getSource()).getText());
                    handleChart();
                    break;
            }
        };
        while (abs.hasMoreElements()) {
            abs.nextElement().addActionListener(click);
        }
    }


    private ButtonGroup createButtons(GridBagConstraints c) {
        if (chartButtonPanel != null)
            chartButtonPanel.setVisible(false);
        chartButtonPanel = new JPanel();
        chartButtonPanel.setLayout(new GridBagLayout());
        chartButtonPanel.setVisible(true);
        Map<String, List<Measurement>> measurementMap = null;
        try {
            measurementMap = new XmlParser().readXMLFiles("resources");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ButtonGroup bGroup = new ButtonGroup();
        if (measurementMap != null && measurementMap.size() != 0) {
            JButton upButton = new JButton("▲");
            bGroup.add(upButton);
            chartButtonPanel.add(upButton, c);
            for (int i = 0; i < 2; i++) {
                JLabel label = new JLabel("");
                chartButtonPanel.add(label, c);
            }
            Set<String> ids = measurementMap.keySet();
            stringList = new ArrayList<>(ids);
            for (int i = files; i < files + 10; i++) {
                if (i < stringList.size()) {
                    JButton jButton = new JButton(stringList.get(i));
                    bGroup.add(jButton);
                    chartButtonPanel.add(jButton, c);
                } else {
                    JLabel label = new JLabel("");
                    chartButtonPanel.add(label, c);
                }

            }
            for (int i = 0; i < 2; i++) {
                JLabel label = new JLabel("");
                chartButtonPanel.add(label, c);
            }
            JButton downButton = new JButton("▼");
            bGroup.add(downButton);
            chartButtonPanel.add(downButton, c);
        } else {
            JLabel label = new JLabel("No measurements yet.");
            chartButtonPanel.add(label);
        }
        return bGroup;
    }

    private void createDataset(String id) {
        Map<String, java.util.List<Measurement>> measurements = new HashMap<>();
        XmlParser xmlParser = new XmlParser();
        try {
            measurements = xmlParser.readXMLFiles("resources");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        dataset = new DefaultCategoryDataset();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        for (Map.Entry<String, List<Measurement>> entry : measurements.entrySet()) {
            if (entry.getKey().equals(id)) {
                for (Measurement measurement : entry.getValue()) {
                    dataset.addValue(measurement.getUnit(), entry.getKey(), df.format(new Date(measurement.getCurrentTime())));
                    if (measurement.getUnitOfMeasurement().equalsIgnoreCase("celsius")) {
                        unitOfMeasurement = "Celsius";
                    } else {
                        unitOfMeasurement = "%";
                    }
                }
            }
        }
    }

    public static void setServerList(Measurement mes) {
        String[] tempArray = new String[serverMesArray.length + 1];
        System.arraycopy(serverMesArray, 0, tempArray, 0, serverMesArray.length);
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        tempArray[tempArray.length - 1] = df.format(new Date(mes.getCurrentTime())) + ", " + mes.getClass().getSimpleName().replace("Measurement", "") + ", " + mes.getUnit() + " " + mes.getUnitOfMeasurement();
        serverMesArray = tempArray;
        serverMesArray[serverMesArray.length - 1] = serverMesArray.length + ". " + serverMesArray[serverMesArray.length - 1];
        serverList.setListData(serverMesArray);
    }

    public static void setClientList(Measurement mes) {
        String[] tempArray = new String[clientMesArray.length + 1];
        System.arraycopy(clientMesArray, 0, tempArray, 0, clientMesArray.length);
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        tempArray[tempArray.length - 1] = df.format(new Date(mes.getCurrentTime())) + ", " + mes.getClass().getSimpleName().replace("Measurement", "") + ", " + mes.getUnit() + " " + mes.getUnitOfMeasurement();
        clientMesArray = tempArray;
        clientMesArray[clientMesArray.length - 1] = clientMesArray.length + ". " + clientMesArray[clientMesArray.length - 1];
        clientList.setListData(clientMesArray);
    }
}
