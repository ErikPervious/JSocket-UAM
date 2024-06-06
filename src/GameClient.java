import java.io.*;
import java.net.*;

public class GameClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 6666);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Servidor: " + fromServer);
                if (fromServer.contains("Faça sua jogada")) {
                    while (true) {
                        System.out.print("Sua jogada: ");
                        fromUser = stdIn.readLine();
                        if (fromUser.equals("0") || fromUser.equals("1") || fromUser.equals("2")) {
                            out.println(fromUser);
                            break;
                        } else {
                            System.out.println("Entrada inválida. Digite 0, 1 ou 2.");
                        }
                    }
                } else if (fromServer.contains("ganhou") || fromServer.equals("Empate!")) {
                    System.out.println("Resultado: " + fromServer);
                    break;
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
