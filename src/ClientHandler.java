import java.io.*;
import java.net.*;
import java.util.Random;

public class ClientHandler extends Thread {
    final Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private static final Random random = new Random();
    private String playerName;
    private final GameHistory history = new GameHistory();

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            playerName = in.readLine();

            System.out.println(playerName + " conectou.");

            int mode = Integer.parseInt(in.readLine());

            if (mode == 1) {
                jogarContraMaquina();
            } else if (mode == 2) {
                synchronized (GameServer.playerQueue) {
                    if (GameServer.playerQueue.isEmpty()) {
                        GameServer.playerQueue.add(this);
                        out.println("Aguardando outro jogador...");
                    } else {
                        ClientHandler otherPlayer = GameServer.playerQueue.poll();
                        System.out.println(playerName + " e " + otherPlayer.getPlayerName() + " estão jogando juntos.");
                        GameSession session = new GameSession(otherPlayer, this);
                        session.start();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void jogarContraMaquina() throws IOException {
        String[] opcoes = {"Pedra", "Papel", "Tesoura"};
        int vitorias = 0;
        int derrotas = 0;
        int empates = 0;

        while (true) {
            out.println("Faça sua jogada (0 - Pedra, 1 - Papel, 2 - Tesoura) ou digite 'sair' para encerrar:");
            String jogada = in.readLine();

            if (jogada.equalsIgnoreCase("sair")) {
                break;
            }

            if (jogada != null && (jogada.equals("0") || jogada.equals("1") || jogada.equals("2"))) {
                int jogadaUsuario = Integer.parseInt(jogada);
                int jogadaSistema = random.nextInt(3);

                String resultado = determinarVencedor(jogadaUsuario, jogadaSistema);
                out.println("Você escolheu: " + opcoes[jogadaUsuario]);
                out.println("O sistema escolheu: " + opcoes[jogadaSistema]);
                out.println(resultado);

                if (resultado.contains(playerName + " ganhou")) {
                    vitorias++;
                } else if (resultado.contains("O sistema ganhou")) {
                    derrotas++;
                } else {
                    empates++;
                }

                history.addGame(playerName + ": " + opcoes[jogadaUsuario] + " | Sistema: " + opcoes[jogadaSistema] + " | " + resultado);
            } else {
                out.println("Jogada inválida. Tente novamente.");
            }
        }

        String historicoFinal = playerName + ": " + vitorias + " vitórias | " + derrotas + " derrotas | " + empates + " empates";
        out.println("Histórico de partidas: " + historicoFinal);

        clientSocket.close();
    }

    private String determinarVencedor(int jogada1, int jogada2) {
        if (jogada1 == jogada2) {
            return "Empate!";
        } else if ((jogada1 == 0 && jogada2 == 2) || (jogada1 == 1 && jogada2 == 0) || (jogada1 == 2 && jogada2 == 1)) {
            return playerName + " ganhou!";
        } else {
            return "O sistema ganhou!";
        }
    }

    public String getPlayerName() {
        return playerName;
    }
}
