import java.io.*;
import java.net.*;

public class GameClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));

            // Recebe a mensagem do servidor
            String serverMessage = in.readLine();
            System.out.println(serverMessage);

            // Aguarda até que o servidor solicite a jogada
            if (!serverMessage.equals("Faça sua jogada (0 - Pedra, 1 - Papel, 2 - Tesoura):")) {
                serverMessage = in.readLine();
                System.out.println(serverMessage);
            }

            // Envia a jogada para o servidor
            String jogada = userIn.readLine();
            out.println(jogada);

            // Recebe e exibe o resultado
            String result = in.readLine();
            System.out.println("Resultado: " + result);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
