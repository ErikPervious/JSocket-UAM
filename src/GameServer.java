import java.io.*;
import java.net.*;

public class GameServer {

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        private String jogada;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                synchronized (GameServer.class) {
                    // Notifica o primeiro cliente que está na fila
                    if (Thread.activeCount() == 3) {
                        out.println("Você está na fila, aguardando outro jogador...");
                        GameServer.class.wait();
                    } else {
                        // Segundo cliente conectado, notifica ambos para jogarem
                        GameServer.class.notify();
                    }
                }

                // Solicita a jogada do cliente até que uma jogada válida seja fornecida
                while (true) {
                    out.println("Faça sua jogada (0 - Pedra, 1 - Papel, 2 - Tesoura):");
                    jogada = in.readLine();

                    if (jogada != null && (jogada.equals("0") || jogada.equals("1") || jogada.equals("2"))) {
                        break;
                    } else {
                        out.println("Jogada inválida. Tente novamente.");
                    }
                }

                // Aguardando a jogada do outro cliente
                synchronized (GameServer.class) {
                    GameServer.class.notify();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        public String getJogada() {
            return jogada;
        }

        public void sendResult(String result) {
            out.println(result);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("Servidor iniciado... Aguardando clientes...");

        // Aceita dois clientes
        Socket client1 = serverSocket.accept();
        System.out.println("Cliente 1 conectado.");
        ClientHandler handler1 = new ClientHandler(client1);
        handler1.start();

        Socket client2 = serverSocket.accept();
        System.out.println("Cliente 2 conectado.");
        ClientHandler handler2 = new ClientHandler(client2);
        handler2.start();

        // Espera que ambos os clientes façam suas jogadas
        synchronized (GameServer.class) {
            GameServer.class.wait();
            GameServer.class.wait();
        }

        // Determina o resultado
        String jogada1 = handler1.getJogada();
        String jogada2 = handler2.getJogada();
        String result = determinarVencedor(jogada1, jogada2);

        // Envia o resultado para os clientes
        handler1.sendResult(result);
        handler2.sendResult(result);

        serverSocket.close();
    }

    private static String determinarVencedor(String jogada1, String jogada2) {
        int j1 = Integer.parseInt(jogada1);
        int j2 = Integer.parseInt(jogada2);
        if (j1 == j2) {
            return "Empate!";
        } else if ((j1 == 0 && j2 == 2) || (j1 == 1 && j2 == 0) || (j1 == 2 && j2 == 1)) {
            return "Jogador 1 ganhou!";
        } else {
            return "Jogador 2 ganhou!";
        }
    }
}
