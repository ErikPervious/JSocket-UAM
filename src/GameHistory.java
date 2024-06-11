import java.util.ArrayList;
import java.util.List;

public class GameHistory {
    private int vitorias;
    private int derrotas;
    private int empates;

    public void addGame(String resultado) {
        if (resultado.contains("ganhou!")) {
            if (resultado.contains("Sistema")) {
                derrotas++;
            } else {
                vitorias++;
            }
        } else if (resultado.contains("Empate!")) {
            empates++;
        }
    }

    public String getHistory(String playerName) {
        return playerName + ": " + vitorias + " vitórias | " + derrotas + " derrotas | " + empates + " empates";
    }

    public int getVitorias() {
        return vitorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public int getEmpates() {
        return empates;
    }
}
