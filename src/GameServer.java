import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class GameServer {

    public static Queue<ClientHandler> playerQueue = new LinkedList<>();

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.println("Digite a porta em que o servidor irá rodar: ");
        int PORT = scanner.nextInt();

        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciado... Aguardando clientes...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Novo cliente conectado.");

            ClientHandler handler = new ClientHandler(clientSocket);
            handler.start();

            logPlayersStatus();
        }
    }

    public static void logPlayersStatus() {
        if (!playerQueue.isEmpty()) {
            System.out.print("Jogadores na fila: ");
            for (ClientHandler player : playerQueue) {
                System.out.print(player.getPlayerName() + ", ");
            }
            System.out.println("aguardando para jogar.");
        }
    }
}
