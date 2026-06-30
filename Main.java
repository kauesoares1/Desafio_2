import service.*;
import model.*;
import java.util.*;

// classe que roda o programa (menu no console)
// aqui so tem a parte de interacao com o usuario, a logica fica no SistemaBolao
public class Main {

    static Scanner sc = new Scanner(System.in);
    static SistemaBolao sistema = new SistemaBolao();

    public static void main(String[] args) {

        // cria um admin padrao pra dar pra entrar no sistema (login: admin / senha: admin)
        sistema.addAdmin(null, "Administrador Geral", "admin", "admin");

        int opcao;
        do {
            System.out.println("\n===== BOLÃO DA COPA 2026 =====");
            System.out.println("1 - Login");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");
            opcao = lerInt();

            if (opcao == 1) {
                fazerLogin();
            }

        } while (opcao != 0);

        System.out.println("Encerrando o sistema...");
    }

    // pede login e senha, e dependendo do tipo de usuario que voltou,
    // manda pro menu certo (admin ou participante)
    private static void fazerLogin() {
        System.out.print("Login: ");
        String login = sc.next();
        System.out.print("Senha: ");
        String senha = sc.next();

        Usuario usuario = sistema.login(login, senha);

        if (usuario == null) {
            System.out.println("Login ou senha inválidos.");
            return;
        }

        System.out.println("Bem-vindo, " + usuario.getNome() + " (" + usuario.getTipo() + ")");

        if (usuario instanceof Administrador) {
            menuAdministrador(usuario);
        } else if (usuario instanceof Participante) {
            menuParticipante((Participante) usuario);
        }
    }

    // ---------------- menu do admin ----------------
    // só o admin tem acesso a essas opcoes (cadastros e resultado dos jogos)

    private static void menuAdministrador(Usuario admin) {
        int op;
        do {
            System.out.println("\n--- MENU ADMINISTRADOR ---");
            System.out.println("1 - Cadastrar participante");
            System.out.println("2 - Cadastrar seleção");
            System.out.println("3 - Cadastrar jogador em uma seleção");
            System.out.println("4 - Cadastrar jogo");
            System.out.println("5 - Informar resultado de um jogo");
            System.out.println("6 - Ver ranking geral");
            System.out.println("0 - Logout");
            System.out.print("Opção: ");
            op = lerInt();

            switch (op) {
                case 1:
                    System.out.print("Nome: "); String n = sc.next();
                    System.out.print("Login: "); String l = sc.next();
                    System.out.print("Senha: "); String s = sc.next();
                    sistema.addParticipante(admin, n, l, s);
                    System.out.println("Participante cadastrado.");
                    break;

                case 2:
                    System.out.print("Nome da seleção: ");
                    sistema.addSelecao(admin, sc.next());
                    System.out.println("Seleção cadastrada.");
                    break;

                case 3:
                    System.out.print("Nome da seleção: "); String selNome = sc.next();
                    System.out.print("Nome do jogador: "); String jogNome = sc.next();
                    Posicao pos = lerPosicao();
                    if (sistema.addJogador(admin, selNome, jogNome, pos)) {
                        System.out.println("Jogador cadastrado.");
                    } else {
                        System.out.println("Seleção não encontrada.");
                    }
                    break;

                case 4:
                    System.out.print("Seleção da casa: "); String casa = sc.next();
                    System.out.print("Seleção visitante: "); String visitante = sc.next();
                    if (sistema.addJogo(admin, casa, visitante)) {
                        System.out.println("Jogo cadastrado.");
                    } else {
                        System.out.println("Uma das seleções não foi encontrada.");
                    }
                    break;

                case 5:
                    informarResultado(admin);
                    break;

                case 6:
                    exibirRanking();
                    break;
            }

        } while (op != 0);
    }

    // admin lança o placar real e quem fez gol, depois ja calcula
    // a pontuacao de todo mundo que apostou nesse jogo
    private static void informarResultado(Usuario admin) {
        listarJogos();
        System.out.print("Índice do jogo: ");
        int idx = lerInt();
        if (idx < 0 || idx >= sistema.getJogos().size()) {
            System.out.println("Jogo inválido.");
            return;
        }

        Jogo jogo = sistema.getJogos().get(idx);

        System.out.print("Gols da casa (" + jogo.getCasa() + "): ");
        int gc = lerInt();
        System.out.print("Gols do visitante (" + jogo.getVisitante() + "): ");
        int gv = lerInt();

        List<Gol> golsDaPartida = new ArrayList<>();
        System.out.print("Quantos jogadores diferentes marcaram gol? ");
        int qtdMarcadores = lerInt();

        for (int i = 0; i < qtdMarcadores; i++) {
            System.out.println("Marcador " + (i + 1) + ":");
            Jogador jogador = escolherJogador(jogo);
            if (jogador == null) continue;
            System.out.print("Quantidade de gols desse jogador: ");
            int qtd = lerInt();
            golsDaPartida.add(new Gol(jogador, qtd));
        }

        if (sistema.finalizarJogo(admin, idx, gc, gv, golsDaPartida)) {
            sistema.calcular();
            System.out.println("Resultado registrado e pontuações calculadas.");
        } else {
            System.out.println("Não foi possível registrar o resultado.");
        }
    }

