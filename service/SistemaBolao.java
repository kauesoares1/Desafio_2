package service;

import model.*;
import java.util.*;

// classe principal do sistema, guarda tudo (usuarios, selecoes, jogos, palpites)
// tem os metodos que o Main chama pra fazer o cadastro/login/etc
public class SistemaBolao {

    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private ArrayList<Selecao> selecoes = new ArrayList<>();
    private ArrayList<Jogo> jogos = new ArrayList<>();
    private ArrayList<Palpite> palpites = new ArrayList<>();

    private CalculadoraPontuacao calc = new CalculadoraPontuacao();

    // login
    // procura um usuario com esse login/senha (admin ou participante)
    // retorna null se nao achar
    public Usuario login(String login, String senha) {
        for (Usuario u : usuarios) {
            if (u.autenticar(login, senha)) {
                return u;
            }
        }
        return null;
    }

    // cadastros (só o Adm pode chamar esses metodos)
    // em cada metodo abaixo passo quem ta chamando (solicitante) e checa se é admin

    public boolean addAdmin(Usuario solicitante, String n, String l, String s) {
        // deixo criar o primeiro admin sem precisar de login (senao
        // ninguem conseguiria entrar no sistema no começo)
        if (!usuarios.isEmpty() && !isAdmin(solicitante)) return false;
        usuarios.add(new Administrador(n, l, s));
        return true;
    }

    public boolean addParticipante(Usuario solicitante, String n, String l, String s) {
        if (!isAdmin(solicitante)) return false;
        usuarios.add(new Participante(n, l, s));
        return true;
    }

    public boolean addSelecao(Usuario solicitante, String n) {
        if (!isAdmin(solicitante)) return false;
        selecoes.add(new Selecao(n));
        return true;
    }

    public boolean addJogador(Usuario solicitante, String selecaoNome, String n, Posicao p) {
        if (!isAdmin(solicitante)) return false;
        Selecao sel = getSelecao(selecaoNome);
        if (sel == null) return false;
        sel.adicionarJogador(new Jogador(n, p));
        return true;
    }

    public boolean addJogo(Usuario solicitante, String casaNome, String visitanteNome) {
        if (!isAdmin(solicitante)) return false;
        Selecao casa = getSelecao(casaNome);
        Selecao visitante = getSelecao(visitanteNome);
        if (casa == null || visitante == null) return false;
        jogos.add(new Jogo(casa, visitante));
        return true;
    }

    // admin informa o resultado real do jogo, depois disso ninguem mais pode palpitar nesse jogo
    public boolean finalizarJogo(Usuario solicitante, int indiceJogo, int golsCasa, int golsVisitante,
                                  List<Gol> golsDaPartida) {
        if (!isAdmin(solicitante)) return false;
        if (indiceJogo < 0 || indiceJogo >= jogos.size()) return false;

        Jogo jogo = jogos.get(indiceJogo);
        jogo.finalizar(golsCasa, golsVisitante);
        for (Gol g : golsDaPartida) {
            jogo.addGol(g);
        }
        return true;
    }

    private boolean isAdmin(Usuario u) {
        return u instanceof Administrador;
    }

    //consultas 

    public Selecao getSelecao(String n) {
        for (Selecao s : selecoes)
            if (s.getNome().equalsIgnoreCase(n))
                return s;
        return null;
    }

    public List<Selecao> getSelecoes() {
        return selecoes;
    }

    public List<Jogo> getJogos() {
        return jogos;
    }

    public Participante buscarParticipante(String login) {
        for (Usuario u : usuarios)
            if (u instanceof Participante && u.getLogin().equals(login))
                return (Participante) u;
        return null;
    }

    //palpites (qualquer participante logado pode dar)

    public boolean addPalpite(Participante participante, int indiceJogo, int gc, int gv, List<Gol> golsPrevistos) {
        if (participante == null) return false;
        if (indiceJogo < 0 || indiceJogo >= jogos.size()) return false;

        Jogo jogo = jogos.get(indiceJogo);
        if (jogo.isFinalizado()) return false; // jogo ja acabou, nao da mais pra palpitar

        Palpite palpite = new Palpite(participante, jogo, gc, gv);
        for (Gol g : golsPrevistos) {
            palpite.addGolPrevisto(g);
        }
        palpites.add(palpite);
        return true;
    }

    //calculo da pontuacao
    // passa em todos os palpites e calcula os que ja podem ser calculados
    // (jogo finalizado e ainda nao calculado)
    public void calcular() {
        for (Palpite p : palpites) {
            if (p.getJogo().isFinalizado() && p.getPontos() == 0) {
                int pts = calc.calcular(p);
                p.getParticipante().adicionarPontos(pts);
            }
        }
    }

    //consulta de pontuacao do participante por jogo

    public List<Palpite> getPalpitesDoParticipante(Participante p) {
        List<Palpite> resultado = new ArrayList<>();
        for (Palpite palpite : palpites) {
            if (palpite.getParticipante().equals(p) || palpite.getParticipante() == p) {
                resultado.add(palpite);
            }
        }
        return resultado;
    }

    //ranking 

    public List<Participante> ranking() {
        List<Participante> lista = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u instanceof Participante) {
                lista.add((Participante) u);
            }
        }
        lista.sort((a, b) -> b.getPontuacao() - a.getPontuacao()); // maior pontuacao primeiro
        return lista;
    }
}