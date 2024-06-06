import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class GameClientApp {

    private JFrame frame;
    private JButton pedraButton;
    private JButton papelButton;
    private JButton tesouraButton;
    private JLabel messageLabel;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public GameClientApp() {
        setupGUI();
        connectToServer();
    }

    private void setupGUI() {
        frame = new JFrame("Joquempô");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new BorderLayout());

        messageLabel = new JLabel("Conectando ao servidor...", SwingConstants.CENTER);
        frame.add(messageLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));

        pedraButton = new JButton("Pedra");
        papelButton = new JButton("Papel");
        tesouraButton = new JButton("Tesoura");

        pedraButton.addActionListener(new JogadaListener(0));
        papelButton.addActionListener(new JogadaListener(1));
        tesouraButton.addActionListener(new JogadaListener(2));

        buttonPanel.add(pedraButton);
        buttonPanel.add(papelButton);
        buttonPanel.add(tesouraButton);

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Aguarda mensagem inicial do servidor
            String serverMessage = in.readLine();
            messageLabel.setText(serverMessage);

            // Aguarda mensagem de jogada do servidor
            if (!serverMessage.equals("Faça sua jogada (0 - Pedra, 1 - Papel, 2 - Tesoura):")) {
                serverMessage = in.readLine();
                messageLabel.setText(serverMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class JogadaListener implements ActionListener {
        private int jogada;

        public JogadaListener(int jogada) {
            this.jogada = jogada;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            out.println(jogada);
            try {
                String result = in.readLine();
                messageLabel.setText("Resultado: " + result);
                pedraButton.setEnabled(false);
                papelButton.setEnabled(false);
                tesouraButton.setEnabled(false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameClientApp::new);
    }
}
