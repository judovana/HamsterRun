package nonsense.hamsterrun.setup;


import nonsense.hamsterrun.Localization;
import nonsense.hamsterrun.VirtualRatSetup;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.network.Commands;
import nonsense.hamsterrun.network.HelloCmd;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;


public class NetworkPane extends JPanel implements Localized, ActionListener {

    private static String hostname = getHostName();
    private static String id = randomStringSimple(10, +1);
    private final RatsPanel ratsPanel;

    private int serverPort = findRandomPort();
    private int clientPort = serverPort;
    private String name;
    private TmpServerThread tmpServer;
    private SayHelloThread tmpClient;
    private JLabel connected;
    private JRadioButton serverSelected;
    private JRadioButton clientSelected;
    private JPanel serverPanel;
    private JPanel clientPanel;
    private JLabel titleOfTitles;

    private Set<HelloCmd> seenPlayers = new HashSet<>();

    private class ObserverThread extends Thread {
        private boolean running = true;

        public ObserverThread() {
            super.setDaemon(true);
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(500);
                    if (serverSelected.isSelected()) {
                        refreshServerStatus();
                    }
                } catch (Exception ex) {

                }
            }
        }
    }


    private class TmpServerThread extends Thread {
        private boolean running = true;
        private final int port;
        private ServerSocket server;

        public TmpServerThread(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                server = serverSocket;
                while (running) {
                    List<HelloCmd> newRats = new ArrayList<>();
                    try (Socket client = serverSocket.accept();
                         BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                         BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {
                        while (true) {
                            String msg = in.readLine();
                            if (msg == null) {
                                break;
                            }
                            if (msg.startsWith(Commands.HANDSHAKE)) {
                                msg = msg.replaceFirst(Commands.HANDSHAKE, "");
                                HelloCmd received = HelloCmd.read(msg);
                                received.setTtl(HelloCmd.MAX_TTL);
                                if (!received.getId().equals(id)) {
                                    newRats.add(received);
                                }
                                out.write(connected.getText());
                                out.newLine();
                                out.flush();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    addSeenPlayer(newRats);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void addSeenPlayer(List<HelloCmd> msgs) {
            for (HelloCmd msg : msgs) {
                seenPlayers.remove(msg);
                seenPlayers.add(msg);
            }
            refreshServerStatus();
        }

        public void cancel() {
            try {
                running = false;
                server.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void refreshServerStatus() {
        for (HelloCmd player : seenPlayers) {
            player.ttl();
            System.out.println(player);
        }
        seenPlayers = seenPlayers.stream().filter(a -> a.getTtl() > 0).collect(Collectors.toSet());
        String s = "<html>";
        for (HelloCmd player : seenPlayers) {
            s = s + "[" + (player.getName() + " " + player.getRat().split(":")[1]) + "] ;  ";
        }
        s = s + "<br/> [";
        for (VirtualRatSetup rat : ratsPanel.getRatsWithView()) {
            s = s + rat.toString().split(":")[1] + " ";
        }
        s = s + "][";
        for (VirtualRatSetup rat : ratsPanel.getRatsWithoutView()) {
            s = s + rat.toString().split(":")[1] + " ";
        }
        s = s + "]";
        connected.setText(s);
        connected.validate();
    }


    private class SayHelloThread extends Thread {
        private boolean running = true;
        private final int port;
        private ServerSocket server;

        public SayHelloThread(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            while (running) {
                sleepCatched(500);
                if (!ratsPanel.getRatsWithView().isEmpty()) {
                    try (Socket clientSocket = new Socket("localhost", port);
                         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                        for (VirtualRatSetup rat : ratsPanel.getRatsWithView()) {
                            new HelloCmd(id, port, rat.toString(), name).send(out);
                        }
                        out.flush();
                        String confirmed = in.readLine();
                        if (clientSelected.isSelected()) {
                            connected.setText("<html>" + Localization.get().getOkConnected() + "<br/> online players: " + confirmed.replace("<br/>", " + severs:"));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        if (clientSelected.isSelected()) {
                            connected.setText("Failed! - " + ex.getMessage());
                        }
                        sleepCatched(500);
                    }

                }
            }
        }

        private void sleepCatched(int i) {
            try {
                Thread.sleep(i);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        public void cancel() {
            running = false;
        }
    }


    private int findRandomPort() {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int freePort = serverSocket.getLocalPort();
            return freePort;
        } catch (Exception e) {
            return 8456;
        }
    }

    private static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception ex) {
            return "localhost";
        }
    }

    public static String randomStringSimple(int length, int caseUpNothingDown) {
        byte[] byteArray = new byte[length];
        Random random = new Random();
        random.nextBytes(byteArray);
        String s = Base64.getEncoder().encodeToString(byteArray).replaceAll("=", "");
        if (caseUpNothingDown == 0) {
            return s;
        } else if (caseUpNothingDown < 0) {
            return s.toLowerCase();
        } else {
            return s.toUpperCase();
        }
    }

    public NetworkPane(World world, SetupWindow parent, RatsPanel rats) {
        this.ratsPanel = rats;
        this.setLayout(new BorderLayout());
        serverPanel = new JPanel();
        serverPanel.setLayout(new GridLayout(0, 1));
        serverSelected = new JRadioButton("<html><div style='text-align: center; font-weight: bold; text-decoration: underline; '>" + "Server" + "</div></html>");
        serverSelected.addActionListener(this);
        serverPanel.add(serverSelected);
        serverPanel.add(new JLabel("All guinea pigs created in '" + Localization.get().getRatsTitle() + "' will be used"));
        //fixme, move to two on one line
        serverPanel.add(new JLabel("this machine: "));
        JTextField serverNameFieldToCopy = new JTextField(hostname);
        serverNameFieldToCopy.setEditable(false);
        serverPanel.add(serverNameFieldToCopy);
        //fixme, move to two on one line
        serverPanel.add(new JLabel("this port: "));
        JSpinner serverSpinner = new JSpinner(new SpinnerNumberModel(serverPort, 1001, 65000, 1));
        serverSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                serverPort = ((Number) serverSpinner.getValue()).intValue();
                resetPorts();
            }
        });
        serverPanel.add(serverSpinner);
        serverPanel.add(new JLabel("Once all expected guinea pigs are in game, you can press start"));
        serverPanel.add(new JButton("Start"));
        this.add(serverPanel, BorderLayout.NORTH);
        titleOfTitles = new JLabel("Connection status:");
        serverPanel.add(titleOfTitles);
        connected = new JLabel();
        this.add(connected);
        clientPanel = new JPanel();
        this.add(clientPanel, BorderLayout.SOUTH);
        clientPanel.setLayout(new GridLayout(0, 1));
        clientSelected = new JRadioButton("<html><div style='text-align: center; font-weight: bold; text-decoration: underline; '>" + "Client" + "</div></html>");
        clientSelected.addActionListener(this);
        clientSelected.setSelected(true);
        kids(clientPanel, true);
        kids(serverPanel, false);
        clientPanel.add(clientSelected);
        clientPanel.add(new JLabel("ALL guinea pigs created in '" + Localization.get().getRatsTitle() + "' will be used"));
        clientPanel.add(new JLabel("id: " + id));
        //fixme, moe to two on one line
        clientPanel.add(new JLabel("name: "));
        JTextField nameField = new JTextField(randomStringSimple(5, -1));
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                process();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                process();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                process();
            }

            private void process() {
                name = nameField.getText();
                if (name.trim().isEmpty()) {
                    name = "unset";
                }
            }
        });
        name = nameField.getText();
        clientPanel.add(nameField);
        //fixme, moe to two on one line
        clientPanel.add(new JLabel("target machine: "));
        clientPanel.add(new JTextField("localhost"));
        //fixme, moe to two on one line
        clientPanel.add(new JLabel("target port: "));
        JSpinner clientSpinner = new JSpinner(new SpinnerNumberModel(clientPort, 1001, 65000, 1));
        clientSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                clientPort = ((Number) clientSpinner.getValue()).intValue();
                resetPorts();
            }
        });
        clientPanel.add(clientSpinner);
        ButtonGroup clientServer = new ButtonGroup();
        clientServer.add(serverSelected);
        clientServer.add(clientSelected);
        new ObserverThread().start();
        resetPorts();
        parent.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                NetworkPane.this.stopTmpThreads();
            }
        });
        setTitles();
    }

    private void resetPorts() {
        stopTmpThreads();
        tmpServer = new TmpServerThread(serverPort);
        tmpServer.start();
        tmpClient = new SayHelloThread(clientPort);
        tmpClient.start();
    }

    private void stopTmpThreads() {
        if (tmpServer != null) {
            tmpServer.cancel();
        }
        if (tmpClient != null) {
            tmpClient.cancel();
        }
    }

    @Override
    public void setTitles() {
        setName(Localization.get().getNetTitle());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (serverSelected.isSelected()) {
            kids(serverPanel, true);
            kids(clientPanel, false);
        }
        if (clientSelected.isSelected()) {
            kids(serverPanel, false);
            kids(clientPanel, true);
        }
    }

    private void kids(JPanel panel, boolean b) {
        Component[] comps = panel.getComponents();
        for (Component c : comps) {
            if (!(c instanceof JRadioButton)) {
                c.setEnabled(b);
            }
        }
        titleOfTitles.setEnabled(true);
    }

}
