package model;

import java.util.ArrayList;

// um jogo da copa, casa x visitante
// aqui tem agregacao com Selecao (o jogo usa selecoes que já existem,
// nao é dono delas) e composição com os gols reais (a lista de gols
// só faz sentido junto com o jogo)
public class Jogo {

    private Selecao casa;
    private Selecao visitante;

    private int golsCasa;
    private int golsVisitante;

    private boolean finalizado; // true depois que o admin lança o resultado

    private ArrayList<Gol> gols = new ArrayList<>(); // gols reais da partida

    public Jogo(Selecao casa, Selecao visitante) {
        this.casa = casa;
        this.visitante = visitante;
    }

    public Selecao getCasa() { return casa; }
    public Selecao getVisitante() { return visitante; }

    public int getGolsCasa() { return golsCasa; }
    public int getGolsVisitante() { return golsVisitante; }

    public boolean isFinalizado() { return finalizado; }

    public ArrayList<Gol> getGols() { return gols; }

    // o admin chama isso pra fechar o jogo com o placar real
    public void finalizar(int gc, int gv) {
        this.golsCasa = gc;
        this.golsVisitante = gv;
        this.finalizado = true;
    }

    public void addGol(Gol g) {
        gols.add(g);
    }

    @Override
    public String toString() {
        return casa + " x " + visitante;
    }
}