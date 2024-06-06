import java.util.Random;
import java.util.Scanner;

public class GameOffline {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Opções de jogadas
        String[] opcoes = {"Pedra", "Papel", "Tesoura"};

        // Loop principal do jogo
        while (true) {
            // Exibe o menu e lê a jogada do usuário
            System.out.println("Escolha sua jogada:");
            System.out.println("0 - Pedra");
            System.out.println("1 - Papel");
            System.out.println("2 - Tesoura");
            System.out.println("3 - Sair");

            int jogadaUsuario = scanner.nextInt();

            // Verifica se o usuário quer sair
            if (jogadaUsuario == 3) {
                System.out.println("Jogo encerrado.");
                break;
            }

            // Gera a jogada do sistema
            int jogadaSistema = random.nextInt(3);

            // Exibe as jogadas
            System.out.println("Você escolheu: " + opcoes[jogadaUsuario]);
            System.out.println("O sistema escolheu: " + opcoes[jogadaSistema]);

            // Determina o vencedor
            if (jogadaUsuario == jogadaSistema) {
                System.out.println("Empate!");
            } else if ((jogadaUsuario == 0 && jogadaSistema == 2) ||
                    (jogadaUsuario == 1 && jogadaSistema == 0) ||
                    (jogadaUsuario == 2 && jogadaSistema == 1)) {
                System.out.println("Você ganhou!");
            } else {
                System.out.println("O sistema ganhou!");
            }

            // Aguarda 5 segundos antes de resetar
            try {
                Thread.sleep(5000);  // 5000 milissegundos = 5 segundos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println();
        }

        scanner.close();
    }
}
