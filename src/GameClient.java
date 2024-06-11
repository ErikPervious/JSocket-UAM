import java.io.*;
import java.net.*;
import java.util.Scanner;

public class GameClient {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Digite o host do servidor: ");
            String HOST = scanner.nextLine();
            System.out.println("Digite o numero da porta do servidor: ");
            int PORT = scanner.nextInt();

            Socket socket = new Socket(HOST, PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;

            System.out.print("Digite seu nome: ");
            fromUser = stdIn.readLine();
            out.println(fromUser);

            while (true) {
                System.out.println("Escolha o modo de jogo:");
                System.out.println("1 - Jogar contra a máquina");
                System.out.println("2 - Jogar contra outro jogador");
                fromUser = stdIn.readLine();
                if (fromUser.equals("1") || fromUser.equals("2")) {
                    out.println(fromUser);
                    break;
                } else {
                    System.out.println("Entrada inválida. Digite 1 ou 2.");
                }
            }

            while ((fromServer = in.readLine()) != null) {
                if (fromServer.contains("Faça sua jogada")) {
                    System.out.println("Servidor: " + fromServer);
                    while (true) {
                        System.out.print("Sua jogada: ");
                        fromUser = stdIn.readLine();
                        if (fromUser.equals("0") || fromUser.equals("1") || fromUser.equals("2") || fromUser.equalsIgnoreCase("sair")) {
                            out.println(fromUser);
                            break;
                        } else {
                            System.out.println("Entrada inválida. Digite 0, 1, 2 ou 'sair'.");
                        }
                    }
                } else if (fromServer.contains("ganhou") || fromServer.equals("Empate!")) {
                    System.out.println("Resultado: " + fromServer);
                } else if (fromServer.contains("Histórico de partidas")) {
                    System.out.println(fromServer);
                } else {
                    System.out.println("Servidor: " + fromServer);
                }
            }

            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Não foi possível conectar ao host.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Erro de E/S ao tentar conectar ao servidor.");
            System.exit(1);
        }
    }
}
