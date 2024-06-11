import java.util.Random;
import java.util.Scanner;

public class GameOffline {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Opções de jogadas
        String[] opcoes = {"Pedra", "Papel", "Tesoura"};

        // Loop principal
        while (true) {
            // mostra o menu
            int jogadaUsuario = -1;
            while (jogadaUsuario < 0 || jogadaUsuario > 3) {
                System.out.println("Escolha sua jogada:");
                System.out.println("0 - Pedra");
                System.out.println("1 - Papel");
                System.out.println("2 - Tesoura");
                System.out.println("3 - Sair");

                if (scanner.hasNextInt()) {
                    jogadaUsuario = scanner.nextInt();
                    if (jogadaUsuario < 0 || jogadaUsuario > 3) {
                        System.out.println("Opção inválida. Tente novamente.");
                    }
                } else {
                    System.out.println("Entrada inválida. Digite um número entre 0 e 3.");
                    scanner.next(); // Limpa a entrada inválida
                }
            }

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

            // Aguarda 2 segundos antes de reiniciar
            try {
                Thread.sleep(2000);  // 2000 milissegundos = 2 segundos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Reiniciando o jogo...\n");
        }

        scanner.close();
    }
}
