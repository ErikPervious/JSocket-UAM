import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<ClientHandler> clients;
    private String clientName;

    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clients) throws IOException {
        this.client = clientSocket;
        this.clients = clients;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            this.clientName = in.readLine();
            System.out.println(clientName + " has connected.");

            while (true) {
                String request = in.readLine();
                if (request == null) {
                    // Cliente desconectou
                    break;
                }
                if (request.startsWith("say")) {
                    int firstSpace = request.indexOf(" ");
                    if (firstSpace != -1) {
                        outToAll(clientName + ": " + request.substring(firstSpace + 1));
                    }
                } if (request.contains("exit")) {
                    break;
                } else {
                    out.println("Type 'say <message>' to chat or 'exit' to quit");
                }
            }
        } catch (IOException e) {
            System.err.println("IO exception in client handler: " + e.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                client.close();
                synchronized (clients) {
                    clients.remove(this);
                    System.out.println(clientName + " has disconnected.");
                }
            } catch (IOException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    private void outToAll(String msg) {
        synchronized (clients) {
            for (ClientHandler aClient : clients) {
                aClient.out.println(msg);
            }
        }
    }
}