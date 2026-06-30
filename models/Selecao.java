package model;

import java.util.ArrayList;

// representa um pais/selecao da copa
// cada selecao tem uma lista de jogadores (composição, pq o jogador
// sempre pertence a uma selecao, nao existe sozinho)
public class Selecao {

    private String nome;
    private ArrayList<Jogador> jogadores = new ArrayList<>();

    public Selecao(String nome) {
        this.nome = nome;
    }

    public String getNome() { return nome; }
    public ArrayList<Jogador> getJogadores() { return jogadores; }

    public void adicionarJogador(Jogador j) {
        jogadores.add(j);
    }

    @Override
    public String toString() {
        return nome;
    }
}