package model;

// guarda um jogador + quantos gols ele fez (ou que o participante
// previu que ele ia fazer)
// classe usada tanto pros gols reais do jogo quanto pros
// gols que o participante chuta no palpite
public class Gol {

    private Jogador jogador;
    private int quantidade;

    public Gol(Jogador jogador, int quantidade) {
        this.jogador = jogador;
        this.quantidade = quantidade;
    }

    public Jogador getJogador() { return jogador; }
    public int getQuantidade() { return quantidade; }

    @Override
    public String toString() {
        return jogador.getNome() + " - " + quantidade;
    }
}