    // mostra os jogadores das duas selecoes do jogo pra escolher um
    // (uso isso tanto pro admin marcar quem fez gol quanto pro
    // participante escolher quem ele acha que vai fazer gol)
    private static Jogador escolherJogador(Jogo jogo) {
        List<Jogador> disponiveis = new ArrayList<>();
        disponiveis.addAll(jogo.getCasa().getJogadores());
        disponiveis.addAll(jogo.getVisitante().getJogadores());

        if (disponiveis.isEmpty()) {
            System.out.println("Nenhum jogador cadastrado nas seleções deste jogo.");
            return null;
        }

        for (int i = 0; i < disponiveis.size(); i++) {
            System.out.println(i + " - " + disponiveis.get(i));
        }
        System.out.print("Escolha o jogador: ");
        int idx = lerInt();
        if (idx < 0 || idx >= disponiveis.size()) return null;
        return disponiveis.get(idx);
    }

    // ---------------- menu do participante ----------------

    private static void menuParticipante(Participante participante) {
        int op;
        do {
            System.out.println("\n--- MENU PARTICIPANTE ---");
            System.out.println("1 - Dar palpite em um jogo");
            System.out.println("2 - Consultar minha pontuação por jogo");
            System.out.println("3 - Ver ranking geral");
            System.out.println("0 - Logout");
            System.out.print("Opção: ");
            op = lerInt();

            switch (op) {
                case 1:
                    darPalpite(participante);
                    break;
                case 2:
                    consultarPontuacao(participante);
                    break;
                case 3:
                    exibirRanking();
                    break;
            }

        } while (op != 0);
    }

    // participante escolhe o jogo, da o placar e fala quem ele acha
    // que vai fazer gol. só funciona se o jogo ainda nao acabou
    private static void darPalpite(Participante participante) {
        listarJogos();
        System.out.print("Índice do jogo: ");
        int idx = lerInt();
        if (idx < 0 || idx >= sistema.getJogos().size()) {
            System.out.println("Jogo inválido.");
            return;
        }

        Jogo jogo = sistema.getJogos().get(idx);
        if (jogo.isFinalizado()) {
            System.out.println("Este jogo já foi finalizado, não é mais possível palpitar.");
            return;
        }

        System.out.print("Seu palpite de gols para " + jogo.getCasa() + ": ");
        int gc = lerInt();
        System.out.print("Seu palpite de gols para " + jogo.getVisitante() + ": ");
        int gv = lerInt();

        List<Gol> golsPrevistos = new ArrayList<>();
        System.out.print("Quantos jogadores diferentes você acha que vão marcar? ");
        int qtd = lerInt();

        for (int i = 0; i < qtd; i++) {
            System.out.println("Marcador previsto " + (i + 1) + ":");
            Jogador jogador = escolherJogador(jogo);
            if (jogador == null) continue;
            System.out.print("Quantos gols esse jogador vai marcar: ");
            int g = lerInt();
            golsPrevistos.add(new Gol(jogador, g));
        }

        if (sistema.addPalpite(participante, idx, gc, gv, golsPrevistos)) {
            System.out.println("Palpite registrado!");
        } else {
            System.out.println("Não foi possível registrar o palpite.");
        }
    }

    // mostra a pontuacao do participante em cada jogo que ele palpitou
    // (era uma das coisas que faltava no enunciado)
    private static void consultarPontuacao(Participante participante) {
        List<Palpite> meusPalpites = sistema.getPalpitesDoParticipante(participante);
        if (meusPalpites.isEmpty()) {
            System.out.println("Você ainda não fez nenhum palpite.");
            return;
        }
        System.out.println("\n--- Sua pontuação por jogo ---");
        for (Palpite p : meusPalpites) {
            System.out.println(p);
        }
        System.out.println("Pontuação total: " + participante.getPontuacao());
    }

    // ---------------- metodos auxiliares ----------------

    private static void listarJogos() {
        List<Jogo> jogos = sistema.getJogos();
        if (jogos.isEmpty()) {
            System.out.println("Nenhum jogo cadastrado ainda.");
            return;
        }
        for (int i = 0; i < jogos.size(); i++) {
            System.out.println(i + " - " + jogos.get(i) +
                    (jogos.get(i).isFinalizado() ? " (finalizado)" : ""));
        }
    }

    private static void exibirRanking() {
        System.out.println("\n--- RANKING GERAL ---");
        List<Participante> ranking = sistema.ranking();
        int pos = 1;
        for (Participante p : ranking) {
            System.out.println(pos + "º - " + p.getNome() + " - " + p.getPontuacao() + " pts");
            pos++;
        }
    }

    // mostra as posicoes do enum numeradas pra escolher uma
    private static Posicao lerPosicao() {
        System.out.println("Posição do jogador:");
        Posicao[] valores = Posicao.values();
        for (int i = 0; i < valores.length; i++) {
            System.out.println(i + " - " + valores[i]);
        }
        int idx = lerInt();
        if (idx < 0 || idx >= valores.length) {
            System.out.println("Posição inválida, definindo como ATACANTE.");
            return Posicao.ATACANTE;
        }
        return valores[idx];
    }

    // le um numero do teclado sem deixar o programa quebrar se
    // a pessoa digitar letra por engano
    private static int lerInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Digite um número válido: ");
            sc.next();
        }
        return sc.nextInt();
    }
}