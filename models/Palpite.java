package model;

import java.util.ArrayList;

// o palpite de um participante pra um jogo: placar que ele acha que vai
// dar + quem ele acha que vai marcar gol
// tem agregacao com Participante e Jogo (sao objetos que já existem)
public class Palpite {

    private Participante participante;
    private Jogo jogo;

    private int golsCasa;
    private int golsVisitante;

    private int pontos; // calculado depois que o jogo termina

    private ArrayList<Gol> golsPrevistos = new ArrayList<>();

    public Palpite(Participante p, Jogo j, int gc, int gv) {
        this.participante = p;
        this.jogo = j;
        this.golsCasa = gc;
        this.golsVisitante = gv;
    }

    public Participante getParticipante() { return participante; }
    public Jogo getJogo() { return jogo; }

    public int getGolsCasa() { return golsCasa; }
    public int getGolsVisitante() { return golsVisitante; }

    public int getPontos() { return pontos; }
    public void setPontos(int p) { this.pontos = p; }

    public ArrayList<Gol> getGolsPrevistos() { return golsPrevistos; }

    public void addGolPrevisto(Gol g) {
        golsPrevistos.add(g);
    }

    // usado na tela de consultar pontuacao do participante
    @Override
    public String toString() {
        return jogo + " | " + golsCasa + "x" + golsVisitante + " | " + pontos + " pts";
    }
}