import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 9090;

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ServerConnection serverConn = new ServerConnection(socket);

            System.out.print("Enter your name: ");
            String name = keyboard.readLine();
            out.println(name);

            new Thread(serverConn).start();

            while (true) {
                System.out.print("> ");
                String command = keyboard.readLine();

                if (command.equals("quit")) break;
                out.println(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }
}