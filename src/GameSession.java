import java.io.*;

public class GameSession extends Thread {
    private final ClientHandler player1;
    private final ClientHandler player2;
    private int player1Wins = 0;
    private int player2Wins = 0;
    private int draws = 0;
    private static final String[] opcoes = {"Pedra", "Papel", "Tesoura"};

    public GameSession(ClientHandler player1, ClientHandler player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void run() {
        try {
            BufferedReader in1 = new BufferedReader(new InputStreamReader(player1.clientSocket.getInputStream()));
            BufferedReader in2 = new BufferedReader(new InputStreamReader(player2.clientSocket.getInputStream()));
            PrintWriter out1 = new PrintWriter(player1.clientSocket.getOutputStream(), true);
            PrintWriter out2 = new PrintWriter(player2.clientSocket.getOutputStream(), true);

            while (true) {
                out1.println("Faça sua jogada (0 - Pedra, 1 - Papel, 2 - Tesoura) ou digite 'sair' para encerrar:");
                out2.println("Faça sua jogada (0 - Pedra, 1 - Papel, 2 - Tesoura) ou digite 'sair' para encerrar:");

                String jogada1 = in1.readLine();
                String jogada2 = in2.readLine();

                if (jogada1.equalsIgnoreCase("sair") || jogada2.equalsIgnoreCase("sair")) {
                    break;
                }

                while (!isValidMove(jogada1) || !isValidMove(jogada2)) {
                    if (!isValidMove(jogada1)) {
                        out1.println("Jogada inválida. Tente novamente.");
                        jogada1 = in1.readLine();
                    }
                    if (!isValidMove(jogada2)) {
                        out2.println("Jogada inválida. Tente novamente.");
                        jogada2 = in2.readLine();
                    }
                }

                String result = determinarVencedor(jogada1, jogada2);
                System.out.println("Resultado: " + result);
                out1.println(result);
                out2.println(result);

                if (result.contains("Empate!")) {
                    draws++;
                } else if (result.contains(player1.getPlayerName() + " ganhou!")) {
                    player1Wins++;
                } else {
                    player2Wins++;
                }
            }

            String historicoFinal1 = player1.getPlayerName() + ": " + player1Wins + " vitórias | " + player2Wins + " derrotas | " + draws + " empates";
            String historicoFinal2 = player2.getPlayerName() + ": " + player2Wins + " vitórias | " + player1Wins + " derrotas | " + draws + " empates";
            out1.println("Histórico de partidas: " + historicoFinal1);
            out2.println("Histórico de partidas: " + historicoFinal2);

            String finalWinner;
            if (player1Wins > player2Wins) {
                finalWinner = player1.getPlayerName();
            } else if (player1Wins < player2Wins) {
                finalWinner = player2.getPlayerName();
            } else {
                finalWinner = "Empate";
            }

            out1.println("Vencedor final: " + finalWinner);
            out2.println("Vencedor final: " + finalWinner);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                player1.clientSocket.close();
                player2.clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isValidMove(String jogada) {
        return jogada.equals("0") || jogada.equals("1") || jogada.equals("2");
    }

    private String determinarVencedor(String jogada1, String jogada2) {
        int j1 = Integer.parseInt(jogada1);
        int j2 = Integer.parseInt(jogada2);
        return determinarVencedor(j1, j2);
    }

    private String determinarVencedor(int jogada1, int jogada2) {
        if (jogada1 == jogada2) {
            return "Empate!";
        } else if ((jogada1 == 0 && jogada2 == 2) || (jogada1 == 1 && jogada2 == 0) || (jogada1 == 2 && jogada2 == 1)) {
            return player1.getPlayerName() + " ganhou!";
        } else {
            return player2.getPlayerName() + " ganhou!";
        }
    }
}